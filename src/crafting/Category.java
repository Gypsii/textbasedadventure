package crafting;

import java.util.ArrayList;

public class Category {

	public String name;
	public ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	public ArrayList<Category> subcategories = new ArrayList<Category>();
	
	public Category(String n){
		name = n;
	}
	
	public Category(){
		
	}
	
	public void addSubcategory(Category c){
		subcategories.add(c);
	}
	
	public void addRecipe(Recipe r){
		recipes.add(r);
	}
}
