package bence.game2048;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

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
	
	enum DIR {
		UP, DOWN, LEFT, RIGHT
	}


	private static final int KEY_LEFT = 57419;
	private static final int KEY_RIGHT = 57421;
	private static final int KEY_UP = 57416;
	private static final int KEY_DOWN = 57424;

	PrintStream out;
	int[][] table = new int[TABLE_SIZE][TABLE_SIZE];

	public void printTable() {
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

	public Cell getNextCell() {
		Random rand = new Random();
		return new Cell(2, rand.nextInt(TABLE_SIZE), rand.nextInt(TABLE_SIZE));
	}

	public void next() {
		Cell cell = getNextCell();
		table[cell.getX()][cell.getY()] = cell.getValue();
		printTable();
		
	}
	
	public void left() {
		moveLeft();
	}

	private void moveLeft() {

		for (int i = 0; i < table.length; i++)
		for (int k = 0; k < table.length -  1; k++)
		for (int j = 0; j < table.length - 1; j++) {
			if (isEmptyCell(j, i) || equalsNext(j, i, DIR.RIGHT)) {
				addNext(j, i, DIR.RIGHT);
			}
		}
	}
	
	private void moveUp() {

		for (int i = 0; i < table.length; i++)
		for (int k = 0; k < table.length -  1; k++)
		for (int j = 0; j < table.length - 1; j++) {
			if (isEmptyCell(i, j) || equalsNext(i, j, DIR.DOWN)) {
				addNext(i, j, DIR.DOWN);
			}
		}
	}
	
	private void moveRight() {

		for (int i = 0; i < table.length; i++)
		for (int k = 0; k < table.length -  1; k++)
		for (int j = table.length - 1; j > 0; j--) {
			if (isEmptyCell(j, i) || equalsNext(j, i, DIR.LEFT)) {
				addNext(j, i, DIR.LEFT);
			}
		}
	}
	
	private void moveDown() {

		for (int i = 0; i < table.length; i++)
		for (int k = 0; k < table.length -  1; k++)
		for (int j = table.length - 1; j > 0; j--) {
			if (isEmptyCell(i, j) || equalsNext(i, j, DIR.UP)) {
				addNext(i, j, DIR.UP);
			}
		}
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
	
	
	private void moveRightOld() {

		int i = 0;
		for (int k = 0; k < table.length -  1; k++)
		for (int j = table.length - 1; j > 0; j--) {
			if (isEmptyCell(j, i)) {
				table[i][j] = table[i][j -1];
				table[i][j - 1] = 0;
			}
		}
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

	public void start() throws IOException {
		next();
		processUserInput();
	}

	public int readFromConsole() throws IOException {
		return RawConsoleInput.read(true);
	}
	
	public void processUserInput() throws IOException  {
		int input = readFromConsole();
		if (input == KEY_LEFT) {
			left();
		}
	}

}

	