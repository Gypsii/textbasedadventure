package crafting;

import java.util.TreeMap;

import item.Item;
import main.Game;
import util.IO;

public class Crafting {

	public static TreeMap<String, Category> categories = new TreeMap<String, Category>();
	
	public static Category all = new Category("Craft:");
	
	public static void listContents(Category c){
		int i = 0;
		if(!c.subcategories.isEmpty()){
			for(i = 0; i < c.subcategories.size(); i++){
				IO.println("(" + i + ") " + c.subcategories.get(i).name);
			}			
		}
		if(!c.recipes.isEmpty()){
			IO.println("Recipes:");
			for(int j = 0; j < c.recipes.size(); j++){
				Recipe r = c.recipes.get(j);
				IO.print("(" + (j + i) + ") ");//j + i because count continues on from categories
				if(Game.canAfford(r, false)){
					IO.print("<green>");
				}else{
					IO.print("<black>");
				}
				IO.print(Item.item(r.product).name + " (" + Item.item(r.components.get(0)).name);
				if(r.componentCounts.get(0)> 1){
					IO.print(" (" + r.componentCounts.get(0) + ")");
				}
				if(r.components.size() > 1){
					for(int k = 1; k < r.components.size(); k++){
						IO.print(", " + Item.item(r.components.get(k)).name);
						if(r.componentCounts.get(k) > 1){
							IO.print(" (" + r.componentCounts.get(k) + ")");
						}
					}
				}		
				IO.println(")<r>");
			}			
		}
	}
	
}
