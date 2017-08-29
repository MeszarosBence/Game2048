package bence.game2048;

public interface Grid {
	void display();
	int readFromKeyboard() throws GameInterruptedException;
}