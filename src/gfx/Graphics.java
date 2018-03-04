package gfx;

import creatures.Creature;
import creatures.Dingo;
import item.Item;
import main.Game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Graphics {

	private static ConsoleWindow console;
	private static JPanel creaturePanel;
	private static JPanel itemPanel;
	private static JPanel invPanel;

	private static JScrollPane creaturePanelScroll;
	private static JScrollPane itemPanelScroll;
	private static JScrollPane invPanelScroll;

	public static Border border = BorderFactory.createLoweredBevelBorder();

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel mainPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();


		console = new ConsoleWindow();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridheight = 10;
		mainPane.add(console, c);

		creaturePanel = new JPanel();
		creaturePanel.setBorder(BorderFactory.createTitledBorder(border, "Creatures"));
		itemPanel = new JPanel();
		itemPanel.setBorder(BorderFactory.createTitledBorder(border, "Items"));
		invPanel = new JPanel();
		invPanel.setBorder(BorderFactory.createTitledBorder(border, "Inventory"));

		creaturePanelScroll = new JScrollPane(
				creaturePanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		itemPanelScroll = new JScrollPane(
				itemPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		invPanelScroll = new JScrollPane(
				invPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);


		creaturePanelScroll.setPreferredSize(new Dimension(200, 200));
		itemPanelScroll.setPreferredSize(new Dimension(200, 200));
		invPanelScroll.setPreferredSize(new Dimension(200, 400));

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 0.5;
		c.gridheight = 1;
		mainPane.add(creaturePanelScroll, c);
		c.gridy = 1;
		mainPane.add(itemPanelScroll, c);
		c.gridy = 2;
		c.weighty = 1.0;
		mainPane.add(invPanelScroll, c);

		frame.add(mainPane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void doWindow() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
				synchronized (Game.toTakeTurn) {
					Game.toTakeTurn.notify();
				}
			}
		});
	}

	public static void send(String text) {
		console.textArea.append(text);
		console.taba.adjustHeight();
		console.scroller.setValue(console.scroller.getMinimum());
	}

	public static void sendLine(String text) {
		send(text + '\n');
	}

	public static String getLine() {
		synchronized (ConsoleWindow.cmdBuffer) {
			while (ConsoleWindow.cmdBuffer.isEmpty()) {
				try {
					ConsoleWindow.cmdBuffer.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return ConsoleWindow.cmdBuffer.poll();
		}
	}

	public static void updateZoneGraphics() {
		int components = creaturePanel.getComponentCount();
		for (int i = components - 1; i >= 0 && components != 0; i--) {
			creaturePanel.remove(i);
		}

		components = itemPanel.getComponentCount();
		for (int i = components - 1; i >= 0 && components != 0; i--) {
			itemPanel.remove(i);
		}

		for(Creature c : Game.zone.creatures) {
			creaturePanel.add(new CreatureCard(c));
		}
		for(Item i : Game.zone.items) {
			itemPanel.add(new ItemCard(i));
		}
		creaturePanelScroll.validate();
		itemPanelScroll.validate();
		creaturePanel.repaint();
		itemPanelScroll.repaint();
	}

	public static void updateInventoryGraphics() {
		int components = invPanel.getComponentCount();
		for (int i = components - 1; i >= 0 && components != 0; i--) {
			invPanel.remove(i);
		}
		for(Item i : Game.player.inv) {
			invPanel.add(new ItemCard(i));
		}
		invPanelScroll.validate();
		invPanel.repaint();
	}

	public static void refresh() {
		updateZoneGraphics();
		updateInventoryGraphics();
	}
}
