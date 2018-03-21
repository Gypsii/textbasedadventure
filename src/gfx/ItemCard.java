package gfx;

import item.Item;
import main.Commands;
import main.Game;
import util.CommandAction;
import util.IO;
import util.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

		// Dude I love callbacks
		JMenuItem take = new JMenuItem("Take");
		take.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Commands.actions.add(new CommandAction() {
					public double act() {
						if(Game.zone.exactItemLoc(item) == -1) {
							return 0;
						}
						return Commands.take(item);
					}
				});
				synchronized (Commands.actions) {
					Commands.actions.notify();
				}
			}
		});

		JMenuItem drop = new JMenuItem("Drop");
		drop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Commands.actions.add(new CommandAction() {
					public double act() {
						if(Game.player.exactItemLoc(item) == -1) {
							return 0;
						}
						return Commands.drop(item);
					}
				});
				synchronized (Commands.actions) {
					Commands.actions.notify();
				}
			}
		});

		JMenuItem eat = new JMenuItem("Eat");
		eat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Commands.actions.add(new CommandAction() {
					public double act() {
						if(Game.player.exactItemLoc(item) == -1 || !item.hasTag("edible")) {
							return 0;
						}
						return Commands.eat(item);
					}
				});
				synchronized (Commands.actions) {
					Commands.actions.notify();
				}
			}
		});

		JMenuItem equip = new JMenuItem("Equip");
		equip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Commands.actions.add(new CommandAction() {
					public double act() {
						if(Game.player.exactItemLoc(item) == -1) {
							return 0;
						}
						return Commands.equip(item);
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
				item.printInfo();
			}
		});

		if (Game.player.inv.contains(item)) {
			menu.add(equip);
			if(item.hasTag("edible")) {
				menu.add(eat);
			}
			menu.add(drop);
		} else {
			menu.add(take);
			if (item.position.distSquared(Game.player.position) > 2) {
				take.setEnabled(false);
			}
		}
		menu.add(info);

	}
}
