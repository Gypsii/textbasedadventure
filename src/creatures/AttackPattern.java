package creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import main.DamageOnHit;
import main.OnHit;
import util.AttackHandler;
import util.IO;
import main.Game;

public class AttackPattern {
	
	public static TreeMap<String, AttackPattern> attacks = new TreeMap<String, AttackPattern>();
	
	public int baseDamage = 0;
	public int damageType = Game.DMG_BLUNT;
	public double duration = 1;
	public String verb = "damaged";
	public boolean canCrit = true;
	public List<OnHit> onHits = new ArrayList<>();
	
	public static AttackPattern weaponAttack = new AttackPattern(-1, 0, "", 1);
	{
		attacks.put("weapon", weaponAttack);
	}
	
	
	public static AttackPattern attack(String id){
		if(attacks.containsKey(id)){
			return attacks.get(id);
		}else{
			IO.print("<lred>Key " + id + " not found!<r>");
			return weaponAttack;
		}
	}
	
	public AttackPattern(int damage, int damageType, String verb, double duration){
		this.baseDamage = damage;
		this.damageType = damageType;
		this.verb = verb;
		this.duration = duration;
	}
	
	public AttackPattern(int damage, int damageType, String verb){
		this.baseDamage = damage;
		this.damageType = damageType;
		this.verb = verb;
		this.duration = 1;
	}
	
	public AttackPattern(int damage, int damageType){
		this.baseDamage = damage;
		this.damageType = damageType;
		this.verb = AttackHandler.getVerb(damageType);
		this.duration = 1;
	}
	
	public AttackPattern(int damage, int damageType, double duration){
		this.baseDamage = damage;
		this.damageType = damageType;
		this.verb = AttackHandler.getVerb(damageType);
		this.duration = duration;
	}
	
	public AttackPattern clone(){
		AttackPattern p = new AttackPattern(baseDamage, damageType, verb, duration);
		p.canCrit = this.canCrit;
		return p;
	}
}
