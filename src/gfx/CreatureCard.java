package gfx;

import creatures.Creature;
import util.Text;

import javax.swing.*;
import java.awt.*;

public class CreatureCard extends Card {

	Creature creature;

	public CreatureCard(Creature c) {
		super();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;

		setPreferredSize(new Dimension(100, 150));

		creature = c;
		JLabel name = new JLabel();
		name.setText(Text.formatForJLabel(creature.getName()));

		JLabel hp = new JLabel();
		hp.setText(Text.formatForJLabel(creature.isAlive() ? creature.hp + "/" + creature.maxHp : "Dead"));

		add(name, constraints);
		constraints.gridy = 1;
		add(hp, constraints);
	}

}
