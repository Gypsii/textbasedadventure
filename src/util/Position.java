package util;

public class Position {

	public int x;
	public int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Position position = (Position) o;

		if (x != position.x) return false;
		return y == position.y;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	@Override
	public String toString() {
		return "Position{" + "x=" + x + ", y=" + y +'}';
	}

	public Position add(Position p) {
		return new Position(x + p.x, y + p.y);
	}

	public int manhattan(Position p) {
		int x1 = p.x - x;
		int y1 = p.y - y;
		return Math.abs(x1) + Math.abs(y1);
	}

	public double distSquared(Position p) {
		int x1 = p.x - x;
		int y1 = p.y - y;
		return x1*x1 + y1*y1;
	}

}
