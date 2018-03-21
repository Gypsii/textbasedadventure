package gfx;

import util.IO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Card extends JPanel{

	JPopupMenu menu = new JPopupMenu("Popup");

	public Card() {
		super(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());
		addMouseListener(new PopupTriggerListener());
	}

	class PopupTriggerListener extends MouseAdapter {
		public void mousePressed(MouseEvent ev) {
			if (ev.isPopupTrigger()) {
				menu.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}

		public void mouseReleased(MouseEvent ev) {
			if (ev.isPopupTrigger()) {
				menu.show(ev.getComponent(), ev.getX(), ev.getY());
			}
		}

		public void mouseClicked(MouseEvent ev) {
		}
	}

}
