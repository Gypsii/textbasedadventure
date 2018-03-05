package util;

public interface TimeObject {

	double getNextTriggerTime();

	void setNextTriggerTime(double t);

	boolean removeOnZoneSwitch();

	double resolve();
}
