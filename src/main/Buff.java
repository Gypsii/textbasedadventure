package main;

public class Buff {
//	public static final int RES_BLUNT = 0;
//	public static final int RES_SLASH = 1;
//	public static final int RES_PIERCE = 2;
//	public static final int RES_BURN = 3;
//	public static final int RES_COLD = 4;
//	public static final int RES_MAGIC = 5;
//	public static final int SHRED_BLUNT = 6;
//	public static final int SHRED_SLASH = 7;
//	public static final int SHRED_PIERCE = 8;
//	public static final int SHRED_BURN = 9;
//	public static final int SHRED_COLD = 10;
//	public static final int SHRED_MAGIC = 11;
	
	
	public static final int RESIST = 0;
	public static final int SHRED = 1;
	public static final int MAX_BUFF_ID = 1;//Update this when new buffs are added.
	
	public int type;
	public int potency;
	public double endTime;
	public Buff applyAfter = null;
	public int additionalData = 0;//used for damage type etc.

	public Buff(int t, int pot, double end){
		type = t;
		potency = pot;
		endTime = end;
	}
	
	public Buff(int t, int pot, double end, int data){
		type = t;
		potency = pot;
		endTime = end;
		additionalData = data;
	}

}
