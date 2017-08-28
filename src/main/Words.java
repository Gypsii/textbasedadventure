package main;

public class Words {

	public String getDamageVerb(int damageType){
		switch(damageType){
		case Game.DMG_BLUNT:
			return "hit";
		case Game.DMG_SLASH:
			return "slashed";
		case Game.DMG_PIERCE:
			return "pierced";
		case Game.DMG_BURN:
			return "burnt";
		case Game.DMG_COLD:
			return "frozen";
		case Game.DMG_MAGIC:
			return "shocked";
		}
		return "damaged";
	}
	
}
