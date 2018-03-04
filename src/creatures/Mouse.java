package creatures;

import main.DamageInstance;
import main.DamageType;
import main.Game;
import util.IO;
import util.Text;


public class Mouse extends Creature{

	public Mouse(){
		name = "Field Mouse";
		maxHp = 9;
		hp = maxHp;
		xp = 2;
		setHostilityTowardsPlayer(false);
		canBeBoss = false;
		courage = (Math.random() * 2);
		addTag("rodent");

		naturalAttackPattern = new AttackPattern(new DamageInstance(2, DamageType.SLASH), "scratched", 0.6);
		
		postInitialisation();
	}

	
	public void restingAction(){
		if(Math.random() < 0.1){
			IO.println(Text.capitalised(Text.getDefName(this)) + " squeaked.");
		}
	}
	
	public void damageSubtrigger(){
		courage -= Math.random();
	}
	
	public String getDescription(){
		return "A tiny brown mouse";
	}
}
