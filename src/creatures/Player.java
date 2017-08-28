package creatures;

import item.Item;
import util.IO;

public class Player extends Creature{
	public int xp = 0;
    public int lvl = 1;
    
    public Player(){
		tags.add(CreatureTag.humanoid);
		tags.add(CreatureTag.human);
		tags.add(CreatureTag.player);
    	dmg = 5;
    	baseDmg = 5;
        maxHp = 100;
        hp = maxHp;
        equipped = Item.unarmed;
        armourChest = new Item();
        armourChest.name = "unarmoured";
    }
    
	public void equip(int id){
    	super.equip(id);
    }
	
	public void equip(Item i){
    	super.equip(i);
		IO.println("<blue>You equip the " + i.getNameWithCount() + "<r>");	
    }
    
    public void abscond(){
    	System.err.println("ERR: Player absconding. This should not happen. Yell at dev");
    }
    
    public void takeDamage(int d) {
		hp -= Math.max(d, 0);
		if(hp <= 0){
			IO.println("RIP. You were killed.");
		}
	}
    
    public void kill(){
		IO.println("RIP. You were killed.");
	}
    
    public void aggravateTrigger(Creature c){

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
