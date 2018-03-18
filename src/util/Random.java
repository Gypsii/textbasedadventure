package util;

public class Random {

	public static boolean chance(double proportion) {
		return Math.random() < proportion;
	}

	public static int range(int max) {
		return range(0, max);
	}

	public static int range(int min, int max) {
		return (int)(Math.random() * (max - min)) + min;
	}

//	public Position randomCardinal() {
//
//		return
//	}

}
