package game.objects.friendlies.powerups;

import data.PlayerStats;
import data.TemporaryGameSettings;
import game.managers.PlayerManager;
import game.objects.friendlies.spaceship.SpaceShip;

public class PowerUpEffect {
	protected PlayerManager friendlyManager = PlayerManager.getInstance();
	protected TemporaryGameSettings temporaryGameSettings = TemporaryGameSettings.getInstance();
	protected PlayerStats playerStats = PlayerStats.getInstance();
	protected SpaceShip playerSpaceShip = friendlyManager.getSpaceship();
	
	protected PowerUps powerUpType;
	public PowerUpEffect(PowerUps powerUpType) {
		this.powerUpType = powerUpType;
		friendlyManager = PlayerManager.getInstance();
		temporaryGameSettings = TemporaryGameSettings.getInstance();
		playerStats = PlayerStats.getInstance();
		playerSpaceShip = friendlyManager.getSpaceship();
	}
	
	public void activatePower() {
		//Placeholder for the extended classes
	}
	
	public void deactivatePower() {
		//Placeholder for the extended classes
	}

}