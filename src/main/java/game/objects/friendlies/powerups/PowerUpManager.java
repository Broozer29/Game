package game.objects.friendlies.powerups;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.PlayerManager;
import game.movement.Direction;
import gamedata.BoostsUpgradesAndBuffsSettings;
import gamedata.DataClass;
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;

public class PowerUpManager {

	private static PowerUpManager instance = new PowerUpManager();
	private List<PowerUp> powerUpsOnTheField = new ArrayList<PowerUp>();

	private PowerUpManager() {

	}

	public static PowerUpManager getInstance() {
		return instance;
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
				if (isNearby(powerUp, friendlyManager.getSpaceship())) {
					if (powerUp.getBounds().intersects(friendlyManager.getSpaceship().getBounds())) {
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
	}

	private boolean isWithinBoardBlockThreshold(Sprite sprite1, Sprite sprite2) {
		int blockDifference = Math.abs(sprite1.getCurrentBoardBlock() - sprite2.getCurrentBoardBlock());
		return blockDifference <= 2;
	}

	private boolean isNearby(Sprite sprite1, Sprite sprite2) {
		if (!isWithinBoardBlockThreshold(sprite1, sprite2)) {
			return false;
		}

		double distance = Math.hypot(sprite1.getXCoordinate() - sprite2.getXCoordinate(),
				sprite1.getYCoordinate() - sprite2.getYCoordinate());
		return distance < 150;
	}

	

	public void resetManager() {
		powerUpsOnTheField = new ArrayList<PowerUp>();
	}

}