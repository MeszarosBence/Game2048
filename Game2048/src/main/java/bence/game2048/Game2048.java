package bence.game2048;

import static org.mockito.Mockito.verify;

import java.io.PrintStream;

public class Game2048 {

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

	PrintStream out;

	public void printTable() {
		out.print(topBorderRow());    
		out.print(contentRow());      
		out.print(middleBorderRow()); 
		out.print(contentRow());      
		out.print(middleBorderRow()); 
		out.print(contentRow());      
		out.print(middleBorderRow()); 
		out.print(contentRow());      
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

	private String contentRow() {
		return VERTICAL + print(3, print(4, SPACE) + VERTICAL) + print(4, SPACE) + VERTICAL + System.lineSeparator();
	}

	private String print(int times, String character) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < times; i++) {
			output.append(character);
		}
		return output.toString();
	}

}
