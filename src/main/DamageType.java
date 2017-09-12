package main;

import java.util.HashMap;

public class DamageType {

	//TODO move damage to file for dynamic damage types
	public static HashMap<String, DamageType> damageVerbs= new HashMap<>();

	public DamageType(String key, String defaultVerb) {

	}

	public static String getDamageVerb(int damageType){
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
