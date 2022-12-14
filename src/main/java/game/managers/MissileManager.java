package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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
	private List<Missile> enemyMissiles = new ArrayList<Missile>();
	private List<Missile> friendlyMissiles = new ArrayList<Missile>();

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

	public void firePlayerMissile(int xCoordinate, int yCoordinate, String missileType) {
		switch (missileType) {
		case ("Player Laserbeam"):
			Missile newMissile = new DefaultPlayerLaserbeam(xCoordinate, yCoordinate, 0);
			this.friendlyMissiles.add(newMissile);
		}

	}

	//Called by all enemy classes when fireAction() is called.
	public void addEnemyMissile(int xCoordinate, int yCoordinate, String missileType, int angleModuloDivider, String missileDirection) {
		switch (missileType) {
		case ("Alien Laserbeam"):
			Missile alienMissile = new AlienLaserbeam(xCoordinate, yCoordinate, missileType, angleModuloDivider);
			this.enemyMissiles.add(alienMissile);
			break;
		case ("Seeker Projectile"):
			Missile seekerMissile = new SeekerProjectile(xCoordinate, yCoordinate, missileType, angleModuloDivider);
			this.enemyMissiles.add(seekerMissile);
			break;
		case ("Flamer Projectile"):
			Missile flamerMissile = new FlamerProjectile(xCoordinate, yCoordinate, missileType, angleModuloDivider);
			this.enemyMissiles.add(flamerMissile);
			break;
		case ("Tazer Projectile"):
			Missile tazerMissile = new TazerProjectile(xCoordinate, yCoordinate, missileType, angleModuloDivider);
			this.enemyMissiles.add(tazerMissile);
			break;
		case ("Bulldozer Projectile"):
			Missile bulldozerMissile = new BulldozerProjectile(xCoordinate, yCoordinate, missileType, angleModuloDivider);
			this.enemyMissiles.add(bulldozerMissile);
			break;
		case ("Bomba Projectile"):
			Missile bombaMissile = new BombaProjectile(xCoordinate, yCoordinate, missileType, angleModuloDivider, missileDirection);
			this.enemyMissiles.add(bombaMissile);
			break;
		case ("Energizer Projectile"):
			Missile energizerMissile = new EnergizerProjectile(xCoordinate, yCoordinate, missileType, angleModuloDivider);
			this.enemyMissiles.add(energizerMissile);
			break;
		}

	}

	public void updateGameTick() {
		moveMissiles();
		checkCollisions();
	}

	private void checkCollisions() {
		checkFriendlyMissileCollision();
		checkEnemyMissileCollision();
	}

	// Checks collision between friendly missiles and enemies
	private void checkFriendlyMissileCollision() {
		for (Missile m : friendlyMissiles) {
			if (m.isVisible()) {
				Rectangle r1 = m.getBounds();
				for (Enemy enemy : enemyManager.getEnemies()) {
					Rectangle r2 = enemy.getBounds();
					if (r1.intersects(r2)) {
						enemy.takeDamage(m.getMissileDamage());
						animationManager.addUpperAnimation(m.getXCoordinate(), m.getYCoordinate(),
								"Impact Explosion One", false);
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
					animationManager.addUpperAnimation(m.getXCoordinate(), m.getYCoordinate(), "Impact Explosion One",
							false);
					m.setVisible(false);
				}
			}
		}
	}

	private void moveMissiles() {
		// Move friendly missiles
		for (int i = 0; i < friendlyMissiles.size(); i++) {
			Missile missile = friendlyMissiles.get(i);

			if (missile.isVisible()) {
				missile.updateGameTick();
			} else {
				friendlyMissiles.remove(i);
			}
		}

		// Move enemy missiles
		for (int i = 0; i < enemyMissiles.size(); i++) {
			Missile missile = enemyMissiles.get(i);

			if (missile.isVisible()) {
				missile.updateGameTick();
			} else {
				enemyMissiles.remove(i);
			}
		}
	}

}
