package main;

import creatures.Creature;
import util.AttackHandler;

public class ShredOnHit extends OnHit{

	public DamageType type;
	public int amount;
	public double duration;

	public ShredOnHit(DamageType type, int amount, double duration) {
		this.type = type;
		this.amount = amount;
		this.duration = duration;
	}

	@Override
	public void apply(Creature attacker, Creature target){
	}

}
