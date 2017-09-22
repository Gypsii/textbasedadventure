package main;

import util.IO;
import creatures.Creature;


public class Skillset{
	
	public static final int BLUNT = 0;
	public static final int SLASH = 2;
	public static final int PIERCE = 1;
	public static final int SWORD = 3;
	public static final int POLE = 4;
	public static final int COOK = 5;
	
	public Creature creature;//the creature who has these skills

	public int bluntLvl = 1;
	double bluntXp = 0;
	
	public int slashLvl = 1;
	double slashXp = 0;
	
	public int pierceLvl = 1;
	double pierceXp = 0;
	
	public int swordLvl = 1;
	double swordXp = 0;
	
	public int poleLvl = 1;
	double poleXp = 0;
	
	public int cookLvl = 1;
	double cookXp = 0;
	
	public Skillset(Creature c){
		creature = c;
	}
	
	/**
	 * Adds {@code amount} xp to the specified skill, incrementing the level if necessary. Should level be
	 * incremented, all statistics that change with level will be updated. 
	 * @param skill the id of the skill to be incremented
	 * @param amount the amount of xp to increment by
	 */
	public void incrementSkill(int skill, double amount){
		switch(skill){
		case BLUNT:
			bluntXp += amount;
			while(bluntXp > xpForLevel(bluntLvl)){
				bluntXp -=  xpForLevel(bluntLvl);
				bluntLvl++;
				if(creature == Game.player){
					IO.println("<purple>You have reached level " + bluntLvl + " with blunt weapons<r>");
				}
				creature.markDamageModified();
			}
			break;
		case SLASH:
			slashXp += amount;
			while(slashXp > xpForLevel(slashLvl)){
				slashXp -=  xpForLevel(slashLvl);
				slashLvl++;
				if(creature == Game.player){
					IO.println("<purple>You have reached level " + slashLvl + " with slashing weapons<r>");
				}
				creature.markDamageModified();
			}
			break;
		case PIERCE:
			pierceXp += amount;
			while(pierceXp > xpForLevel(pierceLvl)){
				pierceXp -=  xpForLevel(pierceLvl);
				pierceLvl++;
				if(creature == Game.player){
					IO.println("<purple>You have reached level " + pierceLvl + " with piercing weapons<r>");		
				}
				creature.markDamageModified();
			}
			break;
		case POLE:
			poleXp += amount;
			while(poleXp > xpForLevel(poleLvl)){
				poleXp -=  xpForLevel(poleLvl);
				poleLvl++;
				if(creature == Game.player){
					IO.println("<purple>You have reached level " + poleLvl + " with polearms<r>");		
				}
				creature.markDamageModified();
			}
			break;		
		case SWORD:
			swordXp += amount;
			while(swordXp > xpForLevel(swordLvl)){
				swordXp -=  xpForLevel(swordLvl);
				swordLvl++;
				if(creature == Game.player){
					IO.println("<purple>You have reached level " + swordLvl + " with swords<r>");		
				}
				creature.markDamageModified();
			}
			break;
		case COOK:
			cookXp += amount;
			while(cookXp > xpForLevel(cookLvl)){
				cookXp -=  xpForLevel(cookLvl);
				cookLvl++;
				if(creature == Game.player){
					IO.println("<purple>You have reached cooking level " + cookLvl + "<r>");		
				}
			}
			break;
		}
	}
	
	double xpForLevel(int level){
		return Math.pow(level, 1.2) * 50;//TODO skill level
	}
	
}
