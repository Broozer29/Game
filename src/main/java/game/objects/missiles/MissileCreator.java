package game.objects.missiles;

import data.PlayerStats;
import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.missiles.missiletypes.AlienLaserbeam;
import game.objects.missiles.missiletypes.BombaProjectile;
import game.objects.missiles.missiletypes.BulldozerProjectile;
import game.objects.missiles.missiletypes.DefaultPlayerLaserbeam;
import game.objects.missiles.missiletypes.EnergizerProjectile;
import game.objects.missiles.missiletypes.FirewallMissile;
import game.objects.missiles.missiletypes.FlamerProjectile;
import game.objects.missiles.missiletypes.FlamethrowerProjectile;
import game.objects.missiles.missiletypes.Rocket1;
import game.objects.missiles.missiletypes.SeekerProjectile;
import game.objects.missiles.missiletypes.TazerProjectile;

public class MissileCreator {

	private static MissileCreator instance = new MissileCreator();

	private MissileCreator() {
	}

	public static MissileCreator getInstance() {
		return instance;
	}

	public Missile createFriendlyMissile(int xCoordinate, int yCoordinate, ImageEnums missileType,
			ImageEnums explosionType, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed,
			int yMovementSpeed, PlayerAttackTypes attackType) {
		Point start = new Point(xCoordinate, yCoordinate);
		Point destination = null;
		float damage = 0;
		switch (attackType) {
		case Flamethrower:
			destination = pathFinder.calculateEndPointBySteps(start, rotation,
					PlayerStats.getInstance().getFlameThrowerMaxSteps(), xMovementSpeed, yMovementSpeed);
			damage = PlayerStats.getInstance().getNormalAttackDamage();
			break;
		case Laserbeam:
			destination = pathFinder.calculateInitialEndpoint(start, rotation, true);
			damage = PlayerStats.getInstance().getNormalAttackDamage();
			break;
		case Rocket:
			destination = pathFinder.calculateInitialEndpoint(start, rotation, true);
			damage = PlayerStats.getInstance().getNormalAttackDamage();
			break;
		case Shotgun:
			break;
		case Firewall:
			destination = pathFinder.calculateInitialEndpoint(start, rotation, true);
	        damage = PlayerStats.getInstance().getFirewallDamage();
			break;
		default:
			destination = pathFinder.calculateInitialEndpoint(start, rotation, true);
			break;
		}

		Missile missile = createMissile(missileType, xCoordinate, yCoordinate, destination, explosionType, rotation,
				scale, pathFinder, xMovementSpeed, yMovementSpeed, true, damage);
		missile.setVisible(true);
		return missile;
	}

	// Called by all enemy classes when fireAction() is called.
	public Missile createEnemyMissile(int xCoordinate, int yCoordinate, ImageEnums missileType,
			ImageEnums explosionType, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed,
			int yMovementSpeed, float damage) {

		Point start = new Point(xCoordinate, yCoordinate);
		Point destination = pathFinder.calculateInitialEndpoint(start, rotation, false);
		Missile missile = createMissile(missileType, xCoordinate, yCoordinate, destination, explosionType, rotation,
				scale, pathFinder, xMovementSpeed, yMovementSpeed, false, damage);
		missile.setVisible(true);
		return missile;
	}

	
	//PlayerStats.getInstance().getNormalAttackDamage()
	//
	//
	private Missile createMissile(ImageEnums missileType, int xCoordinate, int yCoordinate, Point destination,
			ImageEnums explosionType, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed,
			int yMovementSpeed, boolean friendly, float damage) {
		switch (missileType) {
		case Alien_Laserbeam:
			return new AlienLaserbeam(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed,friendly, damage);
		case Seeker_Missile:
			return new SeekerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed, friendly, damage);
		case Flamer_Missile:
			return new FlamerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed, friendly, damage);
		case Tazer_Missile:
			return new TazerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed, friendly, damage);
		case Bulldozer_Missile:
			return new BulldozerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed, friendly, damage);
		case Bomba_Missile:
			return new BombaProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed, friendly, damage);
		case Energizer_Missile:
			return new EnergizerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed, friendly, damage);
		case Player_Laserbeam:
			return new DefaultPlayerLaserbeam(xCoordinate, yCoordinate, destination, missileType, explosionType,
					rotation, scale, pathFinder,damage , xMovementSpeed,
					yMovementSpeed, friendly);
		case Flamethrower_Animation:
			return new FlamethrowerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType,
					rotation, scale, pathFinder, damage, xMovementSpeed,
					yMovementSpeed, friendly);
		case Rocket_1:
			return new Rocket1(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation, scale,
					pathFinder, xMovementSpeed, yMovementSpeed, damage, friendly);
		case FirewallParticle:
		case Firespout_Animation:
			return new FirewallMissile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation, scale,
					pathFinder, xMovementSpeed, yMovementSpeed, damage, friendly);
		default:
			throw new IllegalArgumentException("Invalid missile type: " + missileType);
		}
	}

}
