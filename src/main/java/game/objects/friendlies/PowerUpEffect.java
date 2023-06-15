package game.objects.friendlies;

import data.PlayerPowerUpEffects;

public class PowerUpEffect {
	private PowerUps powerupType;

	public PowerUpEffect(PowerUps powerUpType) {
		this.powerupType = powerUpType;
	}
	
	public void activatePowerEffect() {
		switch (this.powerupType) {
		case DOUBLE_SHOT:
			activateDoubleShot();
			break;
		case TRIPLE_SHOT:
			activateTripleShot();
			break;
		}
	}
	

	private void activateDoubleShot() {
		PlayerPowerUpEffects playerPowerUpEffects = PlayerPowerUpEffects.getInstance();
		playerPowerUpEffects.setDoubleShotActive(true);
	}

	private void activateTripleShot() {
		PlayerPowerUpEffects playerPowerUpEffects = PlayerPowerUpEffects.getInstance();
		playerPowerUpEffects.setTripleShotActive(true);
	}
}
