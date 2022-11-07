package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import game.objects.Enemy;
import game.objects.Missile;
import image.objects.Animation;

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
		Missile newMissile = new Missile(xCoordinate, yCoordinate, missileType);
		this.friendlyMissiles.add(newMissile);
	}

	public void addEnemyMissile(int xCoordinate, int yCoordinate, String missileType) {
		Missile enemyMissile = new Missile(xCoordinate, yCoordinate, missileType);
		this.enemyMissiles.add(enemyMissile);
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
								"Impact Explosion One");
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
				Rectangle r1 = m.getBounds();
				Rectangle r2 = friendlyManager.getSpaceship().getBounds();
				if (r1.intersects(r2)) {
					friendlyManager.getSpaceship().takeHitpointDamage(m.getMissileDamage());
					animationManager.addUpperAnimation(m.getXCoordinate(), m.getYCoordinate(), "Impact Explosion One");
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
