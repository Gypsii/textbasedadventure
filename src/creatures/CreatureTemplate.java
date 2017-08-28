package creatures;

import item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import main.Game;
import main.Zone;
import util.ItemGroup;

public class CreatureTemplate {
	
	public int maxHp;
	public boolean canBeBoss = true;
	public int bossType = 0;
	public double baseCritDmg = 1.25;
	public double baseCritChance = 0;
	public int xp = 0;//xp to be dropped not xp accumulated
	public int baseResists[] = new int[Game.DMG_TYPE_COUNT];
	public int baseDmg;
	
	public String name;
	public String articleIndef = "a";
	public String articleDef = "the";
	public String description = "";
	public String id;

	public Set<Condition> conditions = new HashSet<>();
	public boolean hostile = true;
	public double courage = 100000;//this needs fixing
	public Zone zone;
	
	public String defaultAttackPatternId = "weapon";

	public ArrayList<CreatureTag> tags = new ArrayList<CreatureTag>();
	public ArrayList<CreatureTag> targetTags = new ArrayList<CreatureTag>();
	public ArrayList<Integer> targetWeights = new ArrayList<Integer>();
	
	public ArrayList<String> inv = new ArrayList<String>();
	public ArrayList<Double> itemChance = new ArrayList<Double>();
	public ArrayList<ItemGroup> randomInv = new ArrayList<ItemGroup>();
	public ArrayList<Double> randomItemChance = new ArrayList<Double>();

	public ArrayList<String> butcherInv = new ArrayList<String>();
	public HashMap<String, Double> butcherSuccess = new HashMap<String, Double>();
	public Item equipped = Item.unarmed.clone();

	public ArrayList<Item> shopInv = new ArrayList<Item>();
	public HashMap<String, Integer> prices = new HashMap<String, Integer>();

	@SuppressWarnings("unchecked")
	public Creature creature(){
		Creature c = new Creature();
		c.name = this.name;
		c.articleDef = this.articleDef;
		c.articleIndef = this.articleIndef;
		c.description = this.description;

		c.conditions.addAll(this.conditions);
		c.courage = this.courage;
		c.canBeBoss = this.canBeBoss;
		
		c.baseCritChance = this.baseCritChance;
		c.baseDmg = this.baseDmg;
		c.baseResists = this.baseResists.clone();
		c.maxHp = this.maxHp;
		c.xp = this.xp;
		
		//Don't think these need cloning
		c.equipped = this.equipped;
		
		for(int i = 0; i < inv.size(); i++){
			if(Math.random() < itemChance.get(i)){
				c.addItem(inv.get(i));
			}
		}
		for(int i = 0; i < randomInv.size(); i++){
			if(Math.random() < randomItemChance.get(i)){
				c.addItem(randomInv.get(i).getRandom());
			}
		}
		
		
		c.defaultAttackPatternId = this.defaultAttackPatternId;
		c.defaultAttackPattern = AttackPattern.attack(defaultAttackPatternId);
		
		for(int i = 0; i < butcherInv.size(); i++){
			String id = butcherInv.get(i);
			c.addBodyPart(Item.item(id), butcherSuccess.get(id));
		}
		c.shopInv = (ArrayList<Item>) this.shopInv.clone();
		c.prices = (HashMap<String, Integer>) this.prices.clone();
		
		
		c.setHostilityTowardsPlayer(hostile);
		c.hp = c.maxHp;
		c.dmg = c.calculateDamage();
		c.refreshArmour();			
		return c;
	}
	
}
