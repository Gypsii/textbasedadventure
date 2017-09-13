package creatures;

import main.Game;


public class Rat extends Creature{
	
	public Rat(){
		name = "Rat";
		maxHp = 30;
		hp = maxHp;
		baseDmg = 6;
		xp = 6;
		courage = 0.75 + Math.random();
		addTag("rodent");
		addBodyPart("meatRat", 0.15);
		addBodyPart("meatRat", 0.15);
		
		defaultAttackPattern = new AttackPattern(baseDmg, Game.DMG_PIERCE, "bit", 0.7);
		
		postInitialisation();
	}
	
	public String getDescription(){
		return "A large rodent.";
	}
}
