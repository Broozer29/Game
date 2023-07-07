package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUpEnums;

public class TripleShot extends PowerUpEffect{

	public TripleShot(PowerUpEnums powerUpType) {
		super(powerUpType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void activatePower() {
		temporaryGameSettings.setTripleShotActive(true);
	}
	
	@Override
	public void deactivatePower() {
		
	}
}
