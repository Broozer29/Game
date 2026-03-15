package net.riezebos.bruus.tbd.visualsandaudio.data;

public class DataClass {
	// A singleton class that holds information that is relevant for multiple
	// classes spread throughout the program
	private static DataClass instance = new DataClass();


	//Original dimensions: 1440, 875
	private int windowWidth = 1728;
	private int windowHeight = 1077;
	private String textFont = "Lucida Grande";


	private DataClass() {
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
		return windowHeight;
	}

	public int getInformationCardHeight () {
		return Math.round(60 * getResolutionFactor());
	}

	public int getBoardBlockWidth() {
		return this.windowWidth / getBoardBlockAmount();
	}

	public int getBoardBlockAmount(){
		return 8;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public String getTextFont () {
		return textFont;
	}
}