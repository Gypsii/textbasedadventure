package creatures.Buffs;

import main.DamageType;

public class ResistBuff extends Buff{

	public DamageType resistType;
	public int resistQuantity;

	public ResistBuff(DamageType type, int quantity) {
		this.resistQuantity = quantity;
		this.resistType = type;
		updateWithArmour = true;
	}

	@Override
	public double resolve() {
		super.resolve();
		creature.refreshArmour();
		return -1;
	}

	@Override
	public void apply() {
		creature.resists[resistType.number] += resistQuantity;
	}


}
