package main;

import item.Item;

import java.io.IOException;
import java.util.ArrayList;

import util.IO;

public class Shop {
	
	double markup = 1.5;
	double hagglingValue = 0.9;
	
	public Buyable amethyst = new Buyable("amethyst", 200);
	public Buyable axeBronze = new Buyable("axeBronze", 429);
	public Buyable axeCopper = new Buyable("axeCopper", 200);
	public Buyable axeIron = new Buyable("axeIron", 731);
	public Buyable bread = new Buyable("bread", 8);
	public Buyable butterKnife = new Buyable("butterKnife", 28);
	public Buyable citrine = new Buyable("citrine", 200);
	public Buyable diamond = new Buyable("diamond", 200);
	public Buyable emerald = new Buyable("emerald", 200);
	public Buyable featherSnow = new Buyable("featherSnow", 2);
	public Buyable feather = new Buyable("feather", 2);
	public Buyable featherWarm = new Buyable("featherWarm", 3);
	public Buyable featherBlack = new Buyable("featherBlack", 2);
	public Buyable flint = new Buyable("flint", 3.5);
	public Buyable fork = new Buyable("fork", 35);
	public Buyable fungus = new Buyable("fungus", 33);
	public Buyable gbladeBronze = new Buyable("glaiveBladeBronze", 414);
	public Buyable gbladeCopper = new Buyable("glaiveBladeCopper", 178);
	public Buyable gbladeIron = new Buyable("glaiveBladeIron", 701);
	public Buyable gbladeRusted = new Buyable("glaiveBladeRusted", 110);
	public Buyable glaiveBronze = new Buyable("glaiveBronze", 429);
	public Buyable glaiveCopper = new Buyable("glaiveCopper", 200);
	public Buyable glaiveIron = new Buyable("glaiveIron", 731);
	public Buyable lswordCopper = new Buyable("lswordCopper", 260);
	public Buyable lswordBronze = new Buyable("lswordBronze", 550);
	public Buyable lswordIron = new Buyable("lswordIron", 870);
	public Buyable lswordRusted = new Buyable("lswordRusted", 160);
	public Buyable maceBronze = new Buyable("maceBronze", 398);
	public Buyable maceCopper = new Buyable("maceCopper", 190);
	public Buyable maceIron = new Buyable("maceIron", 676);
	public Buyable maceRusted = new Buyable("maceRusted", 100);
	public Buyable mailCopper = new Buyable("mailCopper", 350);
	public Buyable mailIron = new Buyable("mailIron", 654);
	public Buyable mushroom = new Buyable("mushroom", 4);
	public Buyable mushroomWhite = new Buyable("mushroomWhite", 4);
	public Buyable pikeBronze = new Buyable("pikeBronze", 429);
	public Buyable pikeCopper = new Buyable("pikeCopper", 200);
	public Buyable pikeIron = new Buyable("pikeIron", 731);
	public Buyable pikeRusted = new Buyable("pikeRusted", 134);
	public Buyable plateIron = new Buyable("mailIron", 604);
	public Buyable ruby = new Buyable("ruby", 200);
	public Buyable sapphire = new Buyable("sapphire", 200);
	public Buyable silk = new Buyable("silk", 7);
	public Buyable slimeAdhesive = new Buyable("slimeSticky", 8);
	public Buyable slimeEarth = new Buyable("slimeEarth", 8);
	public Buyable slimeExplosive = new Buyable("slimeExplosive", 8);
	public Buyable slimeFiery = new Buyable("slimeFire", 10);
	public Buyable slimeGem = new Buyable("slimeGem", 20);
	public Buyable slimeIce = new Buyable("slimeIce", 5.5);
	public Buyable slimePlant = new Buyable("slimePlant", 7);
	public Buyable slimeSpice = new Buyable("slimeSpice", 12);
	public Buyable slimeWater = new Buyable("slimeWater", 5);
	public Buyable spearCopper = new Buyable("spearCopper", 142);
	public Buyable spearheadBronze = new Buyable("spearheadBronze", 305);
	public Buyable spearheadCopper = new Buyable("spearheadCopper", 96);
	public Buyable spearheadRusted = new Buyable("spearheadRusted", 60);
	public Buyable spearRusted = new Buyable("spearRusted", 85);
	public Buyable spiceCinnamon = new Buyable("cinnamon", 29);
	public Buyable spiceGinger = new Buyable("ginger", 25);
	public Buyable spiceNutmeg = new Buyable("nutmeg", 27);
	public Buyable spicePepper = new Buyable("pepper", 32);
	public Buyable sswordCopper = new Buyable("sswordCopper", 160);
	public Buyable sswordBronze = new Buyable("sswordBronze", 376);
	public Buyable sswordIron = new Buyable("sswordIron", 640);
	public Buyable sswordRusted = new Buyable("sswordRusted", 126);
	public Buyable stickyString = new Buyable("stickyString", 6.5);
	public Buyable string = new Buyable("string", 4);
	public Buyable tusk = new Buyable("walrusTusk", 32);
	public Buyable vestDingo = new Buyable("vestFurDingo", 165);
	public Buyable vestBear = new Buyable("vestFurBear", 250);

	
	ArrayList<Buyable> items = new ArrayList<Buyable>();//Items that the shop sells
	ArrayList<String> itemIds = new ArrayList<String>();//Ids of items that the shop sells
	ArrayList<Buyable> all = new ArrayList<Buyable>();//A list of every item with prices
	
	public void addItem(Buyable i, double rarity, double localA){
		i.rarityConstant = rarity;
		i.localAvailability = localA;
		i.calculateValue();
		i.buyValue = (int) Math.round(i.marketValue * markup);
		i.sellValue = (int) Math.round(i.marketValue * hagglingValue);
		items.add(i);
	}
	
	public void addItem(String id){
		if(Item.items.containsKey(id)){
			itemIds.add(id);
		}else{
			IO.println("<lred>Invalid item id \"" + id + "\"to add to shop.<r>");
		}
	}
	
	public void addItemToAll(Buyable i, double rarity, double localA){
		i.rarityConstant = rarity;
		i.localAvailability = localA;
		i.calculateValue();
		i.buyValue = (int) Math.round(i.marketValue * markup);
		i.sellValue = (int) Math.round(i.marketValue * hagglingValue);
		all.add(i);
	}
	
	public void printItem(int n){
		//System.out.println(n + ": " + Item.item(items.get(n).id).getNameWithCount() + " (" + items.get(n).buyValue + " gold)");
		Item i = Item.item(itemIds.get(n));
		int cost = (int) (i.cost * markup + 0.5);
		IO.println(n + ": " + i.name + " (" + cost + " gold)");
	}
	
	public Shop(){//Surely there's a better way than this
//		addItemToAll(this.amethyst, 0.8, 1);
//		addItemToAll(this.bread, 0.9, 1);
//		addItemToAll(this.butterKnife, 0.9, 1);
//		addItemToAll(this.citrine, 0.8, 1);
//		addItemToAll(this.diamond, 0.8, 1);
//		addItemToAll(this.emerald, 0.8, 1);
//		addItemToAll(this.flint, 1, 1);
//		addItemToAll(this.featherSnow, 0.7, 1);
//		addItemToAll(this.feather, 1, 1);
//		addItemToAll(this.featherWarm, 0.7, 1);
//		addItemToAll(this.featherBlack, 1, 1);
//		addItemToAll(this.fork, 0.9, 1);
//		addItemToAll(this.fungus, 0.8, 1);
//		addItemToAll(this.gbladeBronze, 0.8, 1);
//		addItemToAll(this.gbladeCopper, 0.8, 1);
//		addItemToAll(this.gbladeIron, 0.8, 1);
//		addItemToAll(this.gbladeRusted, 0.8, 1);
//		addItemToAll(this.glaiveBronze, 0.9, 1);
//		addItemToAll(this.glaiveCopper, 0.9, 1);
//		addItemToAll(this.glaiveIron, 0.9, 1);
//		addItemToAll(this.lswordCopper, 0.9, 1);
//		addItemToAll(this.lswordBronze, 0.9, 1);
//		addItemToAll(this.lswordIron, 0.9, 1);
//		addItemToAll(this.lswordRusted, 1, 1);
//		addItemToAll(this.maceCopper, 0.9, 1);
//		addItemToAll(this.maceBronze, 0.9, 1);
//		addItemToAll(this.maceIron, 0.8, 1);
//		addItemToAll(this.maceRusted, 0.8, 1);
//		addItemToAll(this.mailCopper, 0.9, 1);
//		addItemToAll(this.mailIron, 0.9, 1);
//		addItemToAll(this.mushroom, 0.9, 1);
//		addItemToAll(this.mushroomWhite, 0.9, 1);
//		addItemToAll(this.pikeBronze, 0.9, 1);
//		addItemToAll(this.pikeCopper, 0.9, 1);
//		addItemToAll(this.pikeIron, 0.9, 1);
//		addItemToAll(this.pikeRusted, 0.9, 1);
//		addItemToAll(this.plateIron, 0.9, 1);
//		addItemToAll(this.ruby, 0.8, 1);
//		addItemToAll(this.sapphire, 0.8, 1);
//		addItemToAll(this.silk, 0.9, 1);
//		addItemToAll(this.slimeAdhesive, 0.9, 1);
//		addItemToAll(this.slimeEarth, 0.9, 1);
//		addItemToAll(this.slimeExplosive, 0.75, 1);
//		addItemToAll(this.slimeFiery, 0.9, 1);
//		addItemToAll(this.slimeGem, 0.8, 1);
//		addItemToAll(this.slimeIce, 0.75, 1);
//		addItemToAll(this.slimePlant, 0.75, 1);
//		addItemToAll(this.slimeSpice, 0.75, 1);
//		addItemToAll(this.slimeWater, 0.9, 1);
//		addItemToAll(this.spearCopper, 0.9, 1);
//		addItemToAll(this.spearheadBronze, 0.8, 1);
//		addItemToAll(this.spearheadCopper, 0.8, 1);
//		addItemToAll(this.spearheadRusted, 0.8, 1);
//		addItemToAll(this.spearRusted, 0.9, 1);
//		addItemToAll(this.spiceCinnamon, 0.5, 1.4);
//		addItemToAll(this.spiceGinger, 0.5, 1.4);
//		addItemToAll(this.spiceNutmeg, 0.5, 1.4);
//		addItemToAll(this.spicePepper, 0.5, 1.4);
//		addItemToAll(this.sswordCopper, 0.9, 1);
//		addItemToAll(this.sswordBronze, 0.9, 1);
//		addItemToAll(this.sswordIron, 0.9, 1);
//		addItemToAll(this.sswordRusted, 1, 1);
//		addItemToAll(this.stickyString, 0.9, 1);
//		addItemToAll(this.string, 1, 1);
//		addItemToAll(this.tusk, 0.7, 1);
//		addItemToAll(this.vestDingo, 0.9, 1);
//		addItemToAll(this.vestBear, 0.9, 1);
	}
	
	public int getSalePrice(Item i){
		for(int n = 0; n < all.size(); n++){
			if(all.get(n).id.equals(i.id)){
				return all.get(n).sellValue;
			}
		}
		return 0;
	}

	public void shop() throws IOException{
		IO.println("<blue>You arrive at a shop.<r>");
		//Shop s = Game.level.shop;
		while(true){
			int n = 0;
			IO.println("0: Buy");
			IO.println("1: Sell");
			IO.println("2: Exit");
			try{
				n = Integer.parseInt(Game.br.readLine());
			}catch(NumberFormatException nfe){
				IO.println("<red>Invalid Format!<r>");
				continue;
			}
			if(n == 0){//buying
				while(true){
					IO.println("You have " + Game.money + " gold");
					IO.println("-1: Close");
					for(int i = 0; i < itemIds.size(); i++){
						printItem(i);
					}
					try{
						n = Integer.parseInt(Game.br.readLine());
					}catch(NumberFormatException nfe){
						IO.println("<red>Invalid Format!<r>");
						continue;
					}
					if(n < itemIds.size() && n >= 0){
						Item i = Item.item(itemIds.get(n));
						int cost = (int) (i.cost * markup + 0.5);
						if(Game.money < cost){
							IO.println("<red>You can not afford that item<r>");
						}else{
							IO.println("<blue>You bought the " + i.name + " for " + cost + " gold<r>");  
							Game.player.addItem(i);
							Game.money -= cost;
						}  	
					}else if(n == -1){
						break;
					}else{
						IO.println("<red>There is no item with that ID!<r>");
					}
				}
			}

			else if(n == 1){//Selling
				while(true){
					IO.println("You have " + Game.money + " gold");
					IO.println("-1: Close");
					for(int i = 0; i < Game.player.inv.size(); i++){
						Item item = Game.player.inv.get(i);
						int price = (int)(item.cost * hagglingValue + 0.5);
						IO.print(i + ": " + item.getNameWithCount());		
						if(price > 0){
							IO.print(" (" + price + " gold)");		
						}
						IO.println();
					}
					try{
						n = Integer.parseInt(Game.br.readLine());	
					}catch(NumberFormatException nfe){
						IO.println("<red>Invalid Format!<r>");
						continue;
					}
					if(n < Game.player.inv.size() && n >= 0){
						Item item = Game.player.inv.get(n);
						int price = (int)(item.cost * hagglingValue + 0.5);
						if(price <= 0){
							IO.println("The vendor does not wish to buy this item.");
							continue;
						}
						IO.println("<blue>You sold the " + item.name + " for " + price + " gold<r>");
						Game.money += price;
						item.count -= 1;
						if(item.count < 1){
							Game.player.removeItem(n);
						}
					}else if(n == -1){
						break;
					}else{
						IO.println("<red>There is no item with that ID!<r>");
					}
				}
			}else if(n == 2){//Exiting
				break;
			}else{
				IO.println("<red>That number is not a command<r>");
			}
		}	
	}
}
