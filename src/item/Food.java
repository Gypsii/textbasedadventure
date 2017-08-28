package item;

import java.util.ArrayList;

public class Food extends Item{
	
	public ArrayList<String> tags = new ArrayList<String>();
	public int healthRestore = 0;
	
	public Food(String n, boolean isIng, int value){
		name = n;
		healthRestore = value;
		isFood = true;
	}
	
	public Food(String n, boolean isIng, int value, String pref){
		name = n;
		healthRestore = value;
		isFood = true;
		prefix = pref;
	}
	
	public Food() {
		
	}

	public Food clone(){
		Food i = new Food(this.name, this.isIngredient, this.healthRestore, this.prefix);
		i.id = this.id;
		i.count = this.count;
		i.cost = this.cost;
		i.effects = this.effects;
		i.isFood = true;
		i.tags = this.tags;
		i.armourType = this.armourType;
		i.isGem = this.isGem;
		i.isPolearm = this.isPolearm;
		i.isEnchanted = this.isEnchanted;
		i.enchantLvl = this.enchantLvl;
		i.enchantType = this.enchantType;
		i.effects.addAll(this.effects);
		i.passives.addAll(this.passives);
		i.resists = this.resists.clone();
		i.description = this.description;
		return i;
	}
}
