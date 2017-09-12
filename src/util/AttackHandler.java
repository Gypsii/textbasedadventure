package util;

import main.DamageOnHit;
import main.Game;
import main.OnHit;
import main.Skillset;
import creatures.AttackPattern;
import creatures.Creature;

import java.util.ArrayList;
import java.util.List;

public class AttackHandler {

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
		
		applyDamage(attacker, target, damage, damageType, verb);
		if(crit){IO.println("<cyan>Critical hit!<r>");}
		
		applyOnHits(attacker, target, attackPattern);

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
			if(attacker.equipped.hasTag("polearm")){
				attacker.skills.incrementSkill(Skillset.POLE, 8);
			}
			if(attacker.equipped.hasTag("sword")){
				attacker.skills.incrementSkill(Skillset.SWORD, 8);
			}
		}
		target.aggravateTrigger(attacker);

		return duration;
	}

	public static void applyOnHits(Creature attacker, Creature target, AttackPattern attackPattern){
		List<OnHit> onhits = new ArrayList<>();
		onhits.addAll(attackPattern.onHits);
		if(attackPattern == AttackPattern.weaponAttack){
			onhits.addAll(attacker.equipped.onHits);
		}
		for (OnHit oh : onhits) {
			oh.apply(attacker, target);
		}
	}

	public static void applyDamage(Creature attacker, Creature target, int damage, int damageType, String verb){
		Text.attackMessage(attacker, target, damage, damageType, verb);
		boolean wasAlive = target.isAlive();
		target.hp -= Math.min(target.hp, Math.max(damage, 0));
		Text.healthRemainingMessage(attacker, target, !wasAlive);

		if(target.hp <= 0){
			target.kill();
		}else{
			target.damageTrigger(damage);
		}
	}

	public static void applyDamagePassively(Creature attacker, Creature target, int damage, int damageType, String verb){
		Text.attackMessage(target, damage, damageType, verb);
		boolean wasAlive = target.isAlive();
		target.hp -= Math.min(target.hp, Math.max(damage, 0));
		Text.healthRemainingMessage(null, target, !wasAlive);

		if(target.hp <= 0){
			target.kill();
		}else{
			target.damageTrigger(damage);
		}
	}

	public static void attack(Creature attacker, Creature target){
		attack(attacker, target, AttackPattern.weaponAttack);
	}


}
