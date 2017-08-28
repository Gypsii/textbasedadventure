package creatures;

import main.Game;


public class Mouse extends Creature{

	public Mouse(){
		name = "Field Mouse";
		maxHp = 9;
		hp = maxHp;
		baseDmg = 2;
		xp = 2;
		setHostilityTowardsPlayer(false);
		canBeBoss = false;
		courage = (Math.random() * 2);
		tags.add(CreatureTag.rodent);
		
		defaultAttackPattern = new AttackPattern(baseDmg, Game.DMG_SLASH, "scratched", 0.6);
		
		postInitialisation();
	}

	
	public void restingAction(){
		if(Math.random() < 0.1){
			System.out.println("The " + name + " squeaked");
		}
	}
	
	public void damageSubtrigger(){
		courage -= Math.random();
	}
	
	public String getDescription(){
		return "A tiny brown mouse";
	}
}
