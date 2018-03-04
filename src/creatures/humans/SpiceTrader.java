package creatures.humans;

public class SpiceTrader extends Human{
	
	public void setTags(){
		addTag("merchant");
		addTag("spiceTrader");
	}
	
	public void setWeapon(){
		String i;
		double rand = Math.random();
		if(rand > 0.25){
			i = "sswordBronze";
		}else if(rand > 0.5){
			i = "maceBronze";
		}else if(rand > 0.75){
			i = "axeCopper";
		}else{
			i = "sswordCopper";
		}
		addItem(i);
		equip(i);
	}
	
	public void setArmour(){
		String a = "tunicRagged";
		double rand = Math.random();
		if(rand < 0.1){
			a = "vestFurDingo";
		}else if(rand < 0.4){
			a = "tunicWoolPadded";
		}else if(rand < 0.5){
			a = "tunicLinen";
		}else if(rand < 0.6){
			a = "tunicSilk";
		}else if(rand < 0.95){
			a = "tunicCotton";
		}
		addItem(a);
		equip(a);	
	}
	
	public void setSpecifics(){
		name = "Spice Trader";
		hp = 150;
		maxHp = 150;
		addShopItem("pepper", 45);
		if(Math.random() < 0.3){addShopItem("pepper", 40);}
		if(Math.random() < 0.5){addShopItem("cinnamon", 38);}
		if(Math.random() < 0.3){addShopItem("cinnamon", 35);}
		if(Math.random() < 0.5){addShopItem("nutmeg", 38);}
		if(Math.random() < 0.3){addShopItem("nutmeg", 35);}
		if(Math.random() < 0.5){addShopItem("ginger", 34);}
		if(Math.random() < 0.3){addShopItem("ginger", 30);}
		
		addItem("money", (int)(Math.random() * shopInv.size() * 15) + 15);
	}
	
	public String getDescription(){
		return "Sells a variety of exotic spices. Press H to haggle with the trader.";
	}
}
