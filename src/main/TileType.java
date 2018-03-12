package main;

public enum TileType {

	GRASS (true),
	WALL (false),
	WATER (true),
	TREE (true);

	private boolean canEnter;

	TileType (boolean canEnter) {
		this.canEnter = canEnter;
	}

	public boolean hasSpace() {
		return canEnter;
	}

}
