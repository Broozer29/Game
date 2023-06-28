package game.managers;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import data.TemporaryGameSettings;
import data.audio.AudioEnums;
import data.image.ImageEnums;
import game.movement.Direction;
import game.objects.friendlies.powerups.PowerUp;
import game.objects.friendlies.powerups.PowerUpSpawnTimer;
import game.objects.friendlies.powerups.PowerUps;

public class PowerUpManager {

	private static PowerUpManager instance = new PowerUpManager();
	private List<PowerUp> powerUpsOnTheField = new ArrayList<PowerUp>();
	private Random random = new Random();

	private int minY = 0;
	private int maxY = DataClass.getInstance().getWindowHeight();
	private int minX = 0;
	private int maxX = DataClass.getInstance().getWindowWidth();

	private Direction[] directionEnums = Direction.values();
	private PowerUps[] powerupEnums = PowerUps.values();
	private List<Integer> randomCoordinates = new ArrayList<Integer>();

	private PowerUpManager() {

	}

	public static PowerUpManager getInstance() {
		return instance;
	}

	public void addNewPowerUpToField(PowerUp powerUp) {
		if (!this.powerUpsOnTheField.contains(powerUp)) {
			this.powerUpsOnTheField.add(powerUp);
		}
	}

	public void updateGameTick() {
		movePowerUpsOnTheField();
		removeInvisiblePowerUps();
		checkPowerUpCollision();
	}

	private void movePowerUpsOnTheField() {
		for (PowerUp powerUp : powerUpsOnTheField) {
			powerUp.move();
		}
	}

	private void removeInvisiblePowerUps() {
		for (int i = 0; i < powerUpsOnTheField.size(); i++) {
			if (!powerUpsOnTheField.get(i).isVisible()) {
				powerUpsOnTheField.remove(i);
			}
		}
	}

	public List<PowerUp> getPowerUpsOnTheField() {
		return powerUpsOnTheField;
	}

	private void checkPowerUpCollision() {
		PlayerManager friendlyManager = PlayerManager.getInstance();
		AudioManager audioManager = AudioManager.getInstance();
		for (PowerUp powerUp : powerUpsOnTheField) {
			if (powerUp.isVisible()) {
				Rectangle r1 = powerUp.getBounds();
				Rectangle r2 = friendlyManager.getSpaceship().getBounds();
				if (r1.intersects(r2)) {
					powerUp.startPowerUpTimer();
					powerUp.setVisible(false);
					try {
						audioManager.addAudio(AudioEnums.Power_Up_Acquired);
					} catch (UnsupportedAudioFileException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void spawnPowerUp(PowerUps powerUpType) {
		getRandomSpawnCoords();
		PowerUp newPowerUp = new PowerUp(randomCoordinates.get(0), randomCoordinates.get(1), 1, selectRandomDirection(),
				powerUpType, getIconByPowerUp(powerUpType), getEffectDuration(powerUpType), false);
		newPowerUp.setImageDimensions(50, 50);
		powerUpsOnTheField.add(newPowerUp);
	}

	public void createPowerUpTimer() {
		PowerUpSpawnTimer timer = new PowerUpSpawnTimer(getRandomTimeForSpawner(), selectRandomPowerUp());
	}

	private int getRandomTimeForSpawner() {
		TemporaryGameSettings tempSettings = TemporaryGameSettings.getInstance();

		return random.nextInt((tempSettings.getMaxTimeForPowerUpSpawn() - tempSettings.getMinTimeForPowerUpSpawn()) + 1)
				+ tempSettings.getMinTimeForPowerUpSpawn();
	}

	private PowerUps selectRandomPowerUp() {
		PowerUps randomValue = powerupEnums[random.nextInt(powerupEnums.length)];
//	    if (randomValue == PowerUps.DOUBLE_SHOT || 
//	    		randomValue == PowerUps.TRIPLE_SHOT) {
//	        return selectRandomPowerUp();
//	    }
		if (randomValue == PowerUps.DUMMY_DO_NOT_USE) {
			return selectRandomPowerUp();
		}
		return randomValue;
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

	private int getEffectDuration(PowerUps powerUpType) {
		switch (powerUpType) {
		case DOUBLE_SHOT:
			return 0;
		case HEALTH_AND_SHIELD_RESTORE:
		case INCREASED_MOVEMENT_SPEED:
		case INCREASED_NORMAL_ATTACK_SPEED:
		case INCREASED_NORMAL_DAMAGE:
		case INCREASED_SPECIAL_ATTACK_SPEED:
		case INCREASED_SPECIAL_DAMAGE:
			return TemporaryGameSettings.getInstance().getTemporaryPowerUpDuration();
		case TRIPLE_SHOT:
			return 0;
		default:
			return 0;

		}
	}

	private ImageEnums getIconByPowerUp(PowerUps powerUpType) {
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
		default:
			return ImageEnums.Test_Image;

		}
	}

	public void resetManager() {
		powerUpsOnTheField = new ArrayList<PowerUp>();
		directionEnums = Direction.values();
		powerupEnums = PowerUps.values();
		randomCoordinates = new ArrayList<Integer>();
		random = new Random();
	}

}