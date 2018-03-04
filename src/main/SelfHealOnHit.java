package main;

import creatures.Creature;
import util.AttackHandler;

public class SelfHealOnHit extends OnHit{

	public int heal;
	public double healProportion;

	public SelfHealOnHit(int heal, double healProportion) {
		this.heal = heal;
		this.healProportion = healProportion;
	}

	@Override
	public void apply(Creature attacker, Creature target){
		int amount = Math.max(heal, (int)(attacker.getDamage().amount * healProportion));
		AttackHandler.selfHeal(attacker, amount);
	}

}
