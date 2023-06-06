package game.managers;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.DataClass;
import data.BGOEnums.NebulaThemeEnums;
import data.BGOEnums.SpaceThemeEnums;
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
	private List<BackgroundObject> NebulaList = new ArrayList<BackgroundObject>();

	private List<ImageEnums> planetBGOEnumsList = new ArrayList<ImageEnums>();
	Random random = new Random();
	private int updateFrameCounter = 0;

	private SpaceThemeEnums spaceTheme;
	private NebulaThemeEnums nebulaTheme;

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
		this.spaceTheme = selectRandomSpaceTheme();
		switch (this.spaceTheme) {
		case Default:
			planetBGOEnumsList.add(ImageEnums.Moon);
			planetBGOEnumsList.add(ImageEnums.Lava_Planet);
			planetBGOEnumsList.add(ImageEnums.Mars_Planet);
			break;
		case Three_Random_Planets:
			planetBGOEnumsList.add(ImageEnums.Planet_One);
			planetBGOEnumsList.add(ImageEnums.Planet_Two);
			planetBGOEnumsList.add(ImageEnums.Planet_Three);
			break;
		}

		this.nebulaTheme = selectRandomNebulaTheme();
		// Fill the backgroundenumslist met de objects die horen bij het thema.
		initLists();
	}

	public static BackgroundManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		updateObjects();
	}

	private void initLists() {
		fillBGOList(NebulaList, getNebulaImage(), BGOEnums.Nebula, 1, 3);

		fillBGOList(levelOneStars, ImageEnums.Star, BGOEnums.Star, (float) 0.5, 25);
		fillBGOList(levelTwoStars, ImageEnums.Star, BGOEnums.Star, (float) 0.75, 25);
		fillBGOList(levelThreeStars, ImageEnums.Star, BGOEnums.Star, (float) 1, 25);

		fillBGOList(levelOnePlanets, getRandomPlanetString(), BGOEnums.Planet, (float) 0.2, 1);
		fillBGOList(levelTwoPlanets, getRandomPlanetString(), BGOEnums.Planet, (float) 0.4, 1);
		fillBGOList(levelThreePlanets, getRandomPlanetString(), BGOEnums.Planet, (float) 0.6, 1);

	}

	private void fillBGOList(List<BackgroundObject> listToFill, ImageEnums imageType, BGOEnums bgoType, float scale,
			int amount) {
		Image bgoImage = imageDatabase.getImage(imageType);
		bgoImage = imageResizer.getScaledImage(bgoImage, scale);
		int attemptedTries = 0;
		int nebulaSpawned = 0;

		while (listToFill.size() < amount && attemptedTries < 50) {
			int randomXCoordinate = 0;
			int randomYCoordinate = 0;
			switch (bgoType) {
			case Nebula:
				int nebulaXcoordinate = (-100 + (1024 * nebulaSpawned));
				int nebulaYCoordinate = -0;

				BackgroundObject NebulaBGO = new BackgroundObject(nebulaXcoordinate, nebulaYCoordinate, bgoImage, 1,
						bgoType);
				listToFill.add(NebulaBGO);
				allBackgroundObjects.add(NebulaBGO);
				nebulaSpawned++;
				break;
			case Planet:
			case Star:
				randomXCoordinate = randomCoordinator.getRandomXBGOCoordinate();
				randomYCoordinate = randomCoordinator.getRandomYBGOCoordinate();

				if (randomCoordinator.checkValidBGOXCoordinate(listToFill, randomXCoordinate, bgoImage.getWidth(null))
						&& randomCoordinator.checkValidBGOYCoordinate(listToFill, randomYCoordinate,
								bgoImage.getHeight(null))) {

					BackgroundObject BGO = new BackgroundObject(randomXCoordinate, randomYCoordinate, bgoImage, scale,
							bgoType);
					listToFill.add(BGO);
					allBackgroundObjects.add(BGO);
				}

				break;
			}
			attemptedTries++;

		}
	}

	private void moveBGOList(List<BackgroundObject> listToMove) {
		for (BackgroundObject bgObject : listToMove) {
			if ((bgObject.getXCoordinate() + bgObject.getWidth()) < 0) {

				if (bgObject.getBGOtype().equals(BGOEnums.Planet) || bgObject.getBGOtype().equals(BGOEnums.Star)) {
					Image newBGOImage = imageDatabase.getImage(getRandomPlanetString());
					bgObject.setX(dataClass.getWindowWidth() + 200);
					bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
					bgObject.setNewPlanetImage(newBGOImage);
					
				} else if (bgObject.getBGOtype().equals(BGOEnums.Nebula)) {
					bgObject.setX(-2048);
				}
			}
			bgObject.setX(bgObject.getXCoordinate() - 1);
		}
	}

	// Move all background objects based on the framecounter
	private void updateObjects() {
		if (updateFrameCounter % 10 == 0) {
			moveBGOList(NebulaList);
		}

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

	// Returns a random planet string code
	private ImageEnums getNebulaImage() {
		switch (this.nebulaTheme) {
		case Blue_Nebula_1:
			return ImageEnums.Blue_Nebula_1;
		case Blue_Nebula_2:
			return ImageEnums.Blue_Nebula_2;
		case Blue_Nebula_3:
			return ImageEnums.Blue_Nebula_3;
		case Blue_Nebula_4:
			return ImageEnums.Blue_Nebula_4;
		case Blue_Nebula_5:
			return ImageEnums.Blue_Nebula_5;
		case Blue_Nebula_6:
			return ImageEnums.Blue_Nebula_6;
		case Green_Nebula_1:
			return ImageEnums.Green_Nebula_1;
		case Green_Nebula_2:
			return ImageEnums.Green_Nebula_2;
		case Green_Nebula_3:
			return ImageEnums.Green_Nebula_3;
		case Green_Nebula_4:
			return ImageEnums.Green_Nebula_4;
		case Green_Nebula_5:
			return ImageEnums.Green_Nebula_5;
		case Green_Nebula_6:
			return ImageEnums.Green_Nebula_6;
		case Green_Nebula_7:
			return ImageEnums.Green_Nebula_7;
		case Purple_Nebula_1:
			return ImageEnums.Purple_Nebula_1;
		case Purple_Nebula_2:
			return ImageEnums.Purple_Nebula_2;
		case Purple_Nebula_3:
			return ImageEnums.Purple_Nebula_3;
		case Purple_Nebula_4:
			return ImageEnums.Purple_Nebula_4;
		case Purple_Nebula_5:
			return ImageEnums.Purple_Nebula_5;
		case Purple_Nebula_6:
			return ImageEnums.Purple_Nebula_6;
		case Purple_Nebula_7:
			return ImageEnums.Purple_Nebula_7;
		}
		return null;
	}

	// Initially loads all planets so they dont have to be reloaded from files.
	// Saves memory
	public List<BackgroundObject> getAllBGO() {
		return this.allBackgroundObjects;
	}

	private SpaceThemeEnums selectRandomSpaceTheme() {
		SpaceThemeEnums[] enums = SpaceThemeEnums.values();
		Random random = new Random();
		SpaceThemeEnums randomValue = enums[random.nextInt(enums.length)];
		return randomValue;
	}

	private NebulaThemeEnums selectRandomNebulaTheme() {
		NebulaThemeEnums[] enums = NebulaThemeEnums.values();
		Random random = new Random();
		NebulaThemeEnums randomValue = enums[random.nextInt(enums.length)];
		System.out.println(randomValue);
		return randomValue;
	}

}