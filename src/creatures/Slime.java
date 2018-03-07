package creatures;

import item.Item;

import java.util.Random;

import main.*;
import util.AttackHandler;
import util.IO;
import util.Text;

public class Slime extends Creature {
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
	public static final int ELECTRIC = 11;
	public static final int[] diffConstants = {1, 1, 0, 0, 0, 2, 1, 0, 0, 0, 2, 1};


	public Slime(int t, int s) {//Size should be a power of 2 (enforce this?)
		addTag("slime");
		size = s;
		maxHp = 32;
		xp = 7;
		baseResists[DamageType.BLUNT.number] = -4 * size;
		baseResists[DamageType.SLASH.number] = 7 * size;
		baseResists[DamageType.PIERCE.number] = 20 * size;
		addTarget(Tag.tag("player"), 100);
		naturalAttackPattern = new AttackPattern(null, "hit", 1.2);
		DamageInstance baseDmg = new DamageInstance(8, DamageType.BLUNT);

		name = "ERR UNTYPED SLIME";
		type = t;
		Random random = new Random();
		int dropCount = Math.max((int) (random.nextGaussian() + 2 * size), size);//Normal dist w/ μ = 1 + size, σ = 1, no lower than size
		if (type == VOLATILE) {
			addItem("slimeExplosive", dropCount);
			name = "Volatile Slime";
			baseResists[DamageType.BLUNT.number] = -20 * size;
			xp = 11;
		} else if (type == FIERY) {
			addItem("slimeFire", dropCount);
			name = "Fiery Slime";
			baseResists[DamageType.BURN.number] = 20 * size;
			xp = 10;
			naturalAttackPattern.onHits.add(new DamageOnHit(new DamageInstance(size * 2, DamageType.BURN)));
		} else if (type == ICY) {
			addItem("slimeIce", dropCount);
			if (Math.random() < 0.5) {
				addItem("icicle");
			}
			name = "Icy Slime";
			baseResists[DamageType.BLUNT.number] = 2 * size;
			baseResists[DamageType.SLASH.number] = 5 * size;
			baseResists[DamageType.PIERCE.number] = 15 * size;
			baseResists[DamageType.BURN.number] = -10 * size;
			baseResists[DamageType.COLD.number] = 20 * size;
			naturalAttackPattern.onHits.add(new DamageOnHit(new DamageInstance(size * 2, DamageType.COLD)));
		} else if (type == WATERY) {
			addItem("slimeWater", dropCount);
			name = "Watery Slime";
			baseResists[DamageType.SLASH.number] = 12 * size;
			baseResists[DamageType.BURN.number] = 5 * size;
			baseResists[DamageType.PIERCE.number] = 40 * size;
		} else if (type == STICKY) {
			addItem("slimeSticky", dropCount);
			name = "Sticky Slime";
			baseResists[DamageType.SLASH.number] = 10 * size;
			baseResists[DamageType.BLUNT.number] = 3 * size;
		} else if (type == EARTHEN) {
			addItem("slimeEarth", dropCount);
			for (int i = 0; i < size * 2; i++) {
				if (Math.random() < 0.5) {
					addItem("rock");
				}
			}
			name = "Rocky Slime";
			baseResists[DamageType.BLUNT.number] = 10 * size;
			baseResists[DamageType.SLASH.number] = 15 * size;
			baseResists[DamageType.COLD.number] = 20 * size;
			baseResists[DamageType.BURN.number] = 20 * size;
			xp = 10;
		} else if (type == PLANT) {
			addItem("slimePlant", dropCount);
			name = "Leafy Slime";
			baseResists[DamageType.BURN.number] = -4 * size;
			baseResists[DamageType.BLUNT.number] = 0;
			addBodyPart("leaf", 0.3);
			for (int i = 0; i < size * 2; i++) {
				addBodyPart("leaf", 0.7);
			}
		} else if (type == SPICY) {
			addItem("slimeSpice", dropCount);
			name = "Spicy Slime";
			addBodyPart("nutmeg", 0.2);
			addBodyPart("pepper", 0.3);
			addBodyPart("ginger", 0.2);
			addBodyPart("cinnamon", 0.25);
		} else if (type == GEM) {
			addItem("slimeGem", dropCount);
			name = "Jewelled Slime";
			baseResists[DamageType.PIERCE.number] = 10 * size;
			baseResists[DamageType.BLUNT.number] = 10 * size;
			baseResists[DamageType.SLASH.number] = 5 * size;
			for (int i = 0; i < size; i++) {
				addItem(Item.randomGem());
			}
		} else if (type == FUNGAL) {
			addItem("slimeFungus", dropCount);
			name = "Fungal Slime";
			for (int i = 0; i < size; i++) {
				addBodyPart("mushroom", 0.4);
				addBodyPart("mushroomWhite", 0.4);
			}
		} else if (type == MOLTEN) {
			addItem("slimeMolten", dropCount);
			name = "Molten Slime";
			baseResists[DamageType.BURN.number] = 20 * size;
			baseResists[DamageType.PIERCE.number] = 10 * size;
			baseResists[DamageType.BLUNT.number] = 10 * size;
			baseResists[DamageType.SLASH.number] = 5 * size;
			xp = 12;
			naturalAttackPattern.onHits.add(new DamageOnHit(new DamageInstance(size, DamageType.BURN)));
		} else if (type == ELECTRIC) {
			addItem("slimeElectric", dropCount);
			name = "Electric Slime";
			baseResists[DamageType.MAGIC.number] = 20 * size;
			xp = 12;
			naturalAttackPattern.onHits.add(new DamageOnHit(new DamageInstance(size * 2, DamageType.MAGIC)));
		}
		if (size == 2) {
			maxHp = 84;
			name = "Large " + name;
			xp *= 2.5;
		} else if (size == 4) {
			maxHp = 224;
			name = "Huge " + name;
			xp *= 10;
		} else if (size == 8) {
			maxHp = 440;
			name = "Colossal " + name;
			xp *= 50;
		} else if (size == 16) {
			maxHp = 1180;
			name = "Titanic " + name;
			xp *= 200;
		}
		baseDmg.amount *= size;
		hp = maxHp;

		naturalAttackPattern.baseDamage = baseDmg;

		postInitialisation();
	}

	public void passiveAction() {
		if (type == PLANT || type == FUNGAL) {
			if (hp != maxHp) {
				AttackHandler.selfHeal(this, size * 4, "regenerated");
			}
		}
	}


	public void deathTrigger() {
		if (type == VOLATILE) {
			explode();
		}
	}

	public void explode() {
		DamageInstance explodeDmg = new DamageInstance(size * size * 8, DamageType.FORCE);
		IO.println(Text.capitalised(Text.getDefName(this)) + " exploded.");
		AttackHandler.applyDamagePassively(this, Game.player, explodeDmg, null);

		for (int i = 0; i < Game.zone.creatures.size(); i++) {
			Creature c = Game.zone.creatures.get(i);
			if (c != this && c.isAlive()) {
				AttackHandler.applyDamagePassively(this, c, explodeDmg, null);
			}
		}
	}

	//TODO do something about takeExplosionDamage
	public void takeExplosionDamage(int d) {
		// Observe this ancient code

//		hp -= d;
//		IO.println(Text.capitalised(Text.getDefName(this)) + " took " + d + " damage from the explosion, leaving it on " + Math.max(0, hp) + " hp");
//		if(hp <= 0){
//			kill();
//			if(type == FIERY || type == MOLTEN){
//				int burnDmg = size * 4;
//				int finalDmg = burnDmg - Game.player.resists[DamageType.BURN.number];
//				IO.println(Text.capitalised(Text.getDefName(this)) + " splattered in a shower of flames dealing " + finalDmg + " damage, leaving you on " + Math.max(0, Game.player.hp - finalDmg) + " hp");
//				Game.player.takeDamage(finalDmg);
//				for(int i = 0; i < Game.zone.creatures.size(); i++){
//					Creature c = Game.zone.creatures.get(i);
//					if(c != this && c.isAlive()){
//						finalDmg = burnDmg - c.resists[DamageType.BURN.number];
//						c.takeDamage(finalDmg);
//					}
//				}
//			}
//		}else{
//			damageTrigger(d);
//		}
	}

	public String getDescription() {
		String desc = "A blob of liquid and enzymes.";
		if (type == Slime.FIERY) {
			desc += " It is eternally on fire.";//Red/orange
		} else if (type == Slime.ICY) {
			desc += " It looks mostly frozen and is covered in sharp ice crystals.";//Blue/white?
		} else if (type == Slime.VOLATILE) {
			desc += " It is highly unstable.";//Black
		} else if (type == Slime.STICKY) {
			desc += " Extremely adhesive.";//Orange
		} else if (type == Slime.EARTHEN) {
			desc += " It looks like it is partially made of dirt and rocks.";//Brown
		} else if (type == Slime.PLANT) {
			desc += " It looks like it is largely made up of plants.";//Lime green
		} else if (type == Slime.GEM) {
			desc += " Glittering gems float inside it.";//Transparent with shiny rainbow bits like an oily bubble
		} else if (type == Slime.FUNGAL) {
			desc += " It is held together with a dense network of fungal strands.";//Brownish??
		} else if (type == Slime.ELECTRIC) {
			desc += " It crackles with lightning.";//Electric blue
		}
		return desc;
	}
}
