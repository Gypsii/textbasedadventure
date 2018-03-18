package effects.onhits;

import creatures.Creature;
import util.AttackHandler;

public abstract class OnHit {

	public abstract void apply(Creature attacker, Creature target);

}
