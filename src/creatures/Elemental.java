package creatures;

import item.Item;
import main.DamageInstance;
import main.DamageType;


public class Elemental extends Creature{
	int type;
	public static int FLAME = 0;
	public static int FROST = 1;
	public static int EARTH = 2;
	public static int WATER = 3;
	public static int STORM = 4;//Its not called AIR because air isnt 5 letters long
	public static int FORCE = 5;
	public static int ELEMENTAL_TYPES = 6;
	
	public Elemental(int t){
		type = t;
		name = "<red>ERR UNTYPED ELEMENTAL<r>";
		addTag("elemental");
		DamageInstance base = new DamageInstance(14, DamageType.MAGIC);
		naturalAttackPattern = new AttackPattern(base, 1);
		maxHp = 160;
		xp = 47;
		for(int i = 0; i < DamageType.damageTypeCount(); i++){
			baseResists[i] = 5;
		}
		if(type == FLAME){
			name = "Fire Elemental";
			baseResists[DamageType.BLUNT.number] = 0;
			baseResists[DamageType.SLASH.number] = 0;
			baseResists[DamageType.PIERCE.number] = 0;
			baseResists[DamageType.BURN.number] = 40;
			base.type = DamageType.BURN;
			addItem("ruby");
			addItem("amethyst");
		}else if(type == FROST){
			name = "Frost Elemental";
			baseResists[DamageType.COLD.number] = 40;
			baseResists[DamageType.BURN.number] = -10;
			base.type = DamageType.COLD;
			addItem("sapphire");
			addItem("diamond");
		}else if(type == EARTH){
			name = "Earth Elemental";
			baseResists[DamageType.BLUNT.number] = 15;
			baseResists[DamageType.SLASH.number] = 15;
			baseResists[DamageType.PIERCE.number] = 15;
			baseResists[DamageType.MAGIC.number] = -10;
			base.type = DamageType.BLUNT;
			maxHp = 200;
			addItem("citrine");
			addItem("emerald");
		}else if(type == WATER){
			name = "Water Elemental";
			baseResists[DamageType.BLUNT.number] = 6;
			baseResists[DamageType.SLASH.number] = 6;
			baseResists[DamageType.PIERCE.number] = 6;
			base.type = DamageType.BLUNT;
			maxHp = 280;
			naturalAttackPattern.period = 1.4;
			addItem("sapphire");
			addItem("citrine");
		}else if(type == STORM){
			name = "Air Elemental";
			baseResists[DamageType.MAGIC.number] = 40;
			maxHp = 146;
			naturalAttackPattern.period = 0.3;
			base.amount *= 0.5;
			addItem("amethyst");
			addItem("diamond");
		}else if(type == FORCE){
			name = "Force Elemental";
			baseResists[DamageType.MAGIC.number] = 40;
			naturalAttackPattern.period = 5;
			base.amount *= 6;
			addItem(Item.randomGem());
			addItem(Item.randomGem());
			addItem(Item.randomGem());
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
