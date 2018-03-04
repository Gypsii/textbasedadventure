package main;

public class DamageInstance {

	public int amount;
	public DamageType type;

	public DamageInstance(int amount, DamageType type) {
		this.amount = amount;
		this.type = type;
	}

	public DamageInstance copy() {
		return new DamageInstance(amount, type);
	}

	public String toString() {
		return amount + " " + type.name + " damage";
	}
}
