package creatures.humans;

import creatures.CreatureTag;
import item.Item;
import main.Tag;

public class Bandit extends Human {

	public void setWeapon(){
		double rand = Math.random();
		Item i;
		if(rand < 0.1){
			i = Item.item("maceIron");
		}else if(rand < 0.3){
			i = Item.item("maceRusted");
		}else if(rand < 0.5){
			i = Item.item("sswordCopper");
		}else if(rand < 0.7){
			i = Item.item("glaiveBronze");
		}else if(rand < 0.9){
			i = Item.item("sswordBronze");
		}else{
			i = Item.item("lswordBronze");
		}
		addItem(i);
		equip(i);
	}
	
	public void setArmour(){
		double rand = Math.random();
		String a;
		if(rand < 0.3){
			a = "randomTunic";
		}else if(rand < 0.4){
			a = "plateCopper";
		}else if(rand < 0.5){
			a = "mailCopper";
		}else if(rand < 0.8){
			a = "mailIron";
		}else{
			a = "plateIron";
		}
		addItem(a);
		equip(a);
		
		if(Math.random() < 0.4){
			Item c = Item.randomCloak();
			addItem(c);
			equip(c);
		}
		if(Math.random() < 0.3){
			if(Math.random() < 0.5){
				addItem("ringCopper");
				equip("ringCopper");
			}else{
				addItem("ringSilver");
				equip("ringSilver");
			}
		}
	}
	
	public void setTags(){
		addTag("bandit");
	}
	
	public void setTargets(){
		setHostilityTowardsPlayer(true);
		addTarget(Tag.tag("merchant"), 101);
	}
	
	public void setSpecifics(){
		name = "Bandit";
		addItem("money", (int)(Math.random() * 30 + 5));
		if(Math.random() < 0.2){addItem(Item.randomGem());}
		if(Math.random() < 0.2){addItem(Item.randomGem());}
		if(Math.random() < 0.2){addItem("sandwichVegetable");}
		if(Math.random() < 0.7){addItem("bread");}
		if(Math.random() < 0.2){addItem("cinnamonBun");}
	}
	
	public String getDescription(){
		return "A nasty bandit plunderer.";
	}
}
