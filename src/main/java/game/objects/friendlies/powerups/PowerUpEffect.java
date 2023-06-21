package game.objects.friendlies.powerups;

import data.PlayerStats;
import data.TemporaryGameSettings;
import game.managers.FriendlyManager;
import game.objects.friendlies.friendlyobjects.SpaceShip;

public class PowerUpEffect {
	protected FriendlyManager friendlyManager = FriendlyManager.getInstance();
	protected TemporaryGameSettings temporaryGameSettings = TemporaryGameSettings.getInstance();
	protected PlayerStats playerStats = PlayerStats.getInstance();
	protected SpaceShip playerSpaceShip = friendlyManager.getSpaceship();
	
	protected PowerUps powerUpType;
	public PowerUpEffect(PowerUps powerUpType) {
		this.powerUpType = powerUpType;
		friendlyManager = FriendlyManager.getInstance();
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
