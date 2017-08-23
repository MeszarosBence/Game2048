package bence.game2048;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.inOrder;

import java.io.PrintStream;

import org.fusesource.jansi.AnsiConsole;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import biz.source_code.utils.RawConsoleInput;

public class Game2048Test {
	
	private static final int FOURTH_ROW = 3;

	private static final int THIRD_ROW = 2;

	private static final int SECOND_ROW = 1;

	private static final int FIRST_ROW = 0;

	@Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);
	
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
	private static final String SPACE = " ";
	private static final String ANSI_CLS = "\u001b[2J";
	
	private static final int KEY_LEFT = 57419;
	private static final int KEY_RIGHT = 57421;
	private static final int KEY_UP = 57416;
	private static final int KEY_DOWN = 57424;
	
	
	int[][] table = new int[4][4];
	
	@Spy
	private PrintStream out = new PrintStream(AnsiConsole.out);
	InOrder inOrder = null;

	Game2048 g = new Game2048();
	
	@Before
	public void setup() {
		g.out = out;
		inOrder = inOrder(out);
	}
	
	@Test
	public void testPrintTableWithoutContent() throws Exception {
		
		g.printTable();
		assertThatTablePrintedCorrectly();
		
	}
	
	@Test
	public void testPrintContentOneElement() throws Exception {
		table[0][0] = 2;
		g.table = table;

		g.printTable();
		
		assertThatTablePrintedCorrectly();
		
	}
	
	@Test
	public void moving() throws Exception {
		out.print(ANSI_CLS);
		table[0][0] = 2;
		g.table = table;

		g.printTable();
		
//		Thread.sleep(500);  //In order to verify it visually
		
		assertThatTablePrintedCorrectly();
		out.print(ANSI_CLS);
		
		table[0][0] = 0;
		table[0][1] = 2;
		g.printTable();
		
//		Thread.sleep(500); //In order to verify it visually
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void testPrintContentAllElements() throws Exception {
		table[0][0] = 2;
		table[0][1] = 4;
		table[0][2] = 8;
		table[0][3] = 16;
		table[1][0] = 32;
		table[1][1] = 64;
		table[1][2] = 128;
		table[1][3] = 256;
		table[2][0] = 512;
		table[2][1] = 1024;
		table[2][2] = 2048;
		table[2][3] = 1024;
		table[3][0] = 521;
		table[3][1] = 256;
		table[3][2] = 128;
		table[3][3] = 64;
		g.table = table;
		
		g.printTable();
		
		assertThatTablePrintedCorrectly();
		
	}
	
	@Test
	public void placeFirstCellRandomly() throws Exception {
		
		g.start();
		int found = 0;
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table.length; j++) {
				if (g.table[i][j] > 0) found++;
			}
			
		}
		assertThat(found, is(1));
		
		g.printTable();
		
	}
	
	@Test
	@Ignore
	public void readArrowsInNonBlockingMode() throws Exception {
		
		System.out.println("trying to read a character:");
		int read = RawConsoleInput.read(true);
		if (read == KEY_UP) System.out.println("UP was pressed");
		if (read == KEY_DOWN) System.out.println("DOWN was pressed");
		if (read == KEY_LEFT) System.out.println("LEFT was pressed");
		if (read == KEY_RIGHT) System.out.println("RIGHT was pressed");
		
	}
	
	@Test
	public void testMoveOneValueLeft() throws Exception {
		int randomValue = 2;
		g.table[0][3] = randomValue;
		g.printTable();
		
		g.left();
		
		assertThat(g.table[0][0], is(randomValue));
		g.printTable();
	}
	
	@Test
	public void testMoveValuesLeftAndAddThemIfTheyAreEqual() throws Exception {
		int randomValue = 2;
		g.table[0][3] = randomValue;
		g.table[0][2] = randomValue;
		g.printTable();
		
		g.left();
		
		assertThat(g.table[0][0], is(2 * randomValue));
		assertThat(g.table[0][1], is(0));
		assertThat(g.table[0][2], is(0));
		assertThat(g.table[0][3], is(0));
		g.printTable();
	}
	
	@Test
	public void testMoveValuesLeftAndDontAddThemIfTheyAreNotEqual() throws Exception {
		g.table[3][3] = 4;
		g.table[3][2] = 2;
		g.printTable();
		
		g.left();
		
		assertThat(g.table[3][0], is(2));
		assertThat(g.table[3][1], is(4));
		g.printTable();
	}
	
	@Test
	public void testDontMoveValuesLeft() throws Exception {
		g.table[3][3] = 4;
		g.table[3][2] = 2;
		g.table[3][1] = 4;
		g.table[3][0] = 2;
		g.printTable();
		
		g.left();
		
		assertThat(g.table[3][0], is(2));
		assertThat(g.table[3][1], is(4));
		assertThat(g.table[3][2], is(2));
		assertThat(g.table[3][3], is(4));
		g.printTable();
	}
	
	@Test
	public void testMoveMultipleRowsLeftAndAddValues() throws Exception {
		g.table[3][3] = 2;
		g.table[3][2] = 2;
		g.table[2][3] = 2;
		g.table[2][2] = 2;
		
		g.printTable();
		
		g.left();
		
		assertThat(g.table[3][0], is(4));
		assertThat(g.table[3][1], is(0));
		assertThat(g.table[3][2], is(0));
		assertThat(g.table[3][3], is(0));
		
		assertThat(g.table[2][0], is(4));
		assertThat(g.table[2][1], is(0));
		assertThat(g.table[2][2], is(0));
		assertThat(g.table[2][3], is(0));
		g.printTable();
	}
	
	@Test
	public void testMoveOneValueRight() throws Exception {
		int randomValue = 2;
		g.table[0][0] = randomValue;
		g.printTable();
		
		g.right();
		
		assertThat(g.table[0][3], is(randomValue));
		g.printTable();
	}
	
	@Test
	public void testAddAndMoveTwoValuesRight() throws Exception {
		int randomValue = 2;
		g.table[0][0] = randomValue;
		g.table[0][2] = randomValue;
		g.printTable();
		
		g.right();
		
		assertThat(g.table[0][2], is(0));
		assertThat(g.table[0][3], is(2 * randomValue));
		g.printTable();
	}
	
	
	@Test
	public void testMoveOneValueUp() throws Exception {
		int randomValue = 2;
		g.table[3][0] = randomValue;
		g.printTable();
		
		g.up();
		
		assertThat(g.table[0][0], is(randomValue));
		g.printTable();
	}
	
	
	@Test
	public void testMoveTwoValuesUp() throws Exception {
		int randomValue = 2;
		g.table[1][0] = randomValue;
		g.table[3][0] = randomValue;
		g.printTable();
		
		g.up();
		
		assertThat(g.table[0][0], is(2 * randomValue));
		g.printTable();
	}
	
	@Test
	public void testMoveThreeValuesUp() throws Exception {
		g.table[1][0] = 2;
		g.table[2][0] = 4;
		g.table[3][0] = 8;
		g.printTable();
		
		g.up();
		
		assertThat(g.table[0][0], is(2));
		assertThat(g.table[1][0], is(4));
		assertThat(g.table[2][0], is(8));
		g.printTable();
	}
	
	
	@Test
	public void fullTableUp() throws Exception {
		g.table[0][0] = 2;
		g.table[1][0] = 2;
		g.table[2][0] = 2;
		g.table[3][0] = 2;
		
		g.table[0][1] = 2;
		g.table[1][1] = 2;
		g.table[2][1] = 2;
		g.table[3][1] = 2;
		
		g.table[0][2] = 2;
		g.table[1][2] = 2;
		g.table[2][2] = 2;
		g.table[3][2] = 2; 
		
		g.table[0][3] = 2;
		g.table[1][3] = 2;
		g.table[2][3] = 2;
		g.table[3][3] = 2;
		g.printTable();
		
		g.up();
		
		assertThat(g.table[0][0], is(8));
		assertThat(g.table[0][1], is(8));
		assertThat(g.table[0][2], is(8));
		assertThat(g.table[0][3], is(8));
		g.printTable();
	}
	
	@Test
	public void down() throws Exception {
		g.table[0][0] = 2;
		g.table[1][0] = 2;
		g.table[2][0] = 2;
		g.table[3][0] = 2;
		
		g.table[0][1] = 2;
		g.table[1][1] = 2;
		g.table[2][1] = 2;
		g.table[3][1] = 2;
		
		g.table[0][2] = 2;
		g.table[1][2] = 2;
		g.table[2][2] = 2;
		g.table[3][2] = 2; 
		
		g.table[0][3] = 2;
		g.table[1][3] = 2;
		g.table[2][3] = 2;
		g.table[3][3] = 2;
		g.printTable();
		
		g.down();
		
		assertThat(g.table[3][0], is(8));
		assertThat(g.table[3][1], is(8));
		assertThat(g.table[3][2], is(8));
		assertThat(g.table[3][3], is(8));
		g.printTable();
	}
	
	
	@Test
	public void buttonLeft() throws Exception {
		g = new Game2048() {
			@Override
			public void next() {
				super.table[0][3] = 2;
			}
			
			@Override
			public int readFromConsole() {
				return KEY_LEFT;
			}
		};
		
		g.start();
		assertThat(g.table[0][0], is(2));
		

	}

	private void assertThatTablePrintedCorrectly() {
		inOrder.verify(out).print(topBorderRow());
		inOrder.verify(out).print(contentRow(table[FIRST_ROW]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(table[SECOND_ROW]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(table[THIRD_ROW]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(table[FOURTH_ROW]));
		inOrder.verify(out).print(bottomBorderRow());
	}

	private String topBorderRow() {
		return UPPER_LEFT + print(3, print(4, HORIZONTAL) + BORDER_TOP) + print(4, HORIZONTAL) + UPPER_RIGHT + System.lineSeparator();
	}
	
	private String bottomBorderRow() {
		return LOWER_LEFT + print(3, print(4, HORIZONTAL) + BORDER_BOTTOM) + print(4, HORIZONTAL) + LOWER_RIGHT + System.lineSeparator();
	}

	private String middleBorderRow() {
		return CELL_LEFT + print(3, print(4, HORIZONTAL) + CELL_MIDDLE) + print(4, HORIZONTAL) + CELL_RIGHT + System.lineSeparator();
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
	

	private String printCell(int i) {
		if (i < 2) return "    ";
		if (i < 10 ) return "  " + i + " ";
		if (i < 100) return " " + i + " ";
		if (i < 1000) return " " + i;
		
		return Integer.toString(i);
	}

	private String print(int times, String character) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < times; i++) {
			output.append(character);
		}
		return output.toString();
	}
}
