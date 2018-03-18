package crafting.alchemy;

import effects.DamageEffect;
import effects.Effect;
import effects.HealEffect;
import effects.ResistEffect;
import main.DamageInstance;
import main.DamageType;
import util.Random;

import java.util.*;

public class Alchemy {

	public static Map<String, Alchemical> effects = new HashMap<>();

	private static ArrayList<Alchemical> alchemicalsPool = new ArrayList<>();

	public static void assignEffects() {
		ArrayList<Effect> pool = new ArrayList<>();

		for(int i = 0; i < 20; i++) {
			pool.add(new HealEffect(Random.range(10)));
		}
		for(int i = 0; i < 7; i++) {
			pool.add(new DamageEffect(new DamageInstance(Random.range(15), DamageType.BURN)));
			pool.add(new DamageEffect(new DamageInstance(Random.range(15), DamageType.COLD)));
			pool.add(new DamageEffect(new DamageInstance(Random.range(15), DamageType.MAGIC)));
		}

		for(int i = 0; i < 20; i++) {
			pool.add(new ResistEffect(
					DamageType.get(Random.range(DamageType.damageTypeCount())),
					Random.range(-10, 10),
					Random.range(10)
			));
		}
		Collections.shuffle(pool);



		while(!pool.isEmpty()) {
			Alchemical a = new Alchemical();
			Effect e = pool.get(pool.size()-1);
			pool.remove(pool.size()-1);
			if(Random.chance(0.5)) {
				a.contactEffects.add(e);
			} else {
				a.effects.add(e);
			}
			if(!pool.isEmpty() && Random.chance(0.3)) {
				e = pool.get(pool.size()-1);
				pool.remove(pool.size()-1);
				if(Random.chance(0.5)) {
					a.contactEffects.add(e);
				} else {
					a.effects.add(e);
				}
			}
			alchemicalsPool.add(a);
		}
	}

	public static void addEffectTo(String itemID) {
		if(effects.isEmpty()) {
			assignEffects();
		}
		Alchemical a = alchemicalsPool.get(Random.range(alchemicalsPool.size()));
		effects.put(itemID, a);
	}

}
