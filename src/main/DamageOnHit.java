package main;

import creatures.Creature;
import util.AttackHandler;

public class DamageOnHit extends OnHit{

	public int damage;
	public int damageType;

	public DamageOnHit(int damage, int damageType) {
		this.damage = damage;
		this.damageType = damageType;
	}

	@Override
	public void apply(Creature attacker, Creature target){
		int d = damage - target.resists[damageType];
		AttackHandler.applyDamagePassively(attacker, target, d, damageType, DamageType.getDamageVerb(damageType));
	}

}
