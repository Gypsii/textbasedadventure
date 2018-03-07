package creatures;

import main.DamageInstance;
import main.DamageType;
import main.Game;
import item.Item;
import item.MagicItem;
import main.Tag;

public class FiendGuardian extends Creature{
	
	public FiendGuardian(){
		name = "Demonic Guardian";
		addTag("fiend");
		maxHp = 666;
		hp = maxHp;
		
		xp = 300;
		baseResists[DamageType.BLUNT.number] = 15;
		baseResists[DamageType.SLASH.number] = 30;
		baseResists[DamageType.PIERCE.number] = 8;
		baseResists[DamageType.BURN.number] = 50;

		naturalAttackPattern = new AttackPattern(new DamageInstance(66, DamageType.PIERCE), "clawed", 0.666);
		addTarget(Tag.tag("player"), 666);
		addItem("lswordDemonEmpowered");
		equip("lswordDemonEmpowered");
		if(Math.random() < 0.75){
			addItem(MagicItem.RandomEnchant(Item.item("ringDemon"), (int)(Math.random() + 6.5)));
			equip(1);
		}
		skills.slashLvl = 100;
		critChance = 0.5;
		if(Math.random() < 0.45){addItem("ruby");}
		if(Math.random() < 0.45){addItem("ruby");}
		if(Math.random() < 0.45){addItem("ruby");}
		if(Math.random() < 0.45){addItem("ruby");}
		addTarget(Tag.tag("humanoid"), 200);
		postInitialisation();
	}
	
	public String getDescription(){
		return "His sword is sharp.";
	}
}