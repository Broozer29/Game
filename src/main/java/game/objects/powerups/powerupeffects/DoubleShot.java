package game.objects.powerups.powerupeffects;

import game.objects.powerups.creation.PowerUpEffect;
import game.objects.powerups.PowerUpEnums;

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
