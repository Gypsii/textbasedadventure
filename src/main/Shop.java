package main;

import item.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.IO;

public class Shop {
	
	double markup = 1.5;
	double hagglingValue = 0.9;
	
	List<Item> items = new ArrayList<>();


	public void randomItem(String id, double chance, int min, int max) {
		if(Math.random() < chance){
			addItem(id, (int)(Math.random() * (max-min) + min));
		}

	}
	
	public void addItem(String id){
		addItem(Item.item(id));
	}

	public void addItem(String id, int count){
		Item i = Item.item(id);
		i.count = count;
		addItem(i);
	}

	public void addItem(Item i) {
		Item item = i.clone();
		if (i.isStackable) {
			int loc = findShopItemLoc(i);
			if (loc >= 0) {
				items.get(loc).count += i.count;
			} else {
				items.add(item);
			}
		} else {
			items.add(item);
		}
	}

	public int findShopItemLoc(Item i) {
		for (int x = 0; x < items.size(); x++) {
			if (items.get(x).name == i.name) {
				return x;
			}
		}
		return -1;
	}
	
	public Shop(){

	}

	public void shop() throws IOException{
		IO.println("<blue>You arrive at a shop.<r>");
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

					for(int i = 0; i < items.size(); i++) {
						int cost = (int) (items.get(i).cost * markup + 0.5);
						if (cost <= Game.money) {
							IO.print("<green>");
						}
						IO.print(i + ":" + items.get(i).name + " (" + items.get(i).count);
						if (cost <= Game.money) {
							IO.println(", " + cost + " gold)<r>");
						} else {
							IO.println(")");
						}
					}
					n = IO.readInt(0, items.size(), "There is no item with that ID!");
					if(n < items.size() && n >= 0){
						Item i = items.get(n);
						int cost = (int) (i.cost * markup + 0.5);
						if(Game.money < cost){
							IO.println("<red>You cannot afford that item<r>");
						}else{
							IO.println("<blue>You bought the " + i.name + " for " + cost + " gold<r>");
							Item ic = i.clone();
							ic.count = 1;
							Game.player.addItem(ic);
							Game.money -= cost;
							i.count--;
							if(i.count < 1){
								items.remove(n);
							}
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
						if(price > 0){
							IO.print("<green>");
						}
						IO.println(i + ": " + item.name + " (" + item.count);
						if(price > 0){
							IO.println(", " + price + " gold)<r>");
						}else{
							IO.println(")");
						}

					}
					n = IO.readInt(0, items.size(), "There is no item with that ID!");
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
