package creatures;

import main.Game;


public class Cow extends Creature{

	public Cow(){
		name = "Cow";
		tags.add(CreatureTag.livestock);
		maxHp = 101;
		hp = maxHp;
		baseDmg = 9;
		xp = 12;
		setHostilityTowardsPlayer(false);
		canBeBoss = false;
		for(int i = 0; i < 7; i++){
			addBodyPart("meatCow", 0.3);
		}

		defaultAttackPattern = new AttackPattern(baseDmg, Game.DMG_PIERCE, "gored", 1.2);
		
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
