package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import creatures.Slime;

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
		System.out.println("Temp = " + temp);//TODO this is a debug
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
	
//	public void generate2(){
//		int approxDiff = 0;
//		Random r = new Random();
//		temp = 100 + (int)(r.nextGaussian() * 10);
//		selectSlimes();
//		int humanPresence = Math.max((int)(r.nextGaussian() * 15), 0);
//		int hobgPresence = Math.max((int)(r.nextGaussian() * 15), 0);
//		int fiendPresence = Math.max((int)(r.nextGaussian() * 15), 0);
////		approxDiff += humanPresence * 1.5;
////		approxDiff += hobgPresence;
////		approxDiff += fiendPresence * 4;
//		int magicLevel = Math.max((int)(r.nextGaussian() * 10) + 70, 50);
//	}
	
	public static void generateLevel(int d){
		Level level = new Level();
		level.generate(d);
		Game.levels.put(Game.levelNum, level);
	}
	
	public static Shop generateShop(){
		Shop s = new Shop();
		s.addItem(s.string, 1, 1);
		s.addItem(s.stickyString, 1, 1);
		s.addItem(s.bread, 1, 1);
		if(Math.random() < 0.4){s.addItem("mushroom");}
		if(Math.random() < 0.4){s.addItem("mushroomWhite");}
		if(Math.random() < 0.1){s.addItem("fungus");}
		
		if(Math.random() < 0.4){s.addItem("walrusTusk");}
		int cheapWeapons = 0;
		while(cheapWeapons < 3){
			if(Math.random() < 0.2 && !s.items.contains("sswordCopper")){
				s.addItem("sswordCopper");
				cheapWeapons ++;
			}
			if(Math.random() < 0.2 && !s.items.contains("glaiveBladeRusted")){
				s.addItem("glaiveBladeRusted");
				cheapWeapons ++;
			}
			if(Math.random() < 0.2 && !s.items.contains("maceRusted")){
				s.addItem("maceRusted");
				cheapWeapons ++;		
			}	
			if(Math.random() < 0.2 && !s.items.contains("spearheadRusted")){
				s.addItem("spearheadRusted");
				cheapWeapons ++;		
			}
			if(Math.random() < 0.2 && !s.items.contains("glaiveCopper")){
				s.addItem("glaiveCopper");
				cheapWeapons ++;		
			}
			if(Math.random() < 0.2 && !s.items.contains("lswordCopper")){
				s.addItem("lswordCopper");
				cheapWeapons ++;		
			}
		}
		if(Math.random() < 0.3){s.addItem("sswordBronze");}
		if(Math.random() < 0.2){s.addItem("sswordIron");}
		if(Math.random() < 0.35){s.addItem("maceCopper");}
		if(Math.random() < 0.25){s.addItem("maceBronze");}
		if(Math.random() < 0.15){s.addItem("maceIron");}
		if(Math.random() < 0.15){s.addItem("lswordBronze");}
		if(Math.random() < 0.1){s.addItem("lswordIron");}
		if(Math.random() < 0.3){s.addItem("vestFurDingo");}
		if(Math.random() < 0.2){s.addItem("vestFurBear");}
		if(Math.random() < 0.1){s.addItem("glaiveBladeCopper");}
		if(Math.random() < 0.2){s.addItem("glaiveBladeBronze");}
		if(Math.random() < 0.1){s.addItem("glaiveBladeIron");}
		if(Math.random() < 0.3){s.addItem("spearheadCopper");}
		if(Math.random() < 0.2){s.addItem("spearheadBronze");}
		if(Math.random() < 0.1){s.addItem("pikeCopper");}
		if(Math.random() < 0.1){s.addItem("pikeBronze");}
		if(Math.random() < 0.1){s.addItem("mailCopper");}
		if(Math.random() < 0.1){s.addItem("plateIron");}
		if(Math.random() < 0.1){s.addItem("mailIron");}

	
		//double r = Math.random();
		return s;
	}
	
	public void printLevelDescription(){
		if(temp < 85){
			System.out.println("It is very cold and barren.");
		}else if(temp < 95){
			System.out.println("It is cold.");
		}else if(temp < 105){
			System.out.println("It is temperate.");
		}else if(temp < 115){
			System.out.println("It is warm.");
		}else{
			System.out.println("It is hot.");
		}
	}
}
