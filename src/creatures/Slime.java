package creatures;

import item.Item;

import java.util.Random;

import main.Game;
import util.IO;
import util.Text;

public class Slime extends Creature{
	int type;
	int size;
	public static final int VOLATILE = 0;
	public static final int FIERY = 1;
	public static final int ICY = 2;
	public static final int WATERY = 3;
	public static final int STICKY = 4;
	public static final int EARTHEN = 5;
	public static final int PLANT = 6;
	public static final int SPICY = 7;
	public static final int GEM = 8;
	public static final int FUNGAL = 9;
	public static final int MOLTEN = 10;
	public static final int[] diffConstants = {1, 1, 0, 0, 0, 2, 2, 0, 0, 0, 1}; 
	
	
	public Slime(int t, int s){//Size should be a power of 2
		tags.add(CreatureTag.slime);
		size = s;
		dmg = 8;
		maxHp = 32;
		xp = 7;
		baseResists[Game.DMG_BLUNT] = -4 * size;
		baseResists[Game.DMG_SLASH] = 7 * size;
		baseResists[Game.DMG_PIERCE] = 20 * size;
		
		
		name = "ERR UNTYPED SLIME";
		type = t;
		Random random = new Random();
		int dropCount = Math.max((int)(random.nextGaussian() + 2 * size), size);//Normal dist w/ μ = 1 + size, σ = 1, no lower than size
		if(type == VOLATILE){
			addItem("slimeExplosive", dropCount);
			name = "Volatile Slime";
			baseResists[Game.DMG_BLUNT] = -10 * size;
			xp = 11;
		}else if(type == FIERY){
			addItem("slimeFire", dropCount);
			name = "Fiery Slime";
			baseResists[Game.DMG_BURN] = 20 * size;
			xp = 10;
		}else if(type == ICY){
			addItem("slimeIce", dropCount);
			if(Math.random() < 0.5){
				addItem("icicle");
			}
			name = "Icy Slime";
			baseResists[Game.DMG_BLUNT] = 2 * size;
			baseResists[Game.DMG_SLASH] = 4 * size;
			baseResists[Game.DMG_PIERCE] = 15 * size;
			baseResists[Game.DMG_BURN] = -10 * size;
			baseResists[Game.DMG_COLD] = 20 * size;
		}else if(type == WATERY){
			addItem("slimeWater", dropCount);
			name = "Watery Slime";
			baseResists[Game.DMG_SLASH] = 12 * size;
			baseResists[Game.DMG_BURN] = 8 * size;
			baseResists[Game.DMG_PIERCE] = 40 * size;
		}else if(type == STICKY){
			addItem("slimeSticky", dropCount);
			name = "Sticky Slime";
			baseResists[Game.DMG_SLASH] = 10 * size;
			baseResists[Game.DMG_BLUNT] = 3 * size;
		}else if(type == EARTHEN){
			addItem("slimeEarth", dropCount);
			for(int i = 0; i < size * 2; i++){
				if(Math.random() < 0.5){addItem("rock");}
			}
			name = "Rocky Slime";
			baseResists[Game.DMG_BLUNT] = 10 * size;
			baseResists[Game.DMG_SLASH] = 15 * size;
			baseResists[Game.DMG_COLD] = 20 * size;
			baseResists[Game.DMG_BURN] = 20 * size;
			xp = 10;
		}else if(type == PLANT){
			addItem("slimePlant", dropCount);
			name = "Leafy Slime";
			baseResists[Game.DMG_BURN] = -4 * size;
			baseResists[Game.DMG_BLUNT] = 0;
			addBodyPart("leaf", 0.3);
			for(int i = 0; i < size * 2; i++){
				addBodyPart("leaf", 0.7);
			}
		}else if(type == SPICY){
			addItem("slimeSpice", dropCount);
			name = "Spicy Slime";
			addBodyPart("nutmeg", 0.2);
			addBodyPart("pepper", 0.3);
			addBodyPart("ginger", 0.2);
			addBodyPart("cinnamon", 0.25);
		}else if(type == GEM){
			addItem("slimeGem", dropCount);
			name = "Jewelled Slime";
			baseResists[Game.DMG_PIERCE] = 10 * size;
			baseResists[Game.DMG_BLUNT] = 10 * size;
			baseResists[Game.DMG_SLASH] = 5 * size;
			for(int i = 0; i < size; i++){
				addItem(Item.randomGem());
			}
		}else if(type == FUNGAL){
			addItem("slimeFungus", dropCount);
			name = "Fungal Slime";
			for(int i = 0; i < size; i++){
				addBodyPart("mushroom", 0.4);
				addBodyPart("mushroomWhite", 0.4);
			}
		}else if(type == MOLTEN){
			addItem("slimeMolten", dropCount);
			name = "Molten Slime";
			baseResists[Game.DMG_BURN] = 20 * size;
			baseResists[Game.DMG_PIERCE] = 10 * size;
			baseResists[Game.DMG_BLUNT] = 10 * size;
			baseResists[Game.DMG_SLASH] = 5 * size;
			xp = 12;
		}
		if(size == 2){
			maxHp = 84;
			name = "Large " + name;
			xp *= 2.5;
		}else if(size == 4){
			maxHp = 224;
			name = "Huge " + name;
			xp *= 6;
		}else if(size == 8){
			maxHp = 440;
			name = "Colossal " + name;
			xp *= 20;
		}else if(size == 16){
			maxHp = 1180;
			name = "Titanic " + name;
			xp *= 50;
		}
		dmg *= size;
		hp = maxHp;
		baseDmg = dmg;
		
		defaultAttackPattern = new AttackPattern(baseDmg, Game.DMG_BLUNT, "hit", 1.2);
		
		postInitialisation();
	}
	
	public void passiveAction(){
		if(type == PLANT){
			if(hp != maxHp){
				IO.print("The " + name + " regenerated " + Math.min(size * 20, maxHp - hp) + " hp, leaving it on ");
				hp += size * 4;
				hp = Math.min(hp, maxHp);
				IO.println(hp + " hp");
			}
		}	
	}
	
	
	public void deathTrigger(){
		if(type == 0){
			explode();
		}
	}
	
	public void explode(){
		int explodeDmg = size * size * 8;
		IO.println(Text.capitalised(Text.getDefName(this)) + " exploded dealing " + explodeDmg + " damage, leaving you on " + Math.max(0, Game.player.hp - explodeDmg) + " hp");
		Game.player.takeDamage(explodeDmg);
		for(int i = 0; i < Game.zone.creatures.size(); i++){
			Creature c = Game.zone.creatures.get(i);
			if(c != this && c.isAlive()){
				c.takeDamage(explodeDmg);
			}
		}
	}
	
	public void takeExplosionDamage(int d) {
		hp -= d;
		IO.println(Text.capitalised(Text.getDefName(this)) + " took " + d + " damage from the explosion, leaving it on " + Math.max(0, hp) + " hp");
		if(hp <= 0){
			kill();
			if(type == FIERY || type == MOLTEN){
				int burnDmg = size * 4;
				int finalDmg = burnDmg - Game.player.resists[Game.DMG_BURN];
				IO.println(Text.capitalised(Text.getDefName(this)) + " splattered in a shower of flames dealing " + finalDmg + " damage, leaving you on " + Math.max(0, Game.player.hp - finalDmg) + " hp");
				Game.player.takeDamage(finalDmg);
				for(int i = 0; i < Game.zone.creatures.size(); i++){
					Creature c = Game.zone.creatures.get(i);
					if(c != this && c.isAlive()){
						finalDmg = burnDmg - c.resists[Game.DMG_BURN];
						c.takeDamage(finalDmg);
					}
				}
			}
		}else{
			damageTrigger(d);
		}
	}
	
	public String getDescription(){
		String desc = "A blob of liquid and enzymes.";
		if(type == Slime.FIERY){
			desc += " It is eternally on fire.";//Red/orange
		}else if(type == Slime.ICY){
			desc += " It looks mostly frozen and is covered in sharp ice crystals.";//Blue/white?
		}else if(type == Slime.VOLATILE){
			desc += " It is highly unstable.";//Black
		}else if(type == Slime.STICKY){
			desc += " Extremely adhesive.";//Orange
		}else if(type == Slime.EARTHEN){
			desc += " It looks like it is partially made of dirt and rocks.";//Brown
		}else if(type == Slime.PLANT){
			desc += " It looks like it is largely made up of plants.";//Lime green
		}else if(type == Slime.GEM){
			desc += " Glittering gems float inside it.";//Transparent with shiny rainbow bits like an oily bubble
		}else if(type == Slime.FUNGAL){
			desc += " It is held together with a dense network of fungal strands.";//Brownish??
		}
		return desc;
	}
}
