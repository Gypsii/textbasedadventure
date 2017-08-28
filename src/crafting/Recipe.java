package crafting;

import java.util.ArrayList;

public class Recipe {

	public String product;
	public int productCount = -1;//-1 means take default for item, as this could be != 1
	public ArrayList<String> components = new ArrayList<String>();
	public ArrayList<Integer> componentCounts = new ArrayList<Integer>();
	
	public Recipe(String[] ids, int[] counts){
		product = ids[0];
		for(int i = 1; i < ids.length; i++){
			addComponent(ids[i], counts[i]);
		}
	}
	
	public Recipe(){
		
	}
	
	public void addComponent(String id, int count){
		components.add(id);
		componentCounts.add(count);
	}
		
}
