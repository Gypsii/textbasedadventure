package effects;

import creatures.Creature;
import util.AttackHandler;

public class HealEffect extends Effect{

	public int heal;

	public HealEffect(int heal) {
		this.heal = heal;
	}

	@Override
	public String toString() {
		return "HealEffect{" +
				"heal=" + heal +
				'}';
	}

	@Override
	public void apply(Creature c) {
		AttackHandler.selfHeal(c, heal);
	}
}
