package game.objects.missiles;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import game.managers.AnimationManager;
import game.managers.PlayerManager;
import game.movement.MovementInitiator;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class MissileManager {

	private static MissileManager instance = new MissileManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private PlayerManager friendlyManager = PlayerManager.getInstance();
	private MovementInitiator movementManager = MovementInitiator.getInstance();
	private List<Missile> enemyMissiles = new ArrayList<Missile>();
	private List<Missile> friendlyMissiles = new ArrayList<Missile>();
	private List<SpecialAttack> specialAttacks = new ArrayList<SpecialAttack>();

	private int threshold = 600;
	private int boardBlockThreshold = 4;

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
			this.friendlyMissiles.add(missile);
		} else {
			this.enemyMissiles.add(missile);
		}
	}

	public void updateGameTick() {
		if (animationManager == null || friendlyManager == null || enemyManager == null) {
			animationManager = AnimationManager.getInstance();
			friendlyManager = PlayerManager.getInstance();
			enemyManager = EnemyManager.getInstance();
		}

		moveMissiles();
		checkFriendlyMissileWithEnemyCollision();
		checkEnemyMissileWithPlayerCollision();
		triggerMissileAction();
	}

	private void checkFriendlyMissileWithEnemyCollision() {
		if (enemyManager == null) {
			enemyManager = EnemyManager.getInstance();
		}
		for (Missile missile : friendlyMissiles) {
			if (missile.isVisible()) {
				Rectangle r1 = getMissileBounds(missile);
				for (Enemy enemy : enemyManager.getEnemies()) {
					if (isNearby(missile, enemy)) {
						Rectangle r2 = enemy.getBounds();
						if (r1.intersects(r2)) {
							if (checkPixelCollision(enemy, missile)) {
								handleCollision(enemy, missile);
							}
						}
					}
				}
			}
		}

		for (SpecialAttack specialAttack : specialAttacks) {
			if (specialAttack.getAnimation().isVisible()) {
				checkSpecialAttackWithEnemyCollision(specialAttack);
				checkSpecialAttackWithEnemyMissileCollision(specialAttack);
			}
		}
	}

	private Rectangle getMissileBounds(Missile missile) {
		return missile.getAnimation() != null ? missile.getAnimation().getAnimationBounds() : missile.getBounds();
	}

	// Handles collision for enemies and friendlymissiles (
	private void handleCollision(Enemy enemy, Missile friendlyMissile) {

		if (friendlyMissile.getMissileType() == ImageEnums.Rocket_1) {
			friendlyMissile.missileAction();
		} else {
			enemy.takeDamage(friendlyMissile.getMissileDamage());
			setMissileVisibility(friendlyMissile);
		}

		if (friendlyMissile.getExplosionAnimation() != null) {
			animationManager.addUpperAnimation(friendlyMissile.getExplosionAnimation());
		}
		if (friendlyMissile.getAnimation() != null) {
			friendlyMissile.getAnimation().setVisible(false);
		}
		setMissileVisibility(friendlyMissile);
	}

	// Checks enemy missiles with the player
	private void checkEnemyMissileWithPlayerCollision() {
		for (Missile missile : enemyMissiles) {
			if (missile.isVisible()) {
				if (isNearby(missile, friendlyManager.getSpaceship())) {
					Rectangle r1 = getMissileBounds(missile);
					if (r1.intersects(friendlyManager.getSpaceship().getBounds())) {
						friendlyManager.getSpaceship().takeHitpointDamage(missile.getMissileDamage());
						animationManager.addUpperAnimation(missile.getExplosionAnimation());
						setMissileVisibility(missile);
					}
				}
			}
		}
	}

	// Checks collision between special attacks and enemies
	private void checkSpecialAttackWithEnemyCollision(SpecialAttack specialAttack) {
		for (Missile missile : specialAttack.getSpecialAttackMissiles()) {
			for (Enemy enemy : enemyManager.getEnemies()) {
				if (isNearby(missile, enemy)) {
					Rectangle r1 = getMissileBounds(missile);
					if (r1.intersects(enemy.getBounds())) {
						if (checkPixelCollision(missile, enemy)) {
							enemy.takeDamage(specialAttack.getDamage());
						}
					}
				}
			}
		}

		for (Enemy enemy : enemyManager.getEnemies()) {
			if (isNearby(specialAttack.getAnimation(), enemy)) {
				if (specialAttack.getAnimation().getAnimationBounds().intersects(enemy.getBounds())) {
					if (checkPixelCollision(enemy, specialAttack.getAnimation())) {
						enemy.takeDamage(specialAttack.getDamage());
					}
				}
			}
		}
	}

	// Checks collision between special attacks and enemy missiles
	private void checkSpecialAttackWithEnemyMissileCollision(SpecialAttack specialAttack) {
		if (specialAttack.getSpecialAttackMissiles().size() == 0) {
			for (Missile enemyMissile : enemyMissiles) {
				if (isNearby(specialAttack.getAnimation(), enemyMissile))
					if (enemyMissile.isVisible() && specialAttack.getAnimation().getAnimationBounds()
							.intersects(enemyMissile.getAnimation().getAnimationBounds())) {
						if (checkPixelCollision(enemyMissile.getAnimation(), specialAttack.getAnimation())) {
							setMissileVisibility(enemyMissile);
						}

					}
			}
		}
	}

	private boolean isWithinBoardBlockThreshold(Sprite sprite1, Sprite sprite2) {
		// This causes all other "updatecurrentBoardBlocks" to be redundant
		sprite1.updateCurrentBoardBlock();
		sprite2.updateCurrentBoardBlock();

		int blockDifference = Math.abs(sprite1.getCurrentBoardBlock() - sprite2.getCurrentBoardBlock());
		return blockDifference <= boardBlockThreshold;
	}

	private boolean isNearby(Sprite sprite1, Sprite sprite2) {
		if (!isWithinBoardBlockThreshold(sprite1, sprite2)) {
			return false;
		}

		double distance = Math.hypot(sprite1.getXCoordinate() - sprite2.getXCoordinate(),
				sprite1.getYCoordinate() - sprite2.getYCoordinate());
		return distance < threshold;
	}

	private boolean checkPixelCollision(Sprite sprite1, Sprite sprite2) {
		BufferedImage img1 = null;
		BufferedImage img2 = null;
		if (sprite1 instanceof SpriteAnimation) {
			img1 = ((SpriteAnimation) sprite1).getCurrentFrameImage(false);
		} else {
			img1 = sprite1.getImage();
		}

		if (sprite2 instanceof SpriteAnimation) {
			img2 = ((SpriteAnimation) sprite2).getCurrentFrameImage(false);
		} else {
			img2 = sprite2.getImage();
		}

		if (img1 != null || img2 != null) {
			int xStart = Math.max(sprite1.getXCoordinate(), sprite2.getXCoordinate());
			int yStart = Math.max(sprite1.getYCoordinate(), sprite2.getYCoordinate());
			int xEnd = Math.min(sprite1.getXCoordinate() + img1.getWidth(), sprite2.getXCoordinate() + img2.getWidth());
			int yEnd = Math.min(sprite1.getYCoordinate() + img1.getHeight(),
					sprite2.getYCoordinate() + img2.getHeight());

			for (int y = yStart; y < yEnd; y++) {
				for (int x = xStart; x < xEnd; x++) {
					int pixel1 = img1.getRGB(x - sprite1.getXCoordinate(), y - sprite1.getYCoordinate());
					int alpha1 = (pixel1 >> 24) & 0xff;

					int pixel2 = img2.getRGB(x - sprite2.getXCoordinate(), y - sprite2.getYCoordinate());
					int alpha2 = (pixel2 >> 24) & 0xff;

					if (alpha1 != 0 && alpha2 != 0) {
						return true; // Collision detected
					}
				}
			}
			return false; // No collision detected
		} else {
			return true; //Invisible images, cannot detect pixels because there are none
		}
	}

	// Both friendly & enemy missiles
	private void setMissileVisibility(Missile missile) {
		switch (missile.getMissileType()) {
		case Flamethrower_Animation:
		case FirewallParticle:
		case Firespout_Animation:
			break;
		default:
			// Create method that destroys the missile and plays an explosion animation of the
			// missile removing
			missile.setVisible(false);
			break;
		}
	}

	private void moveMissiles() {
		// Move friendly missiles
		for (int i = friendlyMissiles.size() - 1; i >= 0; i--) {
			if (friendlyMissiles.get(i).isVisible()) {
				movementManager.moveMissile(friendlyMissiles.get(i));
			} else {
				removeFriendlyMissile(friendlyMissiles.get(i));

			}
		}

		// Move enemy missiles
		for (int i = enemyMissiles.size() - 1; i >= 0; i--) {
			if (enemyMissiles.get(i).isVisible()) {
				movementManager.moveMissile(enemyMissiles.get(i));
			} else {
				removeEnemyMissile(enemyMissiles.get(i));
			}
		}

		// Handle special attacks
		for (int i = specialAttacks.size() - 1; i >= 0; i--) {
			SpecialAttack specialAttack = specialAttacks.get(i);

			boolean allMissilesInvisible = true;
			List<Missile> specialAttackMissiles = specialAttack.getSpecialAttackMissiles();
			for (Missile missile : specialAttackMissiles) {
				if (missile.isVisible()) {
					allMissilesInvisible = false;
					break; // No need to check the rest
				}
			}

			// It is vital that special attacks which exist of missiles only have an invisible
			// animation
			if (specialAttack.getAnimation() != null
					&& specialAttack.getAnimation().getImageType() != ImageEnums.Invisible_Animation) {
				allMissilesInvisible = false;
			}

			if (allMissilesInvisible) {
				specialAttack.setVisible(false);
				specialAttack.getAnimation().setVisible(false);
			} else if (!specialAttack.getAnimation().isVisible()) {
				removeSpecialAttack(specialAttack);
			}
		}
	}

	private void triggerMissileAction() {
		for (Missile missile : friendlyMissiles) {
			if (missile.getMissileType() != ImageEnums.Rocket_1) {
				missile.missileAction();
			}
		}

		for (Missile missile : enemyMissiles) {
			if (missile.getMissileType() != ImageEnums.Rocket_1) {
				missile.missileAction();
			}
		}

	}

	private void removeSpecialAttack(SpecialAttack specialAttack) {
		this.specialAttacks.remove(specialAttack);
	}

	private void removeFriendlyMissile(Missile missile) {
		this.friendlyMissiles.remove(missile);
	}

	private void removeEnemyMissile(Missile missile) {
		this.enemyMissiles.remove(missile);
	}

	public void addSpecialAttack(SpecialAttack specialAttack) {
		if (!this.specialAttacks.contains(specialAttack)) {
			specialAttacks.add(specialAttack);
		}
	}

}