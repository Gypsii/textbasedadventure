package gfx;

import creatures.Creature;
import main.Commands;
import main.Game;
import util.CommandAction;
import util.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

		// Dude I love callbacks
		JMenuItem butcher = new JMenuItem("Butcher");
		butcher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Commands.actions.add(new CommandAction() {
					public double act() {
						return Commands.butcher(creature);
					}
				});
				synchronized (Commands.actions) {
					Commands.actions.notify();
				}
			}
		});

		JMenuItem attack = new JMenuItem("Attack");
		attack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Commands.actions.add(new CommandAction() {
					public double act() {
						return Commands.attack(creature);
					}
				});
				synchronized (Commands.actions) {
					Commands.actions.notify();
				}
			}
		});

		JMenuItem info = new JMenuItem("Information");
		info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creature.printInfo();
			}
		});

		if (creature.isAlive()) {
			menu.add(attack);
			double r = Game.player.equipped.reachBonus + Game.player.naturalAttackPattern.reach;
			if (creature.position.distSquared(Game.player.position) > r*r) {
				attack.setEnabled(false);
			}
		} else {
			menu.add(butcher);
			if (creature.position.distSquared(Game.player.position) > 2) {
				butcher.setEnabled(false);
			}
		}
		menu.add(info);
	}


}
