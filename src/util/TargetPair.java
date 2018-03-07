package util;

import creatures.Creature;

public class TargetPair {

	public Creature creature;
	public int weight;
	
	public TargetPair(){
		
	}

	@Override
	public String toString() {
		return "TargetPair{" +
				"creature=" + creature.getName() +
				", weight=" + weight +
				'}';
	}
}
