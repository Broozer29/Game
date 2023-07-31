package game.objects.friendlies;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import game.managers.AnimationManager;
import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.MovementInitiator;
import game.movement.OrbitPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import gamedata.DataClass;
import gamedata.GameStateInfo;
import gamedata.GameStatusEnums;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class FriendlyManager {

	private static FriendlyManager instance = new FriendlyManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private MovementInitiator movementManager = MovementInitiator.getInstance();
	private List<FriendlyObject> activeFriendlyObjects = new ArrayList<FriendlyObject>();
	private List<GuardianDrone> guardianDrones = new ArrayList<GuardianDrone>();
	private Portal finishedLevelPortal;
	
	

	private FriendlyManager() {
		int portalXCoordinate = (int) Math.floor(DataClass.getInstance().getWindowWidth() * 0.55);
		int portalYCoordinate = (DataClass.getInstance().getWindowHeight() / 2);
		
		finishedLevelPortal = new Portal(portalXCoordinate, portalYCoordinate, ImageEnums.Portal5, true, 1);
		finishedLevelPortal.setCenterCoordinates(portalXCoordinate, portalYCoordinate);
	}

	public static FriendlyManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		cycleActiveFriendlyObjects();
		checkFriendlyObjectCollision();
		moveFriendlyObjects();
		spawnFinishedLevelPortal();
	}
	
	private void spawnFinishedLevelPortal() {
		if(GameStateInfo.getInstance().getGameState() == GameStatusEnums.Song_Finished && finishedLevelPortal.getSpawned() == false) {
			finishedLevelPortal.setSpawned(true);
			finishedLevelPortal.setVisible(true);
			AnimationManager.getInstance().addUpperAnimation(finishedLevelPortal);
		}
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
		
		if(GameStateInfo.getInstance().getGameState() == GameStatusEnums.Song_Finished) {
			if(finishedLevelPortal.isVisible()) {
				if(isNearby(PlayerManager.getInstance().getSpaceship(), finishedLevelPortal)) {
					if(PlayerManager.getInstance().getSpaceship().getBounds().intersects(finishedLevelPortal.getAnimationBounds())) {
						GameStateInfo.getInstance().setGameState(GameStatusEnums.Level_Completed);
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

	// Called by a power up only so far
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