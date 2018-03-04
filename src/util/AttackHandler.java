package util;

import main.*;
import creatures.AttackPattern;
import creatures.Creature;

import java.util.ArrayList;
import java.util.List;

public class AttackHandler {

	public static double attack(Creature attacker, Creature target, AttackPattern attackPattern){
		DamageInstance damage;
		double period;
		String verb;

		if(attackPattern == AttackPattern.weaponAttack){
			damage = attacker.getDamage();
			period = attacker.equipped.swingTime;
			verb = damage.type.verb;
		}else{
			damage = attackPattern.baseDamage.copy();
			period = attackPattern.period;
			verb = attackPattern.verb;
		}
		
		boolean crit = false;
		if (attackPattern.canCrit && Math.random() < attacker.critChance) {
			damage.amount *= attacker.critDmg;
			crit = true;
		}

		damage.amount -= target.resists[damage.type.number];
		
		applyDamage(attacker, target, damage, verb);
		if (crit){IO.println("<cyan>Critical hit!<r>");}
		
		applyOnHits(attacker, target, attackPattern);

		if (attacker == Game.player) {
			if (damage.type == DamageType.BLUNT) {
				attacker.skills.incrementSkill(SkillSet.BLUNT, 8);
			} else if (damage.type == DamageType.SLASH) {
				attacker.skills.incrementSkill(SkillSet.SLASH, 8);
			} else if (damage.type == DamageType.PIERCE) {
				attacker.skills.incrementSkill(SkillSet.PIERCE, 8);
			}
			if (attacker.equipped.hasTag("polearm")) {
				attacker.skills.incrementSkill(SkillSet.POLE, 8);
			}
			if (attacker.equipped.hasTag("sword")) {
				attacker.skills.incrementSkill(SkillSet.SWORD, 8);
			}
		}
		target.aggravateTrigger(attacker);

		return period;
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

	public static void applyDamage(Creature attacker, Creature target, DamageInstance damage, String verb){
		Text.attackMessage(attacker, target, damage, verb);
		boolean wasAlive = target.isAlive();
		target.hp -= Math.min(target.hp, Math.max(damage.amount, 0));
		Text.healthRemainingMessage(attacker, target, !wasAlive);

		if(target.hp <= 0){
			target.kill();
		}else{
			target.damageTrigger(damage);
		}
	}

	public static void applyDamagePassively(Creature attacker, Creature target, DamageInstance damage, String verb){
		if (verb == null) {
			verb = damage.type.verbPassive;
		}
		Text.attackMessage(target, damage, verb);
		boolean wasAlive = target.isAlive();
		target.hp -= Math.min(target.hp, Math.max(damage.amount, 0));
		Text.healthRemainingMessage(null, target, !wasAlive);

		if(target.hp <= 0){
			target.kill();
		}else{
			target.damageTrigger(damage);
		}
	}

	public static void selfHeal(Creature c, int health) {
		selfHeal(c, health, "healed");
	}

	public static void selfHeal(Creature c, int health, String verb){
		Text.selfHealMessage(c, health, verb);
		c.hp += health;
		c.hp = Math.min(c.maxHp, c.hp);
		Text.healthRemainingMessage(null, c, false);
	}

	public static void attack(Creature attacker, Creature target){
		attack(attacker, target, AttackPattern.weaponAttack);
	}


}
