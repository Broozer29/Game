package game.objects.missiles;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.PlayerStats;
import data.image.ImageEnums;
import game.managers.AnimationManager;
import game.managers.MovementInitiator;
import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.friendlies.spaceship.PlayerAttackTypes;
import game.objects.friendlies.spaceship.specialAttacks.SpecialAttack;
import visual.objects.Sprite;

public class MissileManager {

	private static MissileManager instance = new MissileManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private PlayerManager friendlyManager = PlayerManager.getInstance();
	private MovementInitiator movementManager = MovementInitiator.getInstance();
	private List<Missile> enemyMissiles = new ArrayList<Missile>();
	private List<Missile> friendlyMissiles = new ArrayList<Missile>();
	private List<SpecialAttack> specialAttacks = new ArrayList<SpecialAttack>();

	private int threshold = 150;
	private int boardBlockThreshold = 2;

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
							handleCollision(enemy, missile);
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

		enemy.takeDamage(friendlyMissile.getMissileDamage());
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
						enemy.takeDamage(specialAttack.getDamage());
					}
				}
			}
		}

		for (Enemy enemy : enemyManager.getEnemies()) {
			if (isNearby(specialAttack.getAnimation(), enemy)) {
				if (specialAttack.getAnimation().getAnimationBounds().intersects(enemy.getBounds())) {
					enemy.takeDamage(specialAttack.getDamage());

				}
			}
		}
	}

	// Checks collision between special attacks and enemy missiles
	private void checkSpecialAttackWithEnemyMissileCollision(SpecialAttack specialAttack) {
		if (specialAttack.getSpecialAttackMissiles() == null) {
			for (Missile enemyMissile : enemyMissiles) {
				if (isNearby(specialAttack.getAnimation(), enemyMissile))
					if (enemyMissile.isVisible()
							&& specialAttack.getAnimation().getAnimationBounds().intersects(enemyMissile.getBounds())) {
						setMissileVisibility(enemyMissile);
					}
			}
		}
	}

	private boolean isWithinBoardBlockThreshold(Sprite sprite1, Sprite sprite2) {
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

	// Both friendly & enemy missiles
	private void setMissileVisibility(Missile missile) {
		switch (missile.getMissileType()) {
		case Flamethrower_Animation:
		case FirewallParticle:
			break;
		default:
			missile.setVisible(false);
			break;
		}
	}

	// Both friendly & enemy missiles
	// Not necesarry if the special attack missiles are within regular friendly/enemy
	// missiles?
	private void setSpecialAttackMissileVisbility(Missile missile) {
		switch (missile.getMissileType()) {
		case FirewallParticle:
			break;
		default:
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
	}

	private void triggerMissileAction() {

		for (Missile missile : friendlyMissiles) {
			missile.missileAction();
		}

		for (Missile missile : enemyMissiles) {
			missile.missileAction();
		}

	}

	private void removeFriendlyMissile(Missile missile) {
		this.friendlyMissiles.remove(missile);
	}

	private void removeEnemyMissile(Missile missile) {
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