package bence.game2048;

import java.io.IOException;

public class GameInterruptedException extends RuntimeException {

	private static final long serialVersionUID = 4126272880857092484L;
	public GameInterruptedException(IOException e) {
		super(e);
	}

	public GameInterruptedException(String message) {
		super(message);
	}


}
