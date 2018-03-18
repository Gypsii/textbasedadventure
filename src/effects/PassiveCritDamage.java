package effects;

import creatures.Creature;

public class PassiveCritDamage extends PassiveEffect{

	public double amount;

	public PassiveCritDamage(double amount) {
		this.amount = amount;
	}

	public void apply(Creature c) {
		c.critDmg += amount;
	}

}
