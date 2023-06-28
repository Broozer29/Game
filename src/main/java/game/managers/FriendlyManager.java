package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.OrbitPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.enemies.Enemy;
import game.objects.friendlies.FriendlyEnums;
import game.objects.friendlies.FriendlyObject;
import game.objects.friendlies.GuardianDrone;
import visual.objects.SpriteAnimation;

public class FriendlyManager {

	private static FriendlyManager instance = new FriendlyManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private MovementInitiator movementManager = MovementInitiator.getInstance();
	private List<FriendlyObject> activeFriendlyObjects = new ArrayList<FriendlyObject>();
	private List<GuardianDrone> guardianDrones = new ArrayList<GuardianDrone>();

	private FriendlyManager() {
		

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
		
		for(GuardianDrone drone : guardianDrones) {
			drone.activateGuardianDrone();
		}
	}

	//Checks collision between friendly objects and enemies
	//Intentionally broken, fix manually
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

	//UNCALLED BUT THIS WORKS
	public void createGuardianDrone() {
		int xCoordinate = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
		int yCoordinate = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();
		float scale = (float) 0.5;
		FriendlyEnums friendlyType = FriendlyEnums.Homing_Missile_Guardian_Bot;
		Point destination = null;
		Direction rotation = Direction.RIGHT;
		PathFinder pathFinder = new OrbitPathFinder(PlayerManager.getInstance().getSpaceship(), 100, 300);
		
		FriendlyObject friendlyObject = new GuardianDrone(xCoordinate, yCoordinate, destination, rotation, friendlyType, scale, pathFinder, 50);
		guardianDrones.add((GuardianDrone) friendlyObject);
		SpriteAnimation animation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Guardian_Bot, true, scale);
		
		friendlyObject.setAnimation(animation);;
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