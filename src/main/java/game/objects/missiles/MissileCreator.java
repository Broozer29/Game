package game.objects.missiles;

import game.objects.missiles.missiletypes.*;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class MissileCreator {

	private static MissileCreator instance = new MissileCreator();

	private MissileCreator() {
	}

	public static MissileCreator getInstance() {
		return instance;
	}

//	public Missile createFriendlyMissile(int xCoordinate, int yCoordinate, ImageEnums missileType,
//			ImageEnums explosionType, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed,
//			int yMovementSpeed, PlayerAttackTypes attackType) {
//		Point start = new Point(xCoordinate, yCoordinate);
//		Point destination = null;
//		float damage = 0;
//		switch (attackType) {
//		case Flamethrower:
//			destination = pathFinder.calculateEndPointBySteps(start, rotation,
//					PlayerStats.getInstance().getFlameThrowerMaxSteps(), xMovementSpeed, yMovementSpeed);
//			damage = PlayerStats.getInstance().getNormalAttackDamage();
//			break;
//		case Laserbeam:
//			destination = pathFinder.calculateInitialEndpoint(start, rotation, true);
//			damage = PlayerStats.getInstance().getNormalAttackDamage();
//			break;
//		case Rocket:
//			destination = pathFinder.calculateInitialEndpoint(start, rotation, true);
//			damage = PlayerStats.getInstance().getNormalAttackDamage();
//			break;
//		case Shotgun:
//			break;
//		case Firewall:
//			destination = pathFinder.calculateInitialEndpoint(start, rotation, true);
//	        damage = PlayerStats.getInstance().getFirewallDamage();
//			break;
//		default:
//			destination = pathFinder.calculateInitialEndpoint(start, rotation, true);
//			break;
//		}
//
//		Missile missile = createMissile(missileType, xCoordinate, yCoordinate, destination, explosionType, rotation,
//				scale, pathFinder, xMovementSpeed, yMovementSpeed, true, damage);
//		missile.setVisible(true);
//		return missile;
//	}

	public Missile createMissile(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		switch (missileConfiguration.getMissileType()) {
			case AlienLaserbeam -> {
				return new AlienLaserbeam(spriteConfiguration,missileConfiguration);
			}
			case BombaProjectile -> {
				return new BombaProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case BulldozerProjectile -> {
				return new BulldozerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case EnergizerProjectile -> {
				return new EnergizerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case FlamerProjectile -> {
				return new FlamerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case SeekerProjectile -> {
				return new SeekerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case TazerProjectile -> {
				return new TazerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case OrbitingMissile -> {
				return new OrbitingMissile(spriteConfiguration,missileConfiguration);
			}
			case FlameThrowerProjectile -> {
				return new FlamethrowerProjectile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
			case DefaultPlayerLaserbeam -> {
				return new DefaultPlayerLaserbeam(spriteConfiguration,missileConfiguration);
			}
			case FirewallMissile -> {
				return new FirewallMissile(upgradeConfig(spriteConfiguration, 2, true),missileConfiguration);
			}
		}
		return null;
	}

	private SpriteAnimationConfiguration upgradeConfig(SpriteConfiguration spriteConfiguration, int frameDelay, boolean infiniteLoop){
		return new SpriteAnimationConfiguration(spriteConfiguration, frameDelay, infiniteLoop);
	}
}


