package bence.game2048;

import java.util.concurrent.ThreadLocalRandom;

import bence.game2048.Game2048.DIR;

public class Table {

	public static final int TABLE_SIZE = 4;
	CellValue[][] table = new CellValue[TABLE_SIZE][TABLE_SIZE];
	private int moves = 0;
	Cell newCell;
	
	public Table() {
  		reset();
	}
	
	public void moveLeft() {
		moveForward(DIR.RIGHT);
	}

	public void moveUp() {
		moveForward(DIR.DOWN);
	}
	
	public void moveRight() {
		moveBackward(DIR.LEFT);
	}

	public void moveDown() {
		moveBackward(DIR.UP);
	}
	
	public void putANewItem() {
		Cell cell = getNextCell();
		table[cell.getY()][cell.getX()] = cell.getValue();
		this.newCell = cell;
		moves = 0;
	}
	
	private Cell getNextCell() {
		Cell randomCell = getRandomCell();

		if (isEmptyCell(randomCell))
			return randomCell;
		else {
			int row = randomCell.getY();
			int column = randomCell.getX();
			int maxTries = TABLE_SIZE * TABLE_SIZE + 1;
			for (int tries = 0; tries < maxTries; tries++) {
				while (row < TABLE_SIZE && tries < maxTries) {
					while (column < TABLE_SIZE && tries < maxTries) {
						if (table[row][column].getValue() == 0)
							return new Cell(2, column, row);
						column++;
					}
					row++;
					column = 0;
				}
				row = 0;
			}
		}

		return null;
	}
	
	private boolean isEmptyCell(Cell randomCell) {
		return table[randomCell.getY()][randomCell.getX()].getValue() == 0;
	}
	
	
	Cell getRandomCell() {
		int[] cells = {2, 4};
		int value = ThreadLocalRandom.current().nextInt(cells.length);
		
		Cell randomCell = new Cell(cells[value], ThreadLocalRandom.current().nextInt(TABLE_SIZE),
				ThreadLocalRandom.current().nextInt(TABLE_SIZE));
		return randomCell;
	}
	
	public int getSize() {
		return TABLE_SIZE;
	}
	
	void addNext(int i, int j, DIR direction) {
		if (direction == DIR.DOWN) {
			table[j][i] = new CellValue(table[j][i].getValue() + table[j + 1][i].getValue());
			table[j + 1][i] = new CellValue(0);
			if (table[j][i].getValue() != table[j + 1][i].getValue()) moves++;
		}

		if (direction == DIR.UP) {
			table[j][i] = new CellValue(table[j][i].getValue() + table[j - 1][i].getValue());
			table[j - 1][i] = new CellValue(0);
			if (table[j][i].getValue() != table[j - 1][i].getValue()) moves++;
		}

		if (direction == DIR.RIGHT) {
			table[j][i] = new CellValue(table[j][i].getValue() + table[j][i + 1].getValue());
			table[j][i + 1] = new CellValue(0);
			if (table[j][i].getValue() != table[j][i + 1].getValue()) moves++;
		}

		if (direction == DIR.LEFT) {
			if (table[j][i].getValue() != table[j][i - 1].getValue()) moves++;
			table[j][i] = new CellValue(table[j][i].getValue() + table[j][i - 1].getValue());
			table[j][i - 1] = new CellValue(0);
		}
	}
	
	boolean equalsNext(int column, int row, DIR direction) {
		if (direction == DIR.DOWN)
			return table[row][column].getValue() == table[row + 1][column].getValue();
		if (direction == DIR.UP)
			return table[row][column].getValue() == table[row - 1][column].getValue();
		if (direction == DIR.RIGHT)
			return table[row][column].getValue() == table[row][column + 1].getValue();
		if (direction == DIR.LEFT)
			return table[row][column].getValue() == table[row][column - 1].getValue();
		return false;
	}
	
	boolean isEmptyCell(int column, int row) {
		return table[row][column].getValue() == 0;
	}
	
	boolean wereCellsMoved() {
		return (moves > 0);
	}

	public CellValue[] getRow(int row) {
		return table[row];
	}

	public void setCell(Cell cell) {
		table[cell.getY()][cell.getX()] = cell.getValue();
	}

	public void setTable(CellValue[][] table) {
		this.table = table;
	}
	
	private void moveForward(DIR dir) {

		for (int i = 0; i < getSize(); i++)
			for (int k = 0; k < getSize() - 1; k++)
				for (int j = 0; j < getSize() - 1; j++) {
					if (dir == DIR.RIGHT)
						moveHorizontal(i, j, dir);
					if (dir == DIR.DOWN)
						moveVertical(i, j, dir);
				}
	}

	private void moveVertical(int i, int j, DIR direction) {
		if (isEmptyCell(i, j) || equalsNext(i, j, direction)) {
			addNext(i, j, direction);
		}
	}


	private void moveBackward(DIR dir) {
		for (int i = 0; i < getSize(); i++)
			for (int k = 0; k < getSize() - 1; k++)
				for (int j = getSize() - 1; j > 0; j--) {
					if (dir == DIR.LEFT)
						moveHorizontal(i, j, dir);

					if (dir == DIR.UP)
						moveVertical(i, j, dir);
				}
	}

	private void moveHorizontal(int i, int j, DIR direction) {
		if (isEmptyCell(j, i) || equalsNext(j, i, direction)) {
			addNext(j, i, direction);
		}
	}
	
	private void reset() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table.length; j++) {
				table[i][j] = new CellValue(0);
			}
		}
	}

	public boolean hasNewCell() {
		if (newCell != null && newCell.getValue().isNew())
			return true;
		return false;
	}
}
