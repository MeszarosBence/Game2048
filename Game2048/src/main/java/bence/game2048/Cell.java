package bence.game2048;

public class Cell {
	private CellValue value;
	private int x;
	private int y;
	
	public Cell(int value, int x, int y) {
		this.value = new CellValue(value, true);
		this.x = x;
		this.y = y;
	}
	
	public CellValue getValue() {
		return value;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
