package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.OrbitPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.enemies.Enemy;
import game.objects.friendlies.friendlyobjects.FriendlyEnums;
import game.objects.friendlies.friendlyobjects.FriendlyObject;
import game.objects.friendlies.friendlyobjects.GuardianDrone;
import visual.objects.SpriteAnimation;

public class FriendlyObjectManager {

	private static FriendlyObjectManager instance = new FriendlyObjectManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private List<FriendlyObject> activeFriendlyObjects = new ArrayList<FriendlyObject>();
	private List<GuardianDrone> guardianDrones = new ArrayList<GuardianDrone>();

	private FriendlyObjectManager() {
		
		int xCoordinate = FriendlyManager.getInstance().getSpaceship().getCenterXCoordinate();
		int yCoordinate = FriendlyManager.getInstance().getSpaceship().getCenterYCoordinate();
		float scale = (float) 0.5;
		FriendlyEnums friendlyType = FriendlyEnums.Homing_Missile_Guardian_Bot;
		Point destination = null;
		Direction rotation = Direction.RIGHT;
		PathFinder pathFinder = new OrbitPathFinder(FriendlyManager.getInstance().getSpaceship(), 100, 300);
		
		FriendlyObject friendlyObject = new GuardianDrone(xCoordinate, yCoordinate, destination, rotation, FriendlyEnums.Homing_Missile_Guardian_Bot, scale, pathFinder, 50);
		guardianDrones.add((GuardianDrone) friendlyObject);
		SpriteAnimation animation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Guardian_Bot, true, scale);
		
		friendlyObject.setAnimation(animation);;
		addActiveFriendlyObject(friendlyObject);
	}

	public static FriendlyObjectManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		cycleActiveFriendlyObjects();
		checkFriendlyObjectCollision();
		moveFriendlyObjects();
	}

	private void cycleActiveFriendlyObjects() {
		for (int i = 0; i < activeFriendlyObjects.size(); i++) {
			FriendlyObject friendly = activeFriendlyObjects.get(i);
			if (!friendly.getAnimation().isVisible()) {
				activeFriendlyObjects.remove(i);
				friendly.setVisible(false);
			}
		}
		
		for(GuardianDrone drone : guardianDrones) {
			drone.activateGuardianDrone();
		}
	}

	private void moveFriendlyObjects() {
		for (int i = 0; i < activeFriendlyObjects.size(); i++) {
			FriendlyObject friendly = activeFriendlyObjects.get(i);
			if (friendly.getAnimation().isVisible()) {
				friendly.move();
			}
		}
	}

	private void checkFriendlyObjectCollision() {
		if (enemyManager == null) {
			enemyManager = EnemyManager.getInstance();
		}

		for (FriendlyObject friendlyObject : activeFriendlyObjects) {
			if (friendlyObject.isVisible()) {
				SpriteAnimation animation = friendlyObject.getAnimation();
				for (Enemy enemy : enemyManager.getEnemies()) {
					Rectangle r1 = animation.getBounds();
					Rectangle r2 = enemy.getBounds();
					if (r1.intersects(r2)) {
					}
				}
			}
		}
	}

	public void createGuardianDrone() {

	}

	public void addActiveFriendlyObject(FriendlyObject friendlyObject) {
		this.activeFriendlyObjects.add(friendlyObject);
	}

	public List<FriendlyObject> getActiveFriendlyObjects() {
		return this.activeFriendlyObjects;
	}

}
