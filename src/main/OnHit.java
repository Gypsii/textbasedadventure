package main;

public class OnHit {

	public static final int MAGIC = 0;
	public static final int BURN = 1;
	public static final int COLD = 2;
	public static final int SELFHEAL = 3;
	public static final int SELFDMG = 4;
	public static final int DEATHEXPLODE = 5;
	public static final int FEARSPIDERS = 6;
	public static final int LIGHTNING = 7;
	public static final int SHRED_BLUNT = 8;
	public static final int SHRED_SLASH = 9;
	public static final int SHRED_PIERCE = 10;
	public static final int SHRED_BURN = 11;
	public static final int SHRED_COLD = 12;
	public static final int SHRED_MAGIC = 13;
	
	public int type;
	public int potency;
	public double duration = 0;

	public OnHit(int t, int p){
		type = t;
		potency = p;
	}
	
	public OnHit(int t, int p, double dur){
		type = t;
		potency = p;
		duration = dur;
	}
	
}
