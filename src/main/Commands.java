package main;

import crafting.alchemy.Alchemical;
import crafting.alchemy.Alchemy;
import crafting.alchemy.Potion;
import gfx.Graphics;
import item.Item;
import item.MagicItem;
import item.Scroll;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import util.*;
import crafting.Category;
import crafting.Crafting;
import crafting.FoodRecipe;
import creatures.Creature;

public class Commands {

	public static Queue<CommandAction> actions = new ArrayDeque<>();
	public static boolean waiting = false;

	private static double printHelp() {
		//Free	nosuwyz,+-;
		//Used	abcdefghijklmpqrtvx.?$
		IO.println("North: k");
		IO.println("South: j");
		IO.println("East: l");
		IO.println("West: h");
		IO.println("Attack: a");
		IO.println("Target enemy: t");
		IO.println("Take item: g");
		IO.println("Drop item: d");
		IO.println("Equip weapon/armour: q");
		IO.println("Make items: m");
		IO.println("Use potion: z");
		IO.println("Brew potion: p");
		IO.println("Cook food: c");
		IO.println("Eat food: f");
		IO.println("View unlocked recipes: r");
		IO.println("Butcher target: b");
		IO.println("Enchant: e");
		IO.println("Haggle with target: $");
		IO.println("Inspect item in inventory: i");
		IO.println("View target info: v");
		IO.println("Wait: .");
		IO.println("Exit final zone: x");
		if(Game.player.name.equals("debug")) {
			IO.println("Spawn item: !item itemcode");
			IO.println("Spawn creature: !creature creaturecode");
			IO.println("Spawn random setpiece: !setpiece");
			IO.println("Heal fully: !heal");
			IO.println("Clear zone: !clear");
			IO.println("Calm enemies: !calm");
			IO.println("Rename target: !rename newname");
		}
		return 0;
	}

	/**
	 * Reads in a player command and executes the turn.
	 * @return the number of standard actions the turn took.
	 * @throws IOException
	 */
	public static double playerTurn() throws IOException{
		if(Game.GRAPHICS_ENABLED){
			Graphics.refresh();
		}
		Text.viewZone();

		CommandAction a = IO.getAction();
		if(!a.useString) {
			return a.act();
		}

		String commandRaw = a.cmd;
		String command = commandRaw.toLowerCase();
		if(command.equals("a") || command.equals("attack")){//TODO maybe lambdas in a map would be cleaner?
			return Commands.commandAttack();
		}
		if(command.equals("k") || command.equals("north")){
			return Commands.commandNorth();
		}
		if(command.equals("j") || command.equals("south")){
			return Commands.commandSouth();
		}
		if(command.equals("l") || command.equals("east")){
			return Commands.commandEast();
		}
		if(command.equals("h") || command.equals("west")){
			return Commands.commandWest();
		}
		if(command.equals("list") || command.equals("look")){
			Text.viewZone();
			return 0;
		}
		if(command.equals(".") || command.equals("wait")){
			return Commands.commandWait();
		}
		if(command.equals("g") || command.equals("take") || command.equals("grab")){
			return Commands.commandTake();
		}
		if(command.equals("t") || command.equals("target")){
			return Commands.commandTarget();
		}
		if(command.equals("v") || command.equals("info")){
			return Commands.commandInfo();
		}
		if(command.equals("i") || command.equals("inspect") || command.equals("inv")){
			return Commands.commandInspect();
		}
		if(command.equals("help") || command.equals("?")){	
			printHelp();
		}
		if(command.equals("q") || command.equals("equip")){
			return Commands.commandEquip();
		}
		if(command.equals("m") || command.equals("create") || command.equals("craft") || command.equals("make")){
			return Commands.commandCraft();
		}
		if(command.equals("d") || command.equals("drop")){
			return Commands.commandDrop();
		}
		if(command.equals("x") || command.equals("exit")){
			return Commands.commandExit();
		}
		if(command.equals("$") || command.equals("haggle")){
			return Commands.commandHaggle();	
		}
		if(command.equals("b") || command.equals("butcher")){
			return Commands.commandButcher();	
		}
		if(command.equals("c") || command.equals("cook")){
			return Commands.commandCook();
		}
		if(command.equals("f") || command.equals("eat") || command.equals("food")){
			return Commands.commandEat();
		}
		if(command.equals("p") || command.equals("potion") || command.equals("quaff")){
			return Commands.commandPotion();
		}
		if(command.equals("z") || command.equals("brew")){
			return Commands.commandBrew();
		}
		if(command.equals("r")){
			FoodRecipe.listRecipes();
			return 0;
		}
		if(command.equals("e") || command.equals("enchant")){
			return Commands.commandEnchant();
		}
		if(command.startsWith("!") && command.length() > 1 && Game.player.name.equals("debug")){
			return TestEnvironment.debugCommands(commandRaw);
		}
		IO.println("<red>Invalid Command<r>");
		return 0;
	}

	private static double commandAttack(){
		ArrayList<Creature> a = new ArrayList<>();
		double r = Game.player.equipped.reachBonus + Game.player.naturalAttackPattern.reach;
		for (int x = 0; x < Zone.TILE_COUNT; x++) {
			for (int y = 0; y < Zone.TILE_COUNT; y++) {
				Position p = new Position(x,y);
				if(p.equals(Game.player.position)) {
					continue;
				}
				if(Game.player.position.distSquared(p) <= r*r) {
					if(Game.zone.getTile(p).creature != null) {
						a.add(Game.zone.getTile(p).creature);
					}
				}
			}
		}
		if(a.isEmpty()){
			IO.println("There is nothing in range to attack");
			return 0;
		} else {
			Text.listTargets(a);
			int n = 0;
			try {
				n = IO.readInt(0, a.size(), "<red>There is no creature with that ID!<r>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(n == -1) {
				return 0;
			}
			return attack(a.get(n));
		}
	}

	private static double commandNorth(){
		if(Game.player.position.y < Zone.TILE_COUNT -1){
			if(Game.player.canMoveInto(Game.player.position.add(new Position(0,1)))) {
				Game.zone.getTile(Game.player.position).creature = null;
				Game.player.position.y++;
				Game.zone.getTile(Game.player.position).creature = Game.player;
				return Game.player.movementTime;
			}
			IO.println("<red>You cannot move there.<r>");
			return 0;
		}
		if(Game.canExitZone()){
			if(Game.position < Level.LEVEL_SIZE * 100){
				IO.println("<blue>You walk North<r>");
				Game.position += 100;
				Game.zone.getTile(Game.player.position).creature = null;
				Game.player.position.y = 0;
				Game.enterZone();
			}else{
				IO.println("<red>You cannot walk North<r>");
			}

		}else {
			IO.println("<red>There are still creatures in this zone<r>");
		}
		return 0;
	}

	private static double commandSouth(){
		if(Game.player.position.y > 0){
			if(Game.player.canMoveInto(Game.player.position.add(new Position(0,-1)))) {
				Game.zone.getTile(Game.player.position).creature = null;
				Game.player.position.y--;
				Game.zone.getTile(Game.player.position).creature = Game.player;
				return Game.player.movementTime;
			}
			IO.println("<red>You cannot move there.<r>");
			return 0;
		}
		if(Game.canExitZone()){
			if(Game.position >= 100){
				IO.println("<blue>You walk South<r>");
				Game.position -= 100;
				Game.zone.getTile(Game.player.position).creature = null;
				Game.player.position.y = Zone.TILE_COUNT-1;
				Game.enterZone();
			}else{
				IO.println("<red>You cannot walk South<r>");
			}
		}else{
			IO.println("<red>There are still creatures in this zone<r>");
		}
		return 0;
	}

	private static double commandWest(){
		if(Game.player.position.x > 0){
			if(Game.player.canMoveInto(Game.player.position.add(new Position(-1,0)))) {
				Game.zone.getTile(Game.player.position).creature = null;
				Game.player.position.x--;
				Game.zone.getTile(Game.player.position).creature = Game.player;
				return Game.player.movementTime;
			}
			IO.println("<red>You cannot move there.<r>");
			return 0;
		}
		if(Game.canExitZone()){
			if(Game.position % 100 > 0){
				IO.println("<blue>You walk West<r>");
				Game.position -= 1;
				Game.zone.getTile(Game.player.position).creature = null;
				Game.player.position.x = Zone.TILE_COUNT-1;
				Game.enterZone();
			}else{
				IO.println("<red>You cannot walk West<r>");
			}
		}else{
			IO.println("<red>There are still creatures in this zone<r>");
		}
		return 0;
	}

	private static double commandEast(){
		if(Game.player.position.x < Zone.TILE_COUNT -1){
			IO.println(""+Game.player.position.add(new Position(1,0)));
			if(Game.player.canMoveInto(Game.player.position.add(new Position(1,0)))) {
				Game.zone.getTile(Game.player.position).creature = null;
				Game.player.position.x++;
				Game.zone.getTile(Game.player.position).creature = Game.player;
				return Game.player.movementTime;
			}
			IO.println("<red>You cannot move there.<r>");
			return 0;
		}
		if(Game.canExitZone()){
			if(Game.position % 100 < Level.LEVEL_SIZE){
				IO.println("<blue>You walk East<r>");
				Game.position += 1;
				Game.zone.removeCreature(Game.player);
				Game.player.position.x = 0;
				Game.enterZone();
			}else{
				IO.println("<red>You cannot walk East<r>");
			}
		}else{
			IO.println("<red>There are still creatures in this zone<r>");
		}
		return 0;
	}

	private static double commandExit() throws IOException{
		if(Game.zone.isFinalZone){
			if(Game.canExitZone()){
				Game.player.hp = Math.max(Game.player.hp, (int)(Game.player.maxHp * 0.75));
				Game.levelNum++;
				Game.levelDiff++;
				Level.generateLevel(Game.levelDiff);
				Game.level = Game.levels.get(Game.levelNum);
				Game.level.shop.shop();
				Game.position = 0;
				Game.player.position = new Position(0,0);
				Game.zone = Game.level.zones.get(0);
				IO.println("You arrive in the next level.");
				Game.level.printLevelDescription();
				Game.enterZone();
			}else{
				IO.println("<red>There are still creatures in this zone!<r>");
			}
		}else{
			IO.println("<red>This is not the final zone!.<r>");	
		}
		return 0;
	}

	private static double commandCook() throws IOException{
		ArrayList<Item> foods = new ArrayList<Item>();
		Boolean finishingRecipe;
		for(;;){
			Text.listInvCookable();
			IO.println(Game.player.inv.size() + ": Finish");
			int n = IO.readInt(0, Game.player.inv.size()+1, "<red>There is no item with that ID!<r>");
			if(n == -1) {
				finishingRecipe = false;
				Game.player.inv.addAll(foods);
				break;
			}
			if(n == Game.player.inv.size()){
				finishingRecipe = true;
				for(int i = 0; i < foods.size(); i++){
					foods.get(i).count --;
					if(foods.get(i).count > 0){
						Game.player.addItem(foods.get(i));
					}
				}
				break;
			}
			if(Game.player.inv.get(n).hasTag("cookable")){
				foods.add(Game.player.inv.get(n));
				Game.player.inv.remove(n);
			}else{
				IO.println("<red>This cannot be used as a food.<r>");
			}
		}
		if(finishingRecipe){
			String result = "failure";
			int xp = 0;
			for(int i = 0; i < FoodRecipe.recipes.size(); i++){
				if(FoodRecipe.recipes.get(i).check(foods)){
					result = FoodRecipe.recipes.get(i).product;
					xp = FoodRecipe.recipes.get(i).xp;
					break;
				}
			}
			IO.println("<blue>You made " + Item.item(result).prefix + " " + Item.item(result).name + "<r>");
			Game.player.skills.incrementSkill(SkillSet.COOK, xp);
			Game.player.addItem(result);
			return 10;
		}
		return 0;
	}

	private static double commandTarget() throws IOException{
		if(Game.zone.creatures.size() == 0) {
			IO.println("<red>There are no creatures in the zone to target.<r>");
			return 0;
		}
		Text.listTargets(Game.zone.creatures);
		int n = IO.readInt(0, Game.zone.creatures.size(), "<red>There is no creature with that ID!<r>");
		if(n != -1) {
			Game.targetIndex = n;
		}
		return 0;
	}

	private static double commandWait() throws IOException{
		IO.println("Wait for how long?");
		double n = 0;
		try{
			n = Double.parseDouble(IO.read());
			if(n > 0){
				IO.println("<blue>You do nothing<r>");
				return n;
			}else{
				IO.println("<red>You can only wait a positive length of time!<r>");
			}
		}catch(NumberFormatException nfe){
			IO.println("<red>Invalid Format!<r>");
		}	
		IO.println("<blue>You do nothing<r>");
		return 0;
	}

	private static double commandInfo(){
		if(Game.zone.creatures.size() > Game.targetIndex){
			Game.zone.creatures.get(Game.targetIndex).printInfo();
		}else{
			IO.println("<red>There is no creature with that ID!<r>");
		}
		return 0;
	}

	private static double commandInspect() throws IOException{
		Text.listInv();
		int n = IO.readInt(0, Game.player.inv.size(), "<red>There is no item with that ID!<r>");
		if(n == -1){
			return 0;
		}
		Game.player.inv.get(n).printInfo();
		return 0;
	}

	private static double commandEquip() throws IOException{
		Text.listInv();
		int n = IO.readInt(0, Game.player.inv.size(), "There is no item with that ID!");
		if(n == -1){
			return 0;
		}
		return equip(Game.player.inv.get(n));
	}

	private static double commandTake() throws IOException{
		if(Game.zone.items.size() == 0){
			IO.println("There are no items to be taken.");
			return 0;
		}
		Text.listItems();
		int n = IO.readInt(0, Game.zone.items.size(), "There is no item with that ID!");
		if(n == -1){
			return 0;
		}
		return take(Game.zone.items.get(n));
	}

	private static double commandDrop() throws IOException{
		Text.listInv();
		int n = IO.readInt(0, Game.player.inv.size(), "<red>There is no item with that ID!<r>");
		if(n == -1){
			return 0;
		}
		return drop(Game.player.inv.get(n));
	}

	private static double commandCraft() throws IOException{
		Category c = Crafting.all;
		while(true){
			IO.println("(-1) Exit");
			Crafting.listContents(c);
			int n = IO.readInt(0, c.subcategories.size() + c.recipes.size(), "<red>There is no recipe or category with that ID!<r>");
			if(n == -1){
				return 0;
			}else if(n < c.subcategories.size()){
				Category newC = c.subcategories.get(n);
				if(newC.recipes.isEmpty() && newC.subcategories.isEmpty()){
					IO.println("This category is empty.");
				}else{
					c = newC;
				}
			}else{
				Game.craft(c.recipes.get(n - c.subcategories.size()));
			}
		}
	}

	private static double commandHaggle() throws IOException{
		Creature c = Game.zone.creatures.get(Game.targetIndex);
		if(c.shopInv.size() > 0 && !c.hostileTowards(Game.player) && c.isAlive()){
			while(true){
				IO.println("You have " + Game.money + " gold");
				IO.println("-1: Exit");
				for(int i = 0; i < c.shopInv.size(); i++){
					IO.println(i + ": " + c.shopInv.get(i).name + " (" + c.shopInv.get(i).count + ", " + c.prices.get(c.shopInv.get(i).name) + " gold)");
				}
				int n = IO.readInt(0,  c.shopInv.size(), "<red>There is no item with that ID!<r>");
				if(n == -1){
					break;
				}
				if(Game.money < c.prices.get(c.shopInv.get(n).name)){
					IO.println("<red>You cannot afford that item<r>");
				}else{
					IO.println("<green>You bought the " + c.shopInv.get(n).name + " for " + c.prices.get(c.shopInv.get(n).name) + " gold<r>");
					Item item = c.shopInv.get(n);
					Item clone = item.clone();
					clone.count = 1;
					Game.player.addItem(clone);
					Game.money -= c.prices.get(item.name);
					c.addItem(Item.item("money"), c.prices.get(item.name));
					item.count--;
					if(item.count < 1){
						c.shopInv.remove(n);
					}
				}
			} 			
		}else{
			if(c.isAlive()) {
				IO.print("<red>The target does not want to sell you anything.<r>");
			} else {
				IO.print("<red>The target is dead.<r>");
			}

		}
		return 0;
	}

	private static double commandButcher() throws IOException{
		ArrayList<Creature> a = new ArrayList<>();
		for (int x = -1; x < Zone.TILE_COUNT; x++) {
			for (int y = 0; y < Zone.TILE_COUNT; y++) {
				Position p = new Position(x,y);
				if(p.equals(Game.player.position) || Game.player.position.distSquared(p) > 2) {
					continue;
				}
				if(Game.zone.getTile(p).creature != null && !Game.zone.getTile(p).creature.isAlive()) {
					a.add(Game.zone.getTile(p).creature);
				}
			}
		}
		if(a.isEmpty()){
			IO.println("There is nothing in range to butcher");
			return 0;
		} else {
			Text.listTargets(a);
			int n = 0;
			try {
				n = IO.readInt(0, a.size(), "<red>There is no creature with that ID!<r>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (n == -1) {
				return 0;
			}
			return butcher(a.get(n));
		}
	}

	private static double commandEat() throws IOException{
		Text.listInvEdible();
		int n = IO.readInt(0, Game.player.inv.size(), "<red>There is no item with that ID!<r>");
		if(n == -1) {
			return 0;
		}
		if(Game.player.inv.get(n).hasTag("edible")){
			return eat(Game.player.inv.get(n));
		}else{
			IO.println("<red>The selected item is not a food!<r>");
		}
		return 0;
	}

	private static double commandEnchant() throws IOException{
		boolean exiting = false;
		Item a = null;
		Scroll s = null;
		Item g;
		for(;;){
			IO.println("Select an item to enchant:");
			Text.listInvEnchantable();
			int n = IO.readInt(0, Game.player.inv.size(), "<red>There is no item with that ID!<r>");
			if(n == -1){
				exiting = true;
				break;
			}
			a = Game.player.inv.get(n);
			if(a.hasTag("enchanted") || a.hasTag("wear_ring") || a.hasTag("wear_cloak") || a.hasTag("wear_hat")){
				a.count--;
				if(a.count <= 0){
					Game.player.removeItem(n);
				}
				break;
			}else{
				IO.println("<red>The selected item is not enchantable<r>");
			}
		}
		if(!exiting && !a.hasTag("enchanted")){
			while(!exiting){
				IO.println("Select a scroll:");
				Text.listInvScrolls();
				int n = IO.readInt(0, Game.player.inv.size());
				if(n == -1){
					exiting = true;
					Game.player.addItem(a);
					break;
				}
				if(Game.player.inv.get(n).getClass().equals(Scroll.scrollCrit.getClass())){
					s = (Scroll) Game.player.inv.get(n);
					break;
				}else{
					IO.println("<red>The selected item is not a scroll<r>");
				}
			}
		}	
		while(!exiting){
			IO.println("Select a gem:");
			Text.listInvGems();
			int n = IO.readInt(0, Game.player.inv.size());
			if(n == -1){
				Game.player.addItem(a);
				break;
			}
			if(Game.player.inv.get(n).hasTag("gem")){
				g = Game.player.inv.get(n);
				if(a.hasTag("enchanted")){
					Game.player.addItem(MagicItem.improveEnchant(a));
				}else{
					Game.player.addItem(MagicItem.playerEnchant(a, s, g));
				}
				g.count--;
				if(g.count <= 0){
					Game.player.removeItem(n);
				}
				return 5;
			}else{
				IO.println("<red>The selected item is not a Gem<r>");
			}
		}
		return 0;
	}

	private static double commandPotion() throws IOException{
		Text.listInvWithTag(Tag.tag("vessel"));
		int n = IO.readInt(0, Game.player.inv.size(), "<red>There is no item with that ID!<r>");
		if(n == -1){
			return 0;
		}
		Item i = Game.player.inv.get(n);
		if(i.potion == null) {
			IO.println("<red>There selected item is not a potion!<r>");
			return 0;
		}
		if(Game.player.equipped == i){
			Game.player.equip(Item.unarmed);
		}else if(Game.player.armourChest == i){
			Game.player.equip("unarmoured");
			Game.player.refreshArmour();
		}else if(Game.player.ring == i){
			Game.player.equip("unarmouredRing");
			Game.player.refreshArmour();
		}else if(Game.player.cloak == i){
			Game.player.equip("unarmouredCloak");
			Game.player.refreshArmour();
		}else if(Game.player.hat == i){
			Game.player.equip("unarmouredHat");
			Game.player.refreshArmour();
		}
		i.potion.applyInternal(Game.player);
		i.potion = null;
		i.name = Item.item(i.id).name;
		return 0.5;
	}

	private static double commandBrew() throws IOException{
		ArrayList<Item> ingredients = new ArrayList<Item>();
		Item vessel;
		Boolean finishingRecipe;
		for(;;){
			Text.listInvWithTag(Tag.tag("vessel"));
			int n = IO.readInt(0, Game.player.inv.size(), "<red>There is no item with that ID!<r>");
			if(n == -1) {
				return 0;
			}
			if(Game.player.inv.get(n).hasTag("vessel")){
				vessel = Game.player.inv.get(n);
				break;
			}else{
				IO.println("<red>This cannot be used as a potion vessel.<r>");
			}
		}
		for(;;){
			Text.listInvWithTag(Tag.tag("alchemical"));
			IO.println(Game.player.inv.size() + ": Finish");
			int n = IO.readInt(0, Game.player.inv.size()+1, "<red>There is no item with that ID!<r>");
			if(n == -1) {
				finishingRecipe = false;
				Game.player.inv.addAll(ingredients);
				break;
			}
			if(n == Game.player.inv.size()){
				finishingRecipe = true;
				for(int i = 0; i < ingredients.size(); i++){
					ingredients.get(i).count--;
					if(ingredients.get(i).count > 0){
						Game.player.addItem(ingredients.get(i));
					}
				}
				break;
			}
			if(Game.player.inv.get(n).hasTag("alchemical")){
				ingredients.add(Game.player.inv.get(n));
				Game.player.inv.remove(n);
			}else{
				IO.println("<red>This cannot be used as a potion ingredient.<r>");
			}
		}
		if(finishingRecipe){
			vessel.count--;
			if(vessel.count <= 0){
				Game.player.removeItem(vessel);
			}
			vessel = vessel.clone();
			vessel.count = 1;
			if (vessel.potion == null) {
				vessel.potion = new Potion();
			}
			for(Item a : ingredients) {
				vessel.potion.concentrations.put(Alchemy.effects.get(a.id), 1.0);
			}
			vessel.name = "Potion";
			vessel.isStackable = false;
			vessel.addTag("drink");
			Game.player.addItem(vessel);
//			IO.println("Potion: " + vessel.potion);
			IO.println("<blue>You made a potion<r>");
			return 10;
		}
		return 0;
	}


	public static double take(Item i) {
		IO.println("<blue>You took the " + i.getNameWithCount() + "<r>");
		if(i.id.equals("money")){
			Game.money += i.count;
			IO.println("<yellow>You now have " + Game.money + " gold<r>");
		}else{
			Game.player.addItem(i);
		}
		if(i.id.equals("egg")){
			Game.triggerBirds();
		}
		Game.zone.removeItem(i);
		return 0;
	}

	public static double drop(Item i) {
		Game.zone.addItem(i);
		IO.println("<blue>You dropped the " + i.getNameWithCount() + "<r>");
		if(Game.player.equipped == i){
			Game.player.equip(Item.unarmed);
		}else if(Game.player.armourChest == i){
			Game.player.equip("unarmoured");
			Game.player.refreshArmour();
		}else if(Game.player.ring == i){
			Game.player.equip("unarmouredRing");
			Game.player.refreshArmour();
		}else if(Game.player.cloak == i){
			Game.player.equip("unarmouredCloak");
			Game.player.refreshArmour();
		}else if(Game.player.hat == i){
			Game.player.equip("unarmouredHat");
			Game.player.refreshArmour();
		}
		Game.player.removeItem(i);
		return 0;
	}

	public static double eat(Item f) {
		IO.println("<blue>You ate the " + f.getNameWithCount() + ", restoring " + f.healthRestore + " health.<r>");
		if(Game.player.equipped == f){//If there's ever edible armour that might need to go in here
			Game.player.equipped = Item.unarmed;
		}
		Game.player.hp += f.healthRestore;
		Game.player.hp = Math.min(Game.player.maxHp, Game.player.hp);
		IO.println("You are now on " + Game.player.hp + " hp");
		f.count--;
		if(f.count <= 0){
			Game.player.removeItem(f);
		}
		return 1;
	}

	public static double equip(Item item) {
		Game.player.equip(item);
		return 1;
	}

	public static double attack(Creature c) {
		AttackHandler.attack(Game.player, c);
		return Game.player.equipped.swingTime;
	}

	public static double butcher(Creature c) {
		IO.println("<blue>You butchered the " + c.getName() + "<r>");
		c.dropButcherItems();
		Game.zone.removeCreature(c);
		return 10;
	}
}