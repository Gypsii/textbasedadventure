package main;

import item.Item;
import item.MagicItem;
import item.Scroll;

import java.io.IOException;
import java.util.ArrayList;

import util.AttackHandler;
import util.IO;
import util.TestEnvironment;
import util.Text;
import crafting.Category;
import crafting.Crafting;
import crafting.FoodRecipe;
import creatures.Creature;

public class Commands {

	private static double printHelp() {
		//Free	gjopury,+-
		//Used	abcdefhiklmnqstvwxz.?
		IO.println("North: n");					
		IO.println("South: s");
		IO.println("East: e");
		IO.println("West: w");
		IO.println("Attack: a");
		IO.println("Target enemy: t");
		IO.println("Take item: k");
		IO.println("Drop item: d");
		IO.println("Equip weapon/armour: q");
		IO.println("Make items: m");
		IO.println("Cook food: c");
		IO.println("Eat food: f");
		IO.println("View unlocked recipes: r");
		IO.println("Butcher target: b");
		IO.println("Enchant: x");
		IO.println("Haggle with target: h");
		IO.println("Look at zone: l");
		IO.println("Inspect item: i");
		IO.println("View Target info: v");
		IO.println("Wait: .");
		IO.println("Leave final zone: z");
		return 0;
	}

	/**
	 * Reads in a player command and exceutes the turn.
	 * @return the number of standard actions the turn took.
	 * @throws IOException
	 */
	public static double playerTurn() throws IOException{
		String command = IO.read().toLowerCase();
		if(command.equals("dingo stole my baby")){
			Game.zone.addItem(Item.item("shanker"));
			return 0;
		}
		if(command.equals("a") || command.equals("attack")){//TODO maybe lambdas in a map would be cleaner?
			return Commands.commandAttack();
		}
		if(command.equals("n") || command.equals("north")){
			return Commands.commandNorth();
		}
		if(command.equals("s") || command.equals("south")){		
			return Commands.commandSouth();
		}
		if(command.equals("e") || command.equals("east")){			
			return Commands.commandEast();
		}
		if(command.equals("w") || command.equals("west")){
			return Commands.commandWest();
		}
		if(command.equals("l") || command.equals("list") || command.equals("look")){
			Text.viewZone();
			return 0;
		}
		if(command.equals(".") || command.equals("wait")){
			return Commands.commandWait();
		}
		if(command.equals("k") || command.equals("take")){
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
		if(command.equals("z")){
			return Commands.commandExit();
		}
		if(command.equals("h") || command.equals("haggle")){
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
		if(command.equals("r")){
			FoodRecipe.listRecipes();
			return 0;
		}
		if(command.equals("x") || command.equals("enchant")){
			return Commands.commandEnchant();
		}
		if(command.equals("!") && Game.player.name.equals("debug")){
			return TestEnvironment.debugCommands();
		}
		IO.print("<red>Invalid Command<r>");
		return 0;
	}

	private static double commandAttack(){
		if(Game.zone.creatures.isEmpty()){
			IO.println("There is nothing to attack");
			return 0;
		}else if(Game.zone.creatures.size() > Game.targetIndex){
			Creature c = Game.zone.creatures.get(Game.targetIndex);
			AttackHandler.attack(Game.player, c);
			return Game.player.equipped.swingTime;
		}else{
			IO.println("<red>There is no creature with that ID!<r>");
			return 0;
		}
	}

	private static double commandNorth(){
		if(Game.canExitZone()){
			if(Game.position < Level.LEVEL_SIZE * 100){
				IO.println("<blue>You walk North<r>");
				Game.position += 100;
				Game.enterZone();
			}else{
				IO.println("<red>You cannot walk North<r>");
			}
			
		}else{
			IO.println("<red>There are still creatures in this zone<r>");
		}
		return 0;
	}

	private static double commandSouth(){
		if(Game.canExitZone()){
			if(Game.position >= 100){
				IO.println("<blue>You walk South<r>");
				Game.position -= 100;
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
		if(Game.canExitZone()){
			if(Game.position % 100 > 0){
				IO.println("<blue>You walk West<r>");
				Game.position -= 1;
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
		if(Game.canExitZone()){
			if(Game.position % 100 < Level.LEVEL_SIZE){
				IO.println("<blue>You walk East<r>");
				Game.position += 1;
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
			Game.player.skills.incrementSkill(Skillset.COOK, xp);
			Game.player.addItem(result);
			return 10;
		}
		return 0;
	}

	private static double commandTarget() throws IOException{
		Text.listTargets();
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
			n = Double.parseDouble(Game.br.readLine());
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
		Game.player.equip(n);
		return 0.2;
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
		IO.println("<blue>You took the " + Game.zone.items.get(n).getNameWithCount() + "<r>"); 
		if(Game.zone.items.get(n).id.equals("money")){
			Game.money += Game.zone.items.get(n).count;
			IO.println("<yellow>You now have " + Game.money + " gold<r>"); 
		}else{
			Game.player.addItem(Game.zone.items.get(n));
		}
		if(Game.zone.items.get(n).id.equals("egg")){
			Game.triggerBirds();
		}
		Game.zone.items.remove(n);	
		return 0;
	}

	private static double commandDrop() throws IOException{
		Text.listInv();
		int n = IO.readInt(0, Game.player.inv.size(), "<red>There is no item with that ID!<r>");
		if(n == -1){
			return 0;
		}
		Item i = Game.player.inv.get(n);
		Game.zone.items.add(i);
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
		Game.player.removeItem(n);	
		return 0;
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
		if(Game.zone.creatures.size() > Game.targetIndex){
			if(!Game.zone.creatures.get(Game.targetIndex).isAlive()){
				IO.println("<blue>You butchered the " + Game.zone.creatures.get(Game.targetIndex).name + "<r>");
				Game.zone.creatures.get(Game.targetIndex).dropButcherItems();
				return 3;
			}else{
				IO.println("<red>This creature is still alive!<r>");
				return 0;
			}
		}else{
			IO.println("<red>There is no creature with that ID!<r>");
			return 0;
		}	
	}

	private static double commandEat() throws IOException{
		Text.listInvEdible();
		int n = IO.readInt(0, Game.player.inv.size(), "<red>There is no item with that ID!<r>");
		if(Game.player.inv.get(n).hasTag("edible")){
			Item f = Game.player.inv.get(n);
			IO.println("<blue>You ate the " + f.getNameWithCount() + ", restoring " + f.healthRestore + " health.<r>");
			if(Game.player.equipped == f){//If there's ever edible armour that might need to go in here
				Game.player.equipped = Item.unarmed;
			}
			Game.player.hp += f.healthRestore;
			Game.player.hp = Math.min(Game.player.maxHp, Game.player.hp);
			IO.println("You are now on " + Game.player.hp + " hp");
			f.count--;
			if(f.count <= 0){
				Game.player.removeItem(n);
			}
			return 1;
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

}
