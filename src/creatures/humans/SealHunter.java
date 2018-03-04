package creatures.humans;

import item.Item;

import java.util.Random;

import main.Tag;

public class SealHunter extends Human{
	
	public void setTargets(){
		addTarget(Tag.tag("seal"), 103);
		addTarget(Tag.tag("walrus"), 101);
	}
	
	public void setSpecifics(){
		addTag("sealHunter");
		name = "Seal Hunter";
		
		Random r = new Random();	
		int value = (int) (10 + Math.abs(r.nextGaussian()) * 25);
		addItem(Item.item("money"), (int)(Math.random() * value + 5));
		
		if(Math.random() < 0.2){addItem("meatSeal");}
		if(Math.random() < 0.2){addItem("meatSeal");}
		if(Math.random() < 0.2){addItem("meatSeal");}
	}
	
	public void setWeapon(){
		String i;
		double rand = Math.random();
		if(rand > 0.25){
			i = "spearBronze";
		}else if(rand > 0.8){
			i = "spearCopper";
		}else{
			i = "spearRusted";
		}
		addItem(i);
		equip(i);
	}
	
	public void setArmour(){
		String a = "tunicRagged";
		double rand = Math.random();
		if(rand < 0.15){
			a = "vestFurBear";
		}else if(rand < 0.4){
			a = "vestFurBear";
		}else if(rand < 0.5){
			a = "tunicWoolPadded";
		}else if(rand < 0.6){
			a = "tunicWool";
		}else if(rand < 0.95){
			a = "tunicCotton";
		}
		addItem(a);
		equip(a);	
	}
	
	public String getDescription(){
		return "Lives in icy lands, hunts seals and walruses.";
	}
}
