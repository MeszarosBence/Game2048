package bence.game2048;

public class Cell {
	private int value;
	public int getValue() {
		return value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private int x;
	private int y;

	public Cell(int value, int x, int y) {
		this.value = value;
		this.x = x;
		this.y = y;
	}
}
