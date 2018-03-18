package effects;

import creatures.Creature;

public class PassiveCritChance extends PassiveEffect{

	public double amount;

	public PassiveCritChance(double amount) {
		this.amount = amount;
	}

	@Override
	public void apply(Creature c) {
		c.critChance += amount;
	}

}
