package game.objects.friendlies.powerups;

import game.objects.friendlies.powerups.powerupeffects.DoubleShot;
import game.objects.friendlies.powerups.powerupeffects.IncreasedAttackDamage;
import game.objects.friendlies.powerups.powerupeffects.IncreasedAttackSpeed;
import game.objects.friendlies.powerups.powerupeffects.IncreasedMovementSpeed;
import game.objects.friendlies.powerups.powerupeffects.IncreasedSpecialAttackDamage;
import game.objects.friendlies.powerups.powerupeffects.IncreasedSpecialAttackSpeed;
import game.objects.friendlies.powerups.powerupeffects.RestorePackage;
import game.objects.friendlies.powerups.powerupeffects.TripleShot;

public class PowerUpEffectFactory {

	
	private static PowerUpEffectFactory instance = new PowerUpEffectFactory();
	private PowerUpEffectFactory() {
		
	}
	
	public static PowerUpEffectFactory getInstance() {
		return instance;
	}
	
	
	public PowerUpEffect createPowerUpEffect(PowerUps powerUpType) {
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
		default:
			System.out.println("Tried to spawn a powerup that is not implemented: " + powerUpType);
			return new RestorePackage(powerUpType);
		}
	}
	
}
