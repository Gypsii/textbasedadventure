package creatures;

import item.Item;
import main.DamageInstance;
import main.DamageType;
import main.Game;
import util.IO;
import util.Text;

public class Bird extends Creature{
	public static final int SNOW = 0;
	public static final int STANDARD = 1;
	public static final int TROPICAL = 2;
	public static final int CROW = 3;

	public int type;
	
	public Bird(int t){
		name = "UNDEFINED BIRD";
		type = t;
		addTag("bird");
		maxHp = 15;
		hp = maxHp;
		xp = 3;
		setHostilityTowardsPlayer(false);
		courage = 1 + Math.random();//between 1 and 2 (note that every attack the bird loses 0-1 courage due to subtrigger)
		
		naturalAttackPattern = new AttackPattern(new DamageInstance(4, DamageType.PIERCE), "pecked", 1);
		
		String featherType = "feather";
		if(type == SNOW){
			name = "Snow Bird";
			featherType = "featherSnow";
		}else if(type == STANDARD){
			name = "Bird";
			featherType = "feather";
		}else if(type == TROPICAL){
			name = "Toucan";
			featherType = "featherWarm";
		}else if(type == CROW){
			name = "Crow";
			featherType = "featherCrow";
		}
		addBodyPart(Item.item(featherType), 0.6);
		addBodyPart(Item.item(featherType), 0.6);
		addBodyPart("meatBird", 0.4);
		postInitialisation();
	}
	
	boolean isAbsconding(){
		return (courage <= 0 && !Game.zone.containsEgg) || courage <= -4;
	}
	
	public void restingAction(){
		if(Math.random() < 0.3){
			IO.println("The " + name + " squawked");
		}
	}
	
	public void abscond(){
		IO.println("The " + name + " flew away");
		Game.zone.creatures.remove(this);	
	}
	
	public void itemStealTrigger(){
		courage += 3;
		targetMap.get(Game.player).weight += 150;
//		this.
		if(!conditions.contains(Condition.ENRAGED)){
			conditions.add(Condition.ENRAGED);
			IO.println(Text.capitalised(Text.getDefName(this)) + " became enraged!");
		}
	}
	
	public void damageSubtrigger(){
		courage -= Math.random();
	}
	
	public String getDescription(){
		if(type == SNOW){
			return "A small white bird, protective of its young. Lives in cold climates.";
		}else if(type == STANDARD){
			return "A small brown bird, protective of its young. Lives in temperate climates.";
		}else if(type == TROPICAL){
			return "A majestic colourful bird, protective of its young. Lives in warm climates.";
		}else if(type == CROW){
			return "Caw caw caw.";
		}
		return    "＜￣｀ヽ、　　　　　　　／￣＞\n"
				+ " ゝ、　　＼　／⌒ヽ,ノ 　/´\n"
				+ "　　　ゝ、　` （ ^_^)／\n"
				+ "　　 　　>　 　 　,ノ\n"
				+ "　　　　　∠_,,,/´”\n"; 
	}
}
