package bence.game2048;

import static bence.game2048.Game2048.DIR.DOWN;
import static bence.game2048.Game2048.DIR.LEFT;
import static bence.game2048.Game2048.DIR.RIGHT;
import static bence.game2048.Game2048.DIR.UP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import bence.game2048.Game2048.DIR;

public class Table {

	public static final int TABLE_SIZE = 4;
	CellValue[][] table = new CellValue[TABLE_SIZE][TABLE_SIZE];
	private int moves = 0;
	private Cell newCell;
	
	BiConsumer<Cell, Cell> move = (current, next) -> {
		if (!isOutOfBounds(next)) {
			table[current.getY()][current.getX()] = new CellValue(table[current.getY()][current.getX()].getValue() + table[next.getY()][next.getX()].getValue());
			table[next.getY()][next.getX()] = CellValue.EMPTY_CELL;
			if (table[current.getY()][current.getX()].getValue() != table[next.getY()][next.getX()].getValue()) moves++;
		}
	};
	
	
	public Table() {
  		reset();
	}
	
	public void moveLeft() {
		switchCells(RIGHT);
	}

	public void moveUp() {
		switchCells(DOWN);
	}
	
	public void moveRight() {
		switchCells(LEFT);
	}

	public void moveDown() {
		switchCells(UP);
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
		
		return findNextEmptyCell(randomCell);
	}

	private Cell findNextEmptyCell(Cell randomCell) {
		int row = randomCell.getY();
		int column = randomCell.getX();
		int maxTries = TABLE_SIZE * TABLE_SIZE + 1;
		for (int tries = 0; tries < maxTries; tries++) {
			while (row < TABLE_SIZE && tries < maxTries) {
				while (column < TABLE_SIZE && tries < maxTries) {
					if (table[row][column].getValue() == 0)
						return new Cell(randomCell.getValue(), column, row);
					column++;
				}
				row++;
				column = 0;
			}
			row = 0;
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
	
	boolean equalsNext(Cell cell, Function<Cell, Cell> nextF) {
		Cell next = nextF.apply(cell);
		return  isOutOfBounds(next) || 
				table[cell.getY()][cell.getX()].getValue() == table[next.getY()][next.getX()].getValue();
	}
	
	boolean isEmptyCell(int column, int row) {
		return table[row][column].getValue() == 0;
	}
	
	boolean wereCellsMoved() {
		return (moves > 0);
	}

	private boolean isOutOfBounds(Cell cell) {
		return cell.getX() < 0 || cell.getX() >= table.length || cell.getY() < 0 || cell.getY() >= table.length;
	}
	
	private void switchCells(DIR dir) {
		Function<Cell, Cell> to = (current) -> {
			switch(dir) {
				case DOWN: return new Cell(0, current.getX(), current.getY() + 1);
				case UP: return new Cell(0, current.getX(), current.getY() - 1);
				case RIGHT: return new Cell(0, current.getX() + 1, current.getY());
				default: return new Cell(0, current.getX() - 1, current.getY());
			}
		};
		
		Predicate<Cell> emptyOrEqualsNext = (c) -> equalsNext(c, to) || table[c.getY()][c.getX()].getValue() == 0;
		
		for (int i = 0; i < getSize(); i++)
			for (int k = 0; k < getSize() - 1; k++)
				switch(dir) {
					case RIGHT:
						getRow(i).stream().filter(emptyOrEqualsNext).forEach((cell) -> move.accept(cell, to.apply(cell)));
						break;
					case LEFT:
						getRowBackwards(i).stream().filter(emptyOrEqualsNext).forEach((cell) -> move.accept(cell, to.apply(cell)));
						break;
					case DOWN:
						getCol(i).stream().filter(emptyOrEqualsNext).forEach((cell) -> move.accept(cell, to.apply(cell)));
						break;
					case UP:
						getColBackwards(i).stream().filter(emptyOrEqualsNext).forEach((cell) -> move.accept(cell, to.apply(cell)));
						break;
				}
	}
	
	public List<Cell> getRow(int row) {
		List<Cell> list = new ArrayList<>();
		
		for (int i = 0; i < table[row].length; i++) {
			list.add(new Cell(table[row][i], i, row));
		}
		
		return list;
	}
	
	public List<Cell> getRowBackwards(int row) {
		List<Cell> list = new ArrayList<>();
		
		for (int i = table[row].length - 1; i >= 0; i--) {
			list.add(new Cell(table[row][i], i, row));
		}
		
		return list;
	}
	
	public List<Cell> getCol(int col) {
		List<Cell> list = new ArrayList<>();
		
		for (int i = 0; i < table.length; i++) {
			list.add(new Cell(table[i][col].getValue(), col, i));
		}
		
		return list;
	}
	
	public List<Cell> getColBackwards(int col) {
		List<Cell> list = new ArrayList<>();
		
		for (int i = table.length - 1; i >= 0 ; i--) {
			list.add(new Cell(table[i][col].getValue(), col, i));
		}
		
		return list;
	}

	private void reset() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table.length; j++) {
				table[i][j] = CellValue.EMPTY_CELL;
			}
		}
	}

	public boolean hasNewCell() {
		if (newCell != null && newCell.getValue().isNew())
			return true;
		return false;
	}

	public void resetNew() {
		if (hasNewCell()) {
			newCell.getValue().resetNew();
		}
	}
}
