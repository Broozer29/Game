package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.objects.GameObject;

public class OrbitPathFinderConfig implements PathFinderConfig {
	private Point start;
	private Direction fallbackDirection;
	private boolean friendly;
	private GameObject gameObject;

	private double radius; // Use the radius from the config

	public OrbitPathFinderConfig(GameObject gameObject, MovementConfiguration movementConfiguration) {
		this.start = gameObject.getCurrentLocation();
		this.fallbackDirection = movementConfiguration.getRotation();
		this.friendly = gameObject.isFriendly();
		this.radius = movementConfiguration.getOrbitRadius();
		this.gameObject = gameObject;
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

	public GameObject getGameObject () {
		return gameObject;
	}

	public Direction getFallbackDirection () {
		return fallbackDirection;
	}

	public void setFallbackDirection (Direction fallbackDirection) {
		this.fallbackDirection = fallbackDirection;
	}

	public double getRadius () {
		return radius;
	}

	public void setRadius (double radius) {
		this.radius = radius;
	}

	public void setGameObject (GameObject gameObject) {
		this.gameObject = gameObject;
	}
}