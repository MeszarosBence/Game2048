package bence.game2048;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ThreadLocalRandom;

import org.fusesource.jansi.AnsiConsole;

import biz.source_code.utils.RawConsoleInput;

public class Game2048 {

	private static final int TABLE_SIZE = 4;
	private static final String HORIZONTAL = "\u2550";
	private static final String VERTICAL = "\u2551";
	private static final String UPPER_LEFT = "\u2554";
	private static final String UPPER_RIGHT = "\u2557";
	private static final String LOWER_LEFT = "\u255A";
	private static final String LOWER_RIGHT = "\u255D";
	private static final String BORDER_TOP = "\u2566";
	private static final String BORDER_BOTTOM = "\u2569";
	private static final String CELL_DOWN = "\u2569";
	private static final String CELL_MIDDLE = "\u256C";
	private static final String CELL_LEFT = "\u2560";
	private static final String CELL_RIGHT = "\u2563";
	private static final String SPACE = " ";
	private static final String ANSI_CLS = "\u001b[2J";
	private static final String ANSI_HOME = "\u001b[H";
	
	PrintStream out = new PrintStream(AnsiConsole.out);
	
	enum DIR {
		UP, DOWN, LEFT, RIGHT
	}


	private static final int KEY_LEFT = 57419;
	private static final int KEY_RIGHT = 57421;
	private static final int KEY_UP = 57416;
	private static final int KEY_DOWN = 57424;

	int[][] table = new int[TABLE_SIZE][TABLE_SIZE];

	public void printTable() {
		out.print(ANSI_CLS + ANSI_HOME);
		out.print(topBorderRow());    
		out.print(contentRow(table[0]));      
		out.print(middleBorderRow()); 
		out.print(contentRow(table[1]));      
		out.print(middleBorderRow()); 
		out.print(contentRow(table[2]));      
		out.print(middleBorderRow()); 
		out.print(contentRow(table[3]));      
		out.print(bottomBorderRow()); 
	}

	private String topBorderRow() {
		return UPPER_LEFT + print(3, print(4, HORIZONTAL) + BORDER_TOP) + print(4, HORIZONTAL) + UPPER_RIGHT
				+ System.lineSeparator();
	}

	private String bottomBorderRow() {
		return LOWER_LEFT + print(3, print(4, HORIZONTAL) + BORDER_BOTTOM) + print(4, HORIZONTAL) + LOWER_RIGHT
				+ System.lineSeparator();
	}

	private String middleBorderRow() {
		return CELL_LEFT + print(3, print(4, HORIZONTAL) + CELL_MIDDLE) + print(4, HORIZONTAL) + CELL_RIGHT
				+ System.lineSeparator();
	}
	
	private String contentRow(int[] content) {
		StringBuilder row = new StringBuilder(VERTICAL);
		for (int i = 0; i < content.length; i++) {
			row.append(printCell(content[i]));
			row.append(VERTICAL);
		}
		row.append(System.lineSeparator());
		
		return row.toString();
	}

	private String print(int times, String character) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < times; i++) {
			output.append(character);
		}
		return output.toString();
	}
	
	private String printCell(int i) {
		if (i < 2) return "    ";
		if (i < 10 ) return "  " + i + " ";
		if (i < 100) return " " + i + " ";
		if (i < 1000) return " " + i;
		
		return Integer.toString(i);
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
		printTable();
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
			printTable();
			processUserInput();
			printTable();
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
