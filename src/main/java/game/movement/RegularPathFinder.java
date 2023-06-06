package game.movement;

import java.util.ArrayList;
import java.util.List;

import data.DataClass;

public class RegularPathFinder implements PathFinder {

	public Path findPath(Point start, Point end, int stepSize, Direction fallbackDirection) {
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
		Path path = new Path(pathList, fallbackDirection, false);
		return path;
	}

	@Override
	public Direction getNextStep(Point currentLocation, Path path, int stepSize, Direction fallbackDirection) {
		if (!path.getWaypoints().isEmpty()) {
			Point nextPoint = path.getWaypoints().get(0);
			return calculateDirection(currentLocation, nextPoint);
		}
		return fallbackDirection;
	}

	@Override
	public boolean shouldRecalculatePath(Path path) {
		return path == null; // only recalculate if we don't have a path yet
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
			
			//Fuck it, deze zijn omgedraaid om een of andere reden.
			if (dy < 0) {
				return Direction.UP;
			} else if (dy > 0) {
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

	@Override
	public Point calculateInitialEndpoint(Point start, Direction rotation) {
		int endXCoordinate = 0;
		int endYCoordinate = 0;
		int xCoordinate = start.getX();
		int yCoordinate = start.getY();
		DataClass dataClass = DataClass.getInstance();

		switch (rotation) {
		case UP:
			endYCoordinate = -150;
			endXCoordinate = xCoordinate;
			break;
		case DOWN:
			endYCoordinate = dataClass.getWindowHeight() + 150;
			endXCoordinate = xCoordinate;
			break;
		case LEFT:
			endYCoordinate = yCoordinate;
			endXCoordinate = 0;
			break;
		case RIGHT:
			endYCoordinate = yCoordinate;
			endXCoordinate = dataClass.getWindowWidth() + 150;
			break;
		case RIGHT_UP:
			endYCoordinate = 0 - 150;
			endXCoordinate = dataClass.getWindowWidth() + 150;
			break;
		case RIGHT_DOWN:
			endYCoordinate = dataClass.getWindowHeight() + 150;
			endXCoordinate = dataClass.getWindowWidth() + 150;
			break;
		case LEFT_UP:
			endYCoordinate = 0 - 150;
			endXCoordinate = 0;
			break;
		case LEFT_DOWN:
			endYCoordinate = dataClass.getWindowHeight() + 150;
			endXCoordinate = 0 + 150;
			break;
		default:
			endYCoordinate = yCoordinate;
			endXCoordinate = 0 + 150;
			break;
		}
		
		Point endPoint = new Point(endXCoordinate, endYCoordinate);
		return endPoint;
	}

}
