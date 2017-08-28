package crafting;

import item.Food;
import item.FoodTag;
import item.Item;

import java.util.ArrayList;

import main.Game;

public class FoodRecipe {
	
	public static ArrayList<FoodRecipe> recipes = new ArrayList<FoodRecipe>();
	
	public ArrayList<String> components = new ArrayList<String>();
	public ArrayList<String> badComponents = new ArrayList<String>();
	public String product;

	public int level;
	public int xp;
	
	public FoodRecipe(String i){
		product = i;
		recipes.add(this);
	}
	
	public void addComponent(String tag){
		components.add(tag);
		badComponents.remove(tag);
	}
	
	public void forbidComponent(String tag){
		badComponents.add(tag);
	}
	
	public void forbidAll(){
		badComponents.add("mushroom");
		badComponents.add("slime");
		badComponents.add("meat");
		badComponents.add("vegetable");
		badComponents.add("fruit");
		badComponents.add("spice");
		badComponents.add("bread");
		badComponents.add("egg");
		badComponents.add("toxic");
	}
	
	public void removeForbiddenComponent(FoodTag tag){
		badComponents.remove(tag);
	}
	
	/**
	 * Checks if the given foods can make this recipe
	 * @param foods
	 * @return
	 */
	public boolean check(ArrayList<Food> foods){
		//System.out.println("Checking " + product.name);
		for(int i = 0; i < badComponents.size(); i++){
			//System.out.println("Checking for bad food " + i);
			for(int j = 0; j < foods.size(); j++){
				if(foods.get(j).tags.contains(badComponents.get(i))){
					return false;
				}
			}
		}
		//System.out.println("Components = " + components.size());
		for(int i = 0; i < components.size(); i++){
			//System.out.println("Checking for food " + i);
			boolean satisfied = false;
			for(int j = 0; j < foods.size(); j++){
				if(foods.get(j).tags.contains(components.get(i))){
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
