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
import data.image.ImageResizer;
import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.objects.friendlies.powerups.PowerUp;
import game.objects.friendlies.powerups.PowerUpSpawnTimer;
import game.objects.friendlies.powerups.PowerUps;

public class PowerUpManager {

	private static PowerUpManager instance = new PowerUpManager();
	private List<PowerUp> powerUpsOnTheField = new ArrayList<PowerUp>();

	private PowerUpManager() {
		createPowerUpTimer();
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
		FriendlyManager friendlyManager = FriendlyManager.getInstance();
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
		List<Integer> randomCoordinates = getRandomSpawnCoords();
		int randomX = randomCoordinates.get(0);
		int randomY = randomCoordinates.get(1);
		Direction randomDirection = selectRandomDirection();
		PowerUp newPowerUp = new PowerUp(randomX, randomY, 1, randomDirection, powerUpType,
				getIconByPowerUp(powerUpType), getEffectDuration(powerUpType), false);
		newPowerUp.setImageDimensions(50, 50);
		powerUpsOnTheField.add(newPowerUp);
	}

	public void createPowerUpTimer() {
		PowerUpSpawnTimer timer = new PowerUpSpawnTimer(getRandomTimeForSpawner(), selectRandomPowerUp());
	}

	private int getRandomTimeForSpawner() {
		TemporaryGameSettings tempSettings = TemporaryGameSettings.getInstance();
		Random random = new Random();
		return random.nextInt((tempSettings.getMaxTimeForPowerUpSpawn() - tempSettings.getMinTimeForPowerUpSpawn()) + 1)
				+ tempSettings.getMinTimeForPowerUpSpawn();
	}

	private PowerUps selectRandomPowerUp() {
		PowerUps[] enums = PowerUps.values();
		Random random = new Random();
		PowerUps randomValue = enums[random.nextInt(enums.length)];

//	    if (randomValue == PowerUps.DOUBLE_SHOT || 
//	    		randomValue == PowerUps.TRIPLE_SHOT) {
//	        return selectRandomPowerUp();
//	    }

		return randomValue;
	}

	private Direction selectRandomDirection() {
		Direction[] enums = Direction.values();
		Random random = new Random();
		Direction randomValue = enums[random.nextInt(enums.length)];
		
		if (randomValue == Direction.NONE) {
			return selectRandomDirection();
		}
		return randomValue;
	}

	private List<Integer> getRandomSpawnCoords() {
		Random random = new Random();

		int minY = 0;
		int maxY = DataClass.getInstance().getWindowHeight();

		int minX = 0;
		int maxX = DataClass.getInstance().getWindowWidth();

		int randomY = random.nextInt((maxY - minY) + 1) + minY;
		int randomX = random.nextInt((maxX - minX) + 1) + minX;

		List<Integer> randomCoordinates = new ArrayList<Integer>();
		randomCoordinates.add(randomX);
		randomCoordinates.add(randomY);

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

}
