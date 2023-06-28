package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.image.ImageEnums;
import game.objects.Explosion;
import game.objects.enemies.Enemy;
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
	//Broken intentionally, rework this manually
	private void checkExplosionPlayerCollision() {
		if (friendlyManager == null) {
			friendlyManager = PlayerManager.getInstance();
		}
		for (Explosion explosion : explosionList) {
			if (explosion.isVisible() && !explosion.isFriendly()) {
				Rectangle r1 = explosion.getAnimation().getBounds();
				Rectangle r2 = friendlyManager.getSpaceship().getBounds();
				if (r1.intersects(r2)) {
					if (!explosion.getDealtDamage()) {
						friendlyManager.getSpaceship().takeHitpointDamage(explosion.getDamage());
						explosion.setDealtDamage(true);
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
				Rectangle r1 = explosion.getAnimation().getBounds();
				for (Enemy enemy : enemyManager.getEnemies()) {
					Rectangle r2 = enemy.getBounds();
					if (r1.intersects(r2)) {
						enemy.takeDamage(explosion.getDamage());
					}
				}

			}
		}
	}

	public void resetManager() {
		this.explosionList = new ArrayList<Explosion>();
	}

}
