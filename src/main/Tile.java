package main;

import creatures.Creature;
import item.Item;
import util.Position;

public class Tile {

	public Position pos;

	public Item item = null;
	public Creature creature = null;
	public TileType tileType = TileType.GRASS;

	public double onFireUntil = -1;

	public Tile(Position p) {
		pos = p;
	}

	//String for formatting purposes, but only return two chars
	public String getSymbol() {
		String primary, secondary;
		if (onFireUntil > Game.getTime()) {
			secondary = "<orange>%<r>";
		} else if (tileType == TileType.WATER) {
			secondary = "<blue>~<r>";
		} else if (tileType == TileType.WALL) {
			secondary =  "<bold>#<-bold>";
		} else {
			if (Game.level.temp >= 115) {
				secondary =  "<lgreen>.<r>";
			} else if (Game.level.temp >= 95) {
				secondary =  "<green>.<r>";
			} else {
				secondary = ".";
			}
		}

		if (creature != null) {
			if (creature == Game.player) {
				primary = "<blue>@<r>";
			} else {
				primary = creature.name.toUpperCase().substring(0,1);
				if(primary.equals("<")) {
					primary = "!";
				}
				if (!creature.isAlive()) {
					primary = "<red>" + primary + "<r>";
				} else if (creature.hostileTowards(Game.player)) {
					primary = "<lred>" + primary + "<r>";
				}
			}
		} else if (item != null) {
			primary = item.name.toLowerCase().substring(0,1);
			if (item.enchantLvl > 0) {
				if(primary.equals("<")) {
					primary = "!";
				}
				primary = "<purple>" + primary + "<r>";
			} else if (item.id == "money") {
				primary = "<yellow>$<r>";
			}
		} else {
			primary = secondary;
		}
		return primary + secondary;
	}

}
