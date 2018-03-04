package creatures;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import main.*;
import util.AttackHandler;
import util.IO;

public class AttackPattern {
	
	public static TreeMap<String, AttackPattern> attacks = new TreeMap<String, AttackPattern>();
	
	public DamageInstance baseDamage = new DamageInstance(0, DamageType.BLUNT);
	public double period = 1;
	public String verb = "damaged";
	public boolean canCrit = true;
	public List<OnHit> onHits = new ArrayList<>();
	
	public static AttackPattern weaponAttack = new AttackPattern(new DamageInstance(0, DamageType.BLUNT), "", 1);
	static {
		attacks.put("weapon", weaponAttack);
	}
	
	
	public static AttackPattern attack(String id){
		if(attacks.containsKey(id)){
			return attacks.get(id).clone();
		}else{
			IO.print("<lred>Key " + id + " not found!<r>");
			return weaponAttack;
		}
	}
	
	public AttackPattern(DamageInstance damage, String verb, double period){
		this.baseDamage = damage;
		this.verb = verb;
		this.period = period;
	}
	
	public AttackPattern(DamageInstance damage, String verb){
		this(damage, verb, 1);
	}
	
	public AttackPattern(DamageInstance damage){
		this(damage, damage.type.verb, 1);
	}
	
	public AttackPattern(DamageInstance damage, double period){
		this(damage, damage.type.verb, period);
	}
	
	public AttackPattern clone(){
		AttackPattern p = new AttackPattern(baseDamage, verb, period);
		p.canCrit = this.canCrit;
		return p;
	}
}
