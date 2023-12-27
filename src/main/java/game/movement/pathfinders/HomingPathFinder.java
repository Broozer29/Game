package game.movement.pathfinders;

import java.util.ArrayList;
import java.util.List;

import game.objects.player.PlayerManager;
import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.HomingPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.objects.GameObject;
import game.objects.enemies.EnemyManager;
import gamedata.DataClass;

public class HomingPathFinder implements PathFinder {
	@Override
	public Path findPath(PathFinderConfig pathFinderConfig) {
		if (!(pathFinderConfig instanceof HomingPathFinderConfig)) {
			throw new IllegalArgumentException("Expected HomingPathFinderConfig");
		} else {
			Point start = ((HomingPathFinderConfig) pathFinderConfig).getStart();
			Direction fallbackDirection = ((HomingPathFinderConfig) pathFinderConfig).getFallbackDirection();
			boolean isHoming = ((HomingPathFinderConfig) pathFinderConfig).isHoming();
			boolean isFriendly = ((HomingPathFinderConfig) pathFinderConfig).isFriendly();

			List<Point> waypoints = new ArrayList<>();
			waypoints.add(start);
			return new Path(waypoints, fallbackDirection, isHoming, isFriendly);
		}
	}

	@Override
	public Direction getNextStep(Point currentLocation, Path path, Direction fallbackDirection) {
		if (shouldRecalculatePath(path)) {
			return fallbackDirection;
		} else {
			Direction calculatedDirection = calculateDirection(currentLocation, path.getHomingTargetLocation());
			return calculatedDirection;
		}
	}

	// If the missile is friendly but there are no enemies, return true
	public boolean shouldRecalculatePath(Path currentPath) {
		boolean hasPassed = false;
		if (currentPath.isFriendly() && !EnemyManager.getInstance().enemiesToHomeTo()) {
			hasPassed = hasPassedTarget(currentPath);
		} else if (!currentPath.isFriendly()) {
			hasPassed = hasPassedTarget(currentPath);
		} else if (currentPath.isFriendly() && EnemyManager.getInstance().enemiesToHomeTo()) {
			hasPassed = true;
		}
		return hasPassed;
	}

	/*-Fallback direction constant updaten met zijn huidige direction werkt niet met deze manier om "passedtarget" te berekenen.*/
	private boolean hasPassedTarget(Path currentPath) {
		Point currentLocation = currentPath.getCurrentLocation();
		Point targetLocation = null;
		if (currentPath.getHomingTargetLocation() == null) {
			targetLocation = getTarget(currentPath.isFriendly()).getCurrentLocation();
		} else {
			targetLocation = currentPath.getHomingTargetLocation();
		}
		switch (currentPath.getFallbackDirection()) {
		case UP:
			return currentLocation.getY() < targetLocation.getY();
		case DOWN:
			return currentLocation.getY() > targetLocation.getY();
		case LEFT:
			return currentLocation.getX() < targetLocation.getX();
		case RIGHT:
			return currentLocation.getX() > targetLocation.getX();
		case LEFT_UP:
			return currentLocation.getX() < targetLocation.getX() || currentLocation.getY() < targetLocation.getY();
		case RIGHT_UP:
			return currentLocation.getX() > targetLocation.getX() || currentLocation.getY() < targetLocation.getY();
		case LEFT_DOWN:
			return currentLocation.getX() < targetLocation.getX() || currentLocation.getY() > targetLocation.getY();
		case RIGHT_DOWN:
			return currentLocation.getX() > targetLocation.getX() || currentLocation.getY() > targetLocation.getY();
		default:
			return false;
		}

	}

	private Direction calculateDirection(Point current, Point target) {
		int dx = target.getX() - current.getX();
		int dy = target.getY() - current.getY();

		if (dx > 0) {
			if (dy > 0) {
				return Direction.RIGHT_DOWN;
			} else if (dy < 0) {
				return Direction.RIGHT_UP;
			} else {
				return Direction.RIGHT;
			}
		} else if (dx < 0) {
			if (dy > 0) {
				return Direction.LEFT_DOWN;
			} else if (dy < 0) {
				return Direction.LEFT_UP;
			} else {
				return Direction.LEFT;
			}
		} else {
			if (dy > 0) {
				return Direction.UP;
			} else if (dy < 0) {
				return Direction.DOWN;
			} else {
				return Direction.NONE; // current and target are the same point
			}
		}
	}

	@Override
	public Point calculateInitialEndpoint(Point start, Direction rotation, boolean friendly) {
		if (friendly) {
			EnemyManager enemyManager = EnemyManager.getInstance();
			if (enemyManager.getClosestEnemy() == null) {
				int endXCoordinate = DataClass.getInstance().getWindowWidth();
				int endYCoordinate = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();
				return new Point(endXCoordinate, endYCoordinate);
			} else
				return enemyManager.getClosestEnemy().getCurrentLocation();
		} else {
			PlayerManager friendlyManager = PlayerManager.getInstance();
			int xCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(0);
			int yCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(1);
			return new Point(xCoordinate, yCoordinate);
		}
	}

	@Override
	public Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementspeed,
			int yMovementspeed) {
		PlayerManager friendlyManager = PlayerManager.getInstance();
		int xCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(0);
		int yCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(1);
		return new Point(xCoordinate, yCoordinate);
	}

	public GameObject getTarget(boolean isFriendly) {
		if (isFriendly) {
			EnemyManager enemyManager = EnemyManager.getInstance();
			return enemyManager.getClosestEnemy();
		} else {
			return PlayerManager.getInstance().getSpaceship();
		}
	}

}