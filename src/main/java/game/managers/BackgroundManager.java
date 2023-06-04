package game.managers;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.DataClass;
import data.image.ImageDatabase;
import data.image.ImageResizer;
import data.image.enums.BGOEnums;
import data.image.enums.ImageEnums;
import game.objects.BackgroundObject;

public class BackgroundManager {

	private static BackgroundManager instance = new BackgroundManager();
	private DataClass dataClass = DataClass.getInstance();
	private ImageDatabase imageDatabase = ImageDatabase.getInstance();
	private SpawningCoordinator randomCoordinator = SpawningCoordinator.getInstance();
	private ImageResizer imageResizer = ImageResizer.getInstance();
	private List<BackgroundObject> allBackgroundObjects = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelOnePlanets = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelTwoPlanets = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelThreePlanets = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelOneStars = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelTwoStars = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> levelThreeStars = new ArrayList<BackgroundObject>();

	private List<ImageEnums> planetBGOEnumsList = new ArrayList<ImageEnums>();
	Random random = new Random();
	private int updateFrameCounter = 0;

	private BackgroundManager() {
		initManager();
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		levelOnePlanets = new ArrayList<BackgroundObject>();
		levelTwoPlanets = new ArrayList<BackgroundObject>();
		levelThreePlanets = new ArrayList<BackgroundObject>();
		levelOneStars = new ArrayList<BackgroundObject>();
		levelTwoStars = new ArrayList<BackgroundObject>();
		levelThreeStars = new ArrayList<BackgroundObject>();
		allBackgroundObjects = new ArrayList<BackgroundObject>();

		imageResizer = ImageResizer.getInstance();
		randomCoordinator = SpawningCoordinator.getInstance();
		imageDatabase = ImageDatabase.getInstance();
		initLists();
	}

	private void initManager() {
		planetBGOEnumsList.add(ImageEnums.Moon);
		planetBGOEnumsList.add(ImageEnums.Lava_Planet);
		planetBGOEnumsList.add(ImageEnums.Mars_Planet);
		planetBGOEnumsList.add(ImageEnums.Planet_One);
		planetBGOEnumsList.add(ImageEnums.Planet_Two);
		planetBGOEnumsList.add(ImageEnums.Planet_Three);
		initLists();
	}

	public static BackgroundManager getInstance() {
		return instance;
	}

	private void initLists() {
		fillBGOList(levelOneStars,ImageEnums.Star, BGOEnums.Star, (float) 0.5, 25);
		fillBGOList(levelTwoStars, ImageEnums.Star, BGOEnums.Star, (float) 0.75, 25);
		fillBGOList(levelThreeStars, ImageEnums.Star, BGOEnums.Star, (float) 1, 25);

		fillBGOList(levelOnePlanets, getRandomPlanetString(), BGOEnums.Planet, (float) 0.2, 1);
		fillBGOList(levelTwoPlanets, getRandomPlanetString(), BGOEnums.Planet, (float) 0.4, 1);
		fillBGOList(levelThreePlanets, getRandomPlanetString(), BGOEnums.Planet, (float) 0.6, 1);

	}

	public void updateGameTick() {
		updateObjects();
	}

	private void fillBGOList(List<BackgroundObject> listToFill, ImageEnums imageType, BGOEnums bgoType, float scale,
			int amount) {
		Image bgoImage = imageDatabase.getImage(imageType);
		bgoImage = imageResizer.getScaledImage(bgoImage, scale);
		int attemptedTries = 0;

		while (listToFill.size() < amount && attemptedTries < 50) {
			int randomXCoordinate = randomCoordinator.getRandomXBGOCoordinate();
			int randomYCoordinate = randomCoordinator.getRandomYBGOCoordinate();
			attemptedTries++;
			if (randomCoordinator.checkValidBGOXCoordinate(listToFill, randomXCoordinate, bgoImage.getWidth(null))
					&& randomCoordinator.checkValidBGOYCoordinate(listToFill, randomYCoordinate,
							bgoImage.getHeight(null))) {

				BackgroundObject BGO = new BackgroundObject(randomXCoordinate, randomYCoordinate, bgoImage, scale,
						bgoType);
				listToFill.add(BGO);
				allBackgroundObjects.add(BGO);
			}
		}
	}

	private void moveBGOList(List<BackgroundObject> listToMove) {
		for (BackgroundObject bgObject : listToMove) {
			if ((bgObject.getXCoordinate() + bgObject.getWidth()) < 0) {
				bgObject.setX(dataClass.getWindowWidth() + 200);
				bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());

				if (bgObject.getBGOtype().equals(BGOEnums.Planet)) {
					Image newBGOImage = imageDatabase.getImage(getRandomPlanetString());
					bgObject.setNewPlanetImage(newBGOImage);
				}
			}
			bgObject.setX(bgObject.getXCoordinate() - 1);
		}
	}

	// Move all background objects based on the framecounter
	private void updateObjects() {
		if (updateFrameCounter % 4 == 0) {
			moveBGOList(levelOneStars);
			moveBGOList(levelOnePlanets);
		}

		if (updateFrameCounter % 3 == 0) {
			moveBGOList(levelTwoStars);
			moveBGOList(levelTwoPlanets);
		}

		if (updateFrameCounter % 2 == 0) {
			moveBGOList(levelThreeStars);
			moveBGOList(levelThreePlanets);
		}

		if (updateFrameCounter > 10) {
			updateFrameCounter = 0;
		}
		updateFrameCounter++;
	}

	// Returns a random planet string code
	private ImageEnums getRandomPlanetString() {
		int randomPlanet = random.nextInt((planetBGOEnumsList.size() - 1) + 1) + 0;
		ImageEnums randomPlanetEnum = planetBGOEnumsList.get(randomPlanet);
		return randomPlanetEnum;
	}

	// Initially loads all planets so they dont have to be reloaded from files.
	// Saves memory


	public List<BackgroundObject> getAllBGO() {
		return this.allBackgroundObjects;
	}

}