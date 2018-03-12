package util;

import creatures.Buffs.Condition;
import item.Item;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.DamageInstance;
import main.DamageType;
import crafting.Category;
import crafting.Crafting;
import crafting.FoodRecipe;
import crafting.Recipe;
import creatures.AttackPattern;
import creatures.Creature;
import creatures.CreatureTemplate;

public class FileReader {
	
	static Pattern objPattern = Pattern.compile("\\{[^}]*\\}");// \{[^}]*\}
	static Pattern tagPattern = Pattern.compile("\\[([^\\:]+):([^\\]]*)]");// \[([^\:]+):([^\]]*)]

	public static void readFromFiles() throws IOException{
//		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
//				new FileOutputStream("filename.txt"), "utf-8"))) {
//			writer.write("something");
//		}
		readMainGameFile("game.txt");
		readFile("item.txt");
		readFile("food.txt");
		readFile("creature.txt");
		readFile("crafting.txt");

	}

	static void readMainGameFile(String fileName) {

	}

	static void readFile(String fileName) throws IOException{
		List<String> lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());

		StringBuilder builder = new StringBuilder();
		
		for(String currLine : lines){
			builder.append(currLine);
		}
		
		String file = builder.toString();

		Matcher o = objPattern.matcher(file);
				
		while(o.find()){
			String obj = o.group(0);
			Matcher t = tagPattern.matcher(obj);
			t.find();
			String tag = t.group(1);
			if(tag.equals("ITEM")){
				itemFromFile(obj);
			}else if(tag.equals("CREATURE")){
				creatureFromFile(obj);
			}else if(tag.equals("ATTACK")){
				attackFromFile(obj);
			}else if(tag.equals("RECIPE")){
				recipeFromFile(obj);
			}else if(tag.equals("CRAFT")){
				craftingRecipeFromFile(obj);
			}else if(tag.equals("CATEGORY")){
				craftingCategoryFromFile(obj);
			}else if(tag.equals("GROUP")){
				groupFromFile(obj);
			}else{
				IO.println("<lred>Invalid group tag<r>");
			}
		}
	}
	
	static void itemFromFile(String obj){
		Matcher t = tagPattern.matcher(obj);
		t.find();
		String id = t.group(2);
		Item i = new Item();
		i.id = id;
		Item.items.put(id, i);
		while(t.find()){
			String tag = t.group(1);
			String value = t.group(2);
			switch(tag){
			case "NAME":
				i.name = value;
				break;
			case "PREFIX":
				i.prefix = value;
				break;
			case "DAMAGE":
				i.dmg.amount = Integer.parseInt(value);
				break;
			case "TAG":
				i.addTag(value);
				break;
			case "DAMAGETYPE":
				i.dmg.type = DamageType.get(value);
//					IO.println("<red>Invalid value \"" + value + "\" for tag \"DAMAGETYPE\" of type item (id " + id + ")<r>");
				break;
			case "BLUNT":
				i.resists[DamageType.BLUNT.number] = Integer.parseInt(value);
				break;
			case "SLASH":
				i.resists[DamageType.SLASH.number] = Integer.parseInt(value);
				break;
			case "PIERCE":
				i.resists[DamageType.PIERCE.number] = Integer.parseInt(value);
				break;
			case "BURN":
				i.resists[DamageType.BURN.number] = Integer.parseInt(value);
				break;
			case "COLD":
				i.resists[DamageType.COLD.number] = Integer.parseInt(value);
				break;
			case "MAGIC":
				i.resists[DamageType.MAGIC.number] = Integer.parseInt(value);
				break;
			case "SWINGSPEED":
				i.swingTime = Double.parseDouble(value);
				break;
			case "REACH":
				i.reachBonus = Double.parseDouble(value);
				break;
			case "COST":
				i.cost = Double.parseDouble(value);
				break;
			case "HEALTH":
				i.addTag("edible");
				i.healthRestore = Integer.parseInt(value);
				break;
			case "DESCRIPTION":
				i.description = value;
				break;
			default:
				IO.println("<red>Invalid tag \"" + tag + "\" for type item (id " + id + ")<r>");
				break;
			}
		}
	}
	
	static void groupFromFile(String obj){
		Matcher t = tagPattern.matcher(obj);
		t.find();
		String id = t.group(2);
		ItemGroup g = new ItemGroup();
		ItemGroup.itemGroups.put(id, g);
		while(t.find()){
			String tag = t.group(1);
			String value = t.group(2);
			switch(tag){
			case "ENTRY":
				if(value.split(";").length == 1){
					g.add(value, 1, new String[0]);
				}else{
					g.add(value.split(";")[0], Double.parseDouble(value.split(";")[1]), new String[0]);
				}
				break;
			case "ADD_TO_LAST":
				g.addDetailToLast(value);
				break;
			default:
				IO.println("<red>Invalid tag \"" + tag + "\" for type group (id " + id + ")<r>");
				break;
			}
		}
	}
	
	static void craftingCategoryFromFile(String obj){
		Matcher t = tagPattern.matcher(obj);
		t.find();
		String id = t.group(2);
		Category c = new Category();
		String parent = "all";
		Crafting.categories.put(id, c);
		while(t.find()){
			String tag = t.group(1);
			String value = t.group(2);
			switch(tag){
			case "NAME":
				c.name = value;
				break;
			case "PARENT":
				parent = value;
				break;
			default:
				IO.println("<red>Invalid tag \"" + tag + "\" for crafting category (id " + id + ")<r>");
				break;
			}
		}
		try{
			Crafting.categories.get(parent).subcategories.add(c);
		}catch(Exception e){
			IO.println("<lred>Invalid parent for category \"" + id + "\". Check ordering is correct.<r>");
		}
	}
	
	static void craftingRecipeFromFile(String obj){
		Matcher t = tagPattern.matcher(obj);
		t.find();
		String id = t.group(2);
		Recipe r = new Recipe();
		r.product = id;
		String category = "all";
		while(t.find()){
			String tag = t.group(1);
			String value = t.group(2);
			switch(tag){
			case "COUNT":
				r.productCount = Integer.parseInt(value);
				break;
			case "CATEGORY":
				category = value;
				break;
			case "COMPONENT":
				if(value.split(";").length == 1){
					r.addComponent(value, 1);
				}else{
					r.addComponent(value.split(";")[0], Integer.parseInt(value.split(";")[1]));
				}
				break;
			default:
				IO.println("<red>Invalid tag \"" + tag + "\" for crafting recipe (id " + id + ")<r>");
				break;
			}
		}
		try{
			Crafting.categories.get(category).addRecipe(r);
		}catch(Exception e){
			IO.println("<lred>Invalid category \"" + category + "\" for crafting recipe of \"" + id + "\". Check ordering is correct.<r>");
		}
	}
	
	static void attackFromFile(String obj){
		Matcher t = tagPattern.matcher(obj);
		t.find();
		String id = t.group(2);
		AttackPattern p = new AttackPattern(new DamageInstance(0, DamageType.BLUNT));
		AttackPattern.attacks.put(id, p);
		while(t.find()){
			String tag = t.group(1);
			String value = t.group(2);
			switch(tag){
			case "DAMAGE":
				p.baseDamage.amount = Integer.parseInt(value);
				break;
			case "DAMAGE_TYPE":
				p.baseDamage.type = DamageType.get(value);
//				IO.println("<red>Invalid value \"" + value + "\" for tag \"DAMAGETYPE\" of type item (id " + id + ")<r>");
			case "VERB":
				p.verb = value;
				break;
			case "DURATION":
				p.period = Double.parseDouble(value);
				break;
			default:
				IO.println("<red>Invalid tag \"" + tag + "\" for type attack (id " + id + ")<r>");
				break;
			}
		}
	}

	static void creatureFromFile(String obj){
		Matcher t = tagPattern.matcher(obj);
		t.find();
		String id = t.group(2);
		CreatureTemplate c = new CreatureTemplate();
		c.id = id;
		Creature.creatures.put(id, c);
		while(t.find()){
			String tag = t.group(1);
			String value = t.group(2);
			switch(tag){
			case "NAME":
				c.name = value;
				break;
			case "HP":
				c.maxHp = Integer.parseInt(value);
				break;
			case "XP":
				c.xp = Integer.parseInt(value);
				break;
			case "DESCRIPTION":
				c.description = value;
				break;
			case "RESIST_BLUNT":
				c.baseResists[DamageType.BLUNT.number] = Integer.parseInt(value);
				break;
			case "RESIST_SLASH":
				c.baseResists[DamageType.SLASH.number] = Integer.parseInt(value);
				break;
			case "RESIST_PIERCE":
				c.baseResists[DamageType.PIERCE.number] = Integer.parseInt(value);
				break;
			case "RESIST_BURN":
				c.baseResists[DamageType.BURN.number] = Integer.parseInt(value);
				break;
			case "RESIST_COLD":
				c.baseResists[DamageType.COLD.number] = Integer.parseInt(value);
				break;
			case "RESIST_MAGIC":
				c.baseResists[DamageType.MAGIC.number] = Integer.parseInt(value);
				break;
			case "CRIT":
				c.baseCritChance = Double.parseDouble(value);
				break;
			case "CRIT_DMG":
				c.baseCritDmg = Double.parseDouble(value);
				break;
			case "CAN_BE_BOSS":
				c.canBeBoss = stringToBoolean(value);
				break;
			case "INDEF_ARTICLE":
				c.articleIndef = value;
				break;
			case "DEF_ARTICLE":
				c.articleDef = value;
				break;
			case "COURAGE":
				c.courage = Double.parseDouble(value);
				break;
			case "ALIVE":
				if(!stringToBoolean(value)){
					c.conditions.add(Condition.DEAD);
				}
				break;
			case "HOSTILE":
				c.hostile = stringToBoolean(value);
				break;
			case "SLEEPING":
				if(stringToBoolean(value)){
					c.conditions.add(Condition.SLEEPING);
				}
				break;
			case "ENRAGEABLE":
				if(stringToBoolean(value)){
					c.conditions.add(Condition.ENRAGEABLE);
				}
				break;
			case "ENRAGED":
				if(stringToBoolean(value)){
					c.conditions.add(Condition.ENRAGED);
				}
				break;
			case "SEALED":
				if(stringToBoolean(value)){
					c.conditions.add(Condition.SEALED);
				}
				break;
			case "ATTACK":
				c.defaultAttackPatternId = value;
				break;
			case "ITEM":
				if(value.split(";").length == 1){
					c.inv.add(value);
					c.itemChance.add(1d);
				}else{
					c.inv.add(value.split(";")[0]);
					c.itemChance.add(Double.parseDouble(value.split(";")[1]));
				}
				break;
			case "ITEMGROUP":
				if(value.split(";").length == 1){
					c.randomInv.add(ItemGroup.group(value));
					c.randomItemChance.add(1d);
				}else{
					c.randomInv.add(ItemGroup.group(value.split(";")[0]));
					c.randomItemChance.add(Double.parseDouble(value.split(";")[1]));
				}
				break;
			case "BUTCHER_DROP":
				c.butcherInv.add(value.split(";")[0]);
				c.butcherSuccess.put(value.split(";")[0], Double.parseDouble(value.split(";")[1]));
				break;
			default:
				IO.println("<red>Invalid tag \"" + tag + "\" for type creature (id " + id + ")<r>");
				break;
			}
		}
	}
	
	static void recipeFromFile(String obj){
		Matcher t = tagPattern.matcher(obj);
		t.find();
		String product = t.group(2);
		FoodRecipe r = new FoodRecipe(product);
		while(t.find()){
			String tag = t.group(1);
			String value = t.group(2);
			switch(tag){
			case "FORBIDALL":
				r.forbidAll();
				break;
			case "REQUIRE":
				r.addComponent(value);
				break;
			case "FORBID":
				r.forbidComponent(value);
				break;
			case "LEVEL":
				r.level = Integer.parseInt(value);
				break;
			case "XP":
				r.xp = Integer.parseInt(value);
				break;
			default:
				IO.println("<red>Invalid tag \"" + tag + "\" for type recipe (product " + product + ")<r>");
				break;
			}
		}
	}
	
	static void shopFromFile(String obj){
		
	}
	
	static boolean stringToBoolean(String str){
		if(str.toLowerCase().equals("true")){
			return true;
		}
		return false;
	}
	
}
