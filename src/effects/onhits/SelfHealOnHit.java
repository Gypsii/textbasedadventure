package effects.onhits;

import creatures.Creature;
import effects.HealEffect;

public class SelfHealOnHit extends OnHit {

	public int heal;
	public double healProportion;

	public SelfHealOnHit(int heal, double healProportion) {
		this.heal = heal;
		this.healProportion = healProportion;
	}

	@Override
	public void apply(Creature attacker, Creature target){
		int amount = Math.max(heal, (int)(attacker.getDamage().amount * healProportion));
		new HealEffect(amount).apply(attacker);
	}

}
