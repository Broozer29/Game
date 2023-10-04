package game.movement;

import game.movement.pathfinderconfigs.BouncingPathFinderConfig;
import game.movement.pathfinderconfigs.HomingPathFinderConfig;
import game.movement.pathfinderconfigs.OrbitPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import game.movement.pathfinders.BouncingPathFinder;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.RegularPathFinder;
import visual.objects.Sprite;

public class SpriteMover {

	private static SpriteMover instance = new SpriteMover();

	private SpriteMover() {

	}

	public static SpriteMover getInstance() {
		return instance;
	}

	private boolean shouldUpdatePathSettings(MovementConfiguration moveConfig) {
		if (moveConfig.getCurrentPath() == null || moveConfig.getCurrentPath().getWaypoints().isEmpty()
				|| moveConfig.getXMovementSpeed() != moveConfig.getLastUsedXMovementSpeed()
				|| moveConfig.getYMovementSpeed() != moveConfig.getLastUsedYMovementSpeed()
				|| moveConfig.getPathFinder().shouldRecalculatePath(moveConfig.getCurrentPath())) {
			return true;
		} else
			return false;
	}

	// Used for movement initialization or recalculation
	private PathFinderConfig getConfigByPathFinder(Sprite sprite, MovementConfiguration moveConfig) {
		PathFinderConfig config = null;
		if (moveConfig.getPathFinder() instanceof OrbitPathFinder) {
			config = new OrbitPathFinderConfig(sprite.getCurrentLocation(), moveConfig.getRotation(),
					sprite.isFriendly());
		} else if (moveConfig.getPathFinder() instanceof RegularPathFinder) {
			config = new RegularPathFinderConfig(sprite.getCurrentLocation(), moveConfig.getDestination(),
					moveConfig.getXMovementSpeed(), moveConfig.getYMovementSpeed(), sprite.isFriendly(),
					moveConfig.getRotation());
		} else if (moveConfig.getPathFinder() instanceof HomingPathFinder) {
			config = new HomingPathFinderConfig(sprite.getCurrentLocation(), moveConfig.getRotation(), true,
					sprite.isFriendly());
		} else if (moveConfig.getPathFinder() instanceof BouncingPathFinder) {
			config = new BouncingPathFinderConfig(sprite, moveConfig.getRotation());
		}
		return config;
	}

	// Moves the sprite only, not it's corresponding animations!
	// Add the "out of bounds" detector!
	public void moveSprite(Sprite sprite, MovementConfiguration moveConfig) {
		if (shouldUpdatePathSettings(moveConfig)) {
			// calculate a new path if necessary
			PathFinderConfig config = getConfigByPathFinder(sprite, moveConfig);
			moveConfig.setCurrentPath(moveConfig.getPathFinder().findPath(config));
			moveConfig.setLastUsedXMovementSpeed(moveConfig.getXMovementSpeed());
			moveConfig.setLastUsedYMovementSpeed(moveConfig.getYMovementSpeed());
		}

		if (moveConfig.getPathFinder() instanceof HomingPathFinder) {
			moveHomingPathFinders(sprite, moveConfig);
		} else {
			moveConfig.getCurrentPath().updateCurrentLocation(sprite.getCurrentLocation());
		}

		// if reached the next point, remove it from the path
		if (sprite.getCurrentLocation().equals(moveConfig.getCurrentPath().getWaypoints().get(0))) {
			moveConfig.getCurrentPath().getWaypoints().remove(0);
		}
		
		if(moveConfig.getPathFinder() instanceof BouncingPathFinder) {
			Direction newDirection = (Direction) ((BouncingPathFinder) moveConfig.getPathFinder()).getNewDirection(sprite, moveConfig.getRotation());
			if (newDirection != moveConfig.getRotation()) {
				moveConfig.setRotation(newDirection);
				moveConfig.setCurrentPath(moveConfig.getPathFinder().findPath(getConfigByPathFinder(sprite, moveConfig)));
			}
		}

		// move towards the next point

		if (moveConfig.getCurrentPath().getWaypoints().size() > 0) {
			sprite.updateCurrentLocation(moveConfig.getCurrentPath().getWaypoints().get(0));
			sprite.setX(moveConfig.getCurrentPath().getWaypoints().get(0).getX());
			sprite.setY(moveConfig.getCurrentPath().getWaypoints().get(0).getY());
		}
		
		if (moveConfig.getPathFinder() instanceof OrbitPathFinder) {
			adjustOrbitPath(moveConfig);
		}
		
		moveConfig.setStepsTaken(moveConfig.getStepsTaken() + 1);

	}

	private void moveHomingPathFinders(Sprite sprite, MovementConfiguration moveConfig) {
		if (moveConfig.getTarget() != null) {
			moveConfig.getCurrentPath().setTarget(moveConfig.getTarget());
		}

		// check if the missile has lost lock
		boolean hasPassedPlayerOrNeverHadLock = false;

		if (moveConfig.getPathFinder().shouldRecalculatePath(moveConfig.getCurrentPath())) {
			hasPassedPlayerOrNeverHadLock = true; // if it should recalculate path, it means it lost lock or never had
		}

		if (hasPassedPlayerOrNeverHadLock) {
			moveConfig.setHasLock(false);
		}

		// only calculate next direction and update location if missile still has lock
		if (moveConfig.hasLock()) {
			// Get the direction of the next step
			Direction nextStep = moveConfig.getPathFinder().getNextStep(moveConfig.getCurrentLocation(),
					moveConfig.getCurrentPath(), moveConfig.getRotation());
			// Based on the direction, calculate the next point
			moveConfig.setNextPoint(calculateNextPoint(moveConfig.getCurrentLocation(), nextStep,
					moveConfig.getXMovementSpeed(), moveConfig.getYMovementSpeed()));
			moveConfig.getCurrentPath().setFallbackDirection(nextStep);
		} else {
			// if missile lost lock, it should keep moving in the last direction
			moveConfig.setRotation(moveConfig.getCurrentPath().getFallbackDirection());
			moveConfig.setNextPoint(calculateNextPoint(moveConfig.getCurrentLocation(),
					moveConfig.getCurrentPath().getFallbackDirection(), moveConfig.getXMovementSpeed(),
					moveConfig.getYMovementSpeed()));
		}

		// Update the current location
		moveConfig.setCurrentLocation(moveConfig.getNextPoint());

	}

	// Adjusts the orbiting path when the target changes coordinates
	private void adjustOrbitPath(MovementConfiguration moveConfig) {
		Sprite target = ((OrbitPathFinder) moveConfig.getPathFinder()).getTarget();
		// Check if the target's position has changed
		if (target.getCenterXCoordinate() != moveConfig.getLastKnownTargetX()
				|| target.getCenterYCoordinate() != moveConfig.getLastKnownTargetY()) {

			int deltaX = 0;
			int deltaY = 0;

			if (moveConfig.getLastKnownTargetX() == 0 || moveConfig.getLastKnownTargetY() == 0) {
				deltaX = 0;
				deltaY = 0;
			} else {
				deltaX = target.getCenterXCoordinate() - moveConfig.getLastKnownTargetX();
				deltaY = target.getCenterYCoordinate() - moveConfig.getLastKnownTargetY();
			}

			// Update the last known position
			moveConfig.setLastKnownTargetX(target.getCenterXCoordinate());
			moveConfig.setLastKnownTargetY(target.getCenterYCoordinate());

			// Adjust the orbit path based on the deltas
			((OrbitPathFinder) moveConfig.getPathFinder()).adjustPathForTargetMovement(moveConfig.getCurrentPath(),
					deltaX, deltaY);
		}
	}

	// Needed for non-orbiting PathFinders, so added to missiles
	private Point calculateNextPoint(Point currentLocation, Direction direction, int XStepSize, int YStepSize) {
		int x = currentLocation.getX();
		int y = currentLocation.getY();

		switch (direction) {
		case UP:
			y -= YStepSize;
			break;
		case DOWN:
			y += YStepSize;
			break;
		case LEFT:
			x -= XStepSize;
			break;
		case RIGHT:
			x += XStepSize;
			break;
		case LEFT_UP:
			x -= XStepSize;
			y -= YStepSize;
			break;
		case LEFT_DOWN:
			x -= XStepSize;
			y += YStepSize;
			break;
		case RIGHT_UP:
			x += XStepSize;
			y -= YStepSize;
			break;
		case RIGHT_DOWN:
			x += XStepSize;
			y += YStepSize;
			break;
		case NONE:
			// no movement
			break;
		}
		return new Point(x, y);
	}
}
