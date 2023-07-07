package game.objects.friendlies;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.image.ImageEnums;
import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.MovementInitiator;
import game.movement.OrbitPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class FriendlyManager {

	private static FriendlyManager instance = new FriendlyManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private MovementInitiator movementManager = MovementInitiator.getInstance();
	private List<FriendlyObject> activeFriendlyObjects = new ArrayList<FriendlyObject>();
	private List<GuardianDrone> guardianDrones = new ArrayList<GuardianDrone>();
	
	

	private FriendlyManager() {
//		createGuardianDrone();
	}

	public static FriendlyManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		cycleActiveFriendlyObjects();
		checkFriendlyObjectCollision();
		moveFriendlyObjects();
	}

	private void moveFriendlyObjects() {
		for (int i = 0; i < activeFriendlyObjects.size(); i++) {
			if (activeFriendlyObjects.get(i).getAnimation().isVisible()) {
				movementManager.moveFriendlyObjects(activeFriendlyObjects.get(i));
			}
		}
	}

	private void cycleActiveFriendlyObjects() {
		for (int i = 0; i < activeFriendlyObjects.size(); i++) {
			if (!activeFriendlyObjects.get(i).getAnimation().isVisible()) {
				activeFriendlyObjects.remove(i);
				activeFriendlyObjects.get(i).setVisible(false);
			}
		}

		for (GuardianDrone drone : guardianDrones) {
			drone.activateGuardianDrone();
		}
	}

	// Checks collision between friendly objects and enemies
	// Intentionally broken, fix manually
	private void checkFriendlyObjectCollision() {
		if (enemyManager == null) {
			enemyManager = EnemyManager.getInstance();
		}

		for (FriendlyObject friendlyObject : activeFriendlyObjects) {
			if (friendlyObject.isVisible()) {
				SpriteAnimation animation = friendlyObject.getAnimation();
				for (Enemy enemy : enemyManager.getEnemies()) {
					if (isNearby(animation, friendlyObject)) {
						if (enemy.getBounds().intersects(animation.getAnimationBounds())) {
							System.out.println(
									"Unimplemeted collision between friendly object and enemy in FriendlyManager: 75");
						}
					}
				}
			}
		}
	}

	private boolean isWithinBoardBlockThreshold(Sprite sprite1, Sprite sprite2) {
		int blockDifference = Math.abs(sprite1.getCurrentBoardBlock() - sprite2.getCurrentBoardBlock());
		return blockDifference <= 3;
	}

	private boolean isNearby(Sprite sprite1, Sprite sprite2) {
		if (!isWithinBoardBlockThreshold(sprite1, sprite2)) {
			return false;
		}

		double distance = Math.hypot(sprite1.getXCoordinate() - sprite2.getXCoordinate(),
				sprite1.getYCoordinate() - sprite2.getYCoordinate());
		return distance < 300;
	}

	// UNCALLED BUT THIS WORKS
	public void createMissileGuardianBot(int xCoordinate, int yCoordinate, Point destination, Direction rotation,
			FriendlyEnums friendlyType, float scale, PathFinder pathFinder) {
		FriendlyObject friendlyObject = new GuardianDrone(xCoordinate, yCoordinate, destination, rotation, friendlyType,
				scale, pathFinder, 50);
		guardianDrones.add((GuardianDrone) friendlyObject);
		SpriteAnimation animation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Guardian_Bot, true, scale);
		friendlyObject.setAnimation(animation);
		addActiveFriendlyObject(friendlyObject);
	}

	public void addActiveFriendlyObject(FriendlyObject friendlyObject) {
		this.activeFriendlyObjects.add(friendlyObject);
	}

	public List<FriendlyObject> getActiveFriendlyObjects() {
		return this.activeFriendlyObjects;
	}

	public void resetManager() {
		activeFriendlyObjects = new ArrayList<FriendlyObject>();
		guardianDrones = new ArrayList<GuardianDrone>();
	}

}