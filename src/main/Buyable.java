package main;


public class Buyable {
	String id;
	double rarityConstant = 1;
	double localAvailability = 1;
	double baseValue;
	double marketValue;
	int buyValue;
	int sellValue;
	
	public Buyable(String id, double v){
		this.id = id;
		baseValue = v;
		calculateValue();
	}
	
	public void calculateValue(){
		marketValue = baseValue / rarityConstant * localAvailability; 
	}
}
