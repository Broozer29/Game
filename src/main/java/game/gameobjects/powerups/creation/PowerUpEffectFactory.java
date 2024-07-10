package game.gameobjects.powerups.creation;

import game.gameobjects.powerups.PowerUpEnums;
import game.gameobjects.powerups.powerupeffects.DoubleShot;
import game.gameobjects.powerups.powerupeffects.MissileGuardianBotPowerUpEffect;
import game.gameobjects.powerups.powerupeffects.IncreasedAttackDamage;
import game.gameobjects.powerups.powerupeffects.RestorePackage;
import game.gameobjects.powerups.powerupeffects.TripleShot;

public class PowerUpEffectFactory {

	
	private static PowerUpEffectFactory instance = new PowerUpEffectFactory();
	private PowerUpEffectFactory() {
		
	}
	
	public static PowerUpEffectFactory getInstance() {
		return instance;
	}
	
	
	public PowerUpEffect createPowerUpEffect(PowerUpEnums powerUpType) {
		switch (powerUpType) {
		case DOUBLE_SHOT:
			return new DoubleShot(powerUpType);
		case HEALTH_AND_SHIELD_RESTORE:
			return new RestorePackage(powerUpType);
		case INCREASED_NORMAL_DAMAGE:
			return new IncreasedAttackDamage(powerUpType);
		case TRIPLE_SHOT:
			return new TripleShot(powerUpType);
		case Guardian_Drone:
			return new MissileGuardianBotPowerUpEffect(powerUpType);
		default:
			System.out.println("Tried to spawn a powerup that is not implemented: " + powerUpType);
			return new RestorePackage(powerUpType);
		}
	}
	
}