package util;

import java.util.Comparator;

import main.Buff;

public class BuffComparator implements Comparator<Buff>{

	@Override
	public int compare(Buff b1, Buff b2) {
		if (b1.potency < b2.potency){
            return 1;
        }
        if (b1.potency > b2.potency){
            return -1;
        }
        return 0;
	}

}
