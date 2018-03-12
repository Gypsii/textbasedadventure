package main;

import creatures.Creature;
import item.Item;
import util.Position;

public class Tile {

	public Position pos;

	public Item item = null;
	public Creature creature = null;
	public TileType tileType = TileType.GRASS;

	public Tile(Position p) {
		pos = p;
	}

	//String for formatting purposes, but only return one char
	public String getSymbol() {

		if (creature != null) {
			if (creature == Game.player) {
				return "<blue>@<r>";
			}
			String s = creature.name.toUpperCase().substring(0,1);
			if(s.equals("<")) {
				s = "!";
			}
			if (!creature.isAlive()) {
				s = "<red>" + s + "<r>";
			} else if (creature.hostileTowards(Game.player)) {
				s = "<lred>" + s + "<r>";
			}
			return s;
		} else if (item != null) {
			String s = item.name.toLowerCase().substring(0,1);
			if (item.enchantLvl > 0) {
				if(s.equals("<")) {
					s = "!";
				}
				s = "<purple>" + s + "<r>";
			} else if (item.id == "money") {
				s = "<yellow>$<r>";
			}
			return s;
		}
		if (tileType == TileType.WATER) {
			return "<blue>~<r>";
		}
		if (tileType == TileType.WALL) {
			return "<bold>#<-bold>";
		}
		if (Game.level.temp >= 115) {
			return "<lgreen>.<r>";
		} else if (Game.level.temp >= 95) {
			return "<green>.<r>";
		}
		return ".";
	}

}
