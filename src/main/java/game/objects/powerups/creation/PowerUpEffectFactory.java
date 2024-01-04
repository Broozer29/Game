package game.objects.powerups.creation;

import game.objects.powerups.PowerUpEnums;
import game.objects.powerups.powerupeffects.DoubleShot;
import game.objects.powerups.powerupeffects.MissileGuardianBot;
import game.objects.powerups.powerupeffects.IncreasedAttackDamage;
import game.objects.powerups.powerupeffects.IncreasedAttackSpeed;
import game.objects.powerups.powerupeffects.IncreasedMovementSpeed;
import game.objects.powerups.powerupeffects.IncreasedSpecialAttackDamage;
import game.objects.powerups.powerupeffects.IncreasedSpecialAttackSpeed;
import game.objects.powerups.powerupeffects.RestorePackage;
import game.objects.powerups.powerupeffects.TripleShot;

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
		case INCREASED_MOVEMENT_SPEED:
			return new IncreasedMovementSpeed(powerUpType);
		case INCREASED_NORMAL_ATTACK_SPEED:
			return new IncreasedAttackSpeed(powerUpType);
		case INCREASED_NORMAL_DAMAGE:
			return new IncreasedAttackDamage(powerUpType);
		case INCREASED_SPECIAL_ATTACK_SPEED:
			return new IncreasedSpecialAttackSpeed(powerUpType);
		case INCREASED_SPECIAL_DAMAGE:
			return new IncreasedSpecialAttackDamage(powerUpType);
		case TRIPLE_SHOT:
			return new TripleShot(powerUpType);
		case Guardian_Drone_Homing_Missile:
			return new MissileGuardianBot(powerUpType);
		default:
			System.out.println("Tried to spawn a powerup that is not implemented: " + powerUpType);
			return new RestorePackage(powerUpType);
		}
	}
	
}