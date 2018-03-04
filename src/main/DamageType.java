package main;

import java.util.HashMap;

public class DamageType {

	public final String id;
	public final int number;
	public final String name;
	public final String verb;
	public final String verbPassive;


	//TODO move damage to file for dynamic damage types
	private static HashMap<String, DamageType> damageTypesString = new HashMap<>();
	private static HashMap<Integer, DamageType> damageTypesInt = new HashMap<>();

	public static final DamageType BLUNT = new DamageType("blunt", "bludgeoning", "hit", "hit");
	public static final DamageType SLASH = new DamageType("slash", "slashing", "slashed", "slashed");
	public static final DamageType PIERCE = new DamageType("pierce", "piercing", "pierced", "pierced");
	public static final DamageType BURN = new DamageType("burn", "burning", "burnt", "burnt");
	public static final DamageType COLD = new DamageType("cold", "cold", "froze", "frozen");
	public static final DamageType MAGIC = new DamageType("magic", "magic", "shocked", "shocked");
	public static final DamageType FORCE = new DamageType("force", "explosive", "blasted", "blasted");

	private static int count = 0;

	public static DamageType get(String key) {
		return damageTypesString.get(key);
	}

	public static DamageType get(int key) {
		return damageTypesInt.get(key);
	}

	public static int damageTypeCount() {
		return 7; // count; // TODO fix this to stop it evaluating as 0 when the arrays are filled.
	}

	public DamageType(String key, String name, String verb, String passiveVerb) {
		this.id = key;
		this.number = ++count;
		this.name = name;
		this.verb = verb;
		this.verbPassive = passiveVerb;
		damageTypesInt.put(number, this);
		damageTypesString.put(key, this);
	}
}
