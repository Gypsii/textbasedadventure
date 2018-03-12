package main;

import creatures.Creature;
import util.AttackHandler;
import util.IO;

public class DamageOnHit extends OnHit{

	public DamageInstance damage;

	public DamageOnHit(DamageInstance dmg) {
		this.damage = dmg;
	}

	@Override
	public void apply(Creature attacker, Creature target){
		DamageInstance d = damage.copy();
		d.amount -= target.resists[d.type.number];
		AttackHandler.applyDamagePassively(attacker, target, d, null);
	}

}
