package game.objects.player.playerpresets;

import game.movement.pathfinders.RegularPathFinder;
import game.objects.missiles.MissileTypeEnums;
import game.objects.player.PlayerStats;
import VisualAndAudioData.image.ImageEnums;

public class GunPreset {
	
	private MissileTypeEnums attackType;
	
	public GunPreset(MissileTypeEnums attackType) {
		this.attackType = attackType;
	}
	
	public void loadPreset() {
		switch (attackType) {
			case DefaultPlayerLaserbeam:
			initLaserbeamPreset();
			break;
			case Rocket1:
			initRocketPreset();
			break;
		default:
			break;
		}
	}
	
	private void initRocketPreset() {
		PlayerStats.getInstance().setAttackSpeed(15);
		PlayerStats.getInstance().setAttackDamage(50);
		PlayerStats.getInstance().setAttackType(MissileTypeEnums.Rocket1);
		PlayerStats.getInstance().setPlayerMissileType(ImageEnums.Rocket_1);
		PlayerStats.getInstance().setPlayerMissileImpactType(ImageEnums.Rocket_1_Explosion);
		PlayerStats.getInstance().setMissileScale(1);
		PlayerStats.getInstance().setMissileImpactScale(1);
	}
	

	private void initLaserbeamPreset() {
		PlayerStats.getInstance().setAttackSpeed(15);
		PlayerStats.getInstance().setAttackDamage(25);
		PlayerStats.getInstance().setAttackType(MissileTypeEnums.DefaultPlayerLaserbeam);
		PlayerStats.getInstance().setPlayerMissileType(ImageEnums.Player_Laserbeam);
		PlayerStats.getInstance().setPlayerMissileImpactType(ImageEnums.Impact_Explosion_One);
		PlayerStats.getInstance().setMissileScale(1);
		PlayerStats.getInstance().setMissileImpactScale(1);
	}
}
