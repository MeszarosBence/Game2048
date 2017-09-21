package bence.game2048;

import java.io.PrintStream;
import java.util.List;

import org.fusesource.jansi.AnsiConsole;

public class ConsoleGrid implements Presentation {
	
	private static final int DEFAULT_DELAY_IN_MS = 300;
	private static final String HORIZONTAL = "\u2550";
	private static final String VERTICAL = "\u2551";
	private static final String UPPER_LEFT = "\u2554";
	private static final String UPPER_RIGHT = "\u2557";
	private static final String LOWER_LEFT = "\u255A";
	private static final String LOWER_RIGHT = "\u255D";
	private static final String BORDER_TOP = "\u2566";
	private static final String BORDER_BOTTOM = "\u2569";
	private static final String CELL_MIDDLE = "\u256C";
	private static final String CELL_LEFT = "\u2560";
	private static final String CELL_RIGHT = "\u2563";
	private static final String ANSI_CLS = "\u001b[2J";
	private static final String ANSI_HOME = "\u001b[H";
	
	PrintStream out = new PrintStream(AnsiConsole.out);
	Table table;
	private int delay = DEFAULT_DELAY_IN_MS;
	
	public ConsoleGrid(Table table) {
		this.table = table;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void display() {
		delayedDisplay();
	}
	
	private void delayedDisplay() {
		displayTable();
		if (table.hasNewCell()) {
			sleep();
			table.resetNew();
			displayTable();
		}
	}
	
	private void displayTable() {
		out.print(ANSI_CLS + ANSI_HOME);
		out.print(topBorderRow());    
		out.print(printRow(table.getRow(0)));      
		out.print(middleBorderRow()); 
		out.print(printRow(table.getRow(1)));      
		out.print(middleBorderRow()); 
		out.print(printRow(table.getRow(2)));      
		out.print(middleBorderRow()); 
		out.print(printRow(table.getRow(3)));      
		out.print(bottomBorderRow());
	}

	private void sleep() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
	}

	public void setOutput(PrintStream out) {
		this.out = out;
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
	
	private String printRow(List<Cell> list) {
		StringBuilder row = new StringBuilder(VERTICAL);
		for (int i = 0; i < list.size(); i++) {
			row.append(formatCell(list.get(i)));
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
	
	private String formatCell(Cell cell) {
		if (cell.getIntValue() < 2 || cell.getValue().isNew()) return "    ";
		else if (cell.getIntValue() < 10 ) return "  " + cell.getValue() + " ";
		else if (cell.getIntValue() < 100) return " " + cell.getValue() + " ";
		else if (cell.getIntValue() < 1000) return " " + cell.getValue();
		
		return cell.toString();
	}

	public void message(String text) {
		out.println(text);
	}
}
