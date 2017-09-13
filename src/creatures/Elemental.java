package creatures;

import main.Game;


public class Elemental extends Creature{
	int type;
	public static int FLAME = 0;
	public static int FROST = 1;
	public static int EARTH = 2;
	public static int WATER = 3;
	public static int STORM = 4;//Its not called AIR because air isnt 5 letters long
	public static int ELEMENTAL_TYPES = 5;
	
	public Elemental(int t){
		type = t;
		name = "<red>ERR UNTYPED ELEMENTAL<r>";
		addTag("elemental");
		baseDmg = 14;
		maxHp = 160;
		xp = 47;
		for(int i = 0; i < Game.DMG_TYPE_COUNT; i++){
			baseResists[i] = 5;
		}
		if(type == FLAME){
			name = "Fire Elemental";
			baseResists[Game.DMG_BLUNT] = 0;
			baseResists[Game.DMG_SLASH] = 0;
			baseResists[Game.DMG_PIERCE] = 0;
			baseResists[Game.DMG_BURN] = 40;
			equip("elementalCoreFire");
			addItem("ruby");
			addItem("amethyst");
		}else if(type == FROST){
			name = "Frost Elemental";
			baseResists[Game.DMG_COLD] = 40;
			baseResists[Game.DMG_BURN] = -10;
			equip("elementalCoreIce");
			addItem("sapphire");
			addItem("diamond");
		}else if(type == EARTH){
			name = "Earth Elemental";
			baseResists[Game.DMG_BLUNT] = 15;
			baseResists[Game.DMG_SLASH] = 15;
			baseResists[Game.DMG_PIERCE] = 15;
			baseResists[Game.DMG_MAGIC] = -10;
			maxHp = 200;
			equip("elementalCoreEarth");
			addItem("citrine");
			addItem("emerald");
		}else if(type == WATER){
			name = "Water Elemental";
			baseResists[Game.DMG_BLUNT] = 6;
			baseResists[Game.DMG_SLASH] = 6;
			baseResists[Game.DMG_PIERCE] = 6;
			maxHp = 280;
			equip("elementalCoreWater");
			addItem("sapphire");
			addItem("citrine");
		}else if(type == STORM){
			name = "Air Elemental";
			baseResists[Game.DMG_MAGIC] = 40;
			maxHp = 146;
			equip("elementalCoreAir");
			addItem("amethyst");
			addItem("diamond");
		}
		hp = maxHp;
		postInitialisation();
	}
	
	public String getDescription(){
		if(type == FLAME){
			return "";
		}else if(type == FROST){
			return "";
		}else if(type == WATER){
			return "";
		}else if(type == EARTH){
			return "";
		}else if(type == STORM){
			return "";
		}
		return "";
	}
	
}
