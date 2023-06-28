package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.PlayerStats;
import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.enemies.Enemy;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.SpecialAttack;
import game.objects.missiles.AlienLaserbeam;
import game.objects.missiles.BombaProjectile;
import game.objects.missiles.BulldozerProjectile;
import game.objects.missiles.DefaultPlayerLaserbeam;
import game.objects.missiles.EnergizerProjectile;
import game.objects.missiles.FlamerProjectile;
import game.objects.missiles.FlamethrowerProjectile;
import game.objects.missiles.Missile;
import game.objects.missiles.SeekerProjectile;
import game.objects.missiles.TazerProjectile;

public class MissileManager {

	private static MissileManager instance = new MissileManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private PlayerManager friendlyManager = PlayerManager.getInstance();
	private MovementInitiator movementManager = MovementInitiator.getInstance();
	private List<Missile> enemyMissiles = new ArrayList<Missile>();
	private List<Missile> friendlyMissiles = new ArrayList<Missile>();
	private List<SpecialAttack> specialAttacks = new ArrayList<SpecialAttack>();

	private List<AlienLaserbeam> alienLaserbeams = new ArrayList<AlienLaserbeam>();
	private List<FlamerProjectile> flamerProjectiles = new ArrayList<FlamerProjectile>();
	private List<BombaProjectile> bombaProjectiles = new ArrayList<BombaProjectile>();
	private List<BulldozerProjectile> bulldozerProjectiles = new ArrayList<BulldozerProjectile>();
	private List<EnergizerProjectile> energizerProjectiles = new ArrayList<EnergizerProjectile>();
	private List<SeekerProjectile> seekerProjectiles = new ArrayList<SeekerProjectile>();
	private List<TazerProjectile> tazerProjectiles = new ArrayList<TazerProjectile>();

	private MissileManager() {
	}

	public void resetManager() {
		enemyMissiles = new ArrayList<Missile>();
		friendlyMissiles = new ArrayList<Missile>();
	}

	public static MissileManager getInstance() {
		return instance;
	}

	public List<Missile> getEnemyMissiles() {
		return enemyMissiles;
	}

	public List<Missile> getFriendlyMissiles() {
		return friendlyMissiles;
	}

	public List<SpecialAttack> getSpecialAttacks() {
		return specialAttacks;
	}

	public void addExistingMissile(Missile missile) {
		missile.setVisible(true);
		if (missile.isFriendly()) {
			friendlyMissiles.add(missile);
		} else {
			enemyMissiles.add(missile);
		}
	}

	public void addFriendlyMissile(int xCoordinate, int yCoordinate, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed,
			PlayerAttackTypes attackType) {
		Point start = new Point(xCoordinate, yCoordinate);
		Point destination = null;
		switch (attackType) {
		case Flamethrower:
			destination = pathFinder.calculateEndPointBySteps(start, rotation,
					PlayerStats.getInstance().getFlameThrowerMaxSteps(), xMovementSpeed, yMovementSpeed);
			break;
		case Laserbeam:
			destination = pathFinder.calculateInitialEndpoint(start, rotation);
			break;
		case Rocket:
			break;
		case Shotgun:
			break;
		default:
			destination = pathFinder.calculateInitialEndpoint(start, rotation);
			break;
		}

		Missile missile = createMissile(missileType, xCoordinate, yCoordinate, destination, explosionType, rotation,
				scale, pathFinder, xMovementSpeed, yMovementSpeed);
		missile.setVisible(true);

		this.friendlyMissiles.add(missile);
	}

	// Called by all enemy classes when fireAction() is called.
	public void addEnemyMissile(int xCoordinate, int yCoordinate, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {

		Point start = new Point(xCoordinate, yCoordinate);
		Point destination = pathFinder.calculateInitialEndpoint(start, rotation);

		Missile missile = createMissile(missileType, xCoordinate, yCoordinate, destination, explosionType, rotation,
				scale, pathFinder, xMovementSpeed, yMovementSpeed);

		this.enemyMissiles.add(missile);
		switch (missileType) {
		case Alien_Laserbeam:
			this.alienLaserbeams.add((AlienLaserbeam) missile);
			break;
		case Seeker_Missile:
			this.seekerProjectiles.add((SeekerProjectile) missile);
			break;
		case Flamer_Missile:
			this.flamerProjectiles.add((FlamerProjectile) missile);
			break;
		case Tazer_Missile:
			this.tazerProjectiles.add((TazerProjectile) missile);
			break;
		case Bulldozer_Missile:
			this.bulldozerProjectiles.add((BulldozerProjectile) missile);
			break;
		case Energizer_Missile:
			this.energizerProjectiles.add((EnergizerProjectile) missile);
			break;
		case Bomba_Missile:
			this.bombaProjectiles.add((BombaProjectile) missile);
			break;
		}

	}

	public void updateGameTick() {
		moveMissiles();
		checkFriendlyMissileCollision();
		checkEnemyMissileCollision();
		triggerMissileAction();
	}

	private void checkFriendlyMissileCollision() {
		
		if (enemyManager == null) {
			enemyManager = EnemyManager.getInstance();
		}
		for (Missile missile : friendlyMissiles) {
			if (missile.isVisible()) {
				Rectangle r1 = null;
				if (missile.getAnimation() != null) {
					r1 = missile.getAnimation().getBounds();
				} else {
					r1 = missile.getBounds();
				}

				for (Enemy enemy : enemyManager.getEnemies()) {
					Rectangle r2 = enemy.getBounds();
					if (r1.intersects(r2)) {
						enemy.takeDamage(missile.getMissileDamage());
						if (missile.getExplosionAnimation() != null) {
							animationManager.addUpperAnimation(missile.getExplosionAnimation());
						}
						if (missile.getAnimation() != null) {
							missile.getAnimation().setVisible(false);
						}
						missile.setVisible(false);
					}
				}
			}
		}
		
		//This is broken intentionally, rework manually
//		for (SpecialAttack specialAttack : specialAttacks) {
//			if (specialAttack.getAnimation().isVisible()) {
//				checkSpecialAttackEnemyCollision(specialAttack);
//				checkSpecialAttackMissileCollision(specialAttack);
//			}
//		}
	}
	
		// Checks collision between enemy missiles and the player shapeship
		private void checkEnemyMissileCollision() {
			if (animationManager == null || friendlyManager == null) {
				animationManager = AnimationManager.getInstance();
				friendlyManager = PlayerManager.getInstance();
			}
			for (Missile missile : enemyMissiles) {
				if (missile.isVisible()) {
					Rectangle r1 = missile.getAnimation().getBounds();
					Rectangle r2 = friendlyManager.getSpaceship().getBounds();

					if (r1.intersects(r2)) {
						friendlyManager.getSpaceship().takeHitpointDamage(missile.getMissileDamage());
						animationManager.addUpperAnimation(missile.getExplosionAnimation());
						missile.setVisible(false);
//						animationManager.addPlayerShieldDamageAnimation();
					}
				}
			}
		}

	private void checkSpecialAttackEnemyCollision(SpecialAttack specialAttack) {
		if (specialAttack.getSpecialAttackMissiles() != null) {
			for (int i = 0; i < specialAttack.getSpecialAttackMissiles().size(); i++) {
				for (Enemy enemy : enemyManager.getEnemies()) {
					if (specialAttack.getSpecialAttackMissiles().get(i).getBounds().intersects(enemy.getBounds())) {
						enemy.takeDamage(specialAttack.getDamage());
					}
				}
			}

		} else {
			for (Enemy enemy : enemyManager.getEnemies()) {
				if (specialAttack.getAnimation().getBounds().intersects(enemy.getBounds())) {
					enemy.takeDamage(specialAttack.getDamage());
				}
			}
		}
	}

	// Checks if the special attack or it's missiles collides with enemy missiles
	private void checkSpecialAttackMissileCollision(SpecialAttack specialAttack) {
//		if (specialAttack.getSpecialAttackMissiles() != null) {
//			for (Missile friendlyMissile : specialAttack.getSpecialAttackMissiles()) {
//				if (friendlyMissile.isVisible()) {
//					for (Missile enemyMissile : enemyMissiles) {
//						if (friendlyMissile.getAnimation().getBounds().intersects(enemyMissile.getAnimation().getBounds())) {
//							handleMissileCollision(enemyMissile);
//						}
//					}
//				}
//			}
//		} else {
//			for (Missile enemyMissile : enemyMissiles) {
//				if (enemyMissile.isVisible() && specialAttack.getAnimation().getBounds().intersects(enemyMissile.getBounds())) {
//					handleMissileCollision(enemyMissile);
//				}
//			}
//		}
	}


	private void moveMissiles() {
		// Move friendly missiles
		for (int i = 0; i < friendlyMissiles.size(); i++) {
			if (friendlyMissiles.get(i).isVisible()) {
				movementManager.moveMissile(friendlyMissiles.get(i));
			} else {
				removeFriendlyMissile(friendlyMissiles.get(i));
			}
		}

		// Move enemy missiles
		for (int i = 0; i < enemyMissiles.size(); i++) {

			if (enemyMissiles.get(i).isVisible()) {
				movementManager.moveMissile(enemyMissiles.get(i));
			} else {
				removeEnemyMissile(enemyMissiles.get(i));
			}
		}
	}

	private void triggerMissileAction() {
		for (AlienLaserbeam missile : alienLaserbeams) {
			missile.missileAction();
		}
		for (FlamerProjectile missile : flamerProjectiles) {
			missile.missileAction();
		}
		for (BombaProjectile missile : bombaProjectiles) {
			missile.missileAction();
		}
		for (EnergizerProjectile missile : energizerProjectiles) {
			missile.missileAction();
		}
		for (SeekerProjectile missile : seekerProjectiles) {
			missile.missileAction();
		}
		for (TazerProjectile missile : tazerProjectiles) {
			missile.missileAction();
		}

		for (Missile missile : friendlyMissiles) {
			missile.missileAction();
		}

	}

	private void removeFriendlyMissile(Missile missile) {
		switch (missile.getMissileType()) {
		case Player_Laserbeam:
			this.friendlyMissiles.remove(missile);
			break;
		case Flamethrower_Animation:
			this.friendlyMissiles.remove(missile);
		}
	}

	private void removeEnemyMissile(Missile missile) {
		switch (missile.getMissileType()) {
		case Alien_Laserbeam:
			this.alienLaserbeams.remove(missile);
			break;
		case Seeker_Missile:
			this.seekerProjectiles.remove(missile);
			break;
		case Flamer_Missile:
			this.flamerProjectiles.remove(missile);
			break;
		case Tazer_Missile:
			this.tazerProjectiles.remove(missile);
			break;
		case Bulldozer_Missile:
			this.bulldozerProjectiles.remove(missile);
			break;
		case Bomba_Missile:
			this.bombaProjectiles.remove(missile);
			break;
		case Energizer_Missile:
			this.energizerProjectiles.remove(missile);
			break;
		}
		this.enemyMissiles.remove(missile);
	}

	public Missile createMissile(ImageEnums missileType, int xCoordinate, int yCoordinate, Point destination,
			ImageEnums explosionType, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed,
			int yMovementSpeed) {
		switch (missileType) {
		case Alien_Laserbeam:
			return new AlienLaserbeam(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed);
		case Seeker_Missile:
			return new SeekerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed);
		case Flamer_Missile:
			return new FlamerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed);
		case Tazer_Missile:
			return new TazerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed);
		case Bulldozer_Missile:
			return new BulldozerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed);
		case Bomba_Missile:
			return new BombaProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed);
		case Energizer_Missile:
			return new EnergizerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder, xMovementSpeed, yMovementSpeed);
		case Player_Laserbeam:
			return new DefaultPlayerLaserbeam(xCoordinate, yCoordinate, destination, missileType, explosionType,
					rotation, scale, pathFinder, PlayerStats.getInstance().getNormalAttackDamage(), xMovementSpeed,
					yMovementSpeed);
		case Flamethrower_Animation:
			return new FlamethrowerProjectile(xCoordinate, yCoordinate, destination, ImageEnums.Flamethrower_Animation,
					explosionType, rotation, scale, pathFinder, PlayerStats.getInstance().getNormalAttackDamage(),
					xMovementSpeed, yMovementSpeed);
		default:
			throw new IllegalArgumentException("Invalid missile type: " + missileType);
		}
	}

	public void addSpecialAttack(SpecialAttack specialAttack) {
		if (!this.specialAttacks.contains(specialAttack)) {
			specialAttacks.add(specialAttack);
		}
	}

}