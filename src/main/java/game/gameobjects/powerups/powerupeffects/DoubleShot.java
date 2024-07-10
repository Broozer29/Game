package game.gameobjects.powerups.powerupeffects;

import game.gameobjects.powerups.creation.PowerUpEffect;
import game.gameobjects.powerups.PowerUpEnums;

public class DoubleShot extends PowerUpEffect{

	public DoubleShot(PowerUpEnums powerUpType) {
		super(powerUpType);
	}
	
	@Override
	public void activatePower() {
		temporaryGameSettings.setDoubleShotActive(true);
	}
	
	@Override
	public void deactivatePower() {
		
	}
}
