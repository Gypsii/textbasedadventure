package creatures.humans;

import item.Item;
import item.MagicItem;

import java.util.Random;

import main.Tag;

public class Hobgoblin extends Humanoid{
	
	public Hobgoblin(){
		name = "Hobgoblin";
		addTag("humanoid");
		addTag("hobgoblin");
		maxHp = 70;
		hp = maxHp;
		naturalAttackPattern.baseDamage.amount = 6;
		xp = 0;
		baseCritChance = 0.25;
		baseCritDmg = 1.5;
		setHostilityTowardsPlayer(false);	
		
		setSpecifics();
		setArmour();
		setWeapon();
		setTargets();
		postInitialisation();
	}
	
	public void setSpecifics(){	
		Random r = new Random();
		addItem(Item.item("money"), Math.max(0, (int)(r.nextGaussian() * 10 + 25)));
		if(Math.random() < 0.3){addItem("mushroom");}
		if(Math.random() < 0.05){addItem("butterKnife");}
		if(Math.random() < 0.2){addItem("bread");}
		if(Math.random() < 0.05){addItem("cinnamonBun");}
		if(Math.random() < 0.1){
			addItem(Item.randomGem());
		}
	}
	
	public void setWeapon(){
		String i;
		double rand = Math.random();
		if(rand < 0.3){
			i = "maceRusted";
		}else if(rand < 0.4){
			i = "pikeRusted";
		}else if(rand < 0.6){
			i = "axeRusted";
		}else if(rand < 0.9){
			i = "sswordRusted";
		}else{
			i = "glaiveBladeRusted";
		}
		addItem(i);
		equip(i);
	}
	
	public void setArmour(){
		String a;
		double rand = Math.random();
		if(rand < 0.33){
			a = "armourLeather";
		}else if(rand < 0.4){
			a = "tunicCotton";
		}else if(rand < 0.6){
			a = "tunicPaddedWool";
		}else if(rand < 0.8){
			a = "tunicWool";
		}else{
			a = "tunicRagged";
		}
		addItem(a);
		equip(a);
		if(Math.random() < 0.2){
			Item magic = MagicItem.pickRandomMagicItem((int)(Math.random() * 3) + 1);//Magic item from level 1-3
			addItem(magic);
			equip(magic);
		}
	}
	
	public void setTargets(){
		addTarget(Tag.tag("fiend"), 150);
		addTarget(Tag.tag("human"), 20);
	}
}
