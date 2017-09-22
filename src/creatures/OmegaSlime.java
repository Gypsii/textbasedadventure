package creatures;

import main.Game;


public class OmegaSlime extends Creature{
	
	public OmegaSlime(){
		name = "Omega Slime";
		maxHp = 65536;
		hp = maxHp;
		baseDmg = 1000;
		xp = 8000;
		
		defaultAttackPattern = new AttackPattern(baseDmg, Game.DMG_MAGIC, "shook");

		postInitialisation();
	}
	
	public String getDescription(){
		return "<purple>Ω<r>";
	}
	
}