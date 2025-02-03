package net.riezebos.bruus.tbd.guiboards.background;

import net.riezebos.bruus.tbd.game.level.SpawningCoordinator;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageDatabase;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageResizer;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.image.BufferedImage;
import java.util.*;

public class BackgroundManager {

    private static final BackgroundManager instance = new BackgroundManager();
    private final DataClass dataClass = DataClass.getInstance();
    private final ImageDatabase imageDatabase = ImageDatabase.getInstance();
    private final SpawningCoordinator randomCoordinator = SpawningCoordinator.getInstance();
    private final ImageResizer imageResizer = ImageResizer.getInstance();
    private final Map<BGOEnums, List<BackgroundObject>> backgroundObjectsMap = new HashMap<>();
    private final List<ImageEnums> planetBGOEnumsList = new ArrayList<>();
    private final Random random = new Random();
    private int updateFrameCounter = 0;
    private int rightmostNebulaEdge = 0;
    private SpaceThemeEnums spaceTheme;
    private NebulaThemeEnums nebulaTheme;
    private PerformanceLogger performanceLogger = null;

    private BackgroundManager () {
        initManager();
        this.performanceLogger = new PerformanceLogger("Background Manager");
    }

    public static BackgroundManager getInstance () {
        return instance;
    }

    public void resetManager () {
        backgroundObjectsMap.clear();
        initManager();
        performanceLogger.reset();
    }

    private void initManager () {
        this.spaceTheme = SpaceThemeEnums.selectRandomSpaceTheme();
//        this.spaceTheme = SpaceThemeEnums.HDNebula;
        initSpaceTheme();
        this.nebulaTheme = NebulaThemeEnums.selectRandomNebulaScene();
//        this.nebulaTheme = NebulaThemeEnums.HDNebula4;
        initBackgroundObjects();
    }

    private void initSpaceTheme () {
        planetBGOEnumsList.clear();
        switch (this.spaceTheme) {
            case Blue:
                planetBGOEnumsList.add(ImageEnums.Star_Blue1);
                planetBGOEnumsList.add(ImageEnums.Star_Blue2);
                planetBGOEnumsList.add(ImageEnums.Star_Blue3);
                planetBGOEnumsList.add(ImageEnums.Star_Blue4);

                planetBGOEnumsList.add(ImageEnums.BluePlanet1);
                planetBGOEnumsList.add(ImageEnums.BluePlanet2);
                planetBGOEnumsList.add(ImageEnums.BluePlanet3);
                planetBGOEnumsList.add(ImageEnums.BluePlanet4);
                planetBGOEnumsList.add(ImageEnums.BluePlanet5);
                planetBGOEnumsList.add(ImageEnums.BluePlanet6);

                break;
            case Green:
                planetBGOEnumsList.add(ImageEnums.GreenPlanet1);
                planetBGOEnumsList.add(ImageEnums.GreenPlanet2);
                break;
            case Purple:
                planetBGOEnumsList.add(ImageEnums.Planet_One);
                planetBGOEnumsList.add(ImageEnums.Planet_Two);
                break;
//            case HDNebula:
//                planetBGOEnumsList.add(ImageEnums.Star_Yellow1);
//                planetBGOEnumsList.add(ImageEnums.Star_Yellow2);
//                planetBGOEnumsList.add(ImageEnums.Moon2);
//                planetBGOEnumsList.add(ImageEnums.Moon3);
//                planetBGOEnumsList.add(ImageEnums.Moon4);
//                break;
        }

        planetBGOEnumsList.add(ImageEnums.Moon2);
        planetBGOEnumsList.add(ImageEnums.Moon3);
        planetBGOEnumsList.add(ImageEnums.Moon4);

        planetBGOEnumsList.add(ImageEnums.Star_Orange1);
        planetBGOEnumsList.add(ImageEnums.Star_Orange2);
        planetBGOEnumsList.add(ImageEnums.Star_Orange3);
        planetBGOEnumsList.add(ImageEnums.Star_Orange4);

        planetBGOEnumsList.add(ImageEnums.Star_Red1);
        planetBGOEnumsList.add(ImageEnums.Star_Red2);
        planetBGOEnumsList.add(ImageEnums.Star_Red3);
        planetBGOEnumsList.add(ImageEnums.Star_Red4);

        planetBGOEnumsList.add(ImageEnums.Lava_Planet);


    }

    private void initBackgroundObjects () {
        // Background objects initialization logic...
        ImageEnums nebula = getNebulaImage(this.nebulaTheme);
        fillBGOList(BGOEnums.Nebula, nebula, 1, 25, 4, 25);
        fillBGOList(BGOEnums.Star, ImageEnums.Star, 1f, 35, 3, 90);
        fillBGOList(BGOEnums.Star, ImageEnums.Star, 0.75f, 35, 2, 90);
        fillBGOList(BGOEnums.Star, ImageEnums.Star, 0.5f, 35, 1, 90);

        fillBGOList(BGOEnums.Planet, getRandomPlanetEnum(), 0.4f, 2, 3, 5);
        fillBGOList(BGOEnums.Planet, getRandomPlanetEnum(), 0.6f, 1, 2, 5);
        fillBGOList(BGOEnums.Planet, getRandomPlanetEnum(), 0.8f, 1, 1, 5);

    }

    private void fillBGOList (BGOEnums bgoType, ImageEnums imageType, float scale, int amount, int depthLevel, int maxTries) {
        List<BackgroundObject> listToFill = backgroundObjectsMap.computeIfAbsent(bgoType, k -> new ArrayList<>());
        BufferedImage bgoImage = imageDatabase.getImage(imageType);
        if (bgoImage == null) {
            System.out.println("Crashed because an empty BackgroundObject image");
            return;
        }

        if (scale != 1 && scale != 0) {
            bgoImage = imageResizer.getScaledImage(bgoImage, scale);
        }

        int bgoSpawned = 0;
        int tries = 0;
        while (sizeOfDepthLevel(listToFill, depthLevel) < amount && tries < maxTries) {
            BackgroundObject bgo = createBackgroundObject(bgoType, imageType, bgoImage, scale, depthLevel, listToFill, bgoSpawned);
            if (bgo != null) {
                listToFill.add(bgo);
                bgoSpawned++;
            }
            tries++;
        }
    }

    private int sizeOfDepthLevel (List<BackgroundObject> list, int depthLevel) {
        int count = 0;
        for (BackgroundObject obj : list) {
            if (obj.getDepthLevel() == depthLevel) {
                count++;
            }
        }
        return count;
    }

    private BackgroundObject createBackgroundObject (BGOEnums bgoType, ImageEnums imageType, BufferedImage bgoImage, float scale, int depthLevel, List<BackgroundObject> existingObjects, int bgoSpawned) {
        int xCoordinate = randomCoordinator.getRandomXBGOCoordinate();
        int yCoordinate = randomCoordinator.getRandomYBGOCoordinate();

        if (bgoType == BGOEnums.Planet || bgoType == BGOEnums.Star) {
            List<BackgroundObject> sameDepthObjects = filterByDepthLevel(existingObjects, depthLevel);
            if (randomCoordinator.checkValidBGOXCoordinate(sameDepthObjects, xCoordinate, bgoImage.getWidth(null)) &&
                    randomCoordinator.checkValidBGOYCoordinate(sameDepthObjects, yCoordinate, bgoImage.getHeight(null))) {


                SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, imageType, scale);
                BackgroundObjectConfiguration bgoConfiguration = createBGOConfiguration(depthLevel, bgoType);
                return new BackgroundObject(spriteConfiguration, bgoConfiguration);
            }

        }

        if (bgoType == BGOEnums.Nebula) {
            // Place Nebula objects next to each other horizontally
            xCoordinate = bgoImage.getWidth() * bgoSpawned;
            yCoordinate = 0;
            SpriteConfiguration spriteConfiguration = createSpriteConfiguration(xCoordinate, yCoordinate, imageType, scale);
            BackgroundObjectConfiguration bgoConfiguration = createBGOConfiguration(depthLevel, bgoType);
            return new BackgroundObject(spriteConfiguration, bgoConfiguration);
        }

        return null;
    }


    private List<BackgroundObject> filterByDepthLevel (List<BackgroundObject> existingObjects, int depthLevel) {
        List<BackgroundObject> filteredList = new ArrayList<>();
        for (BackgroundObject obj : existingObjects) {
            if (obj.getDepthLevel() == depthLevel) {
                filteredList.add(obj);
            }
        }
        return filteredList;
    }

    private void updateObjects () {
        int intervalLevel1 = 2; // Fastest movement for depth level 3
        int intervalLevel2 = 3; // Medium movement for depth level 2
        int intervalLevel3 = 5; // Slower movement for depth level 1
        int intervalLevel4 = 6; // Slowest movement for Nebula level (4)

        for (Map.Entry<BGOEnums, List<BackgroundObject>> entry : backgroundObjectsMap.entrySet()) {
            List<BackgroundObject> bgObjects = entry.getValue();
            for (BackgroundObject bgObject : bgObjects) {
                int moveInterval;
                switch (bgObject.getDepthLevel()) {
                    case 1:
                        moveInterval = intervalLevel1;
                        break;
                    case 2:
                        moveInterval = intervalLevel2;
                        break;
                    case 3:
                        moveInterval = intervalLevel3;
                        break;
                    case 4:
                        moveInterval = intervalLevel4;
                        break;
                    default:
                        moveInterval = 2; // Default interval
                        break;
                }

                if (updateFrameCounter % moveInterval == 0) {
                    moveBackgroundObject(bgObject);
                }
            }
        }

        if (updateFrameCounter > intervalLevel4) {
            updateFrameCounter = 0;
        } else {
            updateFrameCounter++;
        }
    }

    private void updateRightmostNebulaEdge () {
        int maxEdge = 0;
        for (BackgroundObject obj : backgroundObjectsMap.get(BGOEnums.Nebula)) {
            int edge = obj.getXCoordinate() + obj.getWidth();
            if (edge > maxEdge) {
                maxEdge = edge;
            }
        }
        rightmostNebulaEdge = maxEdge;
    }

    private void moveBackgroundObject (BackgroundObject bgObject) {
        //If the object is outside the window screen, re-place it on the right side
        if ((bgObject.getXCoordinate() + bgObject.getWidth()) < 0) {
            switch (bgObject.getBGOtype()) {
                case Planet:
                    bgObject.setXCoordinate(dataClass.getWindowWidth() + 200);
                    bgObject.setYCoordinate(randomCoordinator.getRandomYBGOCoordinate());
                    bgObject.setNewPlanetImage(imageDatabase.getImage(getRandomPlanetEnum()));
                    break;
                case Nebula:
                case Parallex:
                    //This is broken for some inexplicable reason
                    bgObject.setXCoordinate(rightmostNebulaEdge);  // Place right after the last nebula
                    updateRightmostNebulaEdge();  // Update after repositioning
//                    System.out.println("X coordinate: " + bgObject.getXCoordinate() +
//                            " Width: " + bgObject.getWidth() +
//                            " Right most nebula: " + rightmostNebulaEdge +
//                            " Gap: " + (bgObject.getXCoordinate() - rightmostNebulaEdge + bgObject.getWidth()));
                    break;
                case Star:
                    bgObject.setXCoordinate(dataClass.getWindowWidth() + 200);
                    bgObject.setYCoordinate(randomCoordinator.getRandomYBGOCoordinate());
                    break;
            }
        }
        bgObject.setXCoordinate(bgObject.getXCoordinate() - 1); // Adjust this value as needed for speed
    }


    private ImageEnums getNebulaImage (NebulaThemeEnums nebulaTheme) {
        switch (nebulaTheme) {
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
//            case HDNebula1: return ImageEnums.HDBackground1;
//            case HDNebula2: return ImageEnums.HDBackground2;
//            case HDNebula3: return ImageEnums.HDBackground3;
//            case HDNebula4: return ImageEnums.HDBackground4;
//            case HDNebula5: return ImageEnums.HDBackground5;
        }
        return null;
    }

    private ImageEnums getRandomPlanetEnum () {
        int randomPlanet = random.nextInt((planetBGOEnumsList.size() - 1) + 1) + 0;
        ImageEnums randomPlanetEnum = planetBGOEnumsList.get(randomPlanet);
        return randomPlanetEnum;
    }

    public void updateGameTick () {
//        PerformanceLoggerManager.timeAndLog(performanceLogger, "Total", () -> {
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Update Objects", this::updateObjects);
//        });
    }

    public List<BackgroundObject> getAllBGO () {
        List<BackgroundObject> allObjects = new ArrayList<>();
        for (List<BackgroundObject> bgObjectsList : backgroundObjectsMap.values()) {
            allObjects.addAll(bgObjectsList);
        }

        // Sort the objects by depth level in ascending order (lower depth levels should be drawn first)
        allObjects.sort(Comparator.comparingInt(BackgroundObject::getDepthLevel).reversed());

        return allObjects;
    }


    private SpriteConfiguration createSpriteConfiguration (int xCoordinate, int yCoordinate, ImageEnums imageType, float scale) {
        SpriteConfiguration newConfig = new SpriteConfiguration();
        newConfig.setxCoordinate(xCoordinate);
        newConfig.setyCoordinate(yCoordinate);
        newConfig.setImageType(imageType);
        newConfig.setScale(scale);
        return newConfig;
    }

    private BackgroundObjectConfiguration createBGOConfiguration (int depthLevel, BGOEnums bgoType) {
        return new BackgroundObjectConfiguration(depthLevel, bgoType);
    }

    public PerformanceLogger getPerformanceLogger () {
        return this.performanceLogger;
    }
}
