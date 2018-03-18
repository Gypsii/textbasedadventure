package effects;

import creatures.Creature;
import effects.buffs.ResistBuff;
import main.DamageType;
import main.Game;

public class ResistEffect extends Effect{

	public DamageType type;
	public int amount;
	public double duration;

	@Override
	public String toString() {
		return "ResistEffect{" +
				"type=" + type +
				", amount=" + amount +
				", duration=" + duration +
				'}';
	}

	public ResistEffect(DamageType type, int amount, double duration) {
		this.type = type;
		this.amount = amount;
		this.duration = duration;
	}

	@Override
	public void apply(Creature c) {
		new ResistBuff(type, amount).addTo(c, Game.getTime()+duration);
	}
}
