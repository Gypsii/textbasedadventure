package item;

import util.IO;

import java.util.HashMap;
import java.util.Map;

public class ItemTag {

	private static Map<String,ItemTag> itemTags = new HashMap<>();

	public static ItemTag tag(String name){
		if(!itemTags.containsKey(name)){
			addTag(name);
		}
		return itemTags.get(name);
	}

	public static void addTag(String name){
		if(itemTags.containsKey(name)){
			IO.println("<lred>Tag \"" + name + "\" already exists<r>");
		} else {
			itemTags.put(name, new ItemTag(name));
		}
	}

	public String name;

	public ItemTag(String name) {
		this.name = name;
	}
}
