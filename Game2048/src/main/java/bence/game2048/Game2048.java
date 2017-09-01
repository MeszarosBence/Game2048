package bence.game2048;

public class Game2048 {

	enum DIR {
		UP, DOWN, LEFT, RIGHT
	}

	private static final int KEY_LEFT = 57419;
	private static final int KEY_RIGHT = 57421;
	private static final int KEY_UP = 57416;
	private static final int KEY_DOWN = 57424;
	private static final int KEY_QUIT = 113;

	Table table;
	Presentation presentation;
	Input input;
	
	public Game2048() {
		this.table = new Table();
		this.presentation = new ConsoleGrid(table);
		this.input = new ConsoleInput();
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	
	public void setInput(Input input) {
		this.input = input;
	}

	public void left() {
		table.moveLeft();
	}

	
	public void right() {
		table.moveRight();
	}

	public void up() {
		table.moveUp();
	}

	public void down() {
		table.moveDown();
	}

	public void start() {
		drawANewItem();
		
		try {
			while(true) {
				processUserInput();
				if (isValidMove()) {
					drawANewItem();
				}
			}
		} catch (GameInterruptedException e) {
			message("Game Interrupted");
		}
	}

	private void message(String text) {
		presentation.message(text);
	}

	private boolean isValidMove() {
		return table.wereCellsMoved();
	}

	private void drawANewItem() {
		table.putANewItem();
		presentation.display();
	}

	public void processUserInput() throws GameInterruptedException {
		switch (input.readFromKeyboard()) {
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
		case KEY_QUIT:
			throw new GameInterruptedException("Game Interrupted");
		}
	}
}
