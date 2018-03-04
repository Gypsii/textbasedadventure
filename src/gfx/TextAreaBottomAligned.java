package gfx;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class TextAreaBottomAligned extends JPanel implements DocumentListener {
	private JTextArea textArea;
	public JScrollBar scrollBar;

	public TextAreaBottomAligned(JTextArea textArea) {
		this.textArea = textArea;

		setLayout(new BorderLayout());
		setBackground(textArea.getBackground());
		setBorder(textArea.getBorder());
		textArea.getDocument().addDocumentListener(this);

		add(textArea, BorderLayout.SOUTH);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		//adjustHeight();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		//adjustHeight();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	public void adjustHeight() {
		int rows = textArea.getLineCount();
		textArea.setRows(rows);
		//scrollBar.setValue(scrollBar.getMinimum());
	}
}
