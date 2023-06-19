package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Destination;

import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.OrbitPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.enemies.Enemy;
import game.objects.friendlies.friendlyobjects.FriendlyEnums;
import game.objects.friendlies.friendlyobjects.FriendlyObject;
import image.objects.SpriteAnimation;

public class FriendlyObjectManager {

	private static FriendlyObjectManager instance = new FriendlyObjectManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private List<FriendlyObject> activeFriendlyObjects = new ArrayList<FriendlyObject>();

	private FriendlyObjectManager() {
		
		int xCoordinate = FriendlyManager.getInstance().getSpaceship().getCenterXCoordinate();
		int yCoordinate = FriendlyManager.getInstance().getSpaceship().getCenterYCoordinate();
		float scale = (float) 1.0;
		FriendlyEnums friendlyType = FriendlyEnums.Absorbtion_Guardian_Bot;
		Point destination = null;
		Direction rotation = Direction.RIGHT;
		PathFinder pathFinder = new OrbitPathFinder(FriendlyManager.getInstance().getSpaceship(), 30, 60);
		
		
		FriendlyObject friendlyObject = new FriendlyObject(xCoordinate, yCoordinate, destination, rotation, friendlyType, scale, pathFinder);
		SpriteAnimation animation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Default_Player_Engine, true, scale);
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
