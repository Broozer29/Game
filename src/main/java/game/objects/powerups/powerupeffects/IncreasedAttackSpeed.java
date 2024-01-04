package game.objects.powerups.powerupeffects;

import game.objects.powerups.creation.PowerUpEffect;
import game.objects.powerups.PowerUpEnums;

public class IncreasedAttackSpeed extends PowerUpEffect{
	int attackSpeedBonus;
	public IncreasedAttackSpeed(PowerUpEnums powerUpType) {
		super(powerUpType);
	}
	
	@Override
	public void activatePower() {
		attackSpeedBonus = temporaryGameSettings.getDefaultAttackSpeedBonus();
		playerStats.addBonusAttackSpeed(-attackSpeedBonus);
		System.out.println("Reduced attack speed to a total of: " + playerStats.getAttackSpeed());
	}
	
	@Override
	public void deactivatePower() {
		attackSpeedBonus = temporaryGameSettings.getDefaultAttackSpeedBonus();
		playerStats.addBonusAttackSpeed(attackSpeedBonus);
		System.out.println("Returned attack speed to a total of: " + playerStats.getAttackSpeed());
	}
}
