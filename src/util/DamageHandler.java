package util;

import main.Game;
import main.OnHit;
import main.Skillset;
import creatures.AttackPattern;
import creatures.Creature;

public class DamageHandler {

	public static String getVerb(int damageType){
		switch(damageType){
		case Game.DMG_BLUNT:
			return "hit";
		case Game.DMG_SLASH:
			return "slashed";
		case Game.DMG_PIERCE:
			return "pierced";
		case Game.DMG_BURN:
			return "burnt";
		case Game.DMG_COLD:
			return "froze";
		default:
			return "damaged";
		}
	}
	
	public static double attack(Creature attacker, Creature target, AttackPattern attackPattern){
		target.checkBuffs(attacker.nextActionTime);
		int damage = 0;
		int damageType = 0;
		double duration = 1;
		String verb = "";
		
		if(attackPattern == AttackPattern.weaponAttack){
			damage = attacker.dmg;
			damageType = attacker.equipped.dmgType;
			duration = attacker.equipped.swingTime;
			verb = getVerb(damageType);
		}else{
			damage = attackPattern.baseDamage;
			damageType = attackPattern.damageType;
			duration = attackPattern.duration;
			verb = attackPattern.verb;
		}
		
		boolean crit = false;
		if(attackPattern.canCrit && Math.random() < attacker.critChance){
			damage *= attacker.critDmg;
			crit = true;
		}
		damage -= target.resists[damageType];
		
		Text.attackMessage(attacker, target, damage, damageType, verb);
		boolean wasAlive = target.isAlive();
		target.hp -= Math.min(target.hp, Math.max(damage, 0));	
		Text.healthRemainingMessage(attacker, target, !wasAlive);
		if(crit){IO.println("<cyan>Critical hit!<r>");}
		
		if(target.hp <= 0){
			target.kill();
		}else{
			target.damageTrigger(damage);
		}
		
		if(attacker == Game.player){
			switch(attacker.equipped.dmgType){
			case Game.DMG_BLUNT:
				attacker.skills.incrementSkill(Skillset.BLUNT, 8);
				break;
			case Game.DMG_SLASH:
				attacker.skills.incrementSkill(Skillset.SLASH, 8);
				break;
			case Game.DMG_PIERCE:
				attacker.skills.incrementSkill(Skillset.PIERCE, 8);
				break;
			}
			if(attacker.equipped.isPolearm){
				attacker.skills.incrementSkill(Skillset.POLE, 8);
			}
			if(attacker.equipped.isSword){
				attacker.skills.incrementSkill(Skillset.SWORD, 8);
			}
		}
		target.aggravateTrigger(attacker);
		
		//Game.player.target = c;
		for(OnHit o : attacker.equipped.effects){
			attacker.applyOnHit(o);
		}
		for(OnHit o : attacker.armourChest.effects){
			attacker.applyOnHit(o);
		}
		for(OnHit o : attacker.ring.effects){
			attacker.applyOnHit(o);
		}
		for(OnHit o : attacker.hat.effects){
			attacker.applyOnHit(o);
		}
		for(OnHit o : attacker.cloak.effects){
			attacker.applyOnHit(o);
		}
		return duration;
	}
	
	public static void attack(Creature attacker, Creature target){
		attack(attacker, target, AttackPattern.weaponAttack);
	}
	
}
