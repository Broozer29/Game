package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.image.ImageEnums;
import game.objects.Explosion;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class ExplosionManager {

	private static ExplosionManager instance = new ExplosionManager();
	private PlayerManager friendlyManager = PlayerManager.getInstance();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private List<Explosion> explosionList = new ArrayList<Explosion>();

	private ExplosionManager() {

	}

	public void updateGametick() {
		removeFinishedExplosions();
		checkExplosionPlayerCollision();
		checkExplosionEnemyCollision();
	}

	public static ExplosionManager getInstance() {
		return instance;
	}

	public void createAndAddExplosion(int xCoordinate, int yCoordinate, ImageEnums explosionType, float scale,
			float damage, boolean friendly) {
		SpriteAnimation animation = new SpriteAnimation(xCoordinate, yCoordinate, explosionType, false, scale);
		animation.setX(xCoordinate - animation.getWidth() / 2);
		animation.setY(yCoordinate - animation.getHeight() / 2);
		Explosion explosion = new Explosion(xCoordinate, yCoordinate, scale, animation, damage, friendly);
		explosion.setX(xCoordinate - animation.getWidth() / 2);
		explosion.setY(yCoordinate - animation.getHeight() / 2);
		addExistingExplosion(explosion);
	}

	public void addExistingExplosion(Explosion explosion) {
		if (!this.explosionList.contains(explosion)) {
			this.explosionList.add(explosion);
		}
	}

	public List<Explosion> getExplosions() {
		return this.explosionList;
	}

	private void removeFinishedExplosions() {
		for (int i = 0; i < explosionList.size(); i++) {
			if (!explosionList.get(i).getAnimation().isVisible()) {
				explosionList.get(i).setVisible(false);
				explosionList.get(i).getAnimation().setVisible(false);
				explosionList.remove(i);
			}
		}
	}

// used for explosions of the enemy
	// Broken intentionally, rework this manually
	private void checkExplosionPlayerCollision() {
		if (friendlyManager == null) {
			friendlyManager = PlayerManager.getInstance();
		}
		for (Explosion explosion : explosionList) {
			if (explosion.isVisible() && !explosion.isFriendly()) {
				if (isNearby(explosion, friendlyManager.getSpaceship())) {
					if (explosion.getAnimation().getAnimationBounds()
							.intersects(friendlyManager.getSpaceship().getBounds())) {
						if (!explosion.getDealtDamage()) {
							friendlyManager.getSpaceship().takeHitpointDamage(explosion.getDamage());
							explosion.setDealtDamage(true);
						}
					}
				}
			}
		}
	}

	// used for explosions of the player
	private void checkExplosionEnemyCollision() {
		if (enemyManager == null) {
			enemyManager = EnemyManager.getInstance();
		}
		for (Explosion explosion : explosionList) {
			if (explosion.isVisible() && explosion.isFriendly()) {
				for (Enemy enemy : enemyManager.getEnemies()) {
					if (isNearby(explosion, enemy)) {
						if (explosion.getAnimation().getBounds().intersects(enemy.getBounds())) {
							enemy.takeDamage(explosion.getDamage());
						}
					}
				}

			}
		}
	}

	private boolean isWithinBoardBlockThreshold(Sprite sprite1, Sprite sprite2) {
		int blockDifference = Math.abs(sprite1.getCurrentBoardBlock() - sprite2.getCurrentBoardBlock());
		return blockDifference <= 2;
	}

	private boolean isNearby(Sprite sprite1, Sprite sprite2) {
		if (!isWithinBoardBlockThreshold(sprite1, sprite2)) {
			return false;
		}

		double distance = Math.hypot(sprite1.getXCoordinate() - sprite2.getXCoordinate(),
				sprite1.getYCoordinate() - sprite2.getYCoordinate());
		return distance < 150;
	}

	public void resetManager() {
		this.explosionList = new ArrayList<Explosion>();
	}

}
