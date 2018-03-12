package creatures.Buffs;

import creatures.Creature;
import main.Game;
import util.IO;
import util.TimeObject;

public abstract class Buff implements TimeObject{

	private double nextTriggerTime;
	// Whether buff effects need to be reapplied when armour/weapons get recalculated
	boolean updateWithArmour = false;
	boolean updateWithWeapons = false;
	Creature creature;

	@Override
	public double getNextTriggerTime() {
		return nextTriggerTime;
	}

	@Override
	public void setNextTriggerTime(double t) {
		nextTriggerTime = t;
	}

	@Override
	public boolean removeOnZoneSwitch() {
		return false;
	}

	public void apply() {

	}

	public void addTo(Creature c, double expiry) {
		this.creature = c;
		c.buffs.add(this);
		setNextTriggerTime(expiry);
		Game.toTakeTurn.add(this);
		apply();
	}

	@Override
	public double resolve() {
		creature.buffs.remove(this);
		return -1;
	}

}
