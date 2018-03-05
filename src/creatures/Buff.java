package creatures;

import util.TimeObject;

public class Buff implements TimeObject{

	double nextTriggerTime;

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

	@Override
	public double resolve() {
		return -1;
	}

}
