package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.powerups.PowerUpAcquiredText;
import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUpEnums;

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
