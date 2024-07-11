package game.movement.deprecatedpathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.gameobjects.GameObject;

public class RegularPathFinderConfig implements PathFinderConfig {

	private Point start;
	private Point end;
	private int xMovementSpeed;
	private int yMovementSpeed;
	private boolean isFriendly;
	private Direction fallbackDirection;

	public RegularPathFinderConfig(Point start, Point end, int xMovementSpeed, int yMovementSpeed, boolean isFriendly,
			 Direction fallbackDirection) {
		super();
		this.start = start;
		this.end = end;
		this.xMovementSpeed = xMovementSpeed;
		this.yMovementSpeed = yMovementSpeed;
		this.isFriendly = isFriendly;
		this.fallbackDirection = fallbackDirection;
	}

	public RegularPathFinderConfig(GameObject gameObject, MovementConfiguration movementConfiguration) {
		super();
		this.start = gameObject.getCurrentLocation();
		this.end = movementConfiguration.getDestination();
		this.xMovementSpeed = movementConfiguration.getXMovementSpeed();
		this.yMovementSpeed = movementConfiguration.getYMovementSpeed();
		this.isFriendly = gameObject.isFriendly();
		this.fallbackDirection = movementConfiguration.getRotation();
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public int getxMovementSpeed() {
		return xMovementSpeed;
	}

	public void setxMovementSpeed(int xMovementSpeed) {
		this.xMovementSpeed = xMovementSpeed;
	}

	public int getyMovementSpeed() {
		return yMovementSpeed;
	}

	public void setyMovementSpeed(int yMovementSpeed) {
		this.yMovementSpeed = yMovementSpeed;
	}

	public boolean isFriendly() {
		return isFriendly;
	}

	public void setFriendly(boolean isFriendly) {
		this.isFriendly = isFriendly;
	}

	public Direction getMovementDirection () {
		return fallbackDirection;
	}

	public void setMovementDirection (Direction movementDirection) {
		this.fallbackDirection = movementDirection;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

}
