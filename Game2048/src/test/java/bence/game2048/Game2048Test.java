package bence.game2048;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
	
	private static final CellValue VALUE_2 = new CellValue(2);
	private static final CellValue VALUE_0 = new CellValue(0);
	private static final CellValue VALUE_4 = new CellValue(4);
	private static final CellValue VALUE_8 = new CellValue(8);

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

	Table tableControl = new Table();
	CellValue[][] table = new CellValue[Table.TABLE_SIZE][Table.TABLE_SIZE]; 
	
	@Spy
	private PrintStream out = new PrintStream(AnsiConsole.out);
	InOrder inOrder = null;

	Game2048 game = new Game2048();
	ConsoleGrid consoleGrid = new ConsoleGrid(tableControl);
	Presentation presentationMock = mock(Presentation.class);
	Input inputMock = mock(Input.class);
	
	@Before
	public void setup() {
		consoleGrid.setOutput(out);
		consoleGrid.setDelay(0);
		game.setPresentation(consoleGrid);
		game.setTable(tableControl);
		game.setInput(inputMock);
		inOrder = inOrder(out);
		reset(table);
	}
	
	@Test
	public void printTableWithoutContent() throws Exception {
		
		consoleGrid.display();
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void printContentOneElement() throws Exception {
		table[0][0] = VALUE_2;
		VALUE_2.resetNew();
		tableControl.board = table;

		consoleGrid.display();
		
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void moving() throws Exception {
		out.print(ANSI_CLS);
		table[0][0] = VALUE_2;
		tableControl.board = table;

		consoleGrid.display();
		
//		Thread.sleep(500);  //In order to verify it visually
		
		assertThatTablePrintedCorrectly();
		out.print(ANSI_CLS);
		
		table[0][0] = VALUE_0;
		table[0][1] = VALUE_2;
		consoleGrid.display();
		
//		Thread.sleep(500); //In order to verify it visually
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void printContentAllElements() throws Exception {
		table[0][0] = VALUE_2;
		table[0][1] = VALUE_2;
		table[0][2] = VALUE_2;
		table[0][3] = VALUE_2;
		table[1][0] = VALUE_2;
		table[1][1] = VALUE_2;
		table[1][2] = VALUE_2;
		table[1][3] = VALUE_2;
		table[2][0] = VALUE_2;
		table[2][1] = VALUE_2;
		table[2][2] = VALUE_2;
		table[2][3] = VALUE_2;
		table[3][0] = VALUE_2;
		table[3][1] = VALUE_2;
		table[3][2] = VALUE_2;
		table[3][3] = VALUE_2;
		tableControl.board = table;
		
		consoleGrid.display();
		
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void placeFirstCellRandomly() throws Exception {
		
		when(inputMock.readFromKeyboard()).thenThrow(GAME_OVER);
		
		game.start();

		assertThat(countCells(), is(1));
		
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
		tableControl.board[0][3] = VALUE_2;
		consoleGrid.display();
		
		game.left();
		
		assertThat(tableControl.board[0][0], is(VALUE_2));
		consoleGrid.display();
	}
	
	@Test
	public void moveValuesLeftAndAddThemIfTheyAreEqual() throws Exception {
		tableControl.board[0][3] = VALUE_2;
		tableControl.board[0][2] = VALUE_2;
		consoleGrid.display();
		
		game.left();
		
		assertThat(tableControl.board[0][0], is(VALUE_4));
		assertThat(tableControl.board[0][1], is(VALUE_0));
		assertThat(tableControl.board[0][2], is(VALUE_0));
		assertThat(tableControl.board[0][3], is(VALUE_0));
		consoleGrid.display();
	}
	
	@Test
	public void moveValuesLeftAndDontAddThemIfTheyAreNotEqual() throws Exception {
		tableControl.board[3][3] = VALUE_4;
		tableControl.board[3][2] = VALUE_2;
		consoleGrid.display();
		
		game.left();
		
		assertThat(tableControl.board[3][0], is(VALUE_2));
		assertThat(tableControl.board[3][1], is(VALUE_4));
		consoleGrid.display();
	}
	
	@Test
	public void dontMoveValuesIfTheyAlreadyInPosition() throws Exception {
		tableControl.board[3][3] = VALUE_4;
		tableControl.board[3][2] = VALUE_2;
		tableControl.board[3][1] = VALUE_4;
		tableControl.board[3][0] = VALUE_2;
		consoleGrid.display();
		
		game.left();
		
		assertThat(tableControl.board[3][0], is(VALUE_2));
		assertThat(tableControl.board[3][1], is(VALUE_4));
		assertThat(tableControl.board[3][2], is(VALUE_2));
		assertThat(tableControl.board[3][3], is(VALUE_4));
		consoleGrid.display();
	}
	
	@Test
	public void moveMultipleRowsLeftAndAddValues() throws Exception {
		tableControl.board[3][3] = VALUE_2;
		tableControl.board[3][2] = VALUE_2;
		tableControl.board[2][3] = VALUE_2;
		tableControl.board[2][2] = VALUE_2;
		
		consoleGrid.display();
		
		game.left();
		
		assertThat(tableControl.board[2][0], is(VALUE_4));
		assertThat(tableControl.board[2][1], is(VALUE_0));
		assertThat(tableControl.board[2][2], is(VALUE_0));
		assertThat(tableControl.board[2][3], is(VALUE_0));

		
		assertThat(tableControl.board[3][0], is(VALUE_4));
		assertThat(tableControl.board[3][1], is(VALUE_0));
		assertThat(tableControl.board[3][2], is(VALUE_0));
		assertThat(tableControl.board[3][3], is(VALUE_0));
		

		consoleGrid.display();
	}
	
	@Test
	public void moveOneValueRight() throws Exception {
		tableControl.board[0][0] = VALUE_2;
		consoleGrid.display();
		
		game.right();
		
		assertThat(tableControl.board[0][3], is(VALUE_2));
		consoleGrid.display();
	}
	
	@Test
	public void addAndMoveTwoValuesRight() throws Exception {
		tableControl.board[0][0] = VALUE_2;
		tableControl.board[0][2] = VALUE_2;
		consoleGrid.display();
		
		game.right();
		
		assertThat(tableControl.board[0][2], is(VALUE_0));
		assertThat(tableControl.board[0][3], is(VALUE_4));
		consoleGrid.display();
	}
	
	@Test
	public void moveOneValueUp() throws Exception {
		tableControl.board[3][0] = VALUE_2;
		consoleGrid.display();
		
		game.up();
		
		assertThat(tableControl.board[0][0], is(VALUE_2));
		consoleGrid.display();
	}
	
	@Test
	public void moveTwoValuesUp() throws Exception {
		tableControl.board[1][0] = VALUE_2;
		tableControl.board[3][0] = VALUE_2;
		consoleGrid.display();
		
		game.up();
		
		assertThat(tableControl.board[0][0], is(VALUE_4));
		consoleGrid.display();
	}
	
	@Test
	public void moveThreeValuesUp() throws Exception {
		tableControl.board[1][0] = VALUE_2;
		tableControl.board[2][0] = VALUE_4;
		tableControl.board[3][0] = VALUE_8;
		consoleGrid.display();
		
		game.up();
		
		assertThat(tableControl.board[0][0], is(VALUE_2));
		assertThat(tableControl.board[1][0], is(VALUE_4));
		assertThat(tableControl.board[2][0], is(VALUE_8));
		consoleGrid.display();
	}
	
	
	@Test
	public void fullTableUp() throws Exception {
		tableControl.board[0][0] = VALUE_2;
		tableControl.board[1][0] = VALUE_2;
		tableControl.board[2][0] = VALUE_2;
		tableControl.board[3][0] = VALUE_2;
		
		tableControl.board[0][1] = VALUE_2;
		tableControl.board[1][1] = VALUE_2;
		tableControl.board[2][1] = VALUE_2;
		tableControl.board[3][1] = VALUE_2;
		
		tableControl.board[0][2] = VALUE_2;
		tableControl.board[1][2] = VALUE_2;
		tableControl.board[2][2] = VALUE_2;
		tableControl.board[3][2] = VALUE_2; 
		
		tableControl.board[0][3] = VALUE_2;
		tableControl.board[1][3] = VALUE_2;
		tableControl.board[2][3] = VALUE_2;
		tableControl.board[3][3] = VALUE_2;
		consoleGrid.display();
		
		game.up();
		
		assertThat(tableControl.board[0][0], is(VALUE_8));
		assertThat(tableControl.board[0][1], is(VALUE_8));
		assertThat(tableControl.board[0][2], is(VALUE_8));
		assertThat(tableControl.board[0][3], is(VALUE_8));
		consoleGrid.display();
	}
	
	@Test
	public void down() throws Exception {
		tableControl.board[0][0] = VALUE_2;
		tableControl.board[1][0] = VALUE_2;
		tableControl.board[2][0] = VALUE_2;
		tableControl.board[3][0] = VALUE_2;
		
		tableControl.board[0][1] = VALUE_2;
		tableControl.board[1][1] = VALUE_2;
		tableControl.board[2][1] = VALUE_2;
		tableControl.board[3][1] = VALUE_2;
		
		tableControl.board[0][2] = VALUE_2;
		tableControl.board[1][2] = VALUE_2;
		tableControl.board[2][2] = VALUE_2;
		tableControl.board[3][2] = VALUE_2; 
		
		tableControl.board[0][3] = VALUE_2;
		tableControl.board[1][3] = VALUE_2;
		tableControl.board[2][3] = VALUE_2;
		tableControl.board[3][3] = VALUE_2;
		consoleGrid.display();
		
		game.down();
		
		assertThat(tableControl.board[3][0], is(VALUE_8));
		assertThat(tableControl.board[3][1], is(VALUE_8));
		assertThat(tableControl.board[3][2], is(VALUE_8));
		assertThat(tableControl.board[3][3], is(VALUE_8));
		consoleGrid.display();
	}
	
	@Test
	public void buttonLeft() throws Exception {
		prepareTableWith(new Cell(2, 3, 0));
		
		
		when(inputMock.readFromKeyboard()).thenReturn(KEY_LEFT).thenThrow(GAME_OVER);
		
		game.start();
		
		assertThat(tableControl.board[0][0], is(VALUE_2));
	}

	
	@Test
	public void readMultipleKeyStrokes() throws Exception {
		prepareTableWith(new Cell(2, 3, 0));
		
		
		when(inputMock.readFromKeyboard()).thenReturn(KEY_LEFT, KEY_DOWN, KEY_RIGHT, KEY_UP);
		when(inputMock.readFromKeyboard()).thenThrow(GAME_OVER);
		
		game.start();
		
		assertThat(tableControl.board[0][0], is(VALUE_0));
		assertThat(tableControl.board[0][3], is(VALUE_2));
	}
	
	@Test
	public void findNextEmptyCellInRow() throws Exception {
		
		prepareTableWith(new Cell(2, 1, 0));
		
		tableControl.board[0][1] = VALUE_2;
		tableControl.putANewItem();
		
		assertThat(tableControl.board[0][2], is(VALUE_2));
	}
	
	@Test
	public void findNextEmptyCellInNextRow() throws Exception {
		
		prepareTableWith(new Cell(2, 1, 0));
		
		tableControl.board[0][0] = VALUE_2;
		tableControl.board[0][1] = VALUE_2;
		tableControl.board[0][2] = VALUE_2;
		tableControl.board[0][3] = VALUE_2;
		
		tableControl.putANewItem();
		
		assertThat(tableControl.board[1][0], is(VALUE_2));
	}
	
	@Test
	public void dontOverwriteExistingCells() throws Exception {
		tableControl.board[0][1] = VALUE_2;
		tableControl.board[0][2] = VALUE_2;
		tableControl.board[0][3] = VALUE_2;
		
		tableControl.board[1][0] = VALUE_2;
		tableControl.board[1][1] = VALUE_2;
		tableControl.board[1][2] = VALUE_2;
		tableControl.board[1][3] = VALUE_2;

		tableControl.board[2][0] = VALUE_2;
		tableControl.board[2][1] = VALUE_2;
		tableControl.board[2][2] = VALUE_2;
		tableControl.board[2][3] = VALUE_2;
		
		tableControl.board[3][0] = VALUE_2;
		tableControl.board[3][1] = VALUE_2;
		tableControl.board[3][2] = VALUE_2;
		tableControl.board[3][3] = VALUE_2;
		
		tableControl.putANewItem();
		
		assertTrue(tableControl.board[0][0].getValue() > 0);
	}
	
	public void quitWithQ() throws Exception {
		when(inputMock.readFromKeyboard()).thenReturn(KEY_QUIT);
		game.setPresentation(presentationMock);
		game.start();
	}
	
	@Test
	public void dontPutNextCellIfNoMove() throws Exception {
		tableControl = new Table() {
			int i = 0;
			
			@Override
			Cell getRandomCell() {
				return new Cell(2, 3, i++);
			}
		};
		
		game.setPresentation(presentationMock);
		game.setTable(tableControl);
		
		when(inputMock.readFromKeyboard()).thenReturn(KEY_RIGHT, KEY_QUIT);
		
		game.start();
		
		assertThat(countCells(), is(1));
	}
	
	@Test
	public void printMessageWhenQuit() throws Exception {
		game.setPresentation(presentationMock);
		when(inputMock.readFromKeyboard()).thenReturn(KEY_QUIT);
		
		game.start();
		
		verify(presentationMock).message("Game Interrupted");
	}
	
	
	@Test
	public void delayedPrintOfNewCells() throws Exception {
		
		tableControl = new Table() {
			@Override
			Cell getRandomCell() {
				return new Cell(2, 0, 0);
			}
		};

		when(inputMock.readFromKeyboard()).thenReturn(KEY_QUIT);
		
		game.setTable(tableControl);
		consoleGrid.table = tableControl;
		game.start();
		
		table[0][0] = VALUE_2;
		table[0][0].setNew();
		assertThatTablePrintedCorrectly();
		table[0][0].resetNew();
		assertThatTablePrintedCorrectly();
	}
	
	@Test
	public void newCellWithTwoOrFourValues() throws Exception {
		boolean twoReturned = false;
		boolean fourReturned = false;
		int i = 0;
		while (i < 500 || !(twoReturned && fourReturned)) {
			if (tableControl.getRandomCell().getValue().equals(VALUE_2)) {
				twoReturned = true;
			} else fourReturned = true;
			i++;
		}
		
		assertThat(fourReturned,is(true));
		assertThat(twoReturned,is(true));
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

	private String contentRow(CellValue[] content) {
		StringBuilder row = new StringBuilder(VERTICAL);
		for (int i = 0; i < content.length; i++) {
			row.append(printCell(content[i]));
			row.append(VERTICAL);
		}
		row.append(System.lineSeparator());
		
		return row.toString();
	}

	private String printCell(CellValue cell) {
		if (cell.getValue() < 2 || cell.isNew()) return "    ";
		else if (cell.getValue() < 10 ) return "  " + cell + " ";
		else if (cell.getValue() < 100) return " " + cell + " ";
		else if (cell.getValue() < 1000) return " " + cell;
		
		return cell.toString();
	}

	private String print(int times, String character) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < times; i++) {
			output.append(character);
		}
		return output.toString();
	}
	
	private int countCells() {
		int found = 0;
		for (int i = 0; i < tableControl.board.length; i++) {
			for (int j = 0; j < tableControl.board.length; j++) {
				if (tableControl.board[i][j].getValue() > 0) found++;
			}
		}
		return found;
	}
	
	private void prepareTableWith(final Cell cell) {
		tableControl = new Table() {
			@Override
			Cell getRandomCell() {
				return cell;
			}
		};
		
		game.setTable(tableControl);
		game.setPresentation(presentationMock);
	}
	
	private void reset(CellValue[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table.length; j++) {
				table[i][j] = new CellValue(0);
			}
		}
	}
}
