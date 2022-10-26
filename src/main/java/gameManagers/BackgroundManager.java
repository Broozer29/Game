package gameManagers;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Data.DataClass;
import Data.ImageLoader;
import gameObjectes.BackgroundObject;
import gameObjectes.Sprite;

public class BackgroundManager {

	private static BackgroundManager instance = new BackgroundManager();
	private DataClass dataClass = DataClass.getInstance();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private List<BackgroundObject> levelOneObjects = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelTwoObjects = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelThreeObjects = new ArrayList<BackgroundObject>();
	private List<String> allBackgroundObjects = new ArrayList<String>();
	private Map<String, Image> scaledBackgroundObjects = new HashMap<String, Image>();
	private int maximumWidthRange = DataClass.getInstance().getWindowWidth() + 200;
	private int minimumWidthRange = -200;
	private int maximumHeightRange = DataClass.getInstance().getWindowHeight() + 100;
	private int minimumHeightRange = -100;
	Random random = new Random();
	private int updateFrameCounter = 0;
	private int levelOneModifier = 100;
	private int levelTwoModifier = 200;
	private int levelThreeModifier = 350;

	private BackgroundManager() {
		initManager();
	}

	private void initManager() {
		allBackgroundObjects.add("moon1");
		allBackgroundObjects.add("lavaplanet1");
		allBackgroundObjects.add("marsplanet1");
		allBackgroundObjects.add("planet1");
		allBackgroundObjects.add("planet2");
		allBackgroundObjects.add("planet3");
		loadAllPlanets();
		initLists();
	}

	public static BackgroundManager getInstance() {
		return instance;
	}

	private void initLists() {
		fillLevelOne();
		fillLevelTwo();
		fillLevelThree();
	}

	public void updateGameTick() {
		updateObjects();
	}

	// Fill the first list with objects
	private void fillLevelOne() {
		for (int i = 0; i < 1; i++) {
			Image planetImage = setObjectScale(getRandomPlanet(), 1);
			levelTwoObjects.add(new BackgroundObject(getRandomXCoordinate(), getRandomYCoordinate(), planetImage));
		}
	}

	// Fill the second list with objects
	private void fillLevelTwo() {
		for (int i = 0; i < 1; i++) {
			Image planetImage = setObjectScale(getRandomPlanet(), 2);
			levelTwoObjects.add(new BackgroundObject(getRandomXCoordinate(), getRandomYCoordinate(), planetImage));
		}
	}

	// Fill the third list with objects
	private void fillLevelThree() {
		for (int i = 0; i < 1; i++) {
			Image planetImage = setObjectScale(getRandomPlanet(), 3);
			levelTwoObjects.add(new BackgroundObject(getRandomXCoordinate(), getRandomYCoordinate(), planetImage));
		}
	}

	// Itereer alle lijsten en beweeg objecten x-1
	// If objects is outside the window, replace it on the right side and get a new
	// random Y & planet image
	private void updateObjects() {
		if (updateFrameCounter == 2) {
			for (BackgroundObject bgObject : levelOneObjects) {
				if ((bgObject.getXCoordinate() + levelOneModifier + 20) < (0 - levelOneModifier)) {

					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(getRandomYCoordinate());
					bgObject.setNewPlanetImage(setObjectScale(getRandomPlanet(), 1));
				}
				bgObject.setX(bgObject.getXCoordinate() - 1);
			}
		}

		if (updateFrameCounter == 1) {
			for (BackgroundObject bgObject : levelTwoObjects) {
				if ((bgObject.getXCoordinate() + levelTwoModifier + 20) < (0 - levelTwoModifier)) {

					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(getRandomYCoordinate());
					bgObject.setNewPlanetImage(setObjectScale(getRandomPlanet(), 2));
				}
				bgObject.setX(bgObject.getXCoordinate() - 1);
			}
		}

		for (BackgroundObject bgObject : levelThreeObjects) {

			if ((bgObject.getXCoordinate() + levelThreeModifier + 20) < (0 - levelThreeModifier)) {

				bgObject.setX(dataClass.getWindowWidth() + 200);
				bgObject.setY(getRandomYCoordinate());
				bgObject.setNewPlanetImage(setObjectScale(getRandomPlanet(), 3));
			}
			bgObject.setX(bgObject.getXCoordinate() - 1);
		}

		//Update the framecounter and reset it to 0; for different movement speeds between planets
		if (updateFrameCounter > 2) {
			updateFrameCounter = 0;
		}
		updateFrameCounter++;
	}

	private Image getRandomPlanet() {
		int randomPlanet = getRandomBackgroundObject();
		String randomPlanetString = allBackgroundObjects.get(randomPlanet);
		return scaledBackgroundObjects.get(randomPlanetString);
	}

	private int getRandomXCoordinate() {
		return random.nextInt((maximumWidthRange - minimumWidthRange) + 1) + minimumWidthRange;
	}

	private int getRandomYCoordinate() {
		return random.nextInt((maximumHeightRange - minimumHeightRange) + 1) + minimumHeightRange;
	}

	// Returns a random planet to display
	private int getRandomBackgroundObject() {
		return random.nextInt((scaledBackgroundObjects.size() - 1) + 1) + 0;
	}

	// Objects require different scales to create illusion of depth
	private Image setObjectScale(Image image, int level) {
		switch (level) {
		case (1):
			return image.getScaledInstance(levelOneModifier, levelOneModifier, Image.SCALE_SMOOTH);
		case (2):
			return image.getScaledInstance(levelTwoModifier, levelTwoModifier, Image.SCALE_SMOOTH);
		case (3):
			return image.getScaledInstance(levelThreeModifier, levelThreeModifier, Image.SCALE_SMOOTH);
		}
		return image;
	}

	// Initially loads all planets so they dont have to be reloaded from files.
	// Saves memory
	private void loadAllPlanets() {
		for (String object : allBackgroundObjects) {
			Image img = imageLoader.getImage(object);
			scaledBackgroundObjects.put(object, img);
		}

	}

	public List<BackgroundObject> getLevelOneObjects() {
		return levelOneObjects;
	}

	public List<BackgroundObject> getLevelTwoObjects() {
		return levelTwoObjects;
	}

	public List<BackgroundObject> getLevelThreeObjects() {
		return levelThreeObjects;
	}

}
