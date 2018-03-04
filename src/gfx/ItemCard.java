package gfx;

import item.Item;
import util.Text;

import javax.swing.*;
import java.awt.*;

public class ItemCard extends Card {

	Item item;

	public ItemCard(Item i) {
		super();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;

		setPreferredSize(new Dimension(100, 150));

		item = i;
		JLabel name = new JLabel();
		name.setText(Text.formatForJLabel(item.getNameWithCount()));
		add(name, constraints);

		constraints.gridy++;
		JLabel desc = new JLabel();
		desc.setText(Text.formatForJLabel(i.description));
		add(desc, constraints);

	}
}
