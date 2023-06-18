package game.managers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import game.objects.enemies.Enemy;
import game.objects.friendlies.friendlyobjects.FriendlyObject;
import image.objects.SpriteAnimation;

public class FriendlyObjectManager {

	private static FriendlyObjectManager instance = new FriendlyObjectManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private List<FriendlyObject> activeFriendlyObjects = new ArrayList<FriendlyObject>();

	private FriendlyObjectManager() {

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

	public void addActiveFriendlyObject(FriendlyObject friendlyObject) {
		this.activeFriendlyObjects.add(friendlyObject);
	}

	public List<FriendlyObject> getActiveFriendlyObjects() {
		return this.activeFriendlyObjects;
	}

}
