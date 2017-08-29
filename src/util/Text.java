package util;

import item.Scroll;
import main.Game;
import creatures.Creature;

public class Text {
	
	/**
	 * Returns a new string with the first character of the given string in upper case.
	 * 
	 * @param s
	 * @return the given string capitalised.
	 */
	public static String capitalised(String s){
		if(s.length() == 0){
			return "";
		}
		return s.substring(0, 1) + s.substring(1);
	}
	
	public static void attackMessage(Creature attacker, Creature attackee, int damage, int resist_id, String verb){//TODO better name? Also damage names
		attackMessage(attacker, attackee, damage, resist_id, verb, "");
	}
	
	public static void attackMessage(Creature attacker, Creature attackee, int damage, int resist_id, String verb, String weaponName){//TODO better name? Also damage names
		String s = "";
		if(attacker == Game.player){
			s += "<lblue>You";
		}else{
			s += capitalised(getDefName(attacker));
		}
		s += " " + verb + " ";
		if(attackee == Game.player){
			if(attacker == attackee){
				s += "yourself";
			}else{
				s += "you";
			}
		}else{
			if(attacker == attackee){
				s += "itself";
			}else{
				s += getDefName(attackee);
			}
		}
		if(weaponName != ""){
			s += " with ";
			if(attacker == Game.player){		
				s += "your ";
			}else{
				s += "its ";
			}
			s += weaponName;
		}
		s += " for " + damage + " damage";
		IO.print(s);
	}
	
	public static void healthRemainingMessage(Creature attacker, Creature attackee, boolean prekilled){//TODO better name? Also damage names
		String s = "";
		String attackeePronoun;
		if(attackee == Game.player){
			attackeePronoun = "you";
		}else{
			attackeePronoun = "it";
		}
		if(attackee.hp > 0){
			s += ", leaving " + attackeePronoun + " on " + attackee.hp + " hp";
		}else if(!prekilled){
			s += ", killing " + attackeePronoun;
		}
		s += ".";
		if(attacker == Game.player){//player text is blue
			s += "<r>";
		}
		IO.println(s);
	}
	
	/**
	 * Returns a string representing the name of a {@code Creature} preceded by the appropriate indefinite article ("a", "some", etc).
	 * 
	 * @param c the {@code Creature} to name
	 * @return String representation of {@code Creature} name with indefinite article
	 */
	public static String getIndefiniteName(Creature c){
		return (c.articleIndef == "" ? "" : c.articleIndef + " ") + c.getName();
	}
	
	/**
	 * Returns a string representing the name of a {@code Creature} preceded by the appropriate definite article ("the", or no article).
	 * 
	 * @param c the {@code Creature} to name
	 * @return String representation of {@code Creature} name with definite article
	 */
	public static String getDefName(Creature c){
		return (c.articleDef == "" ? "" : c.articleDef + " ") + c.name;
	}

	public static void listTargets(){
		for(int i = 0; i < Game.zone.creatures.size(); i++){
			if(Game.zone.creatures.get(i).isAlive()){
				IO.println(i + ": " + Game.zone.creatures.get(i).getName() + " (" + Game.zone.creatures.get(i).hp + " hp)");
			}else{
				IO.println(i + ": " + Game.zone.creatures.get(i).getName() + " (dead)");
			}
		}
	}

	public static void listItems(){
		for(int i = 0; i < Game.zone.items.size(); i++){
			IO.println(i + ": " + Game.zone.items.get(i).getNameWithCount());		
		}
	}

	public static void listInv(){
		for(int i = 0; i < Game.player.inv.size(); i++){
			IO.println(i + ": " + Game.player.inv.get(i).getNameWithCount());		
		}
	}

	/**
	 * Prints the player's inventory, highlighting foods in green
	 */
	public static void listInvFoods(){
		for(int i = 0; i < Game.player.inv.size(); i++){
			IO.print(i + ": ");
			if(Game.player.inv.get(i).hasTag("edible")){
				IO.println("<green>" + Game.player.inv.get(i).getNameWithCount() + "<r>");
			}else{
				IO.println(Game.player.inv.get(i).getNameWithCount());
			}
		}
	}

	/**
	 * Prints the player's inventory, highlighting enchantable items in green
	 */
	public static void listInvEnchantable(){
		for(int i = 0; i < Game.player.inv.size(); i++){
			IO.print(i + ": ");
			if(Game.player.inv.get(i).armourType > 1){
				IO.println("<green>" + Game.player.inv.get(i).getNameWithCount() + "<r>");
			}else{
				IO.println(Game.player.inv.get(i).getNameWithCount());
			}		
		}
	}

	/**
	 * Prints the player's inventory, highlighting scrolls in green
	 */
	public static void listInvScrolls(){
		for(int i = 0; i < Game.player.inv.size(); i++){
			IO.print(i + ": ");
			if(Game.player.inv.get(i).getClass().equals(Scroll.scrollCrit.getClass())){
				IO.println("<green>" + Game.player.inv.get(i).getNameWithCount() + "<r>");
			}else{
				IO.println(Game.player.inv.get(i).getNameWithCount());
			}
		}
	}

	/**
	 * Prints the player's inventory, highlighting gems in green
	 */
	public static void listInvGems(){
		for(int i = 0; i < Game.player.inv.size(); i++){
			IO.print(i + ": ");
			if(Game.player.inv.get(i).hasTag("gem")){
				IO.println("<green>" + Game.player.inv.get(i).getNameWithCount() + "<r>");	
			}else{
				IO.println(Game.player.inv.get(i).getNameWithCount());
			}
		}
	}

	public static void viewZone(){
		Text.listCreatures();
		if(Game.zone.items.size() > 0){
			IO.print("Items: " + Game.zone.items.get(0).prefix + " " +  Game.zone.items.get(0).getNameWithCount());
			for(int i = 1; i < Game.zone.items.size() - 1; i++){
				IO.print(", " + Game.zone.items.get(i).prefix + " " +  Game.zone.items.get(i).getNameWithCount());
			}
			if(Game.zone.items.size() > 1){
				IO.println(" and " + Game.zone.items.get(Game.zone.items.size() - 1).prefix + " " +  Game.zone.items.get(Game.zone.items.size() - 1).getNameWithCount());
			}else{
				IO.println("");
			}
		}
		Game.zone.printDescription();
	}

	public static void listCreatures(){
		if(Game.zone.creatures.size() > 0){
			IO.print("You see " + getIndefiniteName(Game.zone.creatures.get(0)));
			for(int i = 1; i < Game.zone.creatures.size() - 1; i++){
				IO.print(", " + getIndefiniteName(Game.zone.creatures.get(i)));			
			}
			if(Game.zone.creatures.size() > 1){
				IO.println(" and " + getIndefiniteName(Game.zone.creatures.get(Game.zone.creatures.size() - 1)));
			}else{
				IO.println("");
			}	
		}
	}
}
