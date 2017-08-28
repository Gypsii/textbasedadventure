package item;

import java.util.ArrayList;

import main.ArmourPassive;
import main.Game;
import main.OnHit;

public class MagicItem {
	public static final int RES_BURN = 0;
	public static final int RES_COLD = 1;
	public static final int RES_MAGIC = 2;
	public static final int CRIT = 3;
	public static final int CRIT_DMG = 4;
	public static final int HIT_SHOCK = 5;
	public static final int HIT_BURN = 6;
	public static final int HIT_COLD = 7;
	public static final int HIT_SHRED_BLUNT = 8;
	public static final int HIT_SHRED_SLASH = 9;
	public static final int HIT_SHRED_PIERCE = 10;
	public static final int HIT_SHRED_BURN = 11;
	public static final int HIT_SHRED_COLD = 12;
	public static final int HIT_SHRED_MAGIC = 13;
	public static final int RES_BLUNT = 14;
	public static final int RES_PIERCE = 15;
	public static final int RES_SLASH = 16;	
	public static final int HIT_RES_BLUNT = 17;
	public static final int HIT_RES_SLASH = 18;
	public static final int HIT_RES_PIERCE = 19;
	public static final int HIT_RES_BURN = 20;
	public static final int HIT_RES_COLD = 21;
	public static final int HIT_RES_MAGIC = 22;

	/**
	 * Returns a new instance of the item, enchanted with the given effect and level. The item is renamed accordingly.
	 * <p>Items whose names, effects, or passives differ from their template in {@code Item.items} should not be 
	 * enchanted this way as these will be reset to their default values before being modified by this method.
	 * 
	 * @param i the {@code Item} to be enchanted
	 * @param effect the integer code for the magical effect to be added
	 * @param level the strength of the enchantment
	 * @return new instance of {@code i} that has been enchanted
	 */
	public static Item enchant(Item i, int effect, int level){
		Item magicA = i.clone();
		magicA.enchantLvl = level;
		magicA.enchantType = effect;
		magicA.name = Item.item(magicA.id).name;//Resets name, effects, and passives to those of the base item
		magicA.effects = Item.item(magicA.id).effects;
		magicA.passives = Item.item(magicA.id).passives;
		if(level <= 0){
			level = 0;
			magicA.name = magicA.name + " of Literally Zero ";
		}else if(level == 1){
			magicA.name = magicA.name + " of Negligible ";
		}else if(level == 2){
			magicA.name = magicA.name + " of Minor ";
		}else if(level == 3){
			magicA.name = magicA.name + " of Lesser ";
		}else if(level == 4){
			magicA.name = magicA.name + " of ";
		}else if(level == 5){
			magicA.name = magicA.name + " of Greater ";
		}else if(level == 6){
			magicA.name = magicA.name + " of Major ";
		}else if(level == 7){
			magicA.name = magicA.name + " of Epic ";
		}else if(level == 8){
			magicA.name = magicA.name + " of Legendary ";
		}else{
			magicA.name = magicA.name + " of Legendary (+" + (level - 8) + ") ";
		}
		switch(effect){
		case  RES_BLUNT:
			magicA.resists[Game.DMG_BLUNT] = Item.item(magicA.id).resists[Game.DMG_BLUNT];
			magicA.resists[Game.DMG_BLUNT] += level * 2;
			magicA.name = magicA.name + "Blunt Resist";
			break;
		case  RES_SLASH:
			magicA.resists[Game.DMG_SLASH] = Item.item(magicA.id).resists[Game.DMG_SLASH];
			magicA.resists[Game.DMG_SLASH] += level * 2;
			magicA.name = magicA.name + "Slash Resist";
			break;
		case  RES_PIERCE:
			magicA.resists[Game.DMG_PIERCE] = Item.item(magicA.id).resists[Game.DMG_PIERCE];
			magicA.resists[Game.DMG_PIERCE] += level * 2;
			magicA.name = magicA.name + "Pierce Resist";
			break;
		case  RES_BURN:
			magicA.resists[Game.DMG_BURN] = Item.item(magicA.id).resists[Game.DMG_BURN];
			magicA.resists[Game.DMG_BURN] += level * 2;
			magicA.name = magicA.name + "Fire Resist";
			break;
		case  RES_COLD:
			magicA.resists[Game.DMG_COLD] = Item.item(magicA.id).resists[Game.DMG_COLD];
			magicA.resists[Game.DMG_COLD] += level * 2;
			magicA.name = magicA.name + "Cold Resist";
			break;
		case  RES_MAGIC:
			magicA.resists[Game.DMG_MAGIC] = Item.item(magicA.id).resists[Game.DMG_MAGIC];
			magicA.resists[Game.DMG_MAGIC] += level * 2;
			magicA.name = magicA.name + "Magic Resist";
			break;
		case  CRIT:
			magicA.passives.add(new ArmourPassive(ArmourPassive.CRIT, level * 5));
			magicA.name = magicA.name + "Critical Chance";
			break;
		case  CRIT_DMG:
			magicA.passives.add(new ArmourPassive(ArmourPassive.CRIT_DMG, level * 2));
			magicA.name = magicA.name + "True Strikes";
			break;
		case  HIT_SHOCK:
			magicA.effects.add(new OnHit(OnHit.LIGHTNING, level * 2));
			magicA.name = magicA.name + "Arcing";
			break;
		case  HIT_BURN:
			magicA.effects.add(new OnHit(OnHit.BURN, level * 2));
			magicA.name = magicA.name + "Burning";
			break;
		case  HIT_COLD:
			magicA.effects.add(new OnHit(OnHit.COLD, level * 2));
			magicA.name = magicA.name + "Freezing";
			break;
		case  HIT_SHRED_BLUNT:
			magicA.effects.add(new OnHit(OnHit.SHRED_BLUNT, level * 2, 2));
			magicA.name = magicA.name + "Amplify Blunt";
			break; 
		case  HIT_SHRED_SLASH:							
			magicA.effects.add(new OnHit(OnHit.SHRED_SLASH, level * 2, 2));	
			magicA.name = magicA.name + "Amplify Slashing";				
			break; 
		case  HIT_SHRED_PIERCE:
			magicA.effects.add(new OnHit(OnHit.SHRED_PIERCE, level * 2, 2));
			magicA.name = magicA.name + "Amplify Piercing";
			break;
		case  HIT_SHRED_BURN:								
			magicA.effects.add(new OnHit(OnHit.SHRED_BURN, level * 2, 2));
			magicA.name = magicA.name + "Amplify Burns";				
			break;
		case  HIT_SHRED_COLD:
			magicA.effects.add(new OnHit(OnHit.SHRED_COLD, level * 2, 2));
			magicA.name = magicA.name + "Amplify Cold";
			break;
		case  HIT_SHRED_MAGIC:
			magicA.effects.add(new OnHit(OnHit.SHRED_MAGIC, level * 2, 2));
			magicA.name = magicA.name + "Amplify Magic";
			break;
		}
		magicA.isEnchanted = true;
		magicA.name = "<purple>" + magicA.name + "<r>";
		return magicA;
	}
	
	/**
	 * Enchants a randomly chosen item from a list of cloaks, rings and hats with a random enchantment of the given tier.
	 * @return enchanted <tt>Item</tt>
	 */
	public static Item pickRandomMagicItem(int tier){
		ArrayList<String> rand = new ArrayList<String>();
		rand.add("ringCopper");
		rand.add("ringSilver");
		rand.add("ringGold");
		rand.add("ringCopper");//to increase chance of rings
		rand.add("ringSilver");
		rand.add("ringGold");
		
		if(tier >= 5){
			rand.add("ringDemon");
		}
		
		rand.add("cloakSilk");
		rand.add("cloakCotton");
		rand.add("cloakWool");
		rand.add("cloakLinen");
		
		rand.add("fedora");
		rand.add("topHat");
		rand.add("featherHat");
		rand.add("beret");
		
		Item base = Item.item(rand.get((int)(Math.random() * rand.size())));
		return RandomEnchant(base, tier);
	}
	
	/**
	 * Enchants the given item with a random enchantment of the given tier.
	 * @return Enchanted Item
	 */
	public static Item RandomEnchant(Item base, int tier){
		Item a = enchant(base, (int)(Math.random() * 16), tier);//random() is multiplied by the ID of the largest effect
		return a;
	}
	
	/**
	 * Increases the level of the enchantment on the given <tt>Item</tt> by one. This increase has no cap.
	 * @return The given item with the enchant level incremented.
	 */
	public static Item improveEnchant(Item a){
		a = enchant(a, a.enchantType, a.enchantLvl + 1);
		return a;
	}

	/**
	 * Enchants <tt>Item i</tt> with the enchantment given by the scroll and the gem, enchanting at level six.
	 * @param i the item to enchant
	 * @param scroll the scroll to apply
	 * @param gem the gem to apply
	 * @return <tt>Item i</tt> enchanted 
	 */
	public static Item playerEnchant(Item i, Scroll scroll, Item gem){
		int gemId;
		if(gem.name.equals(Item.item("citrine").name)){
			gemId = 0;
		}else if(gem.name.equals(Item.item("emerald").name)){
			gemId = 1;
		}else if(gem.name.equals(Item.item("diamond").name)){
			gemId = 2;
		}else if(gem.name.equals(Item.item("ruby").name)){
			gemId = 3;
		}else if(gem.name.equals(Item.item("sapphire").name)){
			gemId = 4;
		}else{//Amethyst
			gemId = 5;
		}
		int effect = 0;
		if(scroll.name.equals(Scroll.scrollOnHitShred.name)){
			switch(gemId){
			case 0: effect = HIT_SHRED_BLUNT;
				break;
			case 1: effect = HIT_SHRED_SLASH;
				break;
			case 2: effect = HIT_SHRED_PIERCE;
				break;
			case 3: effect = HIT_SHRED_BURN;
				break;
			case 4: effect = HIT_SHRED_COLD;
				break;
			case 5: effect = HIT_SHRED_MAGIC;
				break;
			}
		}else if(scroll.name.equals(Scroll.scrollOnHitResist.name)){
			switch(gemId){
			case 0: effect = HIT_RES_BLUNT;
				break;
			case 1: effect = HIT_RES_SLASH;
				break;
			case 2: effect = HIT_RES_PIERCE;
				break;
			case 3: effect = HIT_RES_BURN;
				break;
			case 4: effect = HIT_RES_COLD;
				break;
			case 5: effect = HIT_RES_MAGIC;
				break;
			}
		}else if(scroll.name.equals(Scroll.scrollResist.name)){
			switch(gemId){
			case 0: effect = RES_BLUNT;
				break;
			case 1: effect = RES_SLASH;
				break;
			case 2: effect = RES_PIERCE;
				break;
			case 3: effect = RES_BURN;
				break;
			case 4: effect = RES_COLD;
				break;
			case 5: effect = RES_MAGIC;
				break;
			}
		}else if(scroll.name.equals(Scroll.scrollCrit.name)){
			switch(gemId){
			case 0: effect = CRIT;
				break;
			case 1: effect = CRIT;
				break;
			case 2: effect = CRIT;
				break;
			case 3: effect = CRIT_DMG;
				break;
			case 4: effect = CRIT_DMG;
				break;
			case 5: effect = CRIT_DMG;
				break;
			}
		}else if(scroll.name.equals(Scroll.scrollOnHitDmg.name)){
			switch(gemId){
			case 0: effect = HIT_BURN;
				break;
			case 1: effect = HIT_COLD;
				break;
			case 2: effect = HIT_SHOCK;
				break;
			case 3: effect = HIT_BURN;
				break;
			case 4: effect = HIT_COLD;
				break;
			case 5: effect = HIT_SHOCK;
				break;
			}
		}
		return enchant(i, effect, 6);
		
	}
	
}
