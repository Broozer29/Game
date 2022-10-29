package Data;

import domain.User;

public class DataClass {
	// A singleton class that holds information that is relevant for multiple
	// classes spread throughout the program
	private static DataClass instance = new DataClass();

	private int windowWidth = 1280;
	private int windowHeight = 720;
	
	// Tijdelijke string user, liever een domain object
	private String currentUser;

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

	public void setCurrentUser(String user) {
		this.currentUser = user;
	}

}
