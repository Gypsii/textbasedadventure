package creatures;

import main.DamageInstance;
import main.DamageType;
import main.Game;
import item.Item;
import item.MagicItem;
import main.Tag;

public class Fiend extends Creature{
	
	public Fiend(){
		name = "Demonic Fiend";
		addTag("fiend");
		maxHp = 132;
		hp = maxHp;
		xp = 65;
		baseResists[DamageType.BLUNT.number] = 8;
		baseResists[DamageType.SLASH.number] = 13;
		baseResists[DamageType.PIERCE.number] = 5;
		baseResists[DamageType.BURN.number] = 40;

		naturalAttackPattern = new AttackPattern(new DamageInstance(25, DamageType.SLASH), "clawed", 0.666);
		addTarget(Tag.tag("player"), 666);
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
		addTarget(Tag.tag("humanoid"), 100);
		postInitialisation();
	}
	
	public String getDescription(){
		return "A vile hellfiend wielding a cursed demonic sword.";
	}
}