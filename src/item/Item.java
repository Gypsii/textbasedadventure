package item;

import java.util.*;

import main.*;
import util.IO;

public class Item {
	public static TreeMap<String, Item> items = new TreeMap<String, Item>();
	
	public String name = "UNNAMED";
	public String id = "";
	public String description = "";
	public int count = 1;
	public DamageInstance dmg = new DamageInstance(0, DamageType.BLUNT);
	public double swingTime = 1;
	public String prefix = "a"; 
	public double cost = -1;

	public Set<Tag> tags = new HashSet<>();
	public List<OnHit> onHits = new ArrayList<>();

	public boolean isStackable = true;
	public int healthRestore = 0;
	public int enchantLvl = 0;
	public int enchantType = 0;
	public boolean isIngredient = false;

	// Ensure damage types are loaded before determining array size
	public int resists[] = new int[DamageType.damageTypeCount()];
	
	
	public Item(String n, DamageInstance damage, double speed, String pref){//"Weapon" with prefix
		name = n;
		dmg = damage;
		prefix = pref;
		swingTime = speed;
	}
	
	public Item(){
		
	}

	public boolean hasTag(String tag){
		return hasTag(Tag.tag(tag));
	}

	public boolean hasTag(Tag tag){
		return tags.contains(tag);
	}

	public void addTag(String tag){
		addTag(Tag.tag(tag));
	}

	public void addTag(Tag tag){
		tags.add(tag);
	}

	public boolean removeTag(String tag){
		return removeTag(Tag.tag(tag));
	}

	public boolean removeTag(Tag tag){
		if(hasTag(tag)) {
			tags.remove(tag);
			return true;
		}
		return false;
	}
	
	public String getNameWithCount(){
		if(count > 1){
			return name + " (" + count + ")";
		}
		return name;
	}
	
	public Item clone(){
		Item i = new Item(name, dmg, swingTime, prefix);
		i.id = this.id;
		i.isIngredient = this.isIngredient;
		i.count = this.count;
		i.cost = this.cost;
		i.healthRestore = this.healthRestore;
		i.tags.addAll(this.tags);
		i.enchantLvl = this.enchantLvl;
		i.enchantType = this.enchantType;
		i.resists = this.resists.clone();
		i.description = this.description;
		return i;
	}
	
	public static Item bug;
	public static Item unarmed;
	static {
		bug = new Item("Item Table Bug", new DamageInstance(0, DamageType.BLUNT), 1, "an unexpected");
		unarmed = new Item("unarmed", new DamageInstance(0, DamageType.BLUNT), 1, "");
		unarmed.id = "unarmed";
	}

	/**
	 * Returns the {@code Item} mapped to the String {@code id}.
	 * @param id
	 * @return
	 */
	public static Item item(String id){
		if(id.equals("randomGem")){return randomGem();}
		if(id.equals("randomCloak")){return randomCloak();}
		if(id.equals("randomTunic")){return randomTunic();}
		if(items.containsKey(id)){
			return items.get(id);
		}else{
			IO.println("<lred>Key " + id + " not found!<r>");
			return bug;
		}
	}
	
	public void printInfo(){
		IO.printHorizontalLine();
		IO.println(name + ":");
		if(dmg.amount > 0){
			IO.println(dmg.amount + " " + dmg.type.name + " damage.");
			IO.println("Swing time: " + swingTime);
		}
		if(healthRestore > 0) {
			IO.println("Restores <green>" + healthRestore +  "<r> hp when eaten.");
		}else if(healthRestore < 0) {
			IO.println("Deals <red>" + healthRestore +  "<r> damage when eaten.");
		}
		if(!description.equals("")){
			IO.println("");
			IO.println(description);
		}
		IO.printHorizontalLine();
	}
	
	public static Item randomGem(){//TODO do this from tags
		double rand = Math.random();
		if(rand < 0.166){
			return item("emerald");
		}if(rand < 0.333){
			return item("ruby");
		}if(rand < 0.5){
			return item("sapphire");
		}if(rand < 0.666){
			return item("diamond");
		}if(rand < 0.833){
			return item("citrine");
		}
		return item("amethyst");
	}
	
	public static Item randomCloak(){
		double rand = Math.random();
		if(rand < 0.25){
			return item("cloakCotton");
		}if(rand < 0.5){
			return item("cloakSilk");
		}if(rand < 0.75){
			return item("cloakLinen");
		}
		return item("cloakWool");	
	}

	public static Item randomTunic(){
		double rand = Math.random();
		if(rand < 0.25){
			return item("tunicCotton");
		}if(rand < 0.5){
			return item("tunicSilk");
		}if(rand < 0.75){
			return item("tunicLinen");
		}
		return item("tunicWool");	
	}
}