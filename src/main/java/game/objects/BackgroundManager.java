package game.objects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import data.DataClass;
import data.image.ImageDatabase;
import data.image.ImageEnums;
import data.image.ImageResizer;
import game.spawner.SpawningCoordinator;

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

	private List<BackgroundObject> parralex1List = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> parralex2List = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> parralex3List = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> parralex4List = new ArrayList<BackgroundObject>();
	private List<BackgroundObject> parralex5List = new ArrayList<BackgroundObject>();

	private List<ImageEnums> planetBGOEnumsList = new ArrayList<ImageEnums>();
	private Random random = new Random();
	private int updateFrameCounter = 0;
	private int bgoStepSize = 50;
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
		parralex1List = new ArrayList<BackgroundObject>();
		parralex2List = new ArrayList<BackgroundObject>();
		parralex3List = new ArrayList<BackgroundObject>();
		parralex4List = new ArrayList<BackgroundObject>();
		parralex5List = new ArrayList<BackgroundObject>();
		NebulaList = new ArrayList<BackgroundObject>();
		planetBGOEnumsList = new ArrayList<ImageEnums>();
		updateFrameCounter = 0;
		random = new Random();

		imageResizer = ImageResizer.getInstance();
		randomCoordinator = SpawningCoordinator.getInstance();
		imageDatabase = ImageDatabase.getInstance();
		initManager();
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
		ImageEnums nebula = getNebulaImage();
		fillBGOList(NebulaList, nebula, BGOEnums.Nebula, (float) 1, 5);

//		fillBGOList(parralex1List, ImageEnums.Parallex_1, BGOEnums.Parallex, 1, 3);
//		fillBGOList(parralex2List, ImageEnums.Parallex_2, BGOEnums.Parallex, 1, 3);
//		fillBGOList(parralex3List, ImageEnums.Parallex_3, BGOEnums.Parallex, 1, 3);
//		fillBGOList(parralex4List, ImageEnums.Parallex_4, BGOEnums.Parallex, 1, 3);
//		fillBGOList(parralex5List, ImageEnums.Parallex_5, BGOEnums.Parallex, 1, 3);

		fillBGOList(levelOneStars, ImageEnums.Star, BGOEnums.Star, (float) 0.5, 25);
		fillBGOList(levelTwoStars, ImageEnums.Star, BGOEnums.Star, (float) 0.75, 25);
		fillBGOList(levelThreeStars, ImageEnums.Star, BGOEnums.Star, (float) 1, 25);

		fillBGOList(levelOnePlanets, getRandomPlanetEnum(), BGOEnums.Planet, (float) 0.2, 1);
		fillBGOList(levelTwoPlanets, getRandomPlanetEnum(), BGOEnums.Planet, (float) 0.4, 1);
		fillBGOList(levelThreePlanets, getRandomPlanetEnum(), BGOEnums.Planet, (float) 0.6, 1);

	}

	/*- Fills the given BGO list depending using the given image type, depending on the bgotype */
	private void fillBGOList(List<BackgroundObject> listToFill, ImageEnums imageType, BGOEnums bgoType, float scale,
			int amount) {
		BufferedImage bgoImage = imageDatabase.getImage(imageType);
		if (bgoImage == null) {
			System.out.println("Crashed because an empty BackgroundObject image");
		}
		if (scale != 1) {
			bgoImage = imageResizer.getScaledImage(bgoImage, scale);
		}
		int attemptedTries = 0;
		int bgoSpawned = 0;

		while (listToFill.size() < amount && attemptedTries < 50) {
			switch (bgoType) {
			case Nebula:
				int nebulaXcoordinate = ((bgoImage.getWidth() * bgoSpawned));
				int nebulaYCoordinate = 0;

				BackgroundObject NebulaBGO = new BackgroundObject(nebulaXcoordinate, nebulaYCoordinate, bgoImage, 1,
						bgoType);
				listToFill.add(NebulaBGO);
				allBackgroundObjects.add(NebulaBGO);
				bgoSpawned++;
				break;
			case Parallex:
				int parralexXcoordinate = ((bgoImage.getWidth() * bgoSpawned) + bgoSpawned);
				int parralexYCoordinate = 0;

				BackgroundObject parralexBGO = new BackgroundObject(parralexXcoordinate, parralexYCoordinate, bgoImage,
						1, bgoType);
				listToFill.add(parralexBGO);
				allBackgroundObjects.add(parralexBGO);
				bgoSpawned++;
				break;
			case Planet:
			case Star:
				int randomXCoordinate = randomCoordinator.getRandomXBGOCoordinate();
				int randomYCoordinate = randomCoordinator.getRandomYBGOCoordinate();

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


	// Moves all BGO
	private void moveBGOList(List<BackgroundObject> listToMove) {
	    for (BackgroundObject bgObject : listToMove) {
	        if ((bgObject.getXCoordinate() + bgObject.getWidth()) < 0) {
	            if (bgObject.getBGOtype().equals(BGOEnums.Planet)) {
	                bgObject.setX(dataClass.getWindowWidth() + 200);
	                bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
	                bgObject.setNewPlanetImage(imageDatabase.getImage(getRandomPlanetEnum()));
	            } else if (bgObject.getBGOtype().equals(BGOEnums.Nebula)
	                    || bgObject.getBGOtype().equals(BGOEnums.Parallex)) {

	                if (listToMove.size() > 1) {
	                    // get the last element in the list
	                    BackgroundObject lastBGO = listToMove.get(listToMove.size() - 1);
	                    if (bgObject.equals(lastBGO)) {
	                        // if current object is the last one, put it back to right side of the screen
	                        bgObject.setX(dataClass.getWindowWidth());
	                    } else {
	                        // else, put it right after the last image
	                        bgObject.setX(lastBGO.getXCoordinate() + lastBGO.getWidth());
	                    }
	                } else {
	                    bgObject.setX(dataClass.getWindowWidth()); // Positioned just outside the right edge of the screen
	                }
	            } else if (bgObject.getBGOtype().equals(BGOEnums.Star)) {
	                bgObject.setX(dataClass.getWindowWidth() + 200);
	                bgObject.setY(randomCoordinator.getRandomYBGOCoordinate());
	            }
	        }
	        bgObject.setX(bgObject.getXCoordinate() - 1);
	    }
	}

	// Move all background objects based on the framecounter
	private void updateObjects() {
		NebulaList.sort(Comparator.comparingInt(BackgroundObject::getXCoordinate));
		if (updateFrameCounter % 6 == 0) {
			moveBGOList(NebulaList);
		}
		if (updateFrameCounter % 6 == 0) {
			moveBGOList(parralex1List);
		}

		if (updateFrameCounter % 5 == 0) {
			moveBGOList(parralex2List);
		}

		if (updateFrameCounter % 4 == 0) {
			moveBGOList(levelOneStars);
			moveBGOList(levelOnePlanets);
			moveBGOList(parralex3List);
		}

		if (updateFrameCounter % 3 == 0) {
			moveBGOList(levelTwoStars);
			moveBGOList(levelTwoPlanets);
			moveBGOList(parralex4List);
		}

		if (updateFrameCounter % 2 == 0) {
			moveBGOList(levelThreeStars);
			moveBGOList(levelThreePlanets);
			moveBGOList(parralex5List);
		}

		if (updateFrameCounter > 10) {
			updateFrameCounter = 0;
		}
		updateFrameCounter++;
	}

	// Returns a random planet string code
	private ImageEnums getRandomPlanetEnum() {
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
			// Return 2 because 1 isn't actually seamless
			return ImageEnums.Green_Nebula_2;
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
		SpaceThemeEnums randomValue = enums[random.nextInt(enums.length)];
		return randomValue;
	}

	private NebulaThemeEnums selectRandomNebulaTheme() {
		NebulaThemeEnums[] enums = NebulaThemeEnums.values();
		NebulaThemeEnums randomValue = enums[random.nextInt(enums.length)];
		return randomValue;
	}

}