package game.gameobjects.powerups.creation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.pathfinders.BouncingPathFinder;
import game.gameobjects.player.BoostsUpgradesAndBuffsSettings;
import VisualAndAudioData.DataClass;
import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.powerups.PowerUp;
import game.gameobjects.powerups.PowerUpEnums;
import game.gameobjects.powerups.PowerUpManager;
import game.gameobjects.powerups.timers.PowerUpSpawnTimer;
import game.gameobjects.powerups.timers.TimerManager;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class PowerUpCreator {

    private Random random = new Random();

    private int minY = DataClass.getInstance().getPlayableWindowMinHeight();
    private int maxY = DataClass.getInstance().getPlayableWindowMaxHeight();
    private int minX = 0;
    private int maxX = DataClass.getInstance().getWindowWidth();

    private Direction[] directionEnums = Direction.values();
    private List<Integer> randomCoordinates = new ArrayList<Integer>();
    private PowerUpEnums[] powerupEnums = PowerUpEnums.values();

    private static PowerUpCreator instance = new PowerUpCreator();

    private PowerUpCreator () {

    }

    public static PowerUpCreator getInstance () {
        return instance;
    }

    public void initializePowerUpSpawnTimers () {
        PowerUpSpawnTimer spawnTimer = createPowerUpTimer(PowerUpEnums.RANDOM, true);
        TimerManager.getInstance().addTimer(spawnTimer);
        spawnTimer.startOfTimer(); // Start the timer for the first spawn
    }

    public void spawnPowerUp (PowerUpEnums powerUpType) {
        getRandomSpawnCoords();
        if (powerUpType == PowerUpEnums.RANDOM) {
            powerUpType = selectRandomPowerUp();
        }

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(getIconByPowerUp(powerUpType));
        spriteConfiguration.setxCoordinate(randomCoordinates.get(0));
        spriteConfiguration.setyCoordinate(randomCoordinates.get(1));
        spriteConfiguration.setScale(1);

        PowerUpConfiguration powerUpConfiguration = new PowerUpConfiguration(powerUpType, getEffectDuration(powerUpType), false);
        MovementConfiguration movementConfiguration = createMovementConfiguration(1, 1);


        PowerUp newPowerUp = new PowerUp(spriteConfiguration, powerUpConfiguration, movementConfiguration);
        newPowerUp.setImageDimensions(30, 30);
        PowerUpManager.getInstance().getPowerUpsOnTheField().add(newPowerUp);
    }

    private MovementConfiguration createMovementConfiguration (int xSpeed, int ySpeed) {
        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setXMovementSpeed(xSpeed);
        movementConfiguration.setYMovementSpeed(ySpeed);
        movementConfiguration.setRotation(selectRandomDirection());
//        movementConfiguration.setRotation(Direction.DOWN);
        movementConfiguration.setPathFinder(new BouncingPathFinder());
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders(); //not needed but no reason not to
        return movementConfiguration;
    }

    public PowerUpSpawnTimer createPowerUpTimer (PowerUpEnums powerUp, boolean loopable) {
        return new PowerUpSpawnTimer(getRandomTimeForSpawner(), powerUp, loopable);
    }

    public int getRandomTimeForSpawner () {
		BoostsUpgradesAndBuffsSettings tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
		return random.nextInt((tempSettings.getMaxTimeForPowerUpSpawn() - tempSettings.getMinTimeForPowerUpSpawn()) + 1)
				+ tempSettings.getMinTimeForPowerUpSpawn();
//        return 1;
    }

    private Direction selectRandomDirection () {
        List<Direction> viableDirections = Arrays.stream(Direction.values())
                .filter(direction -> direction != Direction.NONE)
                .toList();

        Random random = new Random();
        int randomIndex = random.nextInt(viableDirections.size());

        Direction randomDirection = viableDirections.get(randomIndex);
        if(randomDirection == Direction.NONE){
            randomDirection = Direction.LEFT_UP;
        }
        return randomDirection;
    }

    private List<Integer> getRandomSpawnCoords () {
        if (randomCoordinates.size() > 0) {
            randomCoordinates.set(0, random.nextInt((maxX - minX) + 1) + minX);
            randomCoordinates.set(1, random.nextInt((maxY - minY) + 1) + minY);
        } else {
            randomCoordinates.add(random.nextInt((maxX - minX) + 1) + minX);
            randomCoordinates.add(random.nextInt((maxY - minY) + 1) + minY);
        }
        return randomCoordinates;
    }

    private int getEffectDuration (PowerUpEnums powerUpType) {
        switch (powerUpType) {
            case DOUBLE_SHOT:
                return 0;
            case HEALTH_AND_SHIELD_RESTORE:
            case INCREASED_NORMAL_DAMAGE:
                return BoostsUpgradesAndBuffsSettings.getInstance().getTemporaryPowerUpDuration();
            case TRIPLE_SHOT:
                return 0;
            default:
                return 0;

        }
    }

    private ImageEnums getIconByPowerUp (PowerUpEnums powerUpType) {
        switch (powerUpType) {
            case DOUBLE_SHOT:
                return ImageEnums.DoubleShotIcon;
            case HEALTH_AND_SHIELD_RESTORE:
                return ImageEnums.Starcraft2_Heal;
            case INCREASED_NORMAL_DAMAGE:
                return ImageEnums.Starcraft2_Ignite_Afterburners;
            case TRIPLE_SHOT:
                return ImageEnums.TripleShotIcon;
            case Guardian_Drone:
                return ImageEnums.Starcraft2_Seeker_Missile;
            default:
                return ImageEnums.Test_Image;

        }
    }

    private PowerUpEnums selectRandomPowerUp () {
//		PowerUpEnums randomValue = powerupEnums[random.nextInt(powerupEnums.length)];
//		if (randomValue == PowerUpEnums.DUMMY_DO_NOT_USE || 
//				randomValue == PowerUpEnums.RANDOM ||
//				randomValue == PowerUpEnums.DOUBLE_SHOT ||
//				randomValue == PowerUpEnums.TRIPLE_SHOT) {
//			return selectRandomPowerUp();
//		}
//		return randomValue;
        return PowerUpEnums.Guardian_Drone;

    }


    public void resetManager () {
        directionEnums = Direction.values();
        randomCoordinates = new ArrayList<Integer>();
        random = new Random();
    }
}
