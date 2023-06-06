package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.enemies.Enemy;
import game.objects.missiles.AlienLaserbeam;
import game.objects.missiles.BombaProjectile;
import game.objects.missiles.BulldozerProjectile;
import game.objects.missiles.DefaultPlayerLaserbeam;
import game.objects.missiles.EnergizerProjectile;
import game.objects.missiles.FlamerProjectile;
import game.objects.missiles.Missile;
import game.objects.missiles.SeekerProjectile;
import game.objects.missiles.TazerProjectile;

public class MissileManager {

	private static MissileManager instance = new MissileManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private MovementManager movementManager = MovementManager.getInstance();
	private List<Missile> enemyMissiles = new ArrayList<Missile>();
	private List<Missile> friendlyMissiles = new ArrayList<Missile>();
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

	public void firePlayerMissile(int xCoordinate, int yCoordinate, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder) {
		Point start = new Point(xCoordinate, yCoordinate);
		Point destination = pathFinder.calculateInitialEndpoint(start, rotation);
		Missile missile = createMissile(missileType, xCoordinate, yCoordinate, destination, explosionType, rotation,
				scale, pathFinder);
		this.friendlyMissiles.add(missile);

	}

	// Called by all enemy classes when fireAction() is called.
	public void addEnemyMissile(int xCoordinate, int yCoordinate, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder) {

		Point start = new Point(xCoordinate, yCoordinate);
		Point destination = pathFinder.calculateInitialEndpoint(start, rotation);

		Missile missile = createMissile(missileType, xCoordinate, yCoordinate, destination, explosionType, rotation,
				scale, pathFinder);

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
		}

	}

	public void updateGameTick() {
		moveMissiles();
		checkFriendlyMissileCollision();
		checkEnemyMissileCollision();
		triggerMissileAction();
	}

	// Checks collision between friendly missiles and enemies
	private void checkFriendlyMissileCollision() {
		if (enemyManager == null) {
			enemyManager = EnemyManager.getInstance();
		}
		for (Missile m : friendlyMissiles) {
			if (m.isVisible()) {
				Rectangle r1 = m.getBounds();
				for (Enemy enemy : enemyManager.getEnemies()) {
					Rectangle r2 = enemy.getBounds();
					if (r1.intersects(r2)) {
						enemy.takeDamage(m.getMissileDamage());
						if (m.getExplosionAnimation() != null) {
							animationManager.addUpperAnimation(m.getExplosionAnimation());
						}
						m.setVisible(false);
					}
				}
			}
		}
	}

	// Checks collision between enemy missiles and the player shapeship
	private void checkEnemyMissileCollision() {
		if (animationManager == null || friendlyManager == null) {
			animationManager = AnimationManager.getInstance();
			friendlyManager = FriendlyManager.getInstance();
		}
		for (Missile m : enemyMissiles) {
			if (m.isVisible()) {
				Rectangle r1 = m.getAnimation().getBounds();
				Rectangle r2 = friendlyManager.getSpaceship().getBounds();

				if (r1.intersects(r2)) {
					friendlyManager.getSpaceship().takeHitpointDamage(m.getMissileDamage());
					animationManager.addUpperAnimation(m.getExplosionAnimation());
					m.setVisible(false);
//					animationManager.addPlayerShieldDamageAnimation();
				}
			}
		}
	}

	private void moveMissiles() {
		// Move friendly missiles
		for (int i = 0; i < friendlyMissiles.size(); i++) {
			Missile missile = friendlyMissiles.get(i);
			if (missile.isVisible()) {
				movementManager.moveMissile(missile);
			} else {
				removeFriendlyMissile(missile);
			}
		}

		// Move enemy missiles
		for (int i = 0; i < enemyMissiles.size(); i++) {
			Missile missile = enemyMissiles.get(i);

			if (missile.isVisible()) {
				movementManager.moveMissile(missile);
			} else {
				removeEnemyMissile(missile);
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
	}

	private void removeFriendlyMissile(Missile missile) {
		switch (missile.getMissileType()) {
		case Player_Laserbeam:
			this.friendlyMissiles.remove(missile);
			break;
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
			ImageEnums explosionType, Direction rotation, float scale, PathFinder pathFinder) {
		switch (missileType) {
		case Alien_Laserbeam:
			return new AlienLaserbeam(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder);
		case Seeker_Missile:
			return new SeekerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder);
		case Flamer_Missile:
			return new FlamerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder);
		case Tazer_Missile:
			return new TazerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder);
		case Bulldozer_Missile:
			return new BulldozerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder);
		case Bomba_Missile:
			return new BombaProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder);
		case Energizer_Missile:
			return new EnergizerProjectile(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder);
		case Player_Laserbeam:
			return new DefaultPlayerLaserbeam(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation,
					scale, pathFinder);
		default:
			throw new IllegalArgumentException("Invalid missile type: " + missileType);
		}
	}

}
