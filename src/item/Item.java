package item;

import java.util.ArrayList;
import java.util.TreeMap;

import util.IO;
import main.ArmourPassive;
import main.Game;
import main.OnHit;

public class Item {
	public static TreeMap<String, Item> items = new TreeMap<String, Item>();
	
	public String name = "UNNAMED";
	public String id = "";
	public String description = "";
	public int count = 1;
	public int dmg = 0;
	public double swingTime = 1;
	public int dmgType = 0;
	public String prefix = "a"; 
	public int armourType = 0;
	public double cost = -1;
	
	public boolean isPotion = false;
	public boolean isFood = false;
	public boolean isGem = false;
	public boolean isPolearm = false;
	public boolean isSword = false;
	public boolean isCurrency = false;
	public boolean isStackable = true;
	public boolean isEnchanted = false;
	public int enchantLvl = 0;
	public int enchantType = 0;
	public boolean isIngredient = false;
	public ArrayList<OnHit> effects = new ArrayList<OnHit>();
		
	
	public int resists[] = new int[Game.DMG_TYPE_COUNT];

	public ArrayList<ArmourPassive> passives = new ArrayList<ArmourPassive>();
	
	
	public Item(String n, int damage, int type, double speed, String pref){//"Weapon" with prefix
		name = n;
		dmg = damage;
		prefix = pref;
		dmgType = type;
		swingTime = speed;
	}
	
	public Item(){
		
	}
	
	public String getNameWithCount(){
		if(count > 1){
			return name + " (" + count + ")";
		}
		return name;
	}
	
	public Item clone(){
		Item i = new Item(name, dmg, dmgType, swingTime, prefix);
		i.id = this.id;
		i.armourType = this.armourType;
		i.isIngredient = this.isIngredient;
		i.count = this.count;
		i.cost = this.cost;
		i.isGem = this.isGem;
		i.isPolearm = this.isPolearm;
		i.isEnchanted = this.isEnchanted;
		i.enchantLvl = this.enchantLvl;
		i.enchantType = this.enchantType;
		i.effects.addAll(this.effects);
		i.passives.addAll(this.passives);
		i.resists = this.resists.clone();
		i.description = this.description;
		return i;
	}
	
	public static Item bug = new Item("Item Table Bug", 1, 0, 1, "an unexpected");
	public static Item unarmed = new Item("unarmed", 0, 0, 1, "");

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
		if(dmg > 0){
			IO.print("" + dmg);
			if(dmgType == 0){
				IO.print(" bludgeoning");
			}else if(dmgType == 1){
				IO.print(" piercing");
			}else{
				IO.print(" slashing");
			}
			IO.println(" damage.");
			IO.println("Swing time: " + swingTime);
		}
		if(!description.equals("")){
			IO.println("");
			IO.println(description);
		}
		IO.printHorizontalLine();
	}
	
	public static void doItems() {//TODO get this stuff in the file
		
		item("slimeFire").effects.add(new OnHit(OnHit.BURN, 2));
		item("slimeIce").effects.add(new OnHit(OnHit.COLD, 2));
		item("icicle").effects.add(new OnHit(OnHit.COLD, 2));
		item("fungus").effects.add(new OnHit(OnHit.MAGIC, 4));
		item("spiderbane").effects.add(new OnHit(OnHit.FEARSPIDERS, 2));

		item("lswordSlimeFire").effects.add(new OnHit(OnHit.BURN, 8));
		item("lswordSlimeIce").effects.add(new OnHit(OnHit.COLD, 8));
		//lswordSlimeVolatile.effects.add(new OnHit(OnHit.BURN, 4));
		
		item("lswordDemonFlame").effects.add(new OnHit(OnHit.BURN, 8));
		item("lswordDemonMagic").effects.add(new OnHit(OnHit.MAGIC, 16));
		item("lswordDemonMagic").effects.add(new OnHit(OnHit.SELFDMG, 8));
		item("lswordDemonTap").effects.add(new OnHit(OnHit.SELFHEAL, 4));
		
		item("elementalCoreFire").effects.add(new OnHit(OnHit.BURN, 16));
		item("elementalCoreIce").effects.add(new OnHit(OnHit.COLD, 16));
		item("elementalCoreAir").effects.add(new OnHit(OnHit.LIGHTNING, 8));
		
//		cloakCritDmg.passives.add(new ArmourPassive(ArmourPassive.CRIT_DMG, 25));
//		cloakCritDmg.passives.add(new ArmourPassive(ArmourPassive.CRIT, 25));
	}
	
	public static Item randomGem(){
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