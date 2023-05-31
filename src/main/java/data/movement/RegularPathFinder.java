package data.movement;

import java.util.ArrayList;
import java.util.List;

public class RegularPathFinder implements PathFinder {

	public Path findPath(Point start, Point end, int stepSize) {
		List<Point> pathList = new ArrayList<>();
		Point currentPoint = start;
		pathList.add(start);
		int maxSteps = 20000;
		boolean shouldContinue = true;

		int steps = 0;
		Direction direction = Direction.LEFT;
		while (steps < maxSteps) {
			if (!currentPoint.equals(end) || shouldContinue) {
				direction = calculateDirection(currentPoint, end);
			}
			currentPoint = stepTowards(currentPoint, direction, stepSize);
			pathList.add(currentPoint);
			steps++;
			
		}
		Path path = new Path(pathList);
		return path;
	}

	private boolean isCloseEnough(Point currentPoint, Point end, int stepSize) {
		int xCoordinateDifference = Math.abs(currentPoint.getX() - end.getX());
		int yCoordinateDifference = Math.abs(currentPoint.getY() - end.getY());
		return xCoordinateDifference <= stepSize && yCoordinateDifference <= stepSize;
	}

	@Override
	public Direction getNextStep(Point currentLocation, Path path, int stepSize) {
		if (!path.getWaypoints().isEmpty()) {
			Point nextPoint = path.getWaypoints().get(0);
			return calculateDirection(currentLocation, nextPoint);
		}
		return Direction.LEFT; // or some default direction
	}

	public Direction calculateDirection(Point start, Point end) {
		int dx = end.getX() - start.getX();
		int dy = end.getY() - start.getY();

		if (dx > 0) {
			if (dy > 0) {
				return Direction.RIGHT_UP;
			} else if (dy < 0) {
				return Direction.RIGHT_DOWN;
			} else {
				return Direction.RIGHT;
			}
		} else if (dx < 0) {
			if (dy > 0) {
				return Direction.LEFT_UP;
			} else if (dy < 0) {
				return Direction.LEFT_DOWN;
			} else {
				return Direction.LEFT;
			}
		} else {
			if (dy > 0) {
				return Direction.UP;
			} else if (dy < 0) {
				return Direction.DOWN;
			} else {
				return Direction.LEFT; // start and end are the same point
			}
		}
	}

	public Point stepTowards(Point point, Direction direction, int stepSize) {
	    int x = point.getX();
	    int y = point.getY();

	    switch (direction) {
	        case UP:
	            return new Point(x, y - stepSize);
	        case DOWN:
	            return new Point(x, y + stepSize);
	        case LEFT:
	            return new Point(x - stepSize, y);
	        case RIGHT:
	            return new Point(x + stepSize, y);
	        case RIGHT_UP:
	            return new Point(x + stepSize, y - stepSize);
	        case RIGHT_DOWN:
	            return new Point(x + stepSize, y + stepSize);
	        case LEFT_UP:
	            return new Point(x - stepSize, y - stepSize);
	        case LEFT_DOWN:
	            return new Point(x - stepSize, y + stepSize);
	        default:
	            throw new IllegalArgumentException("Invalid direction: " + direction);
	    }
	}

}
