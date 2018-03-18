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
		addShopItem("pepper", 35 + (int)(Math.random() * 15));
		if(Math.random() < 0.3){addShopItem("pepper", 35 + (int)(Math.random() * 10));}
		if(Math.random() < 0.5){addShopItem("cinnamon", 28 + (int)(Math.random() * 15));}
		if(Math.random() < 0.3){addShopItem("cinnamon", 24 + (int)(Math.random() * 15));}
		if(Math.random() < 0.5){addShopItem("nutmeg", 25 + (int)(Math.random() * 10));}
		if(Math.random() < 0.3){addShopItem("nutmeg", 22 + (int)(Math.random() * 10));}
		if(Math.random() < 0.5){addShopItem("ginger", 29 + (int)(Math.random() * 10));}
		if(Math.random() < 0.3){addShopItem("ginger", 25 + (int)(Math.random() * 10));}
		if(Math.random() < 0.5){addShopItem("cumin", 29 + (int)(Math.random() * 10));}
		if(Math.random() < 0.3){addShopItem("cumin", 25 + (int)(Math.random() * 10));}
		if(Math.random() < 0.5){addShopItem("vanilla", 45 + (int)(Math.random() * 10));}
		if(Math.random() < 0.3){addShopItem("vanilla", 37 + (int)(Math.random() * 10));}
		if(Math.random() < 0.2){addShopItem("saffron", 150 + (int)(Math.random() * 100));}
		if(Math.random() < 0.1){addShopItem("saffron", 140 + (int)(Math.random() * 100));}
		
		addItem("money", (int)(Math.random() * shopInv.size() * 20) + 15);
	}
	
	public String getDescription(){
		return "Sells a variety of exotic spices. Press H to haggle with the trader.";
	}
}
