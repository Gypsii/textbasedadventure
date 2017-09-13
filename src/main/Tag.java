package main;

import util.IO;

import java.util.HashMap;
import java.util.Map;

public class Tag {

	private static Map<String, Tag> itemTags = new HashMap<>();

	public static Tag tag(String name){
		if(!itemTags.containsKey(name)){
			addTag(name);
		}
		return itemTags.get(name);
	}

	public static void addTag(String name){
		if(itemTags.containsKey(name)){
			IO.println("<lred>Tag \"" + name + "\" already exists<r>");
		} else {
			itemTags.put(name, new Tag(name));
		}
	}

	public String name;

	public Tag(String name) {
		this.name = name;
	}
}
