package effects.onhits;

import creatures.Creature;
import effects.DamageEffect;
import main.DamageInstance;
import util.AttackHandler;

public class DamageOnHit extends OnHit {

	public DamageInstance damage;

	public DamageOnHit(DamageInstance dmg) {
		this.damage = dmg;
	}

	@Override
	public void apply(Creature attacker, Creature target){
		new DamageEffect(damage).apply(target);
	}

}
