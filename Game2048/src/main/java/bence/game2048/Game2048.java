package bence.game2048;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ThreadLocalRandom;

import org.fusesource.jansi.AnsiConsole;

import biz.source_code.utils.RawConsoleInput;

public class Game2048 {

	private static final int TABLE_SIZE = 4;
	
	
	enum DIR {
		UP, DOWN, LEFT, RIGHT
	}


	private static final int KEY_LEFT = 57419;
	private static final int KEY_RIGHT = 57421;
	private static final int KEY_UP = 57416;
	private static final int KEY_DOWN = 57424;

	int[][] table = new int[TABLE_SIZE][TABLE_SIZE];
	private Grid grid;

	
	public void setGrid(Grid grid) {
		this.grid = grid;
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
		};
		
		return null;
	}

	Cell getRandomCell() {
		Cell randomCell = new Cell(2, ThreadLocalRandom.current().nextInt(TABLE_SIZE), ThreadLocalRandom.current().nextInt(TABLE_SIZE));
		return randomCell;
	}


	public void next() {
		Cell cell = getNextCell();
		table[cell.getY()][cell.getX()] = cell.getValue();
		grid.display();
	}
	
	public void left() {
		moveLeft();
	}
	
	private void moveForward(DIR dir) {

		for (int i = 0; i < table.length; i++)
		for (int k = 0; k < table.length -  1; k++)
		for (int j = 0; j < table.length - 1; j++) {
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

	private void moveLeft() {
		moveForward(DIR.RIGHT);
	}
	
	private void moveUp() {
		moveForward(DIR.DOWN);
	}
	
	private void moveBackward(DIR dir) {
		for (int i = 0; i < table.length; i++)
		for (int k = 0; k < table.length -  1; k++)
		for (int j = table.length - 1; j > 0; j--) {
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
	
	private void moveRight() {

		moveBackward(DIR.LEFT);
	}
	
	private void moveDown() {

		moveBackward(DIR.UP);
	}

	private void addNext(int i, int j, DIR direction) {
		if (direction == DIR.DOWN) {
			table[j][i] = table [j][i] + table[j + 1][i];
			table[j + 1][i] = 0;
		}
		
		if (direction == DIR.UP) {
			table[j][i] = table [j][i] + table[j - 1][i];
			table[j - 1][i] = 0;
		}
		
		if (direction == DIR.RIGHT) {
			table[j][i] = table [j][i] + table[j][i + 1];
			table[j][i + 1] = 0;
		}
		
		if (direction == DIR.LEFT) {
			table[j][i] = table [j][i] + table[j][i - 1];
			table[j][i - 1] = 0;
		}
		
		
	}

	private boolean equalsNext(int column, int row, DIR direction) {
		if (direction == DIR.DOWN)
			return table [row][column] == table[row + 1][column];
		if (direction == DIR.UP)
			return table [row][column] == table[row - 1][column];
		if (direction == DIR.RIGHT)
			return table [row][column] == table[row][column + 1];
		if (direction == DIR.LEFT)
			return table [row][column] == table[row][column - 1];
		return false;
	}

	private boolean isEmptyCell(int column, int row) {
 		return table[row][column] == 0;
	}
	
	public void right() {
		moveRight();
	}

	public void up() {
		moveUp();
	}

	public void down() {
		moveDown();
	}

	public void start() throws GameInterruptedException {
		while(true) {
			next();
			grid.display();
			processUserInput();
			grid.display();
		}
	}

	int readFromConsole() throws GameInterruptedException {
		int input = 0;
		try {
			input = RawConsoleInput.read(true);
		} catch (IOException e) {
			throw new GameInterruptedException(e);
		}
		return input;
	}
	
	public void processUserInput() throws GameInterruptedException  {
		
			switch (readFromConsole()) {
				case KEY_LEFT: 
					left();
					break;
				case KEY_RIGHT: 
					right();
					break;
				case KEY_UP: 
					up();
					break;
				case KEY_DOWN: 
					down();
					break;
			}
	}
}
