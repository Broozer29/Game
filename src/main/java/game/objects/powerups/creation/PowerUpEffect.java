package game.objects.powerups.creation;

import game.objects.player.PlayerManager;
import game.objects.player.spaceship.SpaceShip;
import game.objects.player.BoostsUpgradesAndBuffsSettings;
import game.objects.player.PlayerStats;
import game.objects.powerups.PowerUpEnums;

public class PowerUpEffect {
	protected PlayerManager friendlyManager = PlayerManager.getInstance();
	protected BoostsUpgradesAndBuffsSettings temporaryGameSettings = BoostsUpgradesAndBuffsSettings.getInstance();
	protected PlayerStats playerStats = PlayerStats.getInstance();
	protected SpaceShip playerSpaceShip = friendlyManager.getSpaceship();
	
	protected PowerUpEnums powerUpType;
	public PowerUpEffect(PowerUpEnums powerUpType) {
		this.powerUpType = powerUpType;
		friendlyManager = PlayerManager.getInstance();
		temporaryGameSettings = BoostsUpgradesAndBuffsSettings.getInstance();
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