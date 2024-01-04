package game.movement.pathfinders;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.OrbitPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import visualobjects.Sprite;

public class OrbitPathFinder implements PathFinder {
	private Sprite target;
	private int radius;
	private int totalFrames;
	private double offsetAngle; // NEW: Angle offset for this drone

	public OrbitPathFinder(Sprite target, int radius, int totalFrames, double offsetAngle) {
		this.target = target;
		this.radius = radius;
		this.totalFrames = totalFrames;
		this.offsetAngle = offsetAngle;
	}

	@Override
	public Path findPath(PathFinderConfig pathFinderConfig) {
		if (!(pathFinderConfig instanceof OrbitPathFinderConfig)) {
			throw new IllegalArgumentException("Expected OrbitPathFinderConfig");
		} else {

			Point start = ((OrbitPathFinderConfig) pathFinderConfig).getStart();
			Direction fallbackDirection = ((OrbitPathFinderConfig) pathFinderConfig).getFallbackDirection();
			boolean isFriendly = ((OrbitPathFinderConfig) pathFinderConfig).isFriendly();

			double angleStep = Math.PI * 2 / totalFrames;
			List<Point> waypoints = new ArrayList<>();

			// Determine the angle for the starting point relative to the target
			double startAngle = Math.atan2(start.getY() - target.getCenterYCoordinate(),
					start.getX() - target.getCenterXCoordinate());

			// Adjust the offset angle if it's more than 2 * PI away from the start angle
			while (Math.abs(startAngle - offsetAngle) > Math.PI) {
				if (startAngle < offsetAngle) {
					startAngle += Math.PI * 2;
				} else {
					startAngle -= Math.PI * 2;
				}
			}

			// Generate waypoints that include a smooth transition from start to orbit
			for (int i = 0; i < totalFrames; i++) {
				double angle = angleStep * i + startAngle; // Use startAngle instead of offsetAngle

				int x = target.getCenterXCoordinate() + (int) (Math.cos(angle) * radius);
				int y = target.getCenterYCoordinate() + (int) (Math.sin(angle) * radius);

				waypoints.add(new Point(x, y));
			}

			return new Path(waypoints, fallbackDirection, false, isFriendly);
		}
	}

	@Override
	public Direction getNextStep(Point currentLocation, Path path, Direction fallbackDirection) {
		// Get the list of waypoints from the path
		List<Point> waypoints = path.getWaypoints();

		// Check if there are any waypoints left. If not, return the fallback direction.
		if (waypoints.isEmpty()) {
			return fallbackDirection;
		}

		// Get the next waypoint
		Point nextWaypoint = waypoints.get(0);

		// Calculate the direction towards the next waypoint
		Direction direction = calculateDirection(currentLocation, nextWaypoint);

		// If the current location is already at the next waypoint,
		// remove the waypoint from the list and recursively call this function to get the next
		// direction.
		if (currentLocation.equals(nextWaypoint)) {
			waypoints.remove(0);
			return getNextStep(currentLocation, path, fallbackDirection);
		}

		return direction;
	}

	public Direction calculateDirection(Point start, Point end) {
		int dx = end.getX() - start.getX();
		int dy = end.getY() - start.getY();

		// Fuck it, om een of andere reden is dy de tegenovergestelde richting.
		// Waarom dit is I dont fucking know maar het werkt op deze manier
		if (dx > 0) {
			if (dy < 0) {
				return Direction.RIGHT_UP;
			} else if (dy > 0) {
				return Direction.RIGHT_DOWN;
			} else {
				return Direction.RIGHT;
			}
		} else if (dx < 0) {
			if (dy < 0) {
				return Direction.LEFT_UP;
			} else if (dy > 0) {
				return Direction.LEFT_DOWN;
			} else {
				return Direction.LEFT;
			}
		} else {

			if (dy < 0) {
				return Direction.UP;
			} else if (dy > 0) {
				return Direction.DOWN;
			} else {
				return Direction.LEFT; // start and end are the same point
			}
		}
	}

	@Override
	public boolean shouldRecalculatePath(Path path) {
		// This could return true if the target has moved a significant amount since the last
		// path calculation.
		return false;
	}

	@Override
	public Point calculateInitialEndpoint(Point start, Direction rotation, boolean friendly) {
		// This method could return the first waypoint on the path.
		return start;
	}

	public void adjustPathForTargetMovement(Path path, int targetDeltaX, int targetDeltaY) {
		List<Point> waypoints = path.getWaypoints();

		for (Point waypoint : waypoints) {
			waypoint.setX(waypoint.getX() + targetDeltaX);
			waypoint.setY(waypoint.getY() + targetDeltaY);
		}
	}

	public Sprite getTarget() {
		return target;
	}

	@Override
	public Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementspeed,
			int yMovementspeed) {
		// Should not be used for OrbitPathFinders
		return start;
	}
	
	public int getFrames() {
		return this.totalFrames;
	}
}
