package game.movement;

import java.util.List;

import game.managers.EnemyManager;
import game.managers.PlayerManager;
import game.objects.enemies.Enemy;

public class Path {
	private List<Point> waypoints;
	private Point currentLocation;
	private Direction fallbackDirection;
	private boolean isHoming;
	private boolean isFriendly;

	public Path(List<Point> wayPoints, Direction fallbackDirection, boolean isHoming, boolean isFriendly) {
		this.waypoints = wayPoints;
		this.setFallbackDirection(fallbackDirection);
		this.setHoming(isHoming);
		this.setFriendly(isFriendly);
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
		if (isFriendly) {
			EnemyManager enemyManager = EnemyManager.getInstance();
			Point closestEnemyPoint = enemyManager.getClosestEnemy();
			return closestEnemyPoint;
		} else {
			PlayerManager friendlyManager = PlayerManager.getInstance();
			int x = friendlyManager.getNearestFriendlyHomingCoordinates().get(0);
			int y = friendlyManager.getNearestFriendlyHomingCoordinates().get(1);
			return new Point(x, y);
		}
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