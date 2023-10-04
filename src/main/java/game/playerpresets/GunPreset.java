package game.playerpresets;

import game.movement.pathfinders.RegularPathFinder;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import gamedata.PlayerStats;
import gamedata.image.ImageDatabase;
import gamedata.image.ImageEnums;

public class GunPreset implements GunPresetInterface {
	
	private PlayerAttackTypes attackType;
	
	public GunPreset(PlayerAttackTypes attackType) {
		this.attackType = attackType;
	}
	
	@Override
	public void loadPreset() {
		switch (attackType) {
		case Flamethrower:
			initFlamethrowerPreset();
			break;
		case Laserbeam:
			initLaserbeamPreset();
			break;
		case Rocket:
			initRocketPreset();
			break;
		case Shotgun:
			break;
		case DUMMY_VALUE:
		case Firewall:
		case NONE:
		default:
			break;
		}
	}
	
	private void initRocketPreset() {
		PlayerStats.getInstance().setAttackSpeed(15);
		PlayerStats.getInstance().setAttackDamage(50);
		PlayerStats.getInstance().setAttackType(PlayerAttackTypes.Rocket);
		PlayerStats.getInstance().setMissilePathFinder(new RegularPathFinder());
		PlayerStats.getInstance().setPlayerMissileType(ImageEnums.Rocket_1);
		PlayerStats.getInstance().setPlayerMissileImpactType(ImageEnums.Rocket_1_Explosion);
		PlayerStats.getInstance().setMissileScale(1);
		PlayerStats.getInstance().setMissileImpactScale(1);
	}
	
	private void initFlamethrowerPreset() {
		PlayerStats.getInstance().setFlameThrowerMaxSteps(ImageDatabase.getInstance().getGif(ImageEnums.Flamethrower_Animation).size());
		PlayerStats.getInstance().setAttackSpeed(5);
		PlayerStats.getInstance().setAttackDamage(20);
		PlayerStats.getInstance().setAttackType(PlayerAttackTypes.Flamethrower);
		PlayerStats.getInstance().setMissilePathFinder(new RegularPathFinder());
		PlayerStats.getInstance().setPlayerMissileType(ImageEnums.Flamethrower_Animation);
		PlayerStats.getInstance().setMissileScale(1);
		PlayerStats.getInstance().setMissileImpactScale(1);
	}

	private void initLaserbeamPreset() {
		PlayerStats.getInstance().setAttackSpeed(15);
		PlayerStats.getInstance().setAttackDamage(25);
		PlayerStats.getInstance().setAttackType(PlayerAttackTypes.Laserbeam);
		PlayerStats.getInstance().setMissilePathFinder(new RegularPathFinder());
		PlayerStats.getInstance().setPlayerMissileType(ImageEnums.Player_Laserbeam);
		PlayerStats.getInstance().setPlayerMissileImpactType(ImageEnums.Impact_Explosion_One);
		PlayerStats.getInstance().setMissileScale(1);
		PlayerStats.getInstance().setMissileImpactScale(1);
	}
}
