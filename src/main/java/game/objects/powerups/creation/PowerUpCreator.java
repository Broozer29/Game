package game.objects.powerups.creation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.movement.Direction;
import game.movement.pathfinders.BouncingPathFinder;
import game.objects.player.BoostsUpgradesAndBuffsSettings;
import VisualAndAudioData.DataClass;
import VisualAndAudioData.image.ImageEnums;
import game.objects.powerups.PowerUp;
import game.objects.powerups.PowerUpEnums;
import game.objects.powerups.PowerUpManager;
import game.objects.powerups.timers.PowerUpSpawnTimer;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class PowerUpCreator {

	private Random random = new Random();

	private int minY = 0;
	private int maxY = DataClass.getInstance().getWindowHeight();
	private int minX = 0;
	private int maxX = DataClass.getInstance().getWindowWidth();

	private Direction[] directionEnums = Direction.values();
	private List<Integer> randomCoordinates = new ArrayList<Integer>();
	private PowerUpEnums[] powerupEnums = PowerUpEnums.values();
	
	private static PowerUpCreator instance = new PowerUpCreator();
	private PowerUpCreator() {
		
	}
	
	public static PowerUpCreator getInstance() {
		return instance;
	}
	
	
	public void addNewPowerUpToField(PowerUp powerUp) {
		if (PowerUpManager.getInstance().getPowerUpsOnTheField().contains(powerUp)) {
			PowerUpManager.getInstance().getPowerUpsOnTheField().add(powerUp);
		}
	}
	
	public void spawnPowerUp(PowerUpEnums powerUpType) {
		getRandomSpawnCoords();
		if(powerUpType == PowerUpEnums.RANDOM) {
			powerUpType = selectRandomPowerUp();
		}
		System.out.println("power up creator 46 staat uitgecomment zet weer aan om een powerup te spawnen");

		SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
		spriteConfiguration.setImageType(getIconByPowerUp(powerUpType));
		spriteConfiguration.setxCoordinate(randomCoordinates.get(0));
		spriteConfiguration.setyCoordinate(randomCoordinates.get(1));
		spriteConfiguration.setScale(1);

		PowerUpConfiguration powerUpConfiguration = new PowerUpConfiguration(powerUpType, getEffectDuration(powerUpType), false, 2,2,
				selectRandomDirection(), new BouncingPathFinder());

		PowerUp newPowerUp = new PowerUp(spriteConfiguration, powerUpConfiguration);
		newPowerUp.setImageDimensions(50, 50);
		PowerUpManager.getInstance().getPowerUpsOnTheField().add(newPowerUp);
	}

	public PowerUpSpawnTimer createPowerUpTimer(PowerUpEnums powerUp, boolean loopable) {
		return new PowerUpSpawnTimer(getRandomTimeForSpawner(), powerUp, loopable);
	}

	private int getRandomTimeForSpawner() {
		BoostsUpgradesAndBuffsSettings tempSettings = BoostsUpgradesAndBuffsSettings.getInstance();
//		return random.nextInt((tempSettings.getMaxTimeForPowerUpSpawn() - tempSettings.getMinTimeForPowerUpSpawn()) + 1)
//				+ tempSettings.getMinTimeForPowerUpSpawn();
		return 20;
	}

	private Direction selectRandomDirection() {
		Direction randomValue = directionEnums[random.nextInt(directionEnums.length)];
		if (randomValue == Direction.NONE) {
			return selectRandomDirection();
		}
		return randomValue;
	}

	private List<Integer> getRandomSpawnCoords() {
		if (randomCoordinates.size() > 0) {
			randomCoordinates.set(0, random.nextInt((maxX - minX) + 1) + minX);
			randomCoordinates.set(1, random.nextInt((maxY - minY) + 1) + minY);
		} else {
			randomCoordinates.add(random.nextInt((maxX - minX) + 1) + minX);
			randomCoordinates.add(random.nextInt((maxY - minY) + 1) + minY);
		}
		return randomCoordinates;
	}

	private int getEffectDuration(PowerUpEnums powerUpType) {
		switch (powerUpType) {
		case DOUBLE_SHOT:
			return 0;
		case HEALTH_AND_SHIELD_RESTORE:
		case INCREASED_MOVEMENT_SPEED:
		case INCREASED_NORMAL_ATTACK_SPEED:
		case INCREASED_NORMAL_DAMAGE:
		case INCREASED_SPECIAL_ATTACK_SPEED:
		case INCREASED_SPECIAL_DAMAGE:
			return BoostsUpgradesAndBuffsSettings.getInstance().getTemporaryPowerUpDuration();
		case TRIPLE_SHOT:
			return 0;
		default:
			return 0;

		}
	}

	private ImageEnums getIconByPowerUp(PowerUpEnums powerUpType) {
		switch (powerUpType) {
		case DOUBLE_SHOT:
			return ImageEnums.DoubleShotIcon;
		case HEALTH_AND_SHIELD_RESTORE:
			return ImageEnums.Starcraft2_Heal;
		case INCREASED_MOVEMENT_SPEED:
			return ImageEnums.Starcraft2_Energizer_Speed;
		case INCREASED_NORMAL_ATTACK_SPEED:
			return ImageEnums.Starcraft2_Dual_Rockets;
		case INCREASED_NORMAL_DAMAGE:
			return ImageEnums.Starcraft2_Ignite_Afterburners;
		case INCREASED_SPECIAL_ATTACK_SPEED:
			return ImageEnums.Starcraft2_Stim1;
		case INCREASED_SPECIAL_DAMAGE:
			return ImageEnums.Starcraft2_Guardian_Shield;
		case TRIPLE_SHOT:
			return ImageEnums.TripleShotIcon;
		case Guardian_Drone_Homing_Missile:
			return ImageEnums.Starcraft2_Seeker_Missile;
		default:
			return ImageEnums.Test_Image;

		}
	}
	
	private PowerUpEnums selectRandomPowerUp() {
//		PowerUpEnums randomValue = powerupEnums[random.nextInt(powerupEnums.length)];
//		if (randomValue == PowerUpEnums.DUMMY_DO_NOT_USE || 
//				randomValue == PowerUpEnums.RANDOM ||
//				randomValue == PowerUpEnums.DOUBLE_SHOT ||
//				randomValue == PowerUpEnums.TRIPLE_SHOT) {
//			return selectRandomPowerUp();
//		}
//		return randomValue;
		return PowerUpEnums.Guardian_Drone_Homing_Missile;

	}
	
	
	public void resetManager() {
		directionEnums = Direction.values();
		randomCoordinates = new ArrayList<Integer>();
		random = new Random();
	}
}
