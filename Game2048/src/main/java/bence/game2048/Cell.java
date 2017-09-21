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
	
	public Cell(CellValue value, int x, int y) {
		this.value = value;
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

	public int getIntValue() {
		return getValue().getValue();
	}

	@Override
	public String toString() {
		return "[" + getX() + "," + getY() + "]:" + value.toString();
	}
	
	
}
