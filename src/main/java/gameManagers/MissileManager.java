package gameManagers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import gameObjectes.Animation;
import gameObjectes.Enemy;
import gameObjectes.Missile;
import gameObjectes.SpaceShip;

public class MissileManager {

	private static MissileManager instance = new MissileManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private List<Missile> enemyMissiles = new ArrayList<Missile>();
	private List<Missile> friendlyMissiles = new ArrayList<Missile>();

	private MissileManager() {

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

	public void addFriendlyMissile(Missile friendlyMissile) {
		this.friendlyMissiles.add(friendlyMissile);
	}

	public void addEnemyMissile(Missile enemyMissile) {
		this.enemyMissiles.add(enemyMissile);
	}

	private void removeEnemyMissile(Missile enemyMissile) {
		this.friendlyMissiles.remove(enemyMissile);
	}

	private void removeFriendlyMissile(Missile friendlyMissile) {
		this.enemyMissiles.remove(friendlyMissile);
	}

	public void updateGameTick() {
		moveMissiles();
		checkCollisions();
	}

	private void checkCollisions() {
		Rectangle spaceshipBounds = friendlyManager.getSpaceship().getBounds();

		// Checks collision between spaceship and enemies
		for (Enemy enemy : enemyManager.getEnemies()) {
			Rectangle alienBounds = enemy.getBounds();
			if (spaceshipBounds.intersects(alienBounds)) {
				friendlyManager.getSpaceship().takeHitpointDamage(5);
			}
		}

		// Checks collision between missile and enemies
		for (Missile m : friendlyMissiles) {
			Rectangle r1 = m.getBounds();
			for (Enemy enemy : enemyManager.getEnemies()) {
				Rectangle r2 = enemy.getBounds();
				if (r1.intersects(r2)) {
					enemy.takeDamage(m.getMissileDamage());
					animationManager.addAnimation(
							new Animation(m.getXCoordinate(), m.getYCoordinate(), "Impact Explosion One"));
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
				missile.move();
			} else {
				friendlyMissiles.remove(i);
			}
		}

		// Move enemy missiles
		for (int i = 0; i < enemyMissiles.size(); i++) {
			Missile missile = enemyMissiles.get(i);

			if (missile.isVisible()) {
				missile.move();
			} else {
				enemyMissiles.remove(i);
			}
		}
	}

}
