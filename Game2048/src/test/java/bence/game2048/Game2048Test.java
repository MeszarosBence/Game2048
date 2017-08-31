package bence.game2048;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
	
	private static final int KEY_QUIT = 113;

	private static final GameInterruptedException GAME_OVER = new GameInterruptedException("Game Over");

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
	private static final String ANSI_CLS = "\u001b[2J";
	private static final String ANSI_HOME = "\u001b[H";
	
	private static final int KEY_LEFT = 57419;
	private static final int KEY_RIGHT = 57421;
	private static final int KEY_UP = 57416;
	private static final int KEY_DOWN = 57424;

	Table t = new Table();
	int[][] table = new int[t.getSize()][t.getSize()]; 
	
	@Spy
	private PrintStream out = new PrintStream(AnsiConsole.out);
	InOrder inOrder = null;

	Game2048 game = new Game2048();
	ConsoleGrid grid = new ConsoleGrid(t);
	Presentation gridMock = mock(Presentation.class);
	Input input = mock(Input.class);
	
	@Before
	public void setup() {
		grid.setOutput(out);
		game.setPresentation(grid);
		game.setTable(t);
		game.setInput(input);
		inOrder = inOrder(out);
		t.setTable(table);
	}
	
	@Test
	public void printTableWithoutContent() throws Exception {
		
		grid.display();
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void printContentOneElement() throws Exception {
		table[0][0] = 2;

		grid.display();
		
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void moving() throws Exception {
		out.print(ANSI_CLS);
		table[0][0] = 2;

		grid.display();
		
//		Thread.sleep(500);  //In order to verify it visually
		
		assertThatTablePrintedCorrectly();
		out.print(ANSI_CLS);
		
		table[0][0] = 0;
		table[0][1] = 2;
		grid.display();
		
//		Thread.sleep(500); //In order to verify it visually
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void printContentAllElements() throws Exception {
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
		
		grid.display();
		
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void placeFirstCellRandomly() throws Exception {
		table[0][3] = 2;
		game.setPresentation(gridMock);
		
		when(input.readFromKeyboard()).thenThrow(GAME_OVER);

		assertThat(countCells(), is(1));
		
	}

	private int countCells() {
		int found = 0;
		for (int i = 0; i < t.table.length; i++) {
			for (int j = 0; j < t.table.length; j++) {
				if (t.table[i][j] > 0) found++;
			}
		}
		return found;
	}
	
	@Test
	@Ignore
	public void readArrowsFromKeyboard() throws Exception {
		
		System.out.println("trying to read a character:");
		int read = RawConsoleInput.read(true);
		System.out.println(read);
		if (read == KEY_UP) System.out.println("UP was pressed");
		if (read == KEY_DOWN) System.out.println("DOWN was pressed");
		if (read == KEY_LEFT) System.out.println("LEFT was pressed");
		if (read == KEY_RIGHT) System.out.println("RIGHT was pressed");
	}
	
	@Test
	public void moveOneValueLeft() throws Exception {
		int randomValue = 2;
		t.table[0][3] = randomValue;
		grid.display();
		
		game.left();
		
		assertThat(t.table[0][0], is(randomValue));
		grid.display();
	}
	
	@Test
	public void moveValuesLeftAndAddThemIfTheyAreEqual() throws Exception {
		int randomValue = 2;
		t.table[0][3] = randomValue;
		t.table[0][2] = randomValue;
		grid.display();
		
		game.left();
		
		assertThat(t.table[0][0], is(2 * randomValue));
		assertThat(t.table[0][1], is(0));
		assertThat(t.table[0][2], is(0));
		assertThat(t.table[0][3], is(0));
		grid.display();
	}
	
	@Test
	public void moveValuesLeftAndDontAddThemIfTheyAreNotEqual() throws Exception {
		t.table[3][3] = 4;
		t.table[3][2] = 2;
		grid.display();
		
		game.left();
		
		assertThat(t.table[3][0], is(2));
		assertThat(t.table[3][1], is(4));
		grid.display();
	}
	
	@Test
	public void dontMoveValuesLeft() throws Exception {
		t.table[3][3] = 4;
		t.table[3][2] = 2;
		t.table[3][1] = 4;
		t.table[3][0] = 2;
		grid.display();
		
		game.left();
		
		assertThat(t.table[3][0], is(2));
		assertThat(t.table[3][1], is(4));
		assertThat(t.table[3][2], is(2));
		assertThat(t.table[3][3], is(4));
		grid.display();
	}
	
	@Test
	public void moveMultipleRowsLeftAndAddValues() throws Exception {
		t.table[3][3] = 2;
		t.table[3][2] = 2;
		t.table[2][3] = 2;
		t.table[2][2] = 2;
		
		grid.display();
		
		game.left();
		
		assertThat(t.table[3][0], is(4));
		assertThat(t.table[3][1], is(0));
		assertThat(t.table[3][2], is(0));
		assertThat(t.table[3][3], is(0));
		
		assertThat(t.table[2][0], is(4));
		assertThat(t.table[2][1], is(0));
		assertThat(t.table[2][2], is(0));
		assertThat(t.table[2][3], is(0));
		grid.display();
	}
	
	@Test
	public void moveOneValueRight() throws Exception {
		int randomValue = 2;
		t.table[0][0] = randomValue;
		grid.display();
		
		game.right();
		
		assertThat(t.table[0][3], is(randomValue));
		grid.display();
	}
	
	@Test
	public void addAndMoveTwoValuesRight() throws Exception {
		int randomValue = 2;
		t.table[0][0] = randomValue;
		t.table[0][2] = randomValue;
		grid.display();
		
		game.right();
		
		assertThat(t.table[0][2], is(0));
		assertThat(t.table[0][3], is(2 * randomValue));
		grid.display();
	}
	
	@Test
	public void moveOneValueUp() throws Exception {
		int randomValue = 2;
		t.table[3][0] = randomValue;
		grid.display();
		
		game.up();
		
		assertThat(t.table[0][0], is(randomValue));
		grid.display();
	}
	
	@Test
	public void moveTwoValuesUp() throws Exception {
		int randomValue = 2;
		t.table[1][0] = randomValue;
		t.table[3][0] = randomValue;
		grid.display();
		
		game.up();
		
		assertThat(t.table[0][0], is(2 * randomValue));
		grid.display();
	}
	
	@Test
	public void moveThreeValuesUp() throws Exception {
		t.table[1][0] = 2;
		t.table[2][0] = 4;
		t.table[3][0] = 8;
		grid.display();
		
		game.up();
		
		assertThat(t.table[0][0], is(2));
		assertThat(t.table[1][0], is(4));
		assertThat(t.table[2][0], is(8));
		grid.display();
	}
	
	
	@Test
	public void fullTableUp() throws Exception {
		t.table[0][0] = 2;
		t.table[1][0] = 2;
		t.table[2][0] = 2;
		t.table[3][0] = 2;
		
		t.table[0][1] = 2;
		t.table[1][1] = 2;
		t.table[2][1] = 2;
		t.table[3][1] = 2;
		
		t.table[0][2] = 2;
		t.table[1][2] = 2;
		t.table[2][2] = 2;
		t.table[3][2] = 2; 
		
		t.table[0][3] = 2;
		t.table[1][3] = 2;
		t.table[2][3] = 2;
		t.table[3][3] = 2;
		grid.display();
		
		game.up();
		
		assertThat(t.table[0][0], is(8));
		assertThat(t.table[0][1], is(8));
		assertThat(t.table[0][2], is(8));
		assertThat(t.table[0][3], is(8));
		grid.display();
	}
	
	@Test
	public void down() throws Exception {
		t.table[0][0] = 2;
		t.table[1][0] = 2;
		t.table[2][0] = 2;
		t.table[3][0] = 2;
		
		t.table[0][1] = 2;
		t.table[1][1] = 2;
		t.table[2][1] = 2;
		t.table[3][1] = 2;
		
		t.table[0][2] = 2;
		t.table[1][2] = 2;
		t.table[2][2] = 2;
		t.table[3][2] = 2; 
		
		t.table[0][3] = 2;
		t.table[1][3] = 2;
		t.table[2][3] = 2;
		t.table[3][3] = 2;
		grid.display();
		
		game.down();
		
		assertThat(t.table[3][0], is(8));
		assertThat(t.table[3][1], is(8));
		assertThat(t.table[3][2], is(8));
		assertThat(t.table[3][3], is(8));
		grid.display();
	}
	
	@Test
	public void buttonLeft() throws Exception {
		t = new Table() {
			@Override
			public void putANewItem() {
			}
		};
		
		game.setTable(t);
		
		t.table[0][3] = 2;
		game.setPresentation(gridMock);
		
		when(input.readFromKeyboard()).thenReturn(KEY_LEFT).thenThrow(GAME_OVER);
		
		try {
			game.start();
		} catch (GameInterruptedException e) {
			System.out.println(e);
		}
		
		assertThat(t.table[0][0], is(2));
	}
	
	@Test
	public void readMultipleKeyStrokes() throws Exception {
		t = new Table() {
			@Override
			public void putANewItem() {
			}
		};
		
		game.setPresentation(gridMock);
		game.setTable(t);
		
		t.table[0][3] = 2;
		
		when(input.readFromKeyboard()).thenReturn(KEY_LEFT, KEY_DOWN, KEY_RIGHT, KEY_UP);
		when(input.readFromKeyboard()).thenThrow(GAME_OVER);
		
		try {
			game.start();
		} catch (GameInterruptedException e) {}
		
		assertThat(t.table[0][0], is(0));
		assertThat(t.table[0][3], is(2));
	}
	
	@Test
	public void findNextEmptyCellInRow() throws Exception {
		
		t = new Table() {
			@Override
			Cell getRandomCell() {
				return new Cell(2, 1, 0);
			}
		};
		
		t.table[0][1] = 2;
		t.putANewItem();
		
		assertThat(t.table[0][2], is(2));
	}
	
	@Test
	public void dontOverwriteExistingCells() throws Exception {
		t.table[0][1] = 2;
		t.table[0][2] = 2;
		t.table[0][3] = 2;
		
		t.table[1][0] = 2;
		t.table[1][1] = 2;
		t.table[1][2] = 2;
		t.table[1][3] = 2;

		t.table[2][0] = 2;
		t.table[2][1] = 2;
		t.table[2][2] = 2;
		t.table[2][3] = 2;
		
		t.table[3][0] = 2;
		t.table[3][1] = 2;
		t.table[3][2] = 2;
		t.table[3][3] = 2;
		
		t.putANewItem();
		
		assertTrue(t.table[0][0] > 0);
	}
	
	@Test(expected = GameInterruptedException.class)
	public void quitWithQ() throws Exception {
		when(input.readFromKeyboard()).thenReturn(KEY_QUIT);
		game.setPresentation(gridMock);
		game.start();
		
	}
	
	@Test
	public void dontPutNextCellIfNoMove() throws Exception {
		t = new Table() {
			int i = 0;
			
			@Override
			Cell getRandomCell() {
				return new Cell(2, 3, i++);
			}
		};
		
		game.setPresentation(gridMock);
		game.setTable(t);
		
		when(input.readFromKeyboard()).thenReturn(KEY_RIGHT, KEY_QUIT);
		
		try {
			game.start();
		} catch (Exception e) {}
		
		assertThat(countCells(), is(1));
	}
	
	private void assertThatTablePrintedCorrectly() {
		inOrder.verify(out).print(ANSI_CLS + ANSI_HOME);
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
		return UPPER_LEFT + print(3, print(4, HORIZONTAL) + BORDER_TOP) + print(4, HORIZONTAL) + UPPER_RIGHT + 			System.lineSeparator();
	}
	
	private String bottomBorderRow() {
		return LOWER_LEFT + print(3, print(4, HORIZONTAL) + BORDER_BOTTOM) + print(4, HORIZONTAL) + LOWER_RIGHT + 			System.lineSeparator();
	}

	private String middleBorderRow() {
		return CELL_LEFT + 
				print(3, print(4, HORIZONTAL) + 
				CELL_MIDDLE) + 
				print(4, HORIZONTAL) + 
				CELL_RIGHT + 
				System.lineSeparator();
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
