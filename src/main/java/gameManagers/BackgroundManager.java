package gameManagers;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Data.DataClass;
import Data.ImageLoader;
import Data.RandomCoordinator;
import imageObjects.BackgroundObject;

public class BackgroundManager {

	private static BackgroundManager instance = new BackgroundManager();
	private DataClass dataClass = DataClass.getInstance();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private RandomCoordinator randomCoordinator = RandomCoordinator.getInstance();
	private List<BackgroundObject> levelOnePlanets = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelTwoPlanets = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelThreePlanets = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelOneStars = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelTwoStars = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelThreeStars = new ArrayList<BackgroundObject>();

	private List<String> allBackgroundObjectStringCodes = new ArrayList<String>();
	private Map<String, Image> scaledBackgroundObjects = new HashMap<String, Image>();

	Random random = new Random();
	private int updateFrameCounter = 0;
	private int levelOneObjectModifier = 75;
	private int levelTwoObjectModifier = 150;
	private int levelThreeObjectModifier = 225;
	private int levelOneStarModifier = 3;
	private int levelTwoStarModifier = 5;
	private int levelThreeStarModifier = 7;

	private BackgroundManager() {
		initManager();
	}

	private void initManager() {
		allBackgroundObjectStringCodes.add("moon1");
		allBackgroundObjectStringCodes.add("lavaplanet1");
		allBackgroundObjectStringCodes.add("marsplanet1");
		allBackgroundObjectStringCodes.add("planet1");
		allBackgroundObjectStringCodes.add("planet2");
		allBackgroundObjectStringCodes.add("planet3");
		loadAllPlanets();
		initLists();
	}

	public static BackgroundManager getInstance() {
		return instance;
	}

	private void initLists() {
		fillLevelOnePlanets();
		fillLevelTwoPlanets();
		fillLevelThreePlanets();
		fillLevelOneStars();
		fillLevelTwoStars();
		fillLevelThreeStars();
	}

	public void updateGameTick() {
		updateObjects();
	}

	// Fill the first list with stars
	private void fillLevelOneStars() {
		while (levelOneStars.size() < 25) {
			Image starImage = setStarScale(imageLoader.getImage("star"), 1);
			int randomXCoordinate = randomCoordinator.getRandomXBGOCoordinate();
			int randomYCoordinate = randomCoordinator.getRandomYBGOCoordinate();
			if (randomCoordinator.checkValidBGOXCoordinate(levelOneStars, randomXCoordinate)
					&& randomCoordinator.checkValidBGOYCoordinate(levelOneStars, randomYCoordinate)) {
				levelOneStars.add(new BackgroundObject(randomXCoordinate, randomYCoordinate, starImage));
			}
		}

	}

	// Fill the second list with stars
	private void fillLevelTwoStars() {
		while (levelTwoStars.size() < 25) {
			Image starImage = setStarScale(imageLoader.getImage("star"), 2);
			int randomXCoordinate = randomCoordinator.getRandomXBGOCoordinate();
			int randomYCoordinate = randomCoordinator.getRandomYBGOCoordinate();
			if (randomCoordinator.checkValidBGOXCoordinate(levelOneStars, randomXCoordinate)
					&& randomCoordinator.checkValidBGOYCoordinate(levelOneStars, randomYCoordinate)) {
				levelTwoStars.add(new BackgroundObject(randomXCoordinate, randomYCoordinate, starImage));
			}
		}
	}

	// Fill the third list with stars
	private void fillLevelThreeStars() {
		while (levelThreeStars.size() < 25) {
			Image starImage = setStarScale(imageLoader.getImage("star"), 3);
			int randomXCoordinate = randomCoordinator.getRandomXBGOCoordinate();
			int randomYCoordinate = randomCoordinator.getRandomYBGOCoordinate();
			if (randomCoordinator.checkValidBGOXCoordinate(levelOneStars, randomXCoordinate)
					&& randomCoordinator.checkValidBGOYCoordinate(levelOneStars, randomYCoordinate)) {
				levelThreeStars.add(new BackgroundObject(randomXCoordinate, randomYCoordinate, starImage));
			}
		}
	}

	// Fill the first list with objects
	private void fillLevelOnePlanets() {
		for (int i = 0; i < 1; i++) {
			Image planetImage = setPlanetScale(getRandomPlanet(), 1);
			levelOnePlanets.add(new BackgroundObject(randomCoordinator.getRandomXBGOCoordinate(),
					randomCoordinator.getRandomYBGOCoordinate(), planetImage));
		}
	}

	// Fill the second list with objects
	private void fillLevelTwoPlanets() {
		for (int i = 0; i < 1; i++) {
			Image planetImage = setPlanetScale(getRandomPlanet(), 2);
			levelTwoPlanets.add(new BackgroundObject(randomCoordinator.getRandomXBGOCoordinate(),
					randomCoordinator.getRandomYBGOCoordinate(), planetImage));
		}
	}

	// Fill the third list with objects
	private void fillLevelThreePlanets() {
		for (int i = 0; i < 1; i++) {
			Image planetImage = setPlanetScale(getRandomPlanet(), 3);
			levelThreePlanets.add(new BackgroundObject(randomCoordinator.getRandomXBGOCoordinate(),
					randomCoordinator.getRandomYBGOCoordinate(), planetImage));
		}
	}

	// Itereer alle lijsten en beweeg objecten x-1
	// If objects is outside the window, replace it on the right side and get a new
	// random Y & planet image
	private void updateObjects() {
		// Updates all level one objects. If their X position is below 0, reset them to
		// windowwidth. If not, move them by one pixel;
		if (updateFrameCounter % 4 == 0) {
			for (BackgroundObject bgObject : levelOnePlanets) {
				if ((bgObject.getXCoordinate() + levelOneObjectModifier + 20) < (0 - levelOneObjectModifier)) {
					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
					bgObject.setNewPlanetImage(setPlanetScale(getRandomPlanet(), 1));
				}
				bgObject.setX(bgObject.getXCoordinate() - 1);
			}

			for (BackgroundObject bgObject : levelOneStars) {
				if ((bgObject.getXCoordinate() + levelOneStarModifier + 20) < (0 - levelOneStarModifier)) {
					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
				}
				bgObject.setX(bgObject.getXCoordinate() - 1);
			}
		}

		// Updates all level two objects. If their X position is below 0, reset them to
		// windowwidth. If not, move them by one pixel;
		if (updateFrameCounter % 3 == 0) {
			for (BackgroundObject bgObject : levelTwoPlanets) {
				if ((bgObject.getXCoordinate() + levelTwoObjectModifier + 20) < (0 - levelTwoObjectModifier)) {
					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
					bgObject.setNewPlanetImage(setPlanetScale(getRandomPlanet(), 2));
				}
				bgObject.setX(bgObject.getXCoordinate() - 1);
			}

			for (BackgroundObject bgObject : levelTwoStars) {
				if ((bgObject.getXCoordinate() + levelTwoStarModifier + 20) < (0 - levelTwoStarModifier)) {
					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
				}
				bgObject.setX(bgObject.getXCoordinate() - 1);
			}
		}

		// Updates all level three objects. If their X position is below 0, reset them
		// to windowwidth. If not, move them by one pixel;
		if (updateFrameCounter % 2 == 0) {
			for (BackgroundObject bgObject : levelThreePlanets) {
				if ((bgObject.getXCoordinate() + levelThreeObjectModifier + 20) < (0 - levelThreeObjectModifier)) {
					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
					bgObject.setNewPlanetImage(setPlanetScale(getRandomPlanet(), 3));
				}
				bgObject.setX(bgObject.getXCoordinate() - 1);
			}

			for (BackgroundObject bgObject : levelThreeStars) {
				if ((bgObject.getXCoordinate() + levelThreeStarModifier + 20) < (0 - levelThreeStarModifier)) {
					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
				}
				bgObject.setX(bgObject.getXCoordinate() - 1);
			}
		}

		// Update the framecounter and reset it to 0; for different movement speeds
		// between planets
		if (updateFrameCounter > 10) {
			updateFrameCounter = 0;
		}
		updateFrameCounter++;
	}

	private Image getRandomPlanet() {
		int randomPlanet = getRandomBackgroundObject();
		String randomPlanetString = allBackgroundObjectStringCodes.get(randomPlanet);
		return scaledBackgroundObjects.get(randomPlanetString);
	}

	// Returns a random planet to display
	private int getRandomBackgroundObject() {
		return random.nextInt((scaledBackgroundObjects.size() - 1) + 1) + 0;
	}

	// Objects require different scales to create illusion of depth
	private Image setPlanetScale(Image image, int level) {
		switch (level) {
		case (1):
			return image.getScaledInstance(levelOneObjectModifier, levelOneObjectModifier, Image.SCALE_SMOOTH);
		case (2):
			return image.getScaledInstance(levelTwoObjectModifier, levelTwoObjectModifier, Image.SCALE_SMOOTH);
		case (3):
			return image.getScaledInstance(levelThreeObjectModifier, levelThreeObjectModifier, Image.SCALE_SMOOTH);
		}
		return image;
	}

	// Stars require different scales to create illusion of depth
	private Image setStarScale(Image image, int level) {
		switch (level) {
		case (1):
			return image.getScaledInstance(levelOneStarModifier, levelOneStarModifier, Image.SCALE_SMOOTH);
		case (2):
			return image.getScaledInstance(levelTwoStarModifier, levelTwoStarModifier, Image.SCALE_SMOOTH);
		case (3):
			return image.getScaledInstance(levelThreeStarModifier, levelThreeStarModifier, Image.SCALE_SMOOTH);
		}
		return image;
	}

	// Initially loads all planets so they dont have to be reloaded from files.
	// Saves memory
	private void loadAllPlanets() {
		for (String object : allBackgroundObjectStringCodes) {
			Image img = imageLoader.getImage(object);
			scaledBackgroundObjects.put(object, img);
		}

	}

	public List<BackgroundObject> getLevelOnePlanets() {
		return levelOnePlanets;
	}

	public List<BackgroundObject> getLevelTwoPlanets() {
		return levelTwoPlanets;
	}

	public List<BackgroundObject> getLevelThreePlanets() {
		return levelThreePlanets;
	}

	public List<BackgroundObject> getLevelOneStars() {
		return levelOneStars;
	}

	public List<BackgroundObject> getLevelTwoStars() {
		return levelTwoStars;
	}

	public List<BackgroundObject> getLevelThreeStars() {
		return levelThreeStars;
	}

}
