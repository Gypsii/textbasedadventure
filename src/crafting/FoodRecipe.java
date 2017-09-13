package crafting;

import item.Item;

import java.util.ArrayList;

import main.Tag;
import main.Game;

public class FoodRecipe {
	
	public static ArrayList<FoodRecipe> recipes = new ArrayList<FoodRecipe>();
	
	public ArrayList<Tag> components = new ArrayList<Tag>();
	public ArrayList<Tag> badComponents = new ArrayList<Tag>();
	public String product;

	public int level;
	public int xp;
	
	public FoodRecipe(String i){
		product = i;
		recipes.add(this);
	}
	
	public void addComponent(String tag){
		addComponent(Tag.tag(tag));
	}

	public void addComponent(Tag tag){
		components.add(tag);
		badComponents.remove(tag);
	}

	public void forbidComponent(String tag){
		forbidComponent(Tag.tag(tag));
	}

	public void forbidComponent(Tag tag){
		badComponents.add(tag);
	}
	
	public void forbidAll(){
		badComponents.add(Tag.tag("mushroom"));
		badComponents.add(Tag.tag("slime"));
		badComponents.add(Tag.tag("meat"));
		badComponents.add(Tag.tag("vegetable"));
		badComponents.add(Tag.tag("fruit"));
		badComponents.add(Tag.tag("spice"));
		badComponents.add(Tag.tag("bread"));
		badComponents.add(Tag.tag("egg"));
		badComponents.add(Tag.tag("toxic"));
	}
	
	public void removeForbiddenComponent(Tag tag){//uhhh this used to be FoodTag but the array is for strings so ????
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
