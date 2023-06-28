package game.playerpresets;

import data.PlayerStats;
import data.image.ImageEnums;
import game.movement.RegularPathFinder;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.PlayerSpecialAttackTypes;

public class LaserbeamPreset implements PlayerPreset{
	
	
	PlayerStats playerStats = PlayerStats.getInstance();
	public LaserbeamPreset() {
		playerStats = PlayerStats.getInstance();
	}
	
	public void loadPreset() {
		// Laserbeam preset
		playerStats.setAttackSpeed(15);
		playerStats.setAttackDamage(25);
		playerStats.setAttackType(PlayerAttackTypes.Laserbeam);
		playerStats.setMissilePathFinder(new RegularPathFinder());
		playerStats.setPlayerMissileType(ImageEnums.Player_Laserbeam);
		playerStats.setPlayerMissileImpactType(ImageEnums.Impact_Explosion_One);
		playerStats.setMissileScale(1);
		playerStats.setMissileImpactScale(1);
		playerStats.setPlayerSpecialAttackType(PlayerSpecialAttackTypes.EMP);
	}
	
}