package creatures;

import item.Item;

import java.util.*;

import main.*;
import util.*;
import util.Text;

public class Creature implements TimeObject{
	public static TreeMap<String, CreatureTemplate> creatures = new TreeMap<String, CreatureTemplate>();

	public Position position;

	public int maxHp;
	public int hp;
	public double movementTime = 0.5;
	public boolean canBeBoss = true;
	public int bossType = 0;
	public double critDmg, baseCritDmg = 1.25;
	public double critChance, baseCritChance = 0;
	public int xp = 0; // xp to be dropped not xp accumulated
	public int resists[] = new int[DamageType.damageTypeCount()];
	public int baseResists[] = new int[DamageType.damageTypeCount()];
	public double nextTriggerTime = 0;
	public SkillSet skills = new SkillSet(this);

	public String name;
	public String articleIndef = "a";
	public String articleDef = "the";
	public String description = "";

	public Set<Condition> conditions = new HashSet<>();
	public Set<Tag> tags = new HashSet<>();

	public double courage = 100000; // this needs fixing
	public Zone zone;

	public AttackPattern naturalAttackPattern;

	public Creature target = null;
	public List<Tag> targetTags = new ArrayList<>();
	public List<Integer> targetWeights = new ArrayList<>();
	static Comparator<TargetPair> targetComparator = new TargetComparator();
	public PriorityQueue<TargetPair> targetQueue = new PriorityQueue<>(targetComparator);
	public Map<Creature, TargetPair> targetMap = new HashMap<>();

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

	private boolean damageDirty = true;
	private DamageInstance calculatedDamage = null;

	public Creature() {
		armourChest = Item.item("unarmoured");
		ring = Item.item("unarmouredCloak");
		cloak = Item.item("unarmouredRing");
		hat = Item.item("unarmouredHat");
		refreshArmour();
		naturalAttackPattern = null;
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
		c.courage = this.courage;
		c.canBeBoss = this.canBeBoss;

		c.baseCritChance = this.baseCritChance;
		c.baseResists = this.baseResists.clone();
		c.maxHp = this.maxHp;
		c.hp = this.hp;
		c.xp = this.xp;
		c.movementTime = this.movementTime;

		//Don't think these need cloning
		c.equipped = this.equipped;
		c.armourChest = this.armourChest;
		c.ring = this.ring;
		c.cloak = this.cloak;
		c.hat = this.hat;

		c.inv.addAll(this.inv);

		c.naturalAttackPattern = this.naturalAttackPattern.clone();


		c.butcherInv.addAll(this.butcherInv);
		c.butcherSuccess = (HashMap<Item, Double>) this.butcherSuccess.clone();
		c.shopInv = (ArrayList<Item>) this.shopInv.clone();
		c.prices = (HashMap<String, Integer>) this.prices.clone();

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
		overwriteStats();
	}

	public boolean hasTag(String tag){
		return hasTag(Tag.tag(tag));
	}

	public boolean hasTag(Tag tag){
		return tags.contains(tag);
	}

	public void addTag(String tag){
		addTag(Tag.tag(tag));
	}

	public void addTag(Tag tag){
		tags.add(tag);
	}

	public boolean removeTag(String tag){
		return removeTag(Tag.tag(tag));
	}

	public boolean removeTag(Tag tag){
		if(hasTag(tag)) {
			tags.remove(tag);
			return true;
		}
		return false;
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
		if(conditions.contains(Condition.SEALED)) {
			result = "sealed " + result;
		}
		return result;
	}


	private static final int NUM_EFFECTS = 16;
	/**
	 * Rolls for whether this {@code Creature} is a boss, then rolls for boss type.
	 * Takes into account the {@code canBeBoss} parameter of this {@code Creature}.
	 */
	public void doBossType() {
		if (Math.random() < 0.1 && this.canBeBoss) {
			bossType = (int) (Math.random() * NUM_EFFECTS) + 1;
			switch (bossType) {
				case 1:
					name = "Devastating " + name;
					baseCritChance += 0.5;
					critChance += 0.5;
					break;
				case 2:
					name = "Resilient " + name; //Maybe have these scale with something?
					baseResists[DamageType.BLUNT.number] += 5; //5 is VERY strong early but near useless late
					baseResists[DamageType.SLASH.number] += 5;
					baseResists[DamageType.PIERCE.number] += 5;
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
					baseResists[DamageType.BLUNT.number] -= 5;
					baseResists[DamageType.SLASH.number] -= 5;
					baseResists[DamageType.PIERCE.number] -= 5;
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
					naturalAttackPattern.baseDamage.amount *= 1.5;
					break;
				case 8:
					name = "Rich " + name;
					addItem("money", (int) (Math.random() * 30) + 20);
					break;
				case 9:
					name = "Aggressive " + name; //TODO stop hitting yourself
					addTarget("rodent", 120);
					addTarget("slime", 120);
					addTarget("humanoid", 120);
					addTarget("fiend", 120);
					addTarget("bird", 120);
					addTarget("spider", 120);
					addTarget("walrus", 120);
					break;
				case 10:
					name = "Easily Infuriated " + name;
					conditions.add(Condition.ENRAGEABLE);
					break;
				case 11:
					name = "Weak " + name;
					naturalAttackPattern.baseDamage.amount *= 1.5;
					break;
				case 12:
					name = "Mystical " + name;
					int d = Math.max(naturalAttackPattern.baseDamage.amount / 4, 1);
					naturalAttackPattern.onHits.add(new DamageOnHit(new DamageInstance(d, DamageType.MAGIC)));
					break;
				case 13:
					name = "Vampiric " + name;
					naturalAttackPattern.onHits.add(new SelfHealOnHit(1, 0.5));
					break;
				case 14:
					name = "Speedy " + name;
					movementTime *= 0.75;
					break;
				case 15:
					name = "Sluggish " + name;
					movementTime *= 1.5;
					break;
				case NUM_EFFECTS:
					conditions.add(Condition.SLEEPING);
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
		if (!isAlive()) {
			return 10000;
		}
		if (conditions.contains(Condition.SLEEPING)) {
			return 0.2;
		}
		passiveAction();
		if (isAbsconding()) {
			abscond();
			return 10000;//TODO remove from queue properly
		} else {
			while (!targetQueue.isEmpty()) {
				TargetPair p = targetQueue.peek();
				target = p.creature;
				if (target.isAlive()) {
					if (p.weight > 0) {
						AttackPattern ap = decideAttackPattern();
						if(Game.zone.distanceTo(this, p.creature) <= ap.reach) {
							return AttackHandler.attack(this, target, ap);
						} else {
							return move();
						}
					} else {
						break;
					}
				} else {
					targetQueue.remove(targetQueue.peek());
				}
			}
			restingAction();
			return moveIdle();
		}
	}

	private double move() {
		Creature c = targetQueue.peek().creature;
		double currDist = position.distSquared(c.position);
		int x[] = {1, -1, 0, 0};
		int y[] = {0, 0, 1, -1};
		int move = (int)(Math.random() * 4);
		for(int i = 0; i < 4; i++) {
			int j = (move + i) % 4;
			Position p = new Position(x[j], y[j]).add(position);
			if (Game.zone.inBounds(p) && p.distSquared(c.position) < currDist && Game.zone.getTile(p).creature == null) {
				Game.zone.moveCreature(this, p);
				return movementTime;
			}
		}
		return Math.min(movementTime, 0.2);
	}

	private double moveIdle() {
		int x[] = {1, -1, 0, 0};
		int y[] = {0, 0, 1, -1};
		int move = (int)(Math.random() * 4);
		for(int i = 0; i < 4; i++) {
			int j = (move + i) % 4;
			Position p = new Position(x[j], y[j]).add(position);
			if (Game.zone.inBounds(p) && Game.zone.getTile(p).creature == null) {
				Game.zone.moveCreature(this, p);
				return movementTime;
			}
		}
		return Math.min(movementTime, 0.2);
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


	public AttackPattern decideAttackPattern() { //TODO make this intelligent
		if(equipped.id.equals(Item.unarmed.id)){
			return naturalAttackPattern;
		}
		return AttackPattern.weaponAttack;
	}

	/**
	 * Executes this {@code Creature}'s action taken for turn when neither attacking nor absconding.
	 */
	public void restingAction() {

	}

	/**
	 * Taken at the start of every turn when alive.
	 */
	public void passiveAction() { // TODO put this separately on the queue so it isn't dependant on swing speed

	}

	/**
	 * Causes this {@code Creature} to leave the zone.
	 */
	public void abscond() { // TODO maybe this should remove creature from targets? Testing required
		IO.println(Text.getDefName(this) + " ran away.");
		Game.zone.removeCreature(this);
	}

	public void equip(int index) {
		equip(inv.get(index));
	}

	public void equip(String id) {
		equip(Item.item(id));
	}

	public void equip(Item i) {
		if (i.hasTag("wear_chest")) {
			armourChest = i;
		} else if (i.hasTag("wear_ring")) { // TODO give a menu when an item can be worn in several slots (or in hand)
			ring = i;
		} else if (i.hasTag("wear_cloak")) {
			cloak = i;
		} else if (i.hasTag("wear_hat")) {
			hat = i;
		}else{
			equipped = i;
			markDamageModified();
		}
		refreshArmour();
	}

	public void markDamageModified() {
		damageDirty = true;
	}

	static final int DPS_PER_LEVEL = 2; // TODO Move this somewhere nicer

	/**
	 *  Returns a new DamageInstance containing a Creature's damage, adjusted for skills.
	 *
	 */
	public DamageInstance getDamage() {
		if(damageDirty) { // Avoid recalculating this
			damageDirty = false;
			int weaponDmg = equipped.dmg.amount;
			if (equipped.dmg.type == DamageType.BLUNT) {
				weaponDmg += (skills.bluntLvl - 1) * (equipped.swingTime * DPS_PER_LEVEL);
			} else if(equipped.dmg.type == DamageType.SLASH) {
				weaponDmg += (skills.slashLvl - 1) * (equipped.swingTime * DPS_PER_LEVEL);
			} else if(equipped.dmg.type == DamageType.PIERCE) {
				weaponDmg += (skills.pierceLvl - 1) * (equipped.swingTime * DPS_PER_LEVEL);
			}
			if (equipped.hasTag("polearm")) {
				weaponDmg += (skills.poleLvl - 1) * (equipped.swingTime * DPS_PER_LEVEL);
			}
			if (equipped.hasTag("sword")) {
				weaponDmg += (skills.swordLvl - 1) * (equipped.swingTime * DPS_PER_LEVEL);
			}
			weaponDmg = Math.min(weaponDmg, equipped.dmg.amount * 2); // Skills can at most double damage
			if(weaponDmg > naturalAttackPattern.baseDamage.amount) {
				calculatedDamage = new DamageInstance(weaponDmg, equipped.dmg.type);
			} else {
				calculatedDamage = naturalAttackPattern.baseDamage.copy();
			}

			if(conditions.contains(Condition.ENRAGED)) {
				calculatedDamage.amount *= 2;
			}
		}
		return calculatedDamage.copy();
	}

	//TODO misses
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
			if (c.hasTag(targetTags.get(j))) {
				weight += targetWeights.get(j);
			}
		}
		return weight;
	}

	public void addTarget(String t, int weight) {
		addTarget(Tag.tag(t), weight);
	}

	public void addTarget(Tag t, int weight) {
		targetTags.add(t);
		targetWeights.add(weight);
	}

	public void removeTarget(Tag t) {
		targetTags.remove(t);
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
			equipped = Item.unarmed;
			markDamageModified();
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
		for (int i = 0; i < DamageType.damageTypeCount(); i++) {
			if (resists[i] > 0) {
				IO.println(Text.capitalised(DamageType.get(i).name + " resistance: " + resists[i]));
			} else if (resists[i] < 0) {
				IO.println(Text.capitalised(DamageType.get(i).name + " vulnerability: " + (0 - resists[i])));
			}
		}
		IO.println("");
		if (equipped.name.equals("unarmed")) {
			IO.println("Damage: " + getDamage() + ".");
		} else {
			IO.println("Weapon: " + equipped.getNameWithCount());
			IO.println("Damage: " + getDamage() + ".");
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
	public void damageTrigger(DamageInstance d) {//TODO rewrite courage
		if (conditions.contains(Condition.SLEEPING)) {
			awaken(false);
		}
		int damage = Math.max(d.amount, 0);
		if (damage > maxHp / 4) {//If damage deals over 25% hp
			double c = Math.max((maxHp / damage) - 0.25, 0);//Lose a decimal amount of courage equal to the %hp of damage over 25%
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
		if (conditions.contains(Condition.SEALED)) {
			IO.println(Text.capitalised(Text.getDefName(this)) + " was unsealed.");
			conditions.remove(Condition.SEALED);
		}
		if (conditions.contains(Condition.SLEEPING) && isAlive()) {
			IO.println(Text.capitalised(Text.getDefName(this)) + " woke up.");
			conditions.remove(Condition.SLEEPING);
		}
		if (conditions.contains(Condition.ENRAGEABLE) && !conditions.contains(Condition.ENRAGED) && isAlive()) {
			IO.println(Text.capitalised(Text.getDefName(this)) + " became enraged!");
			conditions.add(Condition.ENRAGED);
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

	@Override
	public double getNextTriggerTime() {
		return nextTriggerTime;
	}

	@Override
	public void setNextTriggerTime(double t) {
		nextTriggerTime = t;
	}

	@Override
	public boolean removeOnZoneSwitch() {
		return true;
	}

	@Override
	public double resolve() {
		return takeTurn();
	}

	// This can probably afford to be slow, but if it needs to be sped up, just modify the one item that changes.
	public void refreshArmour() {
		critChance = baseCritChance;
		critDmg = baseCritDmg;
		resists = baseResists.clone();
		for (int i = 0; i < DamageType.damageTypeCount(); i++) {
			resists[i] += armourChest.resists[i];
			resists[i] += ring.resists[i];
			resists[i] += cloak.resists[i];
			resists[i] += hat.resists[i];
		}
		for (PassiveEffect e : armourChest.wornPassive) {
			e.apply(this);
		}
		for (PassiveEffect e : ring.wornPassive) {
			e.apply(this);
		}
		for (PassiveEffect e : cloak.wornPassive) {
			e.apply(this);
		}
		for (PassiveEffect e : hat.wornPassive) {
			e.apply(this);
		}
	}

}