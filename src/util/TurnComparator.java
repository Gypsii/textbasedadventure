package util;

import java.util.Comparator;

import creatures.Creature;

public class TurnComparator implements Comparator<TimeObject>{

	@Override
	public int compare(TimeObject c1, TimeObject c2) {
		if (c1.getNextTriggerTime() < c2.getNextTriggerTime()){
            return -1;
        }
        if (c1.getNextTriggerTime() > c2.getNextTriggerTime()){
            return 1;
        }
        return 0;
	}

}
