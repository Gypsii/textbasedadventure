package main;

public class Effect {

	public double potency = 0;
	public double priority = 0;
	
	public int type = 0;
	public static int NONE = -1;
	public static int HEAL = 0;
	public static int DAMAGE = 1;
	public static int BURN = 2;
	public static int PARALYSE = 3;
	public static int COLD = 4;
	
	public Effect(int effect, double pot, double pri){
		potency = pot;
		priority = pri;
		type = effect;
	}
	
}
