package game.objects.powerups.powerupeffects;

import game.objects.powerups.creation.PowerUpEffect;
import game.objects.powerups.PowerUpEnums;

public class IncreasedSpecialAttackDamage extends PowerUpEffect {
	float specialAttackDamageBonus;

	public IncreasedSpecialAttackDamage(PowerUpEnums powerUpType) {
		super(powerUpType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void activatePower() {
		specialAttackDamageBonus = temporaryGameSettings.getDefaultSpecialAttackDamageBonus();
		playerStats.addBonusSpecialAttackDamage(specialAttackDamageBonus);
		
		System.out.println("Increased special attack damage to a total of: " + playerStats.getSpecialAttackDamage());
	}

	@Override
	public void deactivatePower() {
		specialAttackDamageBonus = temporaryGameSettings.getDefaultSpecialAttackDamageBonus();
		playerStats.addBonusSpecialAttackDamage(-specialAttackDamageBonus);
		
		System.out.println("Returned special attack damage to a total of: " + playerStats.getSpecialAttackDamage());
	}
}
