package game.gameobjects.powerups.creation;

import game.gameobjects.player.PlayerManager;
import game.gameobjects.player.spaceship.SpaceShip;
import game.gameobjects.player.BoostsUpgradesAndBuffsSettings;
import game.gameobjects.player.PlayerStats;
import game.gameobjects.powerups.PowerUpEnums;

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