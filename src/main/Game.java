package main;

import creatures.Condition;
import item.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import util.FileReader;
import util.IO;
import util.Text;
import util.TurnComparator;
import crafting.Crafting;
import crafting.Recipe;
import creatures.Bird;
import creatures.Creature;
import creatures.Player;

public class Game{
	public static final int PASSIVECREATURECAP = 3;
	public static final int STARTGOLD = 50;
	
	public static final int DMG_BLUNT = 0;
	public static final int DMG_SLASH = 1;
	public static final int DMG_PIERCE = 2;
	public static final int DMG_BURN = 3;
	public static final int DMG_COLD = 4;
	public static final int DMG_MAGIC = 5;
	public static final int DMG_TYPE_COUNT = 6;
	public static final String[] DAMAGE_TYPE_STRINGS = {"bludgeoning", "slashing", "piercing", "burn", "cold", "magical"};

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
	static Comparator<Creature> comparator = new TurnComparator();
	static PriorityQueue<Creature> toTakeTurn = new PriorityQueue<Creature>(10, comparator);
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException{
		osName = System.getProperty("os.name");
		IO.println(osName);
//		for(;;){
		initialiseGame();
		runGame();		
//		}	
	}
	
	/**
	 * Creates the player, first level and shop, and gets all pre-game information from the player.
	 * @throws IOException
	 */
	public static void initialiseGame() throws IOException{
		if(osName.startsWith("Windows") && !osName.equals("Windows 10")){
			IO.ANSIEnabled = false;
		}else if(osName.equals("Windows 10")){
			IO.println("Windows 10 users may have to change console colours to black on white for best effect");
		}
		IO.print("<black><fnormal>");//resets the colour to default
		Crafting.categories.put("all", Crafting.all);
		
		FileReader.readFromFiles();
		
		player = new Player();
		startingShop = new Shop();
		levelDiff = 1;
		position = 0;
		money = STARTGOLD;
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
		toTakeTurn.add(player);
	}
	
	/**
	 * Takes the turns of all {@code Creature}s in sequence until the player dies.
	 * @throws IOException
	 */
	public static void runGame() throws IOException{
		while(player.hp > 0){
			Creature c = toTakeTurn.remove();
			if(c == player){
				double time = Commands.playerTurn();   //This has to be like this or it breaks
				player.nextActionTime += time;//I can't remember why just now
			}else{
				if(c.isAlive()){
					c.nextActionTime += c.takeTurn();
				}else{
					c.nextActionTime += 10000;
				}
			}
			toTakeTurn.add(c);
		}
		IO.println("Game over.");
	}

	
	/**
	 * Clears the turn queue and refills it with the creatures in the current zone.
	 */
	public static void doTurnList(){
		toTakeTurn.clear();
		player.nextActionTime = -0.01;
		//toTakeTurn.add(player);
		for(int i = 0; i < zone.creatures.size(); i++){
			zone.creatures.get(i).nextActionTime = 0;
			toTakeTurn.add(zone.creatures.get(i));
		}
	}
	
	
	/**
	 * Executes things that happen upon entering a new zone, such as printing descriptions.
	 */
	public static void enterZone(){
		IO.println(position / 100 + ", " + position % 100);
		zone = level.zones.get(position);
		Text.viewZone();
		targetIndex = 0;
		doTurnList();
	}
	
	/**
	 * Checks if the player is able to leave the zone with n/s/e/w commands.
	 * @return <tt>true</tt> if the player can leave the zone, <tt>false</tt> otherwise
	 */
	public static boolean canExitZone(){		
		for(int i = 0; i < zone.creatures.size(); i++){
			Creature c = zone.creatures.get(i);
			if(c.isAlive() && c.hostileTowards(player) && !c.conditions.contains(Condition.SLEEPING)){
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