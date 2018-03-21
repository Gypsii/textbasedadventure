package gfx;

import main.Commands;
import util.CommandAction;
import util.IO;

import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.ActionListener;

import java.awt.*;
import java.util.*;


public class ConsoleWindow extends JPanel implements ActionListener {
	protected JTextField textField;
	protected JTextArea textArea;
	protected TextAreaBottomAligned taba;
	protected JScrollBar scroller;

	public static Queue<String> cmdBuffer = new ArrayDeque<>();

	public ConsoleWindow() {
		super(new BorderLayout());
		textArea = new JTextArea(40, 50);
		textArea.setEditable(false);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		textField = new JTextField(50);
		textField.addActionListener(this);

		taba = new TextAreaBottomAligned(textArea);
		JScrollPane s = new JScrollPane(taba);
		scroller = s.getVerticalScrollBar();
		scroller.setUnitIncrement(16);

		add(s);
		add(textField, BorderLayout.SOUTH);
	}


	public void actionPerformed(ActionEvent e) {
		String text = textField.getText();

		Graphics.sendLine(text);
		if (Commands.waiting) {
			synchronized (Commands.actions) {
				CommandAction a = new CommandAction();
				a.cmd = text;
				a.useString = true;
				Commands.actions.add(a);
				Commands.actions.notify();
			}
		} else {
			synchronized (cmdBuffer) {
				cmdBuffer.add(text);
				cmdBuffer.notify();
			}
		}


		textField.selectAll();

		textArea.setCaretPosition(textArea.getDocument().getLength());
		//textField.requestFocusInWindow();
	}

}
