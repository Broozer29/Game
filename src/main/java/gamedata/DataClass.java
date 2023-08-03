package gamedata;

public class DataClass {
	// A singleton class that holds information that is relevant for multiple
	// classes spread throughout the program
	private static DataClass instance = new DataClass();

	private int windowWidth = 1440;
	private int windowHeight = 875;
	
	private DataClass() {
	}

	public static DataClass getInstance() {
		return instance;
	}
	
	public int getWindowWidth() {
		return this.windowWidth;
	}

	public int getWindowHeight() {
		return this.windowHeight;
	}
	
	public int getBoardBlockWidth() {
		return this.windowWidth / 8;
	}

}