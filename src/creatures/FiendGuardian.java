package creatures;

import main.Game;
import item.Item;
import item.MagicItem;

public class FiendGuardian extends Creature{
	
	public FiendGuardian(){
		name = "Demonic Guardian";
		tags.add(CreatureTag.fiend);
		maxHp = 666;
		hp = maxHp;
		dmg = 40;
		xp = 300;
		baseResists[Game.DMG_BLUNT] = 15;
		baseResists[Game.DMG_SLASH] = 30;
		baseResists[Game.DMG_PIERCE] = 8;
		baseResists[Game.DMG_BURN] = 50;

		addItem("lswordDemonEmpowered");
		equip("lswordDemonEmpowered");
		if(Math.random() < 0.75){
			addItem(MagicItem.RandomEnchant(Item.item("ringDemon"), (int)(Math.random() + 6.5)));
			equip(1);
		}
		skills.slashLvl = 100;
		critChance = 0.5;
		calculateDamage();
		if(Math.random() < 0.45){addItem("ruby");}
		if(Math.random() < 0.45){addItem("ruby");}
		if(Math.random() < 0.45){addItem("ruby");}
		if(Math.random() < 0.45){addItem("ruby");}
		addTarget(CreatureTag.humanoid, 200);
		postInitialisation();
	}
	
	public String getDescription(){
		return "His sword is sharp.";
	}
}