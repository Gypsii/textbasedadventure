package main;

import creatures.Creature;
import util.AttackHandler;

public class SelfHealOnHit extends OnHit{

	public int heal;

	public SelfHealOnHit(int heal) {
		this.heal = heal;
	}

	@Override
	public void apply(Creature attacker, Creature target){
		AttackHandler.selfHeal(attacker, heal);
	}

}
