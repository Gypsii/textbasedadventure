package effects;

import creatures.Creature;
import main.DamageInstance;
import util.AttackHandler;

public class DamageEffect extends Effect {

	public DamageInstance damage;

	public DamageEffect(DamageInstance dmg) {
		this.damage = dmg;
	}

	@Override
	public void apply(Creature c) {
		DamageInstance d = damage.copy();
		d.amount -= c.resists[d.type.number];
		AttackHandler.applyDamagePassively(null, c, d, null);
	}

	@Override
	public String toString() {
		return "DamageEffect{" +
				"damage=" + damage +
				'}';
	}
}
