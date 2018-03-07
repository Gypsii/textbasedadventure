package creatures.humans;

import creatures.AttackPattern;
import item.Item;
import main.DamageInstance;
import main.DamageType;
import main.Tag;

public class Human extends Humanoid{

	public Human(){
		name = "Human";
		addTag("humanoid");
		addTag("human");
		maxHp = 100;
		hp = maxHp;
		naturalAttackPattern = new AttackPattern(new DamageInstance(4, DamageType.BLUNT), "punched", 1);
		xp = 0;		
		courage = 1000 + (Math.random() * 5);//TODO
		removeTarget(Tag.tag("player"));
		
		setSpecifics();
		setArmour();
		setWeapon();
		setTargets();
		postInitialisation();
	}
	
	public void setWeapon(){
		String i = "unarmed";
		if(Math.random() < 0.4){
			if(Math.random() < 0.4){
				i = "sswordBronze";
			}else{
				i = "sswordCopper";
			}
			addItem(i);
			equip(i);
		}
	}
	
	public void setArmour(){
		String a;
		if(Math.random() < 0.3){
			if(Math.random() < 0.5){
				a = "tunicLinen";
			}else{
				a = "tunicSilk";
			}
		}else{
			if(Math.random() < 0.5){
				a = "tunicCotton";
			}else{
				a = "tunicWool";
			}
		}
		addItem(a);
		equip(a);
		
		if(Math.random() < 0.2){
			Item cloak = Item.randomCloak();
			addItem(cloak);
			equip(cloak);
		}
	}
	
}
