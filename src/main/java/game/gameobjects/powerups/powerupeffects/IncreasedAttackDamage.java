package game.gameobjects.powerups.powerupeffects;

import game.gameobjects.powerups.creation.PowerUpEffect;
import game.gameobjects.powerups.PowerUpEnums;

public class IncreasedAttackDamage extends PowerUpEffect{
	float attackDamageBonus;
	
	public IncreasedAttackDamage(PowerUpEnums powerUpType) {
		super(powerUpType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void activatePower() {
		attackDamageBonus = temporaryGameSettings.getDefaultAttackDamageBonus();
		playerStats.modifyBonusDamageMultiplier(attackDamageBonus);
		System.out.println("Added bonus attack damage to a total of: " + playerStats.getNormalAttackDamage());
	}
	
	@Override
	public void deactivatePower() {
		attackDamageBonus = temporaryGameSettings.getDefaultAttackDamageBonus();
		playerStats.modifyBonusDamageMultiplier(-attackDamageBonus);
		System.out.println("Reverted bonus attack damage to a total of: " + playerStats.getNormalAttackDamage());
	}
}
