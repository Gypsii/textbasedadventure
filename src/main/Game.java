package main;

import effects.Condition;
import gfx.Graphics;
import item.Item;

import java.io.IOException;
import java.util.*;

import util.*;
import crafting.Crafting;
import crafting.Recipe;
import creatures.Bird;
import creatures.Creature;
import creatures.Player;

public class Game{
	public static final boolean GRAPHICS_ENABLED = false;

	public static final int PASSIVE_CREATURE_CAP = 3;
	public static final int START_GOLD = 50;

	static String osName;

	public static int targetIndex = 0;
	public static int levelNum = 0; 
	public static HashMap<Integer, Level> levels;
	public static Level level;
	public static int levelDiff;
	public static Zone zone;
	public static int position = 0;
	public static int money;
	public static Player player;
	public static Shop startingShop;
	static Comparator<TimeObject> comparator = new TurnComparator();
	public static PriorityQueue<TimeObject> toTakeTurn = new PriorityQueue<>(10, comparator);

	private static double gameTime = 0;
	public static boolean turnDebug = false;
	public static boolean wordyDebug = false;

	public static void main(String[] args) throws IOException{
		if(GRAPHICS_ENABLED){
			Graphics.doWindow();
			IO.ANSIEnabled = false;
			synchronized (toTakeTurn) { //Synced on something random
				try {
					toTakeTurn.wait(); //Wait for graphics to load before anything prints
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			osName = System.getProperty("os.name");
			if(osName.startsWith("Windows")/* && !osName.equals("Windows 10")*/) {
				IO.ANSIEnabled = false;
			}
//			}else if(osName.equals("Windows 10")){
//				IO.println("Windows 10 users may have to change console colours to black on white for best effect");
//			}
		}
		Crafting.categories.put("all", Crafting.all);

		FileReader.readFromFiles();
		for(;;){
			loadGame();
			runGame();
		}
	}
	
	/**
	 * Creates the player, first level and shop, and gets all pre-game information from the player.
	 * @throws IOException
	 */
	public static void loadGame() throws IOException{
		IO.print("<black><fnormal>");//resets the colour to default

		toTakeTurn.clear();
		player = new Player();
		startingShop = new Shop();
		levelDiff = 1;
		position = 0;
		player.position = new Position(0,0);
		money = START_GOLD;
		levels = new  HashMap<Integer, Level>();

		IO.println("Who do you think you are?");
		player.name = IO.read();
		if(player.name.equals("nocolour")){
			IO.ANSIEnabled = false;
		}
		position = 0;
		zone = new Zone();
		IO.println("Before setting out on your adventure you decide to prepare yourself.");

		player.addItem("bread", 2);
		
		startingShop.addItem("bread", 3);
		startingShop.addItem("pastry", 2);
		startingShop.addItem("mushroom", 4);
		startingShop.addItem("string", 8);
		startingShop.addItem("stickyString", 3);
		startingShop.addItem("clothCotton", 3);
		startingShop.addItem("vial", 4);
		startingShop.addItem("walrusTusk");
		startingShop.addItem("butterKnife");
		startingShop.addItem("glaiveBladeRusted");
		startingShop.addItem("spearheadCopper");
		startingShop.addItem("sswordBronze");
		
		Level.generateLevel(levelDiff);
		level = levels.get(levelNum);
		level.shop = startingShop;
		Game.level.shop.shop();
		IO.print("<blue>You set out on your adventure.<r>");
		level.printLevelDescription();
		enterZone();
	}
	
	/**
	 * Takes the turns of all {@code Creature}s in sequence until the player dies.
	 * @throws IOException
	 */
	public static void runGame() throws IOException{
		toTakeTurn.add(player);
		while(player.hp > 0){
			TimeObject t = toTakeTurn.remove();
			gameTime = t.getNextTriggerTime();
			double time;
			do {
				if(turnDebug) {
					IO.println(t.getNextTriggerTime() + ": " + t + " (now)");
					for(TimeObject t1 : toTakeTurn) {
						IO.println(t1.getNextTriggerTime() + ": " + t1);
					}
					IO.println();
				}

				time = t.resolve();
			} while (time == 0);
			if (time > 0) { // Use negative times to indicate that the TimeObject should be removed.
				t.setNextTriggerTime(gameTime + time);
				toTakeTurn.add(t);
			}
		}
		IO.println("Game over.");
		IO.printHorizontalLine();
	}

	
	/**
	 * Clears the turn queue and refills it with the creatures in the current zone.
	 */
	public static void doTurnList(){
		PriorityQueue<TimeObject> temp = new PriorityQueue<>(10, comparator);
		for (TimeObject t : toTakeTurn) {
			if (!t.removeOnZoneSwitch()) {
				temp.add(t);
			}
		}
		toTakeTurn = temp;
		for(int i = 0; i < zone.creatures.size(); i++){
			zone.creatures.get(i).setNextTriggerTime(player.getNextTriggerTime() + 0.01);
			toTakeTurn.add((TimeObject) zone.creatures.get(i));
		}
	}

	public static double getTime() {
		return gameTime;
	}

	/**
	 * Puts t at the start of the turn order.
	 */
	public static void insertIntoTurnOrder(TimeObject t) {
		double time;
		if(toTakeTurn.isEmpty()) {
			time = 0.0001;
		} else {
			time = toTakeTurn.peek().getNextTriggerTime();
		}
		t.setNextTriggerTime(time - 0.0001);
		toTakeTurn.add(t);
	}
	
	/**
	 * Executes things that happen upon entering a new zone.
	 */
	public static void enterZone(){
		zone.inFocus = false;
		IO.println(position / 100 + ", " + position % 100);
		zone = level.zones.get(position);
		zone.inFocus = true;
		if(GRAPHICS_ENABLED){
			Graphics.updateZoneGraphics();
		}
		targetIndex = 0;
		zone.addCreature(player, player.position);
		doTurnList();
	}
	
	/**
	 * Checks if the player is able to leave the zone with n/s/e/w commands.
	 * @return <tt>true</tt> if the player can leave the zone, <tt>false</tt> otherwise
	 */
	public static boolean canExitZone(){		
		for(int i = 0; i < zone.creatures.size(); i++){
			Creature c = zone.creatures.get(i);
			if(c.isAlive() && c.hostileTowards(player) && !c.conditions.contains(Condition.SLEEPING) && !c.conditions.contains(Condition.SEALED)){
				return false;
			}
		}
		return true;		
	}
	
	
	public static boolean canAfford(Recipe r, boolean printReason){//Move to crafting?
		for(int i = 0; i < r.components.size(); i++){
			int loc = player.findItemLoc(Item.item(r.components.get(i)));
			if(loc == -1 || r.componentCounts.get(i) > player.inv.get(loc).count){
				if(printReason){
					IO.println("<red>You lack the necessary " + Item.item(r.components.get(i)).name + "<r>");
				}
				return false;
			}
		}
		return true;
	}
	
	public static void craft(Recipe r){//Move to crafting?
		if(r.product != null){
			if(canAfford(r, true)){
				for(int i = 0; i < r.components.size(); i++){
					int loc = player.findItemLoc(Item.item(r.components.get(i)));
					player.inv.get(loc).count -= r.componentCounts.get(i);
					if(player.inv.get(loc).count < 1){
						player.removeItem(loc);
					}
				}
				Item product = Item.item(r.product);
				if(r.productCount != -1){
					product = product.clone();//Preserve the count of the original item in the map
					product.count = r.productCount;
				}
				player.addItem(product);
				IO.println("<green>Crafting of " + product.name + " successful!<r>");
			}
		}
	}
	
	public static void triggerBirds(){//TODO Seriously why the fuck is this done this way
		for(int i = 0; i < zone.creatures.size(); i++){
			if(zone.creatures.get(i).getClass() == new Bird(0).getClass()){
				if(zone.creatures.get(i).isAlive()){
					zone.creatures.get(i).itemStealTrigger();
				}
			}
		}
		zone.containsEgg = false;
		for(int i = 0; i < zone.items.size(); i++){
			if(zone.items.get(i).id.equals("egg")){
				zone.containsEgg = true;
			}
		}
	}
}