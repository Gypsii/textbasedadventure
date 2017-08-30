package crafting;

import item.Item;

import java.util.ArrayList;

import item.ItemTag;
import main.Game;

public class FoodRecipe {
	
	public static ArrayList<FoodRecipe> recipes = new ArrayList<FoodRecipe>();
	
	public ArrayList<ItemTag> components = new ArrayList<ItemTag>();
	public ArrayList<ItemTag> badComponents = new ArrayList<ItemTag>();
	public String product;

	public int level;
	public int xp;
	
	public FoodRecipe(String i){
		product = i;
		recipes.add(this);
	}
	
	public void addComponent(String tag){
		addComponent(ItemTag.tag(tag));
	}

	public void addComponent(ItemTag tag){
		components.add(tag);
		badComponents.remove(tag);
	}

	public void forbidComponent(String tag){
		forbidComponent(ItemTag.tag(tag));
	}

	public void forbidComponent(ItemTag tag){
		badComponents.add(tag);
	}
	
	public void forbidAll(){
		badComponents.add(ItemTag.tag("mushroom"));
		badComponents.add(ItemTag.tag("slime"));
		badComponents.add(ItemTag.tag("meat"));
		badComponents.add(ItemTag.tag("vegetable"));
		badComponents.add(ItemTag.tag("fruit"));
		badComponents.add(ItemTag.tag("spice"));
		badComponents.add(ItemTag.tag("bread"));
		badComponents.add(ItemTag.tag("egg"));
		badComponents.add(ItemTag.tag("toxic"));
	}
	
	public void removeForbiddenComponent(ItemTag tag){//uhhh this used to be FoodTag but the array is for strings so ????
		badComponents.remove(tag);
	}
	
	/**
	 * Checks if the given foods can make this recipe
	 * @param foods
	 * @return
	 */
	public boolean check(ArrayList<Item> foods){
		//System.out.println("Checking " + product.name);
		for(int i = 0; i < badComponents.size(); i++){
			//System.out.println("Checking for bad food " + i);
			for(int j = 0; j < foods.size(); j++){
				if(foods.get(j).hasTag(badComponents.get(i))){
					return false;
				}
			}
		}
		//System.out.println("Components = " + components.size());
		for(int i = 0; i < components.size(); i++){
			//System.out.println("Checking for food " + i);
			boolean satisfied = false;
			for(int j = 0; j < foods.size(); j++){
				if(foods.get(j).hasTag(components.get(i))){
					//System.out.println("Found " + i);
					satisfied = true;
					break;
				}
			}
			if(!satisfied){
				//System.out.println(product.name + "failed");
				return false;
			}
		}
		return true;
	}
	
	public static void listRecipes(){
		for(int i = 0; i < recipes.size(); i++){
			if(recipes.get(i).level <= Game.player.skills.cookLvl){
				recipes.get(i).print();
			}
		}
	}

	/**
	 * Prints the product and then requirements for this recipe
	 */
	private void print(){
		System.out.print(Item.item(product).name + ": ");
		for(int i = 0; i < components.size(); i++){
			System.out.print(components.get(i) + ", ");
		}
		System.out.println();
	}
}
