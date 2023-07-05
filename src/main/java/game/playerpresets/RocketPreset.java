package game.playerpresets;

import data.PlayerStats;
import data.image.ImageEnums;
import game.movement.RegularPathFinder;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.PlayerSpecialAttackTypes;

public class RocketPreset implements PlayerPreset{
	
	
	PlayerStats playerStats = PlayerStats.getInstance();
	public RocketPreset() {
		playerStats = PlayerStats.getInstance();
	}
	
	public void loadPreset() {
		// Laserbeam preset
		playerStats.setAttackSpeed(25);
		playerStats.setAttackDamage(40);
		playerStats.setAttackType(PlayerAttackTypes.Rocket);
		playerStats.setMissilePathFinder(new RegularPathFinder());
		playerStats.setPlayerMissileType(ImageEnums.Rocket_1);
		playerStats.setPlayerMissileImpactType(ImageEnums.Rocket_1_Explosion);
		playerStats.setMissileScale(1);
		playerStats.setMissileImpactScale(1);
		playerStats.setPlayerSpecialAttackType(PlayerSpecialAttackTypes.EMP); // Change to new spcial attack
	}
}
