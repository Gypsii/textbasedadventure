package item;


public class Scroll extends Item{
	
	public Scroll(String n){
		name = n;
	}
	
	public Scroll(String n, String pref){
		name = n;
		prefix = pref;
	}
	
	public Scroll clone(){
		Scroll i = new Scroll(this.name, this.prefix);
		i.count = this.count;
		i.effects = this.effects;
		return i;
	}
	
	public static Scroll scrollCrit = new Scroll("Scroll of Critical Strikes");
	public static Scroll scrollResist = new Scroll("Scroll of Immunity");
	public static Scroll scrollOnHitDmg = new Scroll("Scroll of Channelling");
	public static Scroll scrollOnHitShred = new Scroll("Scroll of Degrading");
	public static Scroll scrollOnHitResist  = new Scroll("Scroll of Protecting");
	
}
