package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.objects.GameObject;

public class HomingPathFinderConfig implements PathFinderConfig {

	private Point start;
	private Direction falbackDirection;
	private boolean isHoming = true;
	private boolean isFriendly;

	public HomingPathFinderConfig(GameObject gameObject, MovementConfiguration movementConfiguration) {
		super();
		this.start = gameObject.getCurrentLocation();
		this.falbackDirection = movementConfiguration.getRotation();
		this.isHoming = true;
		this.isFriendly = gameObject.isFriendly();
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public boolean isHoming() {
		return isHoming;
	}

	public void setHoming(boolean isHoming) {
		this.isHoming = isHoming;
	}

	public boolean isFriendly() {
		return isFriendly;
	}

	public void setFriendly(boolean isFriendly) {
		this.isFriendly = isFriendly;
	}

	@Override
	public Direction getMovementDirection () {
		return this.falbackDirection;
	}

	@Override
	public void setMovementDirection (Direction movementDirection) {
		this.falbackDirection = movementDirection;

	}

}
