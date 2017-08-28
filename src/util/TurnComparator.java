package util;

import java.util.Comparator;

import creatures.Creature;

public class TurnComparator implements Comparator<Creature>{

	@Override
	public int compare(Creature c1, Creature c2) {
		if (c1.nextActionTime < c2.nextActionTime){
            return -1;
        }
        if (c1.nextActionTime > c2.nextActionTime){
            return 1;
        }
        return 0;
	}

}
