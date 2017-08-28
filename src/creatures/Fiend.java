package creatures;

import main.Game;
import item.Item;
import item.MagicItem;

public class Fiend extends Creature{
	
	public Fiend(){
		name = "Demonic Fiend";
		tags.add(CreatureTag.fiend);
		maxHp = 132;
		hp = maxHp;
		baseDmg = 16;
		xp = 65;
		baseResists[Game.DMG_BLUNT] = 8;
		baseResists[Game.DMG_SLASH] = 13;
		baseResists[Game.DMG_PIERCE] = 5;
		baseResists[Game.DMG_BURN] = 40;

		double rand = Math.random();
		if(rand < 0.25){
			addItem("lswordDemon");
		}else if(rand < 0.5){
			addItem("lswordDemonTap");
		}else if(rand < 0.75){
			addItem("lswordDemonFlame");
		}else{
			addItem("lswordDemonMagic");
		}
		equip(0);
		if(Math.random() < 0.75){
			addItem(MagicItem.RandomEnchant(Item.item("ringDemon"), (int)(Math.random() + 4.5)));
			equip(1);
		}
		if(Math.random() < 0.45){addItem("ruby");}
		addTarget(CreatureTag.humanoid, 100);
		postInitialisation();
	}
	
	public String getDescription(){
		return "A vile hellfiend wielding a cursed demonic sword.";
	}
}