package main;

import creatures.*;
import effects.Condition;
import item.Item;
import item.MagicItem;
import item.Scroll;

import java.util.ArrayList;
import java.util.Random;

import creatures.humans.Bandit;
import creatures.humans.Hobgoblin;
import creatures.humans.Human;
import creatures.humans.SealHunter;
import creatures.humans.SpiceTrader;
import util.IO;
import util.Position;

public class Zone {
	
	public ArrayList<Creature> creatures = new ArrayList<Creature>();
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<String> descriptors = new ArrayList<String>();//Prints in description of zone.

	public static final int TILE_COUNT = 15;

	private Tile[][] tiles = new Tile[TILE_COUNT][TILE_COUNT];

	public boolean containsEgg = false;//TODO find a better way of doing this that would work for any item
	public boolean isFinalZone = false;

	public boolean inFocus = false;

	public Zone() {
		for (int x = 0; x < TILE_COUNT; x++) {
			for (int y = 0; y < TILE_COUNT; y++) {
				tiles[x][y] = new Tile(new Position(x,y));
			}
		}
	}

	public static Zone generateZone(int diff, int temp, ArrayList<Integer> slimeTypes){
		Random r = new Random();
		temp += r.nextGaussian() * 2;
		Zone zone = new Zone();
//		for(int i = 5; i < 10; i++) {
//			zone.getTile(new Position(5, i)).tileType = TileType.WALL;
//		}
//		for(int i = 6; i < 10; i++) {
//			zone.getTile(new Position(i, 9)).tileType = TileType.WALL;
//		}
//		for(int i = 5; i < 10; i++) {
//			zone.getTile(new Position(i, 4)).tileType = TileType.WATER;
//		}


		int d = (int)(1.5 * diff + Math.random() * diff);
		int loot = (int)(Math.random() * diff + 0.5);
		int value = 0;
		while(d > value){
			if(Math.random() < 0.2){
				if(d - value >= 40){
					zone.addCreature(new FiendGuardian());
					value += 40;
				}
			}
			if(Math.random() < 0.2){
				if(d - value >= 25){
					zone.addCreature(new Fiend());
					value += 25;
				}
			}
			if(temp <= 90){
				if(Math.random() < 0.2){
					if(d - value >= 12){
						zone.addCreature("polarBear");
						value += 12;
					}
				}
			}
			if(temp >= 106){
				if(Math.random() < 0.12){
					if(d - value >= 11){
						zone.addCreature("tiger");
						value += 11;
					}
				}
			}
			if(Math.random() < 0.1){
				if(d - value >= 6){
					zone.addCreature("spiderGiant");
					value += 6;
					for(int i = 0; i < 10; i++){
						if(Math.random() < 1 - 0.1 * i && d - value >= 6){
							zone.addCreature("spiderGiant");
							value += 6;
						}else{
							break;
						}
					}	
				}
			}
			if(temp >= 105){
				if(Math.random() < 0.1){
					if(d - value >= 7){
						zone.addCreature("turtle");
						value += 7;
					}
				}
			}
			if(temp >= 110){
				if(Math.random() < 0.1){
					if(d - value >= 2){
						zone.addCreature("spider");
						value += 2;
					}
				}
			}
			if(temp <= 95){
				if(Math.random() < 0.1){
					if(d - value >= 13){
						zone.addCreature(new SealHunter());
						value += 13;
					}
				}
			}
			
			if(Math.random() < 0.1){
				if(d - value >= 16){
					zone.addCreature(new Bandit());
					value += 16;
				}	
			}
			if(Math.random() < 0.1){
				if(d - value >= 8){
					zone.addCreature(new Hobgoblin());
					value += 8;
				}	
			}
			if(temp <= 110 && Math.random() < 0.1){
				if(d - value >= 5){
					zone.addCreature(new Dingo(Dingo.SNOW));
					value += 5;
				}
			}
			if(Math.random() < 0.3){
				int s = slimeTypes.get((int)(Math.random() * slimeTypes.size()));
				if(d - value >= 21 + Slime.diffConstants[s] * 3){
					zone.addCreature(new Slime(s, 4));
					value += 21 + Slime.diffConstants[s] * 3;
				}else if(d - value >= 5 + Slime.diffConstants[s] * 2){
					zone.addCreature(new Slime(s, 2));
					value += 5 + Slime.diffConstants[s] * 2;
				}else if(d - value >= 3 + Slime.diffConstants[s]){
					zone.addCreature(new Slime(s, 1));
					value += 3 + Slime.diffConstants[s];
				}
			}
			if(d < 10){
				if(Math.random() < 0.10){
					zone.addCreature("spider");
					value += 1;
					for(int i = 0; i < 10; i++){
						if(Math.random() < 1 - 0.12 * i && d > value){
							zone.addCreature("spider");
							value += 1;
						}else{
							break;
						}
					}	
				}
			}	
			if(temp <= 116 && d < 14){
				if(Math.random() < 0.15){
					if(d - value >= 2){
						zone.addCreature("rat");
						value += 2;
					}	
				}
			}
			if(temp <= 96){
				if(Math.random() < 0.15){
					if(d - value >= 2){
						zone.addCreature("walrus");
						value += 2;
					}	
				}
			}
			if(Math.random() < 0.18){
				value += 1;
			}
		}
		//Passive creatures
		int count = 0;
		while(count < Game.PASSIVE_CREATURE_CAP){
			if(temp <= 97 && Math.random() < 0.15 && count < Game.PASSIVE_CREATURE_CAP){//These random numbers are low so creatures at the top are not biased towards as heavily.
				zone.addCreature("seal");
				count ++;
			}
			if(Math.random() < 0.25 && count < Game.PASSIVE_CREATURE_CAP){
				ArrayList<Integer> birds = new ArrayList<Integer>();
				if(temp <= 96){
					birds.add(Bird.SNOW);
				}
				if(temp <= 110 && temp >= 93){
					birds.add(Bird.CROW);
				}
				if(temp <= 118 && temp >= 97){
					birds.add(Bird.STANDARD);
				}
				if(temp >= 114){
					birds.add(Bird.TROPICAL);
				}
				if(birds.size() > 0){
					zone.addCreature(new Bird(birds.get((int)(Math.random() * birds.size()))));
					if(Math.random() < 0.2){
						zone.addItem("egg");
					}
					count ++;
				}
			}
			if(temp <= 118 && temp >= 98 && Math.random() < 0.15 && count < Game.PASSIVE_CREATURE_CAP){
				zone.addCreature(new Mouse());
				count ++;
			}
			if(Math.random() < 0.35){
				count ++;
			}
		}
		//Loot
		int l = 0;
		while(loot > l){//TODO get some more cool loot in here
			double rand = Math.random();
			if(rand < 0.15){
				if(d - l >= 1){
					zone.addItem("stick");
					l += 1;
				}
			}else if(rand < 0.3){
				if(d - l >= 1){
					zone.addItem("flint");
					l += 1;
				}
			}else if(rand < 0.33){
				if(d - l >= 1){
					zone.addItem("mushroomRed");
					l += 1;
				}
			}else if(rand < 0.36){
				if(d - l >= 1){
					zone.addItem("mushroom");
					l += 1;
				}
			}else if(rand < 0.4){
				if(d - l >= 1){
					zone.addItem("mushroomWhite");
					l += 1;
				}
			}else if(rand < 0.5){
				if(d - l >= 1){
					zone.addItem("berries");
					l += 1;
				}
			}else if(rand < 0.6){
				if(d - l >= 5){
					zone.addItem("fungus");
					l += 5;
				}
			}else if(rand < 0.8){
				if(d - l >= 1){
					zone.addItem("rock");
					l += 1;
				}
			}else{
				value ++;
			}	
		}
		return zone;
	}
	
	public static Zone generateSetPiece(){//TODO read setpieces from files
		Zone zone = new Zone();
		int zoneNum = (int)(Math.random() * 22);
		switch(zoneNum){
		case 0:
			default:
			zone.addCreature(new Human(){
				public void overwriteStats(){
					maxHp = 140;
					hp = 140;
					name = "Wandering Chef";
					addTag("merchant");
					for(int i = 0; i < 4; i++){
						if(Math.random() < 0.7){addShopItem("bread", 10);}
					}
					for(int i = 0; i < 4; i++){
						if(Math.random() < 0.6){addShopItem("pastry", 12);}
					}
					if(Math.random() < 0.7){addShopItem("cinnamonBun", 30);}
					if(Math.random() < 0.3){addShopItem("meatSpicy", 50);}
					if(Math.random() < 0.4){addShopItem("cake", 28);}
					if(Math.random() < 0.7){addShopItem("soupFish", 17);}
					if(Math.random() < 0.7){addShopItem("mealMeatMushroom", 24);}
					if(Math.random() < 0.5){addShopItem("jamTart", 43);}
					if(Math.random() < 0.5){
						for(int i = 0; i < 3; i++){
							addShopItem("syrup", 38);
						}
					}
					if(Math.random() < 0.5){
						for(int i = 0; i < 3; i++){
							addShopItem("sugar", 15);
						}
					}
					if(Math.random() < 0.5){
						for(int i = 0; i < 2; i++){
							addShopItem("vanilla", 50);
						}
					}

				}
				
				public void setArmour(){
					addItem("tunicLinenPadded");
					equip("tunicLinenPadded");
					addItem("beret");
					equip("beret");
				}
				
				public void setWeapon(){
					addItem("sswordIron");
					equip("sswordIron");
				}
				
				public String getDescription(){
					return "Sells food and food related products. Press H to haggle with the trader.";
				}
			});
			break;
		case 1:
			Creature graham = Creature.creature("walrus");
			graham.naturalAttackPattern = AttackPattern.weaponAttack;
			graham.name = "Graham the Walrus";
			graham.xp = 30;
			graham.addItem("clawGloves");
			zone.addCreature(graham);
			break;
		case 2:
			zone.addCreature(new Slime(Slime.PLANT, 1));
			zone.addItem("slimeSticky");
			zone.addItem("slimeWater");
			zone.addItem("slimeFire");
			zone.addItem("slimeFire");
			break;
		case 3:
			Position p = new Position((int)(Math.random() * (TILE_COUNT-2) +1), (int)(Math.random() * (TILE_COUNT-2) +1));
			zone.addItem("butterKnife", p.add(new Position(1, 0)));
			zone.addItem("fork", p.add(new Position(-1, 0)));
			Dingo bruce = new Dingo(Dingo.STANDARD);
			bruce.hp = 0;
			bruce.conditions.add(Condition.DEAD);
			bruce.name = "Roasted Dingo";
			bruce.addBodyPart("meatRoast", 1);
			zone.addCreature(bruce, p);
			break;
		case 4:
			zone.addCreature(new SpiceTrader());
			break;
		case 5:
			if(Math.random() < 0.5){zone.addItem("slimeSticky");}
			if(Math.random() < 0.5){zone.addItem("rock");}
			if(Math.random() < 0.4){zone.addItem("featherSnow");}
			if(Math.random() < 0.4){zone.addItem("feather");}
			if(Math.random() < 0.4){zone.addItem("featherWarm");}
			if(Math.random() < 0.6){zone.addItem("flint");}
			if(Math.random() < 0.6){zone.addItem("stick");}
			if(Math.random() < 0.6){zone.addItem("string");}
			if(Math.random() < 0.4){zone.addItem("flax");}
			zone.addItem(Item.randomGem());
			break;
		case 6:
			Creature nigel = Creature.creature("seal");
			nigel.hp = 0;
			nigel.conditions.add(Condition.DEAD);
			zone.addCreature(nigel);
			Creature dwayne = Creature.creature("spiderGiant");
			dwayne.hp = 0;
			dwayne.conditions.add(Condition.DEAD);
			if(Math.random() < 0.6){zone.addCreature(new Bird(Bird.SNOW));}
			if(Math.random() < 0.6){zone.addCreature(new Bird(Bird.SNOW));}
			zone.addCreature(dwayne);
			zone.addItem("spearheadRusted");
			Item i = Item.item("money").clone();
			i.count += (int)(Math.random() * 20);
			zone.addItem(i);
			zone.descriptors.add("You see the remains of a dead adventurer.");
			break;
		case 7:
			zone.addItem("clothCotton");
			if(Math.random() < 0.5){zone.addItem("clothCotton");}
			if(Math.random() < 0.5){zone.addItem("clothCotton");}
			if(Math.random() < 0.5){zone.addItem("clothCotton");}
			if(Math.random() < 0.5){zone.addItem("clothLinen");}
			if(Math.random() < 0.5){zone.addItem("clothLinen");}
			if(Math.random() < 0.5){zone.addItem("clothLinen");}
			if(Math.random() < 0.5){zone.addItem("clothSilk");}
			if(Math.random() < 0.3){zone.addItem("hidePBear");}
			if(Math.random() < 0.5){zone.addItem("hideDingo");}
			zone.descriptors.add("You see a pile of various cloths and pelts.");
			break;
		case 8:
			zone.addItem("mushroomWhite");
			zone.addItem("mushroom");
			zone.addItem("leaf");
			if(Math.random() < 0.4){zone.addItem("fungus");}
			if(Math.random() < 0.4){zone.addItem("fungus");}
			if(Math.random() < 0.4){zone.addItem("mushroomWhite");}
			if(Math.random() < 0.4){zone.addItem("mushroom");}
			if(Math.random() < 0.8){zone.addItem("mushroomRed");}
			if(Math.random() < 0.7){zone.addItem("leaf");}
			if(Math.random() < 0.5){zone.addItem("flax");}
			if(Math.random() < 0.5){zone.addItem("flax");}
			if(Math.random() < 0.5){zone.addItem("cotton");}
			if(Math.random() < 0.5){zone.addItem("cotton");}
			if(Math.random() < 0.7){zone.addItem("stick");}
			break;
		case 9:
			zone.addCreature(new Human(){
				public void overwriteStats(){
					name = "Farmer";
				}
			});
			zone.addCreature(new Cow());
			if(Math.random() < 0.5){zone.addCreature(new Cow());}
			if(Math.random() < 0.5){zone.addCreature(new Cow());}
			break;
		case 10:
			Item i2 = Item.item("icicle").clone();
			i2.count += Math.random() * 4;
			zone.addItem(i2);
			i2 = Item.item("slimeIce").clone();
			i2.count += Math.random() * 2;
			zone.addItem(i2);
			zone.addCreature(new Slime(Slime.ICY, 1){
				public void overwriteStats(){
					double x = Math.random();
					Item ring = Item.item("ringGold");	
					if(x < 0.33){
						ring = MagicItem.enchant(ring, MagicItem.HIT_COLD, (int)(Math.random() + 2.5));
					}else if(x < 0.67){
						ring = MagicItem.enchant(ring, MagicItem.HIT_SHRED_COLD, (int)(Math.random() + 2.5));
					}else{
						ring = MagicItem.enchant(ring, MagicItem.RES_COLD, (int)(Math.random() + 2.5));
					}
					addItem(ring);
					sleep(true);
				}
				
				public void deathTrigger(){
					zone.descriptors.clear();
				}
			});
			zone.descriptors.add("You see frozen ring lodged in a slime.");
			break;
		case 11:
			Item i3 = Item.item("slimeFire").clone();
			i3.count += Math.random() * 2;
			zone.addItem(i3);
			zone.addItem("obsidian");
			zone.addCreature(new Slime(Slime.FIERY, 1){
				public void overwriteStats(){
					double x = Math.random();
					Item ring = Item.item("ringGold");	
					if(x < 0.33){
						ring = MagicItem.enchant(ring, MagicItem.HIT_BURN, (int)(Math.random() + 2.5));
					}else if(x < 0.67){
						ring = MagicItem.enchant(ring, MagicItem.HIT_SHRED_BURN, (int)(Math.random() + 2.5));
					}else{
						ring = MagicItem.enchant(ring, MagicItem.RES_BURN, (int)(Math.random() + 2.5));
					}
					addItem(ring);
					sleep(true);
				}
				
				public void deathTrigger(){
					zone.descriptors.clear();
				}
			});
			zone.descriptors.add("You see ring lodged in a burning slime.");
			break;
		case 12:
			//TODO
		case 13:
			Creature guardGiantSpider = Creature.creature("spiderGiant");
			guardGiantSpider.name = "<purple>Giant Guardian Spider<r>";
			guardGiantSpider.sleep(true);
			guardGiantSpider.addItem(MagicItem.pickRandomMagicItem(5));
			zone.addCreature(guardGiantSpider);
			break;
		case 14:
			zone.addCreature(new Human(){
				public void overwriteStats(){
					name = "Armour Merchant";
					addTag("merchant");
					hp = 200;
					maxHp = 200;
					baseCritChance = 0.25;
					this.addShopItem(Item.item("armourLeather"), (int)(Item.item("armourLeather").cost * (Math.random()/2 + 0.5)));
					this.addShopItem(Item.item("mailCopper"), (int)(Item.item("mailCopper").cost * (Math.random()/2 + 0.5)));
					this.addShopItem(Item.item("plateIron"), (int)(Item.item("plateIron").cost * (Math.random()/2 + 0.5)));
					this.addShopItem(Item.item("mailSteel"), (int)(Item.item("mailSteel").cost * (Math.random()/2 + 0.5)));
				}
				
				public void setArmour(){
					addItem("plateIron");
					equip("plateIron");
				}
				
				public void setWeapon(){
					addItem("lswordBronze");
					equip("lswordBronze");
				}
				
			});
			break;
		case 15:
			zone.addItem("bread");
			if(Math.random() < 0.5){zone.addItem("bread");}
			if(Math.random() < 0.5){zone.addItem("bread");}
			if(Math.random() < 0.5){zone.addItem("bread");}
			if(Math.random() < 0.5){zone.addItem("cinnamonBun");}
			if(Math.random() < 0.2){zone.addItem("stroopwafel");}
			if(Math.random() < 0.5){zone.addItem("pastry");}
			if(Math.random() < 0.5){zone.addItem("jamTart");}
			if(Math.random() < 0.5){zone.addItem("cake");}
			zone.descriptors.add("You see a stash of baked goods.");
			break;
		case 16:
			zone.addCreature(new Slime(Slime.GEM, 1));
			break;
		case 17:
			zone.addCreature(new Elemental((int)(Math.random() * Elemental.ELEMENTAL_TYPES)){//Currently fighting this elemental is optional.
				public void overwriteStats(){
					conditions.add(Condition.SEALED);
				}
			});
			double x = Math.random();
			Scroll s;
			if(x < 0.25){
				s = Scroll.scrollCrit;
			}else if(x < 0.5){
				s = Scroll.scrollOnHitDmg;
			}else if(x < 0.75){
				s = Scroll.scrollOnHitShred;
			}else{
				s = Scroll.scrollResist;
			}
			zone.addItem(s);
			break;
		case 18:
			zone.addCreature(new Human(){
				public void overwriteStats(){
					name = "Jeweller";
					addTag("merchant");
					hp = 165;
					maxHp = 165;
					if(Math.random() < 0.4){addShopItem(Item.item("amethyst"), 180);}
					if(Math.random() < 0.4){addShopItem(Item.item("citrine"), 180);}
					if(Math.random() < 0.4){addShopItem(Item.item("diamond"), 180);}
					if(Math.random() < 0.4){addShopItem(Item.item("emerald"), 180);}
					if(Math.random() < 0.4){addShopItem(Item.item("ruby"), 180);}
					if(Math.random() < 0.4){addShopItem(Item.item("sapphire"), 180);}
					if(this.shopInv.isEmpty()){
						addShopItem(Item.item("diamond"), 180);
					}
				}
				
				public void setArmour(){
					addItem(Item.item("mailIron"));
					equip(Item.item("mailIron"));
					addItem("cloakSilk");
					equip("cloakSilk");
					addItem("topHat");
					equip("topHat");
				}
				
				public void setWeapon(){
					addItem("sswordIron");
					equip("sswordIron");
				}
				
				public String getDescription(){
					 return "Sells precious gemstones. Press $ to haggle with the trader.";
				}
				
			});
			break;
		case 19:
			zone.addCreature(new Human(){
				public void overwriteStats(){
					name = "Scroll Dealer";
					hp = 180;
					maxHp = 180;
					if(Math.random() < 0.7){addShopItem(Scroll.scrollCrit, 320);}
					if(Math.random() < 0.7){addShopItem(Scroll.scrollOnHitDmg, 500);}
					if(Math.random() < 0.7){addShopItem(Scroll.scrollResist, 300);}
					if(Math.random() < 0.7){addShopItem(Scroll.scrollOnHitShred, 435);}
				}
				
				public void setArmour(){
					addItem("tunicLinenPadded");
					equip("tunicLinenPadded");
					addItem("beret");
					equip("beret");
				}
				
				public void setWeapon(){
					addItem("sswordIron");
					equip("sswordIron");
				}
				
				public String getDescription(){
					 return "Sells mystical scrolls. Press $ to haggle with the trader.";
				}
			});
			break;
		case 20:
		case 21:
			Creature guardSpider = Creature.creature("spider");
			guardSpider.name = "<purple>Guardian Spider<r>";
			guardSpider.sleep(true);
			guardSpider.addItem(MagicItem.pickRandomMagicItem(2));
			zone.addCreature(guardSpider);
			break;
		}
		return zone;
	}
	
	public static Zone generateFinalZone(int diff, int temp, ArrayList<Integer> slimeTypes){
		Zone zone = generateZone(diff + 1, temp, slimeTypes);
		zone.descriptors.add("This is the final zone. Press x to leave the level. ");
		zone.isFinalZone = true;
		return zone;
	}

	public void printDescription(){
		if(descriptors.size() > 0){
			for(int i = 0; i < descriptors.size(); i++){
				IO.print(descriptors.get(i));
			}
			IO.println("");
		}
	}

	public void printMap() {
		String s = "";
		for (int y = TILE_COUNT-1; y >= 0; y--) {
			for (int x = 0; x < TILE_COUNT; x++) {
				s += tiles[x][y].getSymbol();
			}
			s += "\n";
		}
		IO.println(s);
	}

	public boolean inBounds(Position p) {
		return p.x < TILE_COUNT && p.y < TILE_COUNT && p.x >= 0 && p.y >= 0;
	}

	public int distanceTo(Creature c1, Creature c2) {
		if(!((creatures.contains(c1) || c1 == Game.player) && (creatures.contains(c2) || c2 == Game.player))) {
			throw new IllegalArgumentException("Compared creatures are not in the current zone");
		}
		return c1.position.manhattan(c2.position);
	}
	
	public void addCreature(String id){
		addCreature(Creature.creature(id));
	}
	
	public void addCreature(Creature c){
		Position p;
		do {
			p = new Position((int)(Math.random() * TILE_COUNT), (int)(Math.random() * TILE_COUNT));
		} while(!c.canMoveInto(p));
		addCreature(c, p);
	}

	public void addCreature(Creature c, Position p) {
		c.position = p;
		if(inFocus){
			//Graphics.notifyCreatureAddition(c);
		}
		c.zone = this;
		for(Creature c2 : creatures){
			c2.addPresentCreature(c);
			c.addPresentCreature(c2);
		}
		for(int i = 0; i < creatures.size(); i++){
			if(creatures.get(i) == c) {
				continue;
			}
			if(creatures.get(i).position == c.position) {
				i = 0;
				int x,y;
				do {
					x = (int)(Math.random() * TILE_COUNT);
					y = (int)(Math.random() * TILE_COUNT);
				} while(tiles[x][y].creature != null && tiles[x][y].tileType.hasSpace());
				c.position = new Position(x, y);
			}
		}
		if (c != Game.player) {
			creatures.add(c);
			if(Game.zone == this){
				Game.insertIntoTurnOrder(c); //puts the new creature just at the front of the queue.
			}
		}

		tiles[c.position.x][c.position.y].creature = c;
	}

	public void removeCreature(Creature c) {
		if (c != Game.player) {
			creatures.remove(c);
		}
		tiles[c.position.x][c.position.y].creature = null;
	}

	public void moveCreature(Creature c, Position p) {
		getTile(c.position).creature = null;
		getTile(p).creature = c;
		c.position = p;
	}

	public void addItem(String i, Position p){
		addItem(Item.item(i), p);
	}

	public void addItem(Item i, Position p){

		Item item = i.clone();
    	if (i.isStackable) {
    		int loc = findItemLoc(i);
    		if(loc >= 0){
    			items.get(loc).count += i.count;
				return;
    		}
    	}

		for(int j = 0; j < items.size(); j++){
			if(items.get(j) == item) {
				continue;
			}
			if(items.get(j).position == item.position) {
				j = 0;
				int x,y;
				do {
					x = (int)(Math.random() * TILE_COUNT);
					y = (int)(Math.random() * TILE_COUNT);
				} while(tiles[x][y].item != null);
				item.position = new Position(x, y);
			}
		}
		items.add(item);
		tiles[p.x][p.y].item = item;
		item.position = p;
		if (item.id.equals("egg")) {
			containsEgg = true;
		}
	}

	public void addItem(Item i){
		int x, y;
		do {
			x = (int)(Math.random() * TILE_COUNT);
			y = (int)(Math.random() * TILE_COUNT);
		} while(tiles[x][y].item != null);
		addItem(i, new Position(x, y));
	}

	public void addItem(String id){
		addItem(Item.item(id));
	}
	
	/**
	 * @return Index of item in zone ArrayList or -1 if not present
	 */
	public int findItemLoc(Item item){
		for(int x = 0; x < items.size(); x++){
    		if(items.get(x).name == item.name){
    			return x;
    		}
    	}
		return -1;
	}

	/**
	 * @return Index of an item object in zone ArrayList or -1 if not present
	 */
	public int exactItemLoc(Item item){
		for(int x = 0; x < items.size(); x++){
			if(items.get(x) == item){
				return x;
			}
		}
		return -1;
	}

	public void removeItem(Item i) {
		items.remove(i);
		tiles[i.position.x][i.position.y].item = null;
	}


	public Tile getTile(Position position) {
		return tiles[position.x][position.y];
	}

}