package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUpEnums;

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
