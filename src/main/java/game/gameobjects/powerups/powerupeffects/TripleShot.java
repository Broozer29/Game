package game.gameobjects.powerups.powerupeffects;

import game.gameobjects.powerups.creation.PowerUpEffect;
import game.gameobjects.powerups.PowerUpEnums;

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
