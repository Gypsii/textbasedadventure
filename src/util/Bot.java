package util;

import main.Level;
import main.Game;
import main.Zone;
import creatures.Creature;
import creatures.Player;
import item.Item;

public class Bot {

	//TODO delete or fix

	static int targeting = -1;
	static int taking = -1;
	static int equipping = -1;
	static int eating = -1;
	static int silk = 0;
	static boolean[][] visited = new boolean[4][4];
	static int x = 0;
	static int y = 0;
	static boolean inShop = true;
	static int craftStage = -1;
	
	static Player me = Game.player;
	
	public static String takeTurn(){
//		Zone zone =  Game.zone;
//		if(targeting >= 0){
//			String t = String.valueOf(targeting);
//			targeting = -1;
//			return t;
//		}
//		if(taking >= 0){
//			String t = String.valueOf(taking);
//			if(zone.items.get(taking).id.equals("silk")){
//				silk++;
//			}
//			taking = -1;
//			return t;
//		}
//		if(equipping >= 0){
//			String e = String.valueOf(equipping);
//			equipping = -1;
//			return e;
//		}
//		if(craftStage >= 0){
//			craftStage++;
//			switch(craftStage){
//			case 1:
//				return "2";
//			case 2:
//				return "0";
//			case 3:
//				return "0";
//			default:
//				craftStage = -1;
//				silk = -1234567890;
//				return "-1";
//			}
//		}
//		if(eating >= 0){
//			String e = String.valueOf(eating);
//			eating = -1;
//			return e;
//		}
//
//		if(!zone.items.isEmpty()){
//			for(int i = 0; i < zone.items.size(); i++){
//				if(!zone.items.get(i).id.equals("egg")){
//					taking = i;
//					return "k";
//				}
//			}
//		}
//
//		for(int n = 0; n < me.inv.size(); n++){
//			double best = me.equipped.dmg / me.equipped.swingTime;
//			Item i = me.inv.get(n);
//			if(i.armourType > 0){
////				Item a = me.inv.get(n);
////				int resists = (a.bluntResist + a.slashResist + a.pierceResist) * 3 + a.burnResist + a.coldResist + a.magicResist;
////				int myResists = (me.bluntArmour + me.slashArmour + me.pierceArmour) * 3 + me.burnArmour + me.coldArmour + me.magicArmour;
////				if(resists > myResists){
////					equipping = n;
////				}
//			}else{
//				if(i.dmg / i.swingTime > best){
//					best = i.dmg / i.swingTime;
//					equipping = n;
//				}
//			}
//		}
//
//		if(equipping >= 0){
//			return "q";
//		}
//
//		if(!zone.creatures.isEmpty()){
//			Creature t = zone.creatures.get(Game.targetIndex);
//
//			if(t.isAlive() && t.hostileTowards(me)){
//				return "a";
//			}else{
//				for(int i = 0; i < zone.creatures.size(); i++){
//					Creature c = zone.creatures.get(i);
//					if(c.isAlive() && c.hostileTowards(me)){
//						targeting = i;
//						return "t";
//					}
//				}
//			}
//		}
//		if(silk > 4){
//			craftStage = 0;
//			return "m";
//		}
//		for(int i = 0; i < me.inv.size(); i++){
//			if(me.inv.get(i).isFood){
//				Food f = (Food)me.inv.get(i);
//				if(f.healthRestore > 0 && me.maxHp - me.hp >= f.healthRestore){
//					eating = i;
//					return "f";
//				}
//			}
//		}
//
		return nextZone();

	}
	
	static String nextZone(){
//		if(y < Level.LEVEL_SIZE && x != Level.LEVEL_SIZE){
//			y++;
//			visited[x][y] = true;
//			return "n";
//		}else if(x < Level.LEVEL_SIZE){
//			x++;
//			visited[x][y] = true;
//			return "e";
//		}else if(x == Level.LEVEL_SIZE && y > 0 && !visited[x][y-1]){
//			IO.print("<cyan>GOING SOUTH<r>");
//			y--;
//			visited[x][y] = true;
//			return "s";
//		}else if(y < Level.LEVEL_SIZE){
//			y++;
//			visited[x][y] = true;
//			return "n";
//		}
//
//		for(int i = 0; i < Level.LEVEL_SIZE; i++){
//			for(int j = 0; j < Level.LEVEL_SIZE; j++){
//				visited[i][j] = false;
//			}
//		}
//		x = 0;
//		y = 0;
////		inShop = true;
		return "z";
	}
	
	
}
