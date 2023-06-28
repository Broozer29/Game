package game.playerpresets;

import data.PlayerStats;
import data.image.ImageDatabase;
import data.image.ImageEnums;
import game.movement.RegularPathFinder;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.PlayerSpecialAttackTypes;

public class FlamethrowerPreset implements PlayerPreset{

	PlayerStats playerStats = PlayerStats.getInstance();
	public FlamethrowerPreset() {
		playerStats = PlayerStats.getInstance();
	}
	public void loadPreset() {
		// Flamethrower preset
		
		playerStats.setFlameThrowerMaxSteps(ImageDatabase.getInstance().getGif(ImageEnums.Flamethrower_Animation).size());
		playerStats.setAttackSpeed(5);
		playerStats.setAttackDamage(20);
		playerStats.setAttackType(PlayerAttackTypes.Flamethrower);
		playerStats.setMissilePathFinder(new RegularPathFinder());
		playerStats.setPlayerMissileType(ImageEnums.Flamethrower_Animation);
		playerStats.setMissileScale(1);
		playerStats.setMissileImpactScale(1);
		playerStats.setPlayerSpecialAttackType(PlayerSpecialAttackTypes.Firewall);
		playerStats.setFirewallSize(3);
		playerStats.setFirewallDamage((float) 0.5);
		playerStats.setFirewallSpeed(3);
		playerStats.setSpecialAttackSpeed(5);
		
	}
}