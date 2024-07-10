package game.gameobjects.powerups.powerupeffects;

import game.gameobjects.powerups.creation.PowerUpEffect;
import game.gameobjects.powerups.PowerUpEnums;

public class RestorePackage extends PowerUpEffect{

	public RestorePackage(PowerUpEnums powerUpType) {
		super(powerUpType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void activatePower() {
		float healAmount = temporaryGameSettings.getRepairPackageHealthRestore();
		float shieldAmount = temporaryGameSettings.getRepairPackageShieldRestore();

		playerSpaceShip.repairHealth(healAmount);
		playerSpaceShip.repairShields(shieldAmount);
	}
	
	@Override
	public void deactivatePower() {

	}
}
