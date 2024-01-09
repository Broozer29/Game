package game.movement;

import java.util.List;

import game.objects.GameObject;

public class Path {
	private List<Point> waypoints;
	private Point currentLocation;
	private Direction fallbackDirection;
	private boolean isHoming;
	private boolean isFriendly;
	private GameObject target;

	public Path(List<Point> wayPoints, Direction fallbackDirection, boolean isHoming, boolean isFriendly) {
		this.waypoints = wayPoints;
		this.setFallbackDirection(fallbackDirection);
		this.setHoming(isHoming);
		this.setFriendly(isFriendly);
	}

	public List<Point> getWaypoints() {
		return waypoints;
	}

	// Used for homing paths
	public void setTarget(GameObject target) {
		this.target = target;
	}

	public GameObject getTarget() {
		return this.target;
	}

	public void setWaypoints(List<Point> waypoints) {
		this.waypoints = waypoints;
	}

	public void updateCurrentLocation(Point point) {
		this.currentLocation = point;
	}

	public Point getCurrentLocation() {
		return this.currentLocation;
	}

	public void setCurrentLocation(Point point) {
		this.currentLocation = point;
	}

	public Point getHomingTargetLocation() {
		if (target != null) {
			return target.getCurrentLocation();
		} else return null;
	}

	public Direction getFallbackDirection() {
		return fallbackDirection;
	}

	public void setFallbackDirection(Direction fallbackDirection) {
		this.fallbackDirection = fallbackDirection;
	}

	public boolean isHoming() {
		return isHoming;
	}

	public void setHoming(boolean isHoming) {
		this.isHoming = isHoming;
	}

	public void setFriendly(boolean isFriendly) {
		this.isFriendly = isFriendly;
	}

	public boolean isFriendly() {
		return this.isFriendly;
	}

}