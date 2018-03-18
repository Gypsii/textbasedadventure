package effects.onhits;

import effects.ResistEffect;
import creatures.Creature;
import main.DamageType;

public class ShredOnHit extends OnHit {

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
		new ResistEffect(type, -amount, duration).apply(target);
	}

}
