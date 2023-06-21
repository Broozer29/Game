package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.powerups.PowerUpAcquiredText;
import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUps;

public class DoubleShot extends PowerUpEffect{

	public DoubleShot(PowerUps powerUpType) {
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
