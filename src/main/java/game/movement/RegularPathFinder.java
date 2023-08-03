package game.movement;

import java.util.ArrayList;
import java.util.List;

import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import gamedata.DataClass;

public class RegularPathFinder implements PathFinder {

	@Override
	public Path findPath(PathFinderConfig pathFinderConfig) {
		if (!(pathFinderConfig instanceof RegularPathFinderConfig)) {
			throw new IllegalArgumentException("Expected RegularPathFinderConfig");
		} else {

			Point start = ((RegularPathFinderConfig) pathFinderConfig).getStart();
			Point end = ((RegularPathFinderConfig) pathFinderConfig).getEnd();
			Direction fallbackDirection = ((RegularPathFinderConfig) pathFinderConfig).getFallbackDirection();
			boolean isFriendly = ((RegularPathFinderConfig) pathFinderConfig).isFriendly();
			int XStepSize = ((RegularPathFinderConfig) pathFinderConfig).getxMovementSpeed();
			int YStepSize = ((RegularPathFinderConfig) pathFinderConfig).getyMovementSpeed();

			List<Point> pathList = new ArrayList<>();
			Point currentPoint = start;
			pathList.add(start);
			int maxXSteps = (DataClass.getInstance().getWindowWidth() / XStepSize) * 2;
			int maxYSteps = (DataClass.getInstance().getWindowWidth() / YStepSize) * 2;
			boolean shouldContinue = true;

			int maxSteps = 0;
			if (maxXSteps > maxYSteps) {
				maxSteps = maxXSteps;
			} else {
				maxSteps = maxYSteps;
			}

			int steps = 0;
			Direction direction = Direction.LEFT;
			while (steps < maxSteps) {
				if (!currentPoint.equals(end) || shouldContinue) {
					direction = calculateDirection(currentPoint, end);
				}
				currentPoint = stepTowards(currentPoint, direction, XStepSize, YStepSize);
				pathList.add(currentPoint);
				steps++;

			}
			return new Path(pathList, fallbackDirection, false, isFriendly);
		}
	}

	@Override
	public Direction getNextStep(Point currentLocation, Path path, Direction fallbackDirection) {
		if (!path.getWaypoints().isEmpty()) {
			return calculateDirection(currentLocation, path.getWaypoints().get(0));
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

	public Point stepTowards(Point point, Direction direction, int XStepSize, int YStepSize) {
		int x = point.getX();
		int y = point.getY();
		switch (direction) {
		case UP:
			return new Point(x, y - YStepSize);
		case DOWN:
			return new Point(x, y + YStepSize);
		case LEFT:
			return new Point(x - XStepSize, y);
		case RIGHT:
			return new Point(x + XStepSize, y);
		case RIGHT_UP:
			return new Point(x + XStepSize, y - YStepSize);
		case RIGHT_DOWN:
			return new Point(x + XStepSize, y + YStepSize);
		case LEFT_UP:
			return new Point(x - XStepSize, y - YStepSize);
		case LEFT_DOWN:
			return new Point(x - XStepSize, y + YStepSize);
		default:
			throw new IllegalArgumentException("Invalid direction: " + direction);
		}
	}

	@Override
	public Point calculateInitialEndpoint(Point start, Direction rotation, boolean friendly) {
		int endXCoordinate = 0;
		int endYCoordinate = 0;
		int xCoordinate = start.getX();
		int yCoordinate = start.getY();
		DataClass dataClass = DataClass.getInstance();

		// friendly is not used for regular paths
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
			endXCoordinate = 0 - 150;
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
			endXCoordinate = 0 - 150;
			break;
		case LEFT_DOWN:
			endYCoordinate = dataClass.getWindowHeight() + 150;
			endXCoordinate = 0 - 150;
			break;
		default:
			endYCoordinate = yCoordinate;
			endXCoordinate = 0 + 150;
			break;
		}

		Point endPoint = new Point(endXCoordinate, endYCoordinate);
		return endPoint;
	}

	@Override
	public Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementspeed,
			int yMovementspeed) {

		int endXCoordinate = start.getX();
		int endYCoordinate = start.getY();
		int xDelta = steps * xMovementspeed;
		int yDelta = steps * yMovementspeed;

		switch (rotation) {
		case UP:
			endYCoordinate -= yDelta;
			break;
		case DOWN:
			endYCoordinate += yDelta;
			break;
		case LEFT:
			endXCoordinate -= xDelta;
			break;
		case RIGHT:
			endXCoordinate += xDelta;
			break;
		case RIGHT_UP:
			endYCoordinate -= yDelta;
			endXCoordinate += xDelta;
			break;
		case RIGHT_DOWN:
			endYCoordinate += yDelta;
			endXCoordinate += xDelta;
			break;
		case LEFT_UP:
			endYCoordinate -= yDelta;
			endXCoordinate -= xDelta;
			break;
		case LEFT_DOWN:
			endYCoordinate += yDelta;
			endXCoordinate -= xDelta;
			break;
		default:
			endXCoordinate += xDelta;
			break;
		}

		Point endPoint = new Point(endXCoordinate, endYCoordinate);
		return endPoint;
	}

}