package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUpEnums;

public class IncreasedMovementSpeed extends PowerUpEffect{

	int regularMovementBonus;
	int boostedMovementBonus;
	public IncreasedMovementSpeed(PowerUpEnums powerUpType) {
		super(powerUpType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void activatePower() {
		regularMovementBonus = temporaryGameSettings.getDefaultMovementSpeedBoostAmount();
		boostedMovementBonus = temporaryGameSettings.getDefaultBoostedMovementSpeedBoostAmount();
		
		playerStats.addBoostedBonusMovementSpeed(boostedMovementBonus);
		playerStats.addBonusMovementSpeed(regularMovementBonus);
		
		System.out.println("Increased movement speed to a total of: " + playerStats.getMovementSpeed());
		System.out.println("Increased boosted movement speed to a total of: " + playerStats.getBoostedMovementSpeed());
	}
	
	@Override
	public void deactivatePower() {
		playerStats.addBoostedBonusMovementSpeed(-boostedMovementBonus);
		playerStats.addBonusMovementSpeed(-regularMovementBonus);
		
		System.out.println("Returned movement speed to a total of: " + playerStats.getMovementSpeed());
		System.out.println("Returned boosted movement speed to a total of: " + playerStats.getBoostedMovementSpeed());
	}
}
