package game.managers;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.audio.AudioEnums;
import game.movement.Direction;
import game.objects.friendlies.PowerUps;
import game.objects.friendlies.friendlyobjects.PowerUp;
import game.spawner.PowerUpTimer;

public class PowerUpManager {

	private static PowerUpManager instance = new PowerUpManager();
	private List<PowerUp> powerUpsOnTheField = new ArrayList<PowerUp>();

	private PowerUpManager() {
		createPowerUp();
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
	
	private void createPowerUp() {
		PowerUp newPowerUp = new PowerUp(500, 500, 1, Direction.LEFT_DOWN, PowerUps.DOUBLE_SHOT, 0, false);
		powerUpsOnTheField.add(newPowerUp);
		
		PowerUp newPowerUp2 = new PowerUp(100, 100, 1, Direction.LEFT_UP, PowerUps.TRIPLE_SHOT, 0, false);
		powerUpsOnTheField.add(newPowerUp2);
	}

}
