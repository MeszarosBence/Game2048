package bence.game2048;

import static bence.game2048.Game2048.DIR.DOWN;
import static bence.game2048.Game2048.DIR.LEFT;
import static bence.game2048.Game2048.DIR.RIGHT;
import static bence.game2048.Game2048.DIR.UP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import bence.game2048.Game2048.DIR;

public class Table {

	public static final int TABLE_SIZE = 4;
	CellValue[][] board = new CellValue[TABLE_SIZE][TABLE_SIZE];
	private int moves = 0;
	private Cell newCell;
	
	BiConsumer<Cell, Cell> move = (current, next) -> {
		if (!isOutOfBounds(next)) {
			board[current.getY()][current.getX()] = new CellValue(board[current.getY()][current.getX()].getValue() + board[next.getY()][next.getX()].getValue());
			board[next.getY()][next.getX()] = CellValue.EMPTY_CELL;
			if (board[current.getY()][current.getX()].getValue() != board[next.getY()][next.getX()].getValue()) moves++;
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
		board[cell.getY()][cell.getX()] = cell.getValue();
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
			while (row < TABLE_SIZE) {
				while (column < TABLE_SIZE) {
					if (board[row][column].getValue() == 0)
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
		return board[randomCell.getY()][randomCell.getX()].getValue() == 0;
	}
	
	Cell getRandomCell() {
		int[] cells = {2, 4};
		int value = ThreadLocalRandom.current().nextInt(cells.length);
		
		return new Cell(cells[value], ThreadLocalRandom.current().nextInt(TABLE_SIZE),
				ThreadLocalRandom.current().nextInt(TABLE_SIZE));
	}
	
	public int getSize() {
		return TABLE_SIZE;
	}
	
	boolean equalsNext(Cell cell, UnaryOperator<Cell> nextF) {
		Cell next = nextF.apply(cell);
		return  isOutOfBounds(next) || 
				board[cell.getY()][cell.getX()].getValue() == board[next.getY()][next.getX()].getValue();
	}
	
	boolean isEmptyCell(int column, int row) {
		return board[row][column].getValue() == 0;
	}
	
	boolean wereCellsMoved() {
		return (moves > 0);
	}

	private boolean isOutOfBounds(Cell cell) {
		return cell.getX() < 0 || cell.getX() >= board.length || cell.getY() < 0 || cell.getY() >= board.length;
	}
	
	private void switchCells(DIR dir) {
		UnaryOperator<Cell> to = current -> {
			switch(dir) {
				case DOWN: return new Cell(0, current.getX(), current.getY() + 1);
				case UP: return new Cell(0, current.getX(), current.getY() - 1);
				case RIGHT: return new Cell(0, current.getX() + 1, current.getY());
				default: return new Cell(0, current.getX() - 1, current.getY());
			}
		};
		
		Predicate<Cell> emptyOrEqualsNext = c -> equalsNext(c, to) || board[c.getY()][c.getX()].getValue() == 0;
		
		for (int i = 0; i < getSize(); i++)
			for (int k = 0; k < getSize() - 1; k++)
				switch(dir) {
					case RIGHT:
						getRow(i).stream().filter(emptyOrEqualsNext).forEach(cell -> move.accept(cell, to.apply(cell)));
						break;
					case LEFT:
						getRowBackwards(i).stream().filter(emptyOrEqualsNext).forEach(cell -> move.accept(cell, to.apply(cell)));
						break;
					case DOWN:
						getCol(i).stream().filter(emptyOrEqualsNext).forEach(cell -> move.accept(cell, to.apply(cell)));
						break;
					case UP:
						getColBackwards(i).stream().filter(emptyOrEqualsNext).forEach(cell -> move.accept(cell, to.apply(cell)));
						break;
				}
	}
	
	public List<Cell> getRow(int row) {
		List<Cell> list = new ArrayList<>();
		
		for (int i = 0; i < board[row].length; i++) {
			list.add(new Cell(board[row][i], i, row));
		}
		
		return list;
	}
	
	public List<Cell> getRowBackwards(int row) {
		List<Cell> list = new ArrayList<>();
		
		for (int i = board[row].length - 1; i >= 0; i--) {
			list.add(new Cell(board[row][i], i, row));
		}
		
		return list;
	}
	
	public List<Cell> getCol(int col) {
		List<Cell> list = new ArrayList<>();
		
		for (int i = 0; i < board.length; i++) {
			list.add(new Cell(board[i][col].getValue(), col, i));
		}
		
		return list;
	}
	
	public List<Cell> getColBackwards(int col) {
		List<Cell> list = new ArrayList<>();
		
		for (int i = board.length - 1; i >= 0 ; i--) {
			list.add(new Cell(board[i][col].getValue(), col, i));
		}
		
		return list;
	}

	private void reset() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = CellValue.EMPTY_CELL;
			}
		}
	}

	public boolean hasNewCell() {
		return (newCell != null && newCell.getValue().isNew());
	}

	public void resetNew() {
		if (hasNewCell()) {
			newCell.getValue().resetNew();
		}
	}
}
