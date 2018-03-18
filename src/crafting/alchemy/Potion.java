package crafting.alchemy;

import creatures.Creature;

import java.util.HashMap;
import java.util.Map;

public class Potion {

		public Map<Alchemical, Double> concentrations = new HashMap<>();

		public Potion clone() {
			return this; // TODO clone
		}

	@Override
	public String toString() {
		return "Potion{" +
				"concentrations=" + concentrations +
				'}';
	}

	public void applyExternal(Creature c) {
			for(Alchemical a : concentrations.keySet()) {
				a.applyExternal(c, concentrations.get(a));
			}
		}

		public void applyInternal(Creature c) {
			for(Alchemical a : concentrations.keySet()) {
				a.applyInternal(c, concentrations.get(a));
			}
		}

}
