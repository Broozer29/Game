package net.riezebos.bruus.tbd.game.movement;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;

import java.util.List;

public class Path {
	private List<Point> waypoints;
	private Point currentLocation;
	private Direction originalDirection;
	private Direction fallbackDirection;
	private boolean isHoming;
	private boolean isFriendly;
	private GameObject target;

	public Path(List<Point> wayPoints, Direction fallbackDirection) {
		this.waypoints = wayPoints;
		this.originalDirection = fallbackDirection;
		this.setFallbackDirection(fallbackDirection);
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
		if(currentLocation == null && !waypoints.isEmpty()){
			currentLocation = waypoints.get(0);
		}
		return this.currentLocation;
	}

	public void setCurrentLocation(Point point) {
		this.currentLocation = point;
	}

	public Point getHomingTargetLocation() {
		if (target != null) {
			return new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate());
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

	public Direction getOriginalDirection () {
		return originalDirection;
	}
}