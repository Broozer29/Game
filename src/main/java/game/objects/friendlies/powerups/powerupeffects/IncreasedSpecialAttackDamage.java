package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUpEnums;

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
