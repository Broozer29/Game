package game.movement;

import java.util.List;

import game.managers.FriendlyManager;

public class Path {
	private List<Point> waypoints;
	private Point currentLocation;
	private Direction fallbackDirection;
	private boolean isHoming;

	public Path(List<Point> wayPoints, Direction fallbackDirection, boolean isHoming) {
		this.waypoints = wayPoints;
		this.setFallbackDirection(fallbackDirection);
		this.setHoming(isHoming);
	}

	public List<Point> getWaypoints() {
		return waypoints;
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
		FriendlyManager friendlyManager = FriendlyManager.getInstance();
		int x = friendlyManager.getNearestFriendlyHomingCoordinates().get(0);
		int y = friendlyManager.getNearestFriendlyHomingCoordinates().get(1);
		return new Point(x,y);
	}

	public Direction getFallbackDirection() {
		return fallbackDirection;
	}

	public void setFallbackDirection(Direction fallbackDirection) {
//		System.out.println(fallbackDirection);
		this.fallbackDirection = fallbackDirection;
	}

	public boolean isHoming() {
		return isHoming;
	}

	public void setHoming(boolean isHoming) {
		this.isHoming = isHoming;
	}

	// constructor, getters, setters, etc.
}