package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.objects.GameObject;

public class OrbitPathFinderConfig implements PathFinderConfig {
	private Point start;
	private Direction fallbackDirection;
	private boolean friendly;

	public OrbitPathFinderConfig(GameObject gameObject, MovementConfiguration movementConfiguration) {
		this.start = gameObject.getCurrentLocation();
		this.fallbackDirection = movementConfiguration.getRotation();
		this.friendly = gameObject.isFriendly();
	}

	@Override
	public void setStart(Point start) {
		this.start = start;
	}

	@Override
	public Point getStart() {
		return start;
	}

	@Override
	public Direction getMovementDirection () {
		return fallbackDirection;
	}

	@Override
	public void setMovementDirection (Direction movementDirection) {
		this.fallbackDirection = movementDirection;

	}

	@Override
	public boolean isFriendly() {
		return friendly;
	}

	@Override
	public void setFriendly(boolean isFriendly) {
		this.friendly = isFriendly;

	}

}