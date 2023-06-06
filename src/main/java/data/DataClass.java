package data;

public class DataClass {
	// A singleton class that holds information that is relevant for multiple
	// classes spread throughout the program
	private static DataClass instance = new DataClass();

	private int windowWidth = 1440;
	private int windowHeight = 920;
	
//	private int windowWidth = 1280;
//	private int windowHeight = 720;
	
	
	
	// Tijdelijke string user, liever een domain object

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

}
