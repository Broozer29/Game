package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUps;

public class IncreasedAttackDamage extends PowerUpEffect{
	float attackDamageBonus;
	
	public IncreasedAttackDamage(PowerUps powerUpType) {
		super(powerUpType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void activatePower() {
		attackDamageBonus = temporaryGameSettings.getDefaultAttackDamageBonus();
		playerStats.addBonusDefaultAttackDamage(attackDamageBonus);
		System.out.println("Added bonus attack damage to a total of: " + playerStats.getNormalAttackDamage());
	}
	
	@Override
	public void deactivatePower() {
		attackDamageBonus = temporaryGameSettings.getDefaultAttackDamageBonus();
		playerStats.addBonusDefaultAttackDamage(-attackDamageBonus);
		System.out.println("Reverted bonus attack damage to a total of: " + playerStats.getNormalAttackDamage());
	}
}
