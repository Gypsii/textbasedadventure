package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import creatures.Slime;
import item.Item;
import util.IO;

public class Level {
	public HashMap<Integer, Zone> zones = new  HashMap<Integer, Zone>();
	ArrayList<Integer> slimeTypes = new ArrayList<Integer>();
	public static final int LEVEL_SIZE = 3;
	public Shop shop;
	public int temp = 100;
	static final int SLIME_STANDARD = 0;
	static final int SLIME_COLD = 1;
	static final int SLIME_FIRE = 2;
	static final int SLIME_JUNGLE = 3;
	static final int SLIME_MAGIC = 4;
	static final int SLIME_MIXED_BAG = 5;
	
	public Level(){
		
	}
	
	public void selectSlimes(){
		int slimeType = (int)(Math.random() * 5);//TODO real slime stuff
		switch(slimeType){
		case SLIME_STANDARD:
			slimeTypes.add(Slime.STICKY);
			slimeTypes.add(Slime.STICKY);
			slimeTypes.add(Slime.WATERY);
			slimeTypes.add(Slime.WATERY);
			slimeTypes.add(Slime.VOLATILE);
			slimeTypes.add(Slime.PLANT);
			break;
		case SLIME_JUNGLE:
			slimeTypes.add(Slime.STICKY);
			slimeTypes.add(Slime.STICKY);
			slimeTypes.add(Slime.PLANT);
			slimeTypes.add(Slime.PLANT);
			slimeTypes.add(Slime.SPICY);
			slimeTypes.add(Slime.FUNGAL);
			break;
		case SLIME_COLD:
			slimeTypes.add(Slime.ICY);
			slimeTypes.add(Slime.ICY);
			slimeTypes.add(Slime.WATERY);
			slimeTypes.add(Slime.WATERY);
			slimeTypes.add(Slime.STICKY);
			slimeTypes.add(Slime.EARTHEN);	
			break;
		case SLIME_FIRE:
			slimeTypes.add(Slime.FIERY);
			slimeTypes.add(Slime.FIERY);
			slimeTypes.add(Slime.MOLTEN);
			slimeTypes.add(Slime.MOLTEN);
			slimeTypes.add(Slime.EARTHEN);
			slimeTypes.add(Slime.VOLATILE);
			break;
		case SLIME_MAGIC:
			slimeTypes.add(Slime.ELECTRIC);
			slimeTypes.add(Slime.ELECTRIC);
			slimeTypes.add(Slime.FUNGAL);
			slimeTypes.add(Slime.FUNGAL);
			slimeTypes.add(Slime.ICY);
			slimeTypes.add(Slime.FIERY);
			break;
		case SLIME_MIXED_BAG:
			slimeTypes.add(Slime.STICKY);
			slimeTypes.add(Slime.WATERY);
			slimeTypes.add(Slime.ICY);
			slimeTypes.add(Slime.FIERY);
			slimeTypes.add(Slime.EARTHEN);
			slimeTypes.add(Slime.PLANT);
			break;
		}
	}
	
	public void generate(int diff){
		Random r = new Random();
		temp += (int)(r.nextGaussian() * 10);
		IO.println("Temp = " + temp);//TODO this is a debug
		selectSlimes();
		int setX = (int)(Math.random() * (LEVEL_SIZE - 1)) + 1;
		int setY = (int)(Math.random() * (LEVEL_SIZE - 1)) + 1;
		for(int w = 0; w <= LEVEL_SIZE; w++){
			for(int h = 0; h <= LEVEL_SIZE; h++){
				if((w == LEVEL_SIZE && h == 0) || (h == LEVEL_SIZE && w == 0) || (h == setY && w == setX)){
					zones.put(w + (100 * h), Zone.generateSetPiece());
				}else if(w == LEVEL_SIZE && h == LEVEL_SIZE){
					zones.put(w + (100 * h), Zone.generateFinalZone(diff, temp, slimeTypes));
				}else{
					zones.put(w + (100 * h), Zone.generateZone(diff, temp, slimeTypes));
				}
			}
		}
		shop = generateShop();
	}

	public static void generateLevel(int d){
		Level level = new Level();
		level.generate(d);
		Game.levels.put(Game.levelNum, level);
	}

	public static Shop generateShop(){
		Shop s = new Shop();
		s.addItem("bread", 4);
		s.randomItem("string", 0.8, 4, 6);
		s.randomItem("stickyString", 0.8, 2, 3);
		s.randomItem("clothCotton", 0.6, 2, 6);
		s.randomItem("mushroom", 0.4, 1, 6);
		s.randomItem("mushroomWhite", 0.4, 1, 6);
		s.randomItem("fungus", 0.1, 1, 2);
		s.randomItem("walrusTusk", 0.1, 1, 3);

		double r = Math.random();
		if(r < 0.02){
			s.randomItem("pepper", 0.7, 1, 5);
			s.randomItem("nutmeg", 0.7, 1, 5);
			s.randomItem("ginger", 0.7, 1, 5);
			s.randomItem("cinnamon", 0.7, 1, 5);
			s.randomItem("cumin", 0.7, 1, 5);
		} else if(r < 0.04){
			s.randomItem("diamond", 0.3, 1, 2);
			s.randomItem("emerald", 0.3, 1, 2);
			s.randomItem("ruby", 0.3, 1, 2);
			s.randomItem("sapphire", 0.3, 1, 2);
			s.randomItem("citrine", 0.3, 1, 2);
			s.randomItem("amethyst", 0.3, 1, 2);
		}

		int cheapWeapons = 0;
		while(cheapWeapons <3){
			switch ((int)(Math.random() * 6)) {
				case 0:
					if(s.findShopItemLoc(Item.item("sswordCopper")) == -1){
						s.addItem("sswordCopper");
						cheapWeapons++;
					}
					break;
				case 1:
					if(s.findShopItemLoc(Item.item("glaiveBladeRusted")) == -1){
						s.addItem("glaiveBladeRusted");
						cheapWeapons++;
					}
					break;
				case 2:
					if(s.findShopItemLoc(Item.item("maceRusted")) == -1){
						s.addItem("maceRusted");
						cheapWeapons++;
					}
					break;
				case 3:
					if(s.findShopItemLoc(Item.item("spearheadRusted")) == -1){
						s.addItem("spearheadRusted");
						cheapWeapons++;
					}
					break;
				case 4:
					if(s.findShopItemLoc(Item.item("glaiveCopper")) == -1){
						s.addItem("glaiveCopper");
						cheapWeapons++;
					}
					break;
				case 5:
					if(s.findShopItemLoc(Item.item("lswordCopper")) == -1){
						s.addItem("lswordCopper");
						cheapWeapons++;
					}
					break;
			}
		}

		s.randomItem("sswordBronze", 0.3, 1, 1);
		s.randomItem("sswordIron", 0.2, 1, 1);
		s.randomItem("maceCopper", 0.35, 1, 1);
		s.randomItem("maceBronze", 0.25, 1, 1);
		s.randomItem("maceIron", 0.15, 1, 1);
		s.randomItem("lswordBronze", 0.15, 1, 1);
		s.randomItem("lswordIron", 0.1, 1, 1);
		s.randomItem("vestFurDingo", 0.3, 1, 1);
		s.randomItem("vestFurBear", 0.2, 1, 1);
		s.randomItem("glaiveBladeCopper", 0.1, 1, 1);
		s.randomItem("glaiveBladeBronze", 0.2, 1, 1);
		s.randomItem("glaiveBladeIron", 0.1, 1, 1);
		s.randomItem("spearheadCopper", 0.3, 1, 1);
		s.randomItem("spearheadBronze", 0.2, 1, 1);
		s.randomItem("pikeCopper", 0.1, 1, 1);
		s.randomItem("pikeBronze", 0.1, 1, 1);
		s.randomItem("mailCopper", 0.1, 1, 1);
		s.randomItem("plateIron", 0.1, 1, 1);
		s.randomItem("mailIron", 0.1, 1, 1);
		return s;
	}
	
	public void printLevelDescription(){
		if(temp < 85){
			IO.println("It is very cold and barren.");
		}else if(temp < 95){
			IO.println("It is cold.");
		}else if(temp < 105){
			IO.println("It is temperate.");
		}else if(temp < 115){
			IO.println("It is warm.");
		}else{
			IO.println("It is hot.");
		}
	}
}
