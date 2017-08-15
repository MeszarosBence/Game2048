package bence.game2048;


import static org.mockito.Mockito.inOrder;

import java.io.PrintStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

public class Game2048Test {
	
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
	
	int[][] content = new int[4][4];
	
	@Spy
	private PrintStream out = new PrintStream(System.out);
	InOrder inOrder = null;

	Game2048 g = new Game2048();
	
	@Before
	public void setup() {
		g.out = out;
		g.content = content;
		inOrder = inOrder(out);
		
	}

	@Test
	public void testName() throws Exception {
		
	}
	
	@Test
	public void testPrintTableWithoutContent() throws Exception {
		
		g.printTable();
		
		inOrder.verify(out).print(topBorderRow());
		inOrder.verify(out).print(contentRow(content[0]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(content[1]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(content[2]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(content[3]));
		inOrder.verify(out).print(bottomBorderRow());
		
	}
	
	@Test
	public void testPrintContentOneElement() throws Exception {
		content[0][0] = 2;
		
		g.content = content;
		
		g.printTable();
		
		inOrder.verify(out).print(topBorderRow());
		inOrder.verify(out).print(contentRow(content[0]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(content[1]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(content[2]));
		inOrder.verify(out).print(middleBorderRow());
		inOrder.verify(out).print(contentRow(content[3]));
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
