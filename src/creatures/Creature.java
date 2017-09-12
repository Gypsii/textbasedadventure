package creatures;

import item.Item;

import java.util.*;

import main.Game;
import main.Skillset;
import main.Zone;
import util.*;
import util.Text;

public class Creature {
	public static TreeMap<String, CreatureTemplate> creatures = new TreeMap<String, CreatureTemplate>();

	public int maxHp;
	public int hp;
	public int dmg;
	public boolean canBeBoss = true;
	public int bossType = 0;
	public double critDmg, baseCritDmg = 1.25;
	public double critChance, baseCritChance = 0;
	public int xp = 0;//xp to be dropped not xp accumulated
	public int resists[] = new int[Game.DMG_TYPE_COUNT];
	public int baseResists[] = new int[Game.DMG_TYPE_COUNT];
	public int baseDmg;//Should this exist?
	public double nextActionTime = 0;
	public Skillset skills = new Skillset(this);

	public String name;
	public String articleIndef = "a";
	public String articleDef = "the";
	public String description = "";

	public Set<Condition> conditions = new HashSet<>();

	public double courage = 100000;//this needs fixing
	public Zone zone;

	public String defaultAttackPatternId = "weapon";
	public AttackPattern defaultAttackPattern;

	public Creature target = null;
	public ArrayList<CreatureTag> tags = new ArrayList<CreatureTag>();
	public ArrayList<CreatureTag> targetTags = new ArrayList<CreatureTag>();
	public ArrayList<Integer> targetWeights = new ArrayList<Integer>();
	static Comparator<TargetPair> targetComparator = new TargetComparator();
	public PriorityQueue<TargetPair> targetQueue = new PriorityQueue<TargetPair>(targetComparator);
	public HashMap<Creature, TargetPair> targetMap = new HashMap<Creature, TargetPair>();

	public ArrayList<Item> inv = new ArrayList<Item>();
	public ArrayList<Item> butcherInv = new ArrayList<Item>();
	public HashMap<Item, Double> butcherSuccess = new HashMap<Item, Double>();
	public Item equipped = Item.unarmed.clone();
	public Item armourChest;
	public Item ring;
	public Item cloak;
	public Item hat;

	public ArrayList<Item> shopInv = new ArrayList<Item>();
	public HashMap<String, Integer> prices = new HashMap<String, Integer>();


	public Creature() {
		armourChest = Item.item("unarmoured");
		ring = Item.item("unarmouredCloak");
		cloak = Item.item("unarmouredRing");
		hat = Item.item("unarmouredHat");
		refreshArmour();
		defaultAttackPattern = AttackPattern.attack(defaultAttackPatternId);
		addPresentCreature(Game.player);
		setHostilityTowardsPlayer(true);
	}


	/**
	 * Returns a copy of this creature, similarly to calling creature() on a CreatureTemplate.
	 *
	 * @return the copied creature
	 */
	@SuppressWarnings("unchecked")
	public Creature copyBase() {
		Creature c = new Creature();

		c.name = this.name;
		c.articleDef = this.articleDef;
		c.articleIndef = this.articleIndef;
		c.description = this.description;

		c.conditions.addAll(this.conditions);
//		c.alive = this.alive;
//		c.enraged = this.enraged;
//		c.enrageable = this.enrageable;
		c.courage = this.courage;
		c.canBeBoss = this.canBeBoss;

		c.baseCritChance = this.baseCritChance;
		c.baseDmg = this.baseDmg;
		c.baseResists = this.baseResists.clone();
		c.maxHp = this.maxHp;
		c.hp = this.hp;
		c.xp = this.xp;

		//Don't think these need cloning
		c.equipped = this.equipped;
		c.armourChest = this.armourChest;
		c.ring = this.ring;
		c.cloak = this.cloak;
		c.hat = this.hat;

		c.inv.addAll(this.inv);

		c.defaultAttackPattern = this.defaultAttackPattern;
		c.defaultAttackPatternId = this.defaultAttackPatternId;


		c.butcherInv.addAll(this.butcherInv);
		c.butcherSuccess = (HashMap<Item, Double>) this.butcherSuccess.clone();
		c.shopInv = (ArrayList<Item>) this.shopInv.clone();
		c.prices = (HashMap<String, Integer>) this.prices.clone();

		c.dmg = c.calculateDamage();
		c.refreshArmour();

		return c;
	}


	static Creature bug = new Creature();

	public static Creature creature(String id) {
		if (creatures.containsKey(id)) {
			return creatures.get(id).creature();
		} else {
			IO.print("<lred>Key " + id + " not found!<r>");
			return bug;
		}
	}

	/**
	 * Things that need to happen always after this {@code Creature}'s specific constructor does its things
	 */
	public void postInitialisation() {
		doBossType();
		refreshArmour();
		this.dmg = calculateDamage();
		overwriteStats();
	}

	public String getName() {
		String result = name;

		if(!isAlive()) {
			result = "dead " + result;
		}else {
			if(conditions.contains(Condition.ENRAGED)) {
				result = "enraged " + result;
			}
			if(conditions.contains(Condition.SLEEPING)) {
				result = "sleeping " + result;
			}
		}
		return result;
	}

	/**
	 * Rolls for whether this {@code Creature} is a boss, then rolls for boss type.
	 * Takes into account the {@code canBeBoss} parameter of this {@code Creature}.
	 */
	public void doBossType() {
		if (Math.random() < 0.1 && this.canBeBoss) {
			bossType = (int) (Math.random() * 11) + 1;//times number of cases
			switch (bossType) {
				case 1:
					name = "Devastating " + name;
					baseCritChance += 0.5;
					critChance += 0.5;
					break;
				case 2:
					name = "Resilient " + name;
					baseResists[Game.DMG_BLUNT] += 5;
					baseResists[Game.DMG_SLASH] += 5;
					baseResists[Game.DMG_PIERCE] += 5;
					break;
				case 3:
					name = "Tough " + name;
					hp *= 1.5;
					break;
				case 4:
					name = "Sickly " + name;
					hp *= 0.75;
					break;
				case 5:
					name = "Vulnerable " + name;
					baseResists[Game.DMG_BLUNT] -= 5;
					baseResists[Game.DMG_SLASH] -= 5;
					baseResists[Game.DMG_PIERCE] -= 5;
					break;
				case 6:
					name = "Stylish " + name;
					double rand = Math.random();
					if (rand < 0.20) {
						addItem("fedora");
						equip("fedora");
					} else if (rand < 0.40) {
						addItem("beret");
						equip("beret");
					} else if (rand < 0.60) {
						addItem("featherHat");
						equip("featherHat");
					} else if (rand < 0.80) {
						addItem("topHat");
						equip("topHat");
					} else {
						addItem("hatPirate");
						equip("hatPirate");
					}
					break;
				case 7:
					name = "Brutal " + name;
					baseDmg *= 1.25;
					break;
				case 8:
					name = "Rich " + name;
					addItem("money", (int) (Math.random() * 20) + 20);
					break;
				case 9:
					name = "Agressive " + name;
					addTarget(CreatureTag.rodent, 120);
					addTarget(CreatureTag.slime, 120);
					addTarget(CreatureTag.humanoid, 120);
					addTarget(CreatureTag.fiend, 120);
					addTarget(CreatureTag.bird, 120);
					addTarget(CreatureTag.spider, 120);
					addTarget(CreatureTag.walrus, 120);
					break;
				case 10:
					name = "Easily Infuriated " + name;
					conditions.add(Condition.ENRAGEABLE);
//				enrageable = true;
					break;
				case 11:
					name = "Weak " + name;
					baseDmg *= 0.75;
					break;
			}
			name = "<purple>" + name + "<r>";
		}
	}

	/**
	 * Gets called after everything else so this can be overwritten to change whatever is needed for special unique creatures
	 */
	public void overwriteStats() {

	}

	//Overwriting this one is not recommended
	public double takeTurn() {
		if (conditions.contains(Condition.SLEEPING)) {
			return 0.2;
		}
		passiveAction();
		if (isAbsconding()) {
			abscond();
			return 10000;
		} else {
			while (!targetQueue.isEmpty()) {
				TargetPair p = targetQueue.peek();
				target = p.creature;
				if (target.isAlive()) {
					if (p.weight > 0) {
						AttackPattern ap = decideAttackPattern();
						return AttackHandler.attack(this, target, ap);
					} else {
						break;
					}
				} else {
					targetQueue.remove(targetQueue.peek());
				}
			}
			restingAction();
			return 1;
		}
	}

	/**
	 * Checks if this {@code Creature} is wishes to abscond.
	 * <p>
	 * This is an independent method so it can be overridden without overriding {@code takeTurn} (currently used by birds).
	 *
	 * @return {@code true} if absconding
	 */
	boolean isAbsconding() {
		return courage <= 0;
	}
	
	/*
	 * Actions
	 */

	public AttackPattern decideAttackPattern() {
		return defaultAttackPattern;
	}

	/**
	 * Executes this {@code Creature}'s action taken for turn when neither attacking nor absconding.
	 */
	public void restingAction() {

	}

	/**
	 * Taken at the start of every turn when alive.
	 */
	public void passiveAction() {

	}

	/**
	 * Causes this {@code Creature} to leave the zone.
	 */
	public void abscond() {//TODO maybe this should remove creature from targets? Testing required
		IO.println(Text.getDefName(this) + " ran away.");
		Game.zone.creatures.remove(this);
	}

	public void equip(int index) {
		equip(inv.get(index));
	}

	public void equip(String id) {
		equip(Item.item(id));
	}

	public void equip(Item i) {
		if (i.armourType == 0) {
			equipped = i;
			dmg = calculateDamage();
		} else {
			if (i.armourType == 1) {
				armourChest = i;
			} else if (i.armourType == 2) {
				ring = i;
			} else if (i.armourType == 3) {
				cloak = i;
			} else if (i.armourType == 4) {
				hat = i;
			}
			refreshArmour();
		}
	}

	/**
	 * Calculates the damage of this {@code Creature} based off its weapons and {@code SkillSet}
	 *
	 * @return DamageType calculation results
	 */
	public int calculateDamage() {
		int dmgPerLvl = 2;
		int weaponDmg = equipped.dmg;
		switch (equipped.dmgType) {
			case Game.DMG_BLUNT:
				weaponDmg += (skills.bluntLvl - 1) * (equipped.swingTime * dmgPerLvl);
				break;
			case Game.DMG_SLASH:
				weaponDmg += (skills.slashLvl - 1) * (equipped.swingTime * dmgPerLvl);
				break;
			case Game.DMG_PIERCE:
				weaponDmg += (skills.pierceLvl - 1) * (equipped.swingTime * dmgPerLvl);
				break;
		}
		if (equipped.hasTag("polearm")) {
			weaponDmg += (skills.poleLvl - 1) * (equipped.swingTime * dmgPerLvl);
		}
		if (equipped.hasTag("sword")) {
			weaponDmg += (skills.swordLvl - 1) * (equipped.swingTime * dmgPerLvl);
		}
		weaponDmg = Math.min(weaponDmg, equipped.dmg * 2);
		return Math.max(baseDmg, weaponDmg);
	}

	public boolean checkAttackSuccess(Creature c) {//TODO attack hits/misses
		return true;
	}
	

	/*
	 * Targeting
	 * 
	 * WARNING
	 * Targeting code is sinister dark magic. Be wary all those who attemt to decypher it
	 */


	/**
	 * Informs this {@code Creature} that c is present in the zone for targeting purposes.
	 *
	 * @param c
	 */
	public void addPresentCreature(Creature c) {
		TargetPair p = new TargetPair();
		p.creature = c;
		p.weight = findWeight(c);
		targetQueue.add(p);
		targetMap.put(c, p);
	}

	private int findWeight(Creature c) {
		int weight = 0;
		for (int j = 0; j < targetTags.size(); j++) {
			for (int k = 0; k < c.tags.size(); k++) {
				if (c.tags.get(k) == targetTags.get(j)) {
					weight += targetWeights.get(j);
				}
			}
		}
		return weight;
	}

	public void addTarget(CreatureTag t, int weight) {
		targetTags.add(t);
		targetWeights.add(weight);
	}

	public boolean hostileTowards(Creature c) {
		return targetMap.get(c).weight > 0;
	}

	public void setHostilityTowardsPlayer(boolean h) {//Maybe this could take an int?
		for (TargetPair p : targetQueue) {
			if (p.creature == Game.player) {
				if (h) {
					p.weight = 100;
				} else {
					p.weight = 0;
				}
				break;
			}
		}
	}
	
	/*
	 * Things happening to creature
	 */

	public void takeDamage(int d) {
		hp -= Math.max(d, 0);
		if (hp <= 0) {
			kill();
		} else {
			damageTrigger(d);
		}
	}

	public void kill() {
		if (isAlive()) {
			IO.println(Text.capitalised(Text.getDefName(this)) + " was killed");
			conditions.add(Condition.DEAD);
			dropItems();
			deathTrigger();
			if (this.xp > 0) {
				Game.player.giveXp(xp);//TODO maybe check if player should be given this xp?
			}
		}
	}
	
	/*
	 * Inventory manipulation
	 */

	/**
	 * Drop {@code Item}s from {@code inv} and {@code shopInv} into the current zone. Equipped items are not included
	 * unless specifically in {@code inv} as well.
	 */
	public void dropItems() {
		for (int i = 0; i < inv.size(); i++) {
			Game.zone.addItem(inv.get(i));
			IO.println("<yellow>" + Text.getDefName(this) + " dropped " + inv.get(i).prefix + " " + inv.get(i).getNameWithCount() + "<r>");
		}
		inv.clear();
		for (int i = 0; i < shopInv.size(); i++) {
			Game.zone.addItem(shopInv.get(i));
			IO.println("<yellow>" + Text.getDefName(this) + " dropped " + shopInv.get(i).prefix + " " + shopInv.get(i).getNameWithCount() + "<r>");
		}
		shopInv.clear();
	}

	/**
	 * Rolls for butcher items from {@code butcherInv} and puts them into the zone.
	 */
	public void dropButcherItems() {
		for (int i = 0; i < butcherInv.size(); i++) {
			for (int j = 0; j < butcherInv.get(i).count; j++) {
				if (Math.random() < butcherSuccess.get(butcherInv.get(i))) {
					Game.zone.addItem(butcherInv.get(i));
					IO.println("<yellow>" + Text.getDefName(this) + " dropped " + butcherInv.get(i).prefix + " " + butcherInv.get(i).getNameWithCount() + "<r>");
				}
			}
		}
		butcherInv.clear();
	}

	/**
	 * Adds the {@code Item i} to this {@code Creature}'s inventory. If a matching item is found, the items are
	 * stacked instead of being added to a new index.
	 *
	 * @param i
	 */
	public void addItem(Item i) {
		Item item = i.clone();
		if (i.isStackable) {
			int loc = findItemLoc(i);
			if (loc >= 0) {
				inv.get(loc).count += i.count;
			} else {
				inv.add(item);
			}
		} else {
			inv.add(item);
		}
	}

	public void addItem(String id) {
		Item item = Item.item(id);
		addItem(item);
	}

	public void addItem(String id, int count) {
		Item item = Item.item(id);
		addItem(item, count);
	}

	public void addItem(Item i, int count) {
		Item item = i.clone();
		item.count = count;
		inv.add(item);
	}

	/**
	 * Removes the first occurrence of the item at the specified index from the inventory of this
	 * creature. If this creature does not contain the item, it is unchanged.
	 * If this creature has this item equipped, it will be unequipped.
	 * <p>
	 * Note that in ordinary circumstances, duplicates can but do not necessarily occur with
	 * stackable items that are identical. In this case, the first occurrence will be removed,
	 * regardless of whether it is at the specified index.
	 *
	 * @param i
	 */
	public void removeItem(int i) {
		removeItem(inv.get(i));
	}

	/**
	 * Removes the first occurrence of the specified {@code Item} from the inventory of this
	 * creature. If this creature does not contain the item, it is unchanged.
	 * If this creature has this item equipped, it will be unequipped.
	 * <p>
	 * Note that in ordinary circumstances, duplicates can but do not necessarily occur with
	 * stackable items that are identical. In this case, the first occurrence will be removed.
	 *
	 * @param i
	 */
	public void removeItem(Item i) {
		if (equipped == i) {
			equip(Item.unarmed);
			dmg = calculateDamage();
		} else if (armourChest == i) {
			equipped = Item.item("unarmoured");
			refreshArmour();
		} else if (cloak == i) {
			equipped = Item.item("unarmouredCloak");
			refreshArmour();
		} else if (ring == i) {
			equipped = Item.item("unarmouredRing");
			refreshArmour();
		} else if (hat == i) {
			equipped = Item.item("unarmouredHat");
			refreshArmour();
		}
		inv.remove(i);
	}

	/**
	 * Adds the item corresponding to the specified string to the shop inventory of this
	 * {@code Creature} with the specified price.
	 *
	 * @param i the id of the item to add
	 * @param p the price of the item
	 */
	public void addShopItem(String i, int p) {
		addShopItem(Item.item(i), p);
	}

	/**
	 * Adds the specified item to the shop inventory of this {@code Creature} with the
	 * specified price.
	 *
	 * @param i the item to add
	 * @param p the price of the item
	 */
	public void addShopItem(Item i, int p) {
		Item item = i.clone();
		if (i.isStackable) {
			int loc = findShopItemLoc(i);
			if (loc >= 0) {
				shopInv.get(loc).count += i.count;
			} else {
				shopInv.add(item);
			}
		} else {
			shopInv.add(item);
		}
		prices.put(i.name, p);
	}

	/**
	 * Adds the specified item to the butcher inventory of this {@code Creature} with the
	 * specified chance of success.
	 *
	 * @param i           the id of the item to add
	 * @param successRate the chance of success
	 */
	public void addBodyPart(String i, double successRate) {
		addBodyPart(Item.item(i), successRate);
	}

	/**
	 * Adds the item corresponding to the specified string to the butcher inventory of this {@code Creature} with the
	 * specified chance of success.
	 *
	 * @param i           the item to add
	 * @param successRate the chance of success
	 */
	public void addBodyPart(Item i, double successRate) {
		Item item = i.clone();
		butcherInv.add(item);
		butcherSuccess.put(item, successRate);
	}

	/**
	 * Finds the index of the given {@code Item} in this {@code Creature}'s {@code inv}, or -1 if not present.
	 *
	 * @return inventory index of item or -1 if not present
	 */
	public int findItemLoc(Item i) {
		for (int x = 0; x < inv.size(); x++) {
			if (inv.get(x).name == i.name) {
				return x;
			}
		}
		return -1;
	}

	/**
	 * Finds the index of the given {@code Item} in this {@code Creature}'s {@code shopInv}, or -1 if not present.
	 *
	 * @return shop inventory index of item or -1 if not present
	 */
	public int findShopItemLoc(Item i) {
		for (int x = 0; x < shopInv.size(); x++) {
			if (shopInv.get(x).name == i.name) {
				return x;
			}
		}
		return -1;
	}
	
	/*
	 * Other
	 */

	public void printInfo() {
		IO.printHorizontalLine();
		IO.println(name + ":");
		IO.println("Health: " + Math.max(0, hp) + "/" + maxHp);
		for (int i = 0; i < Game.DMG_TYPE_COUNT; i++) {
			if (resists[i] > 0) {
				IO.println(Game.DAMAGE_TYPE_STRINGS[i] + " resistance: " + resists[i]);
			} else if (resists[i] < 0) {
				IO.println(Game.DAMAGE_TYPE_STRINGS[i] + " vulnerability: " + (0 - resists[i]));
			}
		}
		IO.println("");
		if (!equipped.name.equals("unarmed")) {
			IO.println("Weapon: " + equipped.getNameWithCount());
			IO.println("DamageType: " + dmg + " " + Game.DAMAGE_TYPE_STRINGS[equipped.dmgType]);
		} else {
			IO.println("DamageType: " + defaultAttackPattern.baseDamage + " " + Game.DAMAGE_TYPE_STRINGS[defaultAttackPattern.damageType]);
		}
		IO.println("");
		IO.println(getDescription());
		IO.printHorizontalLine();
	}


	public String getDescription() {
		return description;
	}

	/**
	 * Activates all actions that occur when a creature is damaged, and applies effects specific to this creature.
	 * <p>
	 * General effects include decreasing courage and awakening sleeping creatures. Specific effects are defined
	 * in Creature.damageSubtrigger()
	 *
	 * @param d
	 */
	public void damageTrigger(int d) {//TODO rewrite courage
		if (conditions.contains(Condition.SLEEPING)) {
			awaken(false);
		}
		d = Math.max(d, 0);
		if (d > maxHp / 4) {//If damage deals over 25% hp
			double c = Math.max((maxHp / d) - 0.25, 0);//Lose a decimal amount of courage equal to the %hp of damage over 25%
			if (hp < maxHp / 4) {
				c *= 2;
			}//Doubled if under half hp
			courage -= c;
		}
		damageSubtrigger();//Creature specific		
	}

	public boolean isAlive() {
		return !conditions.contains(Condition.DEAD);
	}

	/**
	 * Puts this creature to sleep, displaying sleep text.
	 */
	public void sleep() {
		sleep(false);
	}

	/**
	 * Puts this creature to sleep, displaying sleep text if {@code silent == false}.
	 */
	public void sleep(boolean silent) {
		if (!conditions.contains(Condition.SLEEPING)) {
			if (!silent) {
				IO.println(Text.capitalised(Text.getDefName(this)) + " fell asleep.");
			}
			conditions.add(Condition.SLEEPING);
		}
	}

	/**
	 * Awakens this creature, displaying wake text.
	 */
	public void awaken() {
		awaken(true);
	}

	/**
	 * Awakens this creature, displaying wake text if {@code print == false}.
	 */
	public void awaken(boolean print) {
		if (conditions.contains(Condition.SLEEPING)) {
			conditions.remove(Condition.SLEEPING);
			if (print) {
				IO.println(Text.capitalised(Text.getDefName(this)) + " woke up.");
			}
		}
	}

	/**
	 * Executes this {@code Creature}'s specific damage triggered effects
	 */
	public void damageSubtrigger() {

	}

	public void aggravateTrigger(Creature c) {
		if (c == Game.player && isAlive()) {
			setHostilityTowardsPlayer(true);
		}
		if (!targetMap.containsKey(c)) {
			TargetPair p = new TargetPair();
			p.creature = c;
			p.weight = findWeight(c);
			targetMap.put(c, p);
			targetQueue.add(p);
		}
		targetMap.get(c).weight += 10;
		if (conditions.contains(Condition.ENRAGEABLE) && !conditions.contains(Condition.ENRAGED) && isAlive()) {
			IO.println(Text.capitalised(Text.getDefName(this)) + " became enraged!");
			conditions.add(Condition.ENRAGED);
			dmg *= 2;
		}
	}

	/**
	 * Executes actions that happen in addition to normal death events like drops/xp/ceasing being alive.
	 */
	public void deathTrigger() {

	}

	//Birds use this
	public void itemStealTrigger() {

	}

	public void refreshArmour() {
		critChance = baseCritChance;
		critDmg = baseCritDmg;
		resists = baseResists.clone();
		for (int i = 0; i < Game.DMG_TYPE_COUNT; i++) {
			resists[i] += armourChest.resists[i];
			resists[i] += ring.resists[i];
			resists[i] += cloak.resists[i];
			resists[i] += hat.resists[i];
		}
	}

}