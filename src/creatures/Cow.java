package creatures;

import main.DamageInstance;
import main.DamageType;
import main.Tag;


public class Cow extends Creature{

	public Cow(){
		name = "Cow";
		addTag("livestock");
		maxHp = 101;
		hp = maxHp;
		xp = 12;
		canBeBoss = false;
		for(int i = 0; i < 7; i++){
			addBodyPart("meatCow", 0.3);
		}

		naturalAttackPattern = new AttackPattern(new DamageInstance(15, DamageType.PIERCE), "gored", 1.7);
		
		postInitialisation();
	}
	
	public void restingAction(){
		if(Math.random() < 0.1){
			System.out.println("The " + name + " chewed its cud");
		}
		if(Math.random() < 0.1){
			System.out.println("The " + name + " nibbled some plants");
		}
	}
	
	public String getDescription(){
		return "Moo";
	}
}
