package creatures;

import item.Item;
import main.DamageInstance;
import main.DamageType;
import main.Game;
import main.Tag;

public class Dingo extends Creature{
	public int type;
	public static final int STANDARD = 0;
	public static final int SNOW = 1;
	public static final int HELL = 2;
	
	public Dingo(int type){
		maxHp = 45;
		hp = maxHp;
		xp = 15;
		courage = 5 + (Math.random() * 3);

		conditions.add(Condition.ENRAGEABLE);
		
		addBodyPart("meatDingo", 0.3);
		addBodyPart("meatDingo", 0.3);
		addBodyPart("meatDingo", 0.3);
		if(Math.random() < 0.05){
			addItem("baby");
		}

		naturalAttackPattern = new AttackPattern(new DamageInstance(9, DamageType.PIERCE), "bit", 1);

		if(type == STANDARD){
			name = "Dingo";
			addBodyPart(Item.item("hideDingo"), 0.5);
		}else if(type == SNOW){
			name = "Snow Dingo";
			addBodyPart(Item.item("hideDingo"), 0.5);
		}else if(type == HELL){
			name = "Hellish Dingo";
			naturalAttackPattern.baseDamage.amount *= 2;
			addBodyPart(Item.item("hideHellDingo"), 0.5);
		}
		addTarget(Tag.tag("rodent"), 120);
		addTarget(Tag.tag("player"), 100);
		
		postInitialisation();
	}
	
	
//	public double decideAttackPattern(){
//		if(Game.zone.findItemLoc(Item.item("baby")) == -1){
//			if(enraged){
//				attack(1, "bit");
//				return 1;
//			}else{
//				attack(1, "nipped");
//				return 1;
//			}
//		}else{
//			int babyLoc = Game.zone.findItemLoc(Item.item("baby"));
//			addItem(Game.zone.items.get(babyLoc));
//			Game.zone.items.remove(babyLoc);
//			IO.println(Text.getDefName(this) + " stole the Baby!");
//			return 0.5;
//		}
//	}
	

	public void deathTrigger(){
		for(int i = 0; i < Game.zone.creatures.size(); i++){
			if(Game.zone.creatures.get(i).getClass() == this.getClass()){
				Game.zone.creatures.get(i).courage -= 1.5;
			}
		}
	}
	
	
	public String getDescription(){
		if(type == STANDARD){
			return "Filthy dog creature.";
		}else if(type == SNOW){
			 return "Dingo: Snow edition.";
		}else if(type == HELL){
			return "Dingo: Hell edition.";
		}
		return "Dingo";
	}
}
