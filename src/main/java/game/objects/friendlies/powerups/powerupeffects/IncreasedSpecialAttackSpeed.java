package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUps;

public class IncreasedSpecialAttackSpeed extends PowerUpEffect {
	int specialAttackSpeedBonus;

	public IncreasedSpecialAttackSpeed(PowerUps powerUpType) {
		super(powerUpType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void activatePower() {
		specialAttackSpeedBonus = temporaryGameSettings.getDefaultSpecialAttackSpeedBonus();
		playerStats.addBonusSpecialAttackSpeed(-specialAttackSpeedBonus);

		System.out.println("Increased special attack speed to a total of: " + playerStats.getSpecialAttackSpeed());
	}

	@Override
	public void deactivatePower() {
		specialAttackSpeedBonus = temporaryGameSettings.getDefaultSpecialAttackSpeedBonus();
		playerStats.addBonusSpecialAttackSpeed(specialAttackSpeedBonus);
		
		System.out.println("Returned special attack speed to a total of: " + playerStats.getSpecialAttackSpeed());
	}
}
