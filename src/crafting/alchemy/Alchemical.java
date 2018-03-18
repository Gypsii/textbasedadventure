package crafting.alchemy;

import creatures.Creature;
import effects.Effect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Alchemical {

	List<Effect> effects = new ArrayList<>();
	List<Effect> contactEffects = new ArrayList<>();
	Set<AlchemicalModifier> modifiers = new HashSet<>();

	public void applyExternal(Creature c, double concentration) {
		for(Effect e : contactEffects) {
			e.apply(c);
		}
	}

	public void applyInternal(Creature c, double concentration) {
		for(Effect e : contactEffects) {
			e.apply(c);
		}
		for(Effect e : effects) {
			e.apply(c);
		}
	}

	@Override
	public String toString() {
		return "Alchemical{" +
				"effects=" + effects +
				", contactEffects=" + contactEffects +
				", modifiers=" + modifiers +
				'}';
	}
}
