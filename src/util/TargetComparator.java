package util;

import java.util.Comparator;

public class TargetComparator implements Comparator<TargetPair>{

	@Override
	public int compare(TargetPair c1, TargetPair c2) {
		if (c1.weight < c2.weight){
            return 1;
        }
        if (c1.weight > c2.weight){
            return -1;
        }
        return 0;
	}

}
