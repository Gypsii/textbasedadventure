package creatures.humans;

import item.Item;

import java.util.Random;

import creatures.Creature;


public class Humanoid extends Creature{
	
	public Humanoid(){
		/*
		name = "Human";
		tags.add(CreatureTag.humanoid);
		maxHp = 50;
		hp = maxHp;
		baseDmg = 3;
		xp = 0;		
		courage = 10 + (Math.random() * 5);
		
		hatesPlayer = false;
		targetTags.remove(CreatureTag.player);
		stopsZoneExit = false;	
		
		setSpecifics();
		setArmour();
		setWeapon();
		setTags();
		setTargets();
		overwriteStats();
		*/
	}
	
	public void setSpecifics(){
		Random r = new Random();	
		addItem(Item.item("money"), (int)(Math.random() * Math.abs(r.nextGaussian()) * 25) + 5);
	}
	
	public void setArmour(){

	}
	
	public void setWeapon(){
		Item i = Item.unarmed;
		equip(i);
	}
		
	public void setTargets(){
			
	}
	
}
