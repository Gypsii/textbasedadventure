package creatures;

import item.Item;
import main.Commands;
import main.DamageInstance;
import main.DamageType;
import util.IO;

import java.io.IOException;

public class Player extends Creature{
	public int xp = 0;
    public int lvl = 1;
    
    public Player(){
		addTag("humanoid");
		addTag("human");
		addTag("player");
        maxHp = 100;
        hp = maxHp;
        equipped = Item.unarmed;
        armourChest = new Item();
        armourChest.name = "unarmoured";

		naturalAttackPattern = new AttackPattern(new DamageInstance(5, DamageType.BLUNT), "punched", 1);
    }
    
//	public void equip(int id){
//    	super.equip(id);
//    }
	
	public void equip(Item i){
    	super.equip(i);
		IO.println("<blue>You equip the " + i.getNameWithCount() + "<r>");	
    }
    
    public void abscond(){
    	System.err.println("ERR: Player absconding.");
    }
    
//    public void takeDamage(int d) {
//		hp -= Math.max(d, 0);
//		if(hp <= 0){
//			IO.println("RIP. You were killed.");
//		}
//	}
    
    public void kill(){
		IO.println("RIP. You were killed.");
	}
    
    public void aggravateTrigger(Creature c){

    }

    public double resolve() {
		try {
			return Commands.playerTurn();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean removeOnZoneSwitch() {
    	return false;
	}
    
    public void giveXp(int x) {
		xp += x;
		IO.println("You gained " + x + " XP (" + xp + "/" + (int)(Math.pow(lvl, 2) + 19) + ")");
		while(xp >= Math.pow(lvl, 2) + 19){
			xp -=  Math.pow(lvl, 2) + 19;
			lvl ++;
			maxHp += (lvl + 3) * 2;
			hp += (lvl + 3) * 2;
			IO.println("<purple>You have reached level " + lvl + "<r>");
			IO.println("Your max hp is now " + maxHp);
		}
	}

}
