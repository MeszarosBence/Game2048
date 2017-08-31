package bence.game2048;

import java.io.IOException;

import biz.source_code.utils.RawConsoleInput;

public class ConsoleInput implements Input {

	public int readFromKeyboard() throws GameInterruptedException {
		int input = 0;
		try {
			input = RawConsoleInput.read(true);
		} catch (IOException e) {
			throw new GameInterruptedException(e);
		}
		return input;
	}

}
