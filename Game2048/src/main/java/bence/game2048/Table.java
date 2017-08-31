package bence.game2048;

import java.util.concurrent.ThreadLocalRandom;

import bence.game2048.Game2048.DIR;

public class Table {
	
	private static final int TABLE_SIZE = 4;
	int[][] table = new int[TABLE_SIZE][TABLE_SIZE];
	private int moves = 0;
	
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
		moves = 0;
	}
	
	private Cell getNextCell() {
		Cell randomCell = getRandomCell();

		if (table[randomCell.getY()][randomCell.getX()] == 0)
			return randomCell;
		else {
			int row = randomCell.getY();
			int column = randomCell.getX();
			int maxTries = TABLE_SIZE * TABLE_SIZE + 1;
			for (int tries = 0; tries < maxTries; tries++) {
				while (row < TABLE_SIZE && tries < maxTries) {
					while (column < TABLE_SIZE && tries < maxTries) {
						if (table[row][column] == 0)
							return new Cell(2, column, row);
						column++;
					}
					row++;
				}
				row = 0;
				column = 0;
			}
		}

		return null;
	}
	
	
	Cell getRandomCell() {
		Cell randomCell = new Cell(2, ThreadLocalRandom.current().nextInt(TABLE_SIZE),
				ThreadLocalRandom.current().nextInt(TABLE_SIZE));
		return randomCell;
	}
	
	public int getSize() {
		return TABLE_SIZE;
	}
	
	void addNext(int i, int j, DIR direction) {
		if (direction == DIR.DOWN) {
			table[j][i] = table[j][i] + table[j + 1][i];
			table[j + 1][i] = 0;
			if (table[j][i] != table[j + 1][i]) moves++;
		}

		if (direction == DIR.UP) {
			table[j][i] = table[j][i] + table[j - 1][i];
			table[j - 1][i] = 0;
			if (table[j][i] != table[j - 1][i]) moves++;
		}

		if (direction == DIR.RIGHT) {
			table[j][i] = table[j][i] + table[j][i + 1];
			table[j][i + 1] = 0;
			if (table[j][i] != table[j][i + 1]) moves++;
		}

		if (direction == DIR.LEFT) {
			if (table[j][i] != table[j][i - 1]) moves++;
			table[j][i] = table[j][i] + table[j][i - 1];
			table[j][i - 1] = 0;
		}
	}
	
	boolean equalsNext(int column, int row, DIR direction) {
		if (direction == DIR.DOWN)
			return table[row][column] == table[row + 1][column];
		if (direction == DIR.UP)
			return table[row][column] == table[row - 1][column];
		if (direction == DIR.RIGHT)
			return table[row][column] == table[row][column + 1];
		if (direction == DIR.LEFT)
			return table[row][column] == table[row][column - 1];
		return false;
	}
	
	boolean isEmptyCell(int column, int row) {
		return table[row][column] == 0;
	}
	
	boolean wereCellsMoved() {
		return (moves > 0);
	}

	public int[] getRow(int row) {
		return table[row];
	}

	public void setCell(Cell cell) {
		table[cell.getY()][cell.getX()] = cell.getValue();
	}

	public void setTable(int[][] table) {
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
}
