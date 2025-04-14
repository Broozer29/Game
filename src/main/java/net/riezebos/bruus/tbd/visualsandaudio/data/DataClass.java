package net.riezebos.bruus.tbd.visualsandaudio.data;

public class DataClass {
	// A singleton class that holds information that is relevant for multiple
	// classes spread throughout the program
	private static DataClass instance = new DataClass();


	//Original dimensions: 1440, 875
	private int windowWidth = 1728;
	private int windowHeight = 1077;
	private String textFont = "Lucida Grande";

	private int informationCardWidth = 0;
	private int informationCardHeight = 0;
	
	private DataClass() {
		informationCardWidth = windowWidth;
		informationCardHeight = Math.round(100 * getResolutionFactor());
	}

	public float getResolutionFactor(){
		return (float) windowWidth / 1440;
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

	public int getPlayableWindowMinHeight(){
		return 0;
	}

	public int getPlayableWindowMaxHeight(){
		return windowHeight - informationCardHeight;
	}

	public int getInformationCardWidth () {
		return informationCardWidth;
	}

	public int getInformationCardHeight () {
		return informationCardHeight;
	}

	public int getBoardBlockWidth() {
		return this.windowWidth / 8;
	}

	public String getTextFont () {
		return textFont;
	}
}