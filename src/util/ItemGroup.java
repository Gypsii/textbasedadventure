package util;

import item.Item;
import item.MagicItem;

import java.util.ArrayList;
import java.util.TreeMap;

public class ItemGroup {

	public static TreeMap<String, ItemGroup> itemGroups = new TreeMap<String, ItemGroup>();
	
	private ArrayList<String> items = new ArrayList<String>();
	private ArrayList<ArrayList<String>> extra = new ArrayList<ArrayList<String>>();
	private ArrayList<Double> weights = new ArrayList<Double>();
	private double totalWeight = 0;
	
	
	public static ItemGroup group(String id){
		if(itemGroups.containsKey(id)){
			return itemGroups.get(id);
		}
		return new ItemGroup();
	}
	
	public void add(String id, double weight, String[] args){
		items.add(id);
		weights.add(weight);
		extra.add(new ArrayList<String>());
		totalWeight += weight;
		for(String s : args){ 
			extra.get(extra.size() - 1).add(s);
		}
	}
	
	public void addDetailToLast(String s){
		extra.get(extra.size() - 1).add(s);
	}
	
	public Item getRandom(){
		double value = Math.random() * totalWeight;
		double currentWeight = 0;
		for(int i = 0; i < items.size(); i++){
			currentWeight += weights.get(i);
			if(currentWeight > value){
				return item(i);
			}
		}
		return Item.item(items.get(items.size() - 1));
	}
	
	private Item item(int index){
		Item i = Item.item(items.get(index));
		for(String s : extra.get(index)){
			if(s.split(";")[0].equals("enchant")){
				i = MagicItem.RandomEnchant(i, Integer.parseInt(s.split(";")[1]));
			}
		}
		return i;
	}
	
}
