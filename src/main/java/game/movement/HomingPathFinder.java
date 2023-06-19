package game.movement;

import java.util.ArrayList;
import java.util.List;

import game.managers.EnemyManager;
import game.managers.FriendlyManager;

public class HomingPathFinder implements PathFinder {
	@Override
	public Path findPath(Point start, Point end, int XstepSize, int YStepSize, Direction fallbackDirection,
			boolean isFriendly) {
		List<Point> waypoints = new ArrayList<>();
		waypoints.add(start);
		return new Path(waypoints, fallbackDirection, true, isFriendly);
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

	// If the missile is friendly but there are no enemies, return false
	public boolean shouldRecalculatePath(Path currentPath) {
		boolean hasPassed = false;
		if (currentPath.isFriendly()) {
			return EnemyManager.getInstance().enemiesToHomeTo();
		} else {
			hasPassed = hasPassedTarget(currentPath);
		}

		return hasPassed;
	}

	/*-Fallback direction constant updaten met zijn huidige direction werkt niet met deze manier om "passedtarget" te berekenen.*/
	private boolean hasPassedTarget(Path currentPath) {
		Point currentLocation = currentPath.getCurrentLocation();
		Point targetLocation = currentPath.getHomingTargetLocation();
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
	public Point calculateInitialEndpoint(Point start, Direction rotation) {
		FriendlyManager friendlyManager = FriendlyManager.getInstance();
		int xCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(0);
		int yCoordinate = friendlyManager.getNearestFriendlyHomingCoordinates().get(1);

		return new Point(xCoordinate, yCoordinate);
	}

}