package game.movement.pathfinders;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import VisualAndAudioData.DataClass;

public class RegularPathFinder implements PathFinder {

    private final int windowWidth;
    private final int playableWindowMinHeight;
    private final int playableWindowMaxHeight;

    public RegularPathFinder () {
        this.windowWidth = DataClass.getInstance().getWindowWidth();
        this.playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        this.playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();
    }

    @Override
    public Path findPath(PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof RegularPathFinderConfig)) {
            throw new IllegalArgumentException("Expected RegularPathFinderConfig");
        } else {
            RegularPathFinderConfig config = (RegularPathFinderConfig) pathFinderConfig;
            Point start = config.getStart();
            Point end = config.getEnd();
            Direction fallbackDirection = config.getMovementDirection();
            boolean isFriendly = config.isFriendly();
            int XStepSize = config.getxMovementSpeed();
            int YStepSize = config.getyMovementSpeed();

            if (end == null) {
                end = calculateInitialEndpoint(start, fallbackDirection, isFriendly);
            }

            List<Point> pathList = new ArrayList<>();
            Point currentPoint = start;
            pathList.add(start);

            int maxXSteps = XStepSize > 0 ? (DataClass.getInstance().getWindowWidth() / XStepSize) * 2 : 1;
            int maxYSteps = YStepSize > 0 ? (DataClass.getInstance().getWindowWidth() / YStepSize) * 2 : 1;
            int maxSteps = Math.max(maxXSteps, maxYSteps);

            int steps = 0;
            Direction direction = Direction.LEFT;
            while (steps < maxSteps) {
                direction = calculateDirection(currentPoint, end);
                Point nextPoint = stepTowards(currentPoint, direction, XStepSize, YStepSize);

                if (isCloseToDestination(nextPoint, end)) {
                    pathList.add(nextPoint); // Add the final point if it's close to the destination
                    break; // Stop the loop if close enough to the end
                }

                pathList.add(nextPoint);
                currentPoint = nextPoint;
                steps++;
            }
            return new Path(pathList, fallbackDirection, false, isFriendly);
        }
    }

    // Helper method to determine if two points are close to each other
    private boolean isCloseToDestination(Point current, Point destination) {
        final int proximityThreshold = 2; // Define how close points need to be
        return Math.abs(current.getX() - destination.getX()) <= proximityThreshold &&
                Math.abs(current.getY() - destination.getY()) <= proximityThreshold;
    }

    @Override
    public Direction getNextStep (Point currentLocation, Path path, Direction fallbackDirection) {
        if (!path.getWaypoints().isEmpty()) {
            return calculateDirection(currentLocation, path.getWaypoints().get(0));
        }
        return fallbackDirection;
    }

    @Override
    public boolean shouldRecalculatePath (Path path) {
        return(path == null || path.getWaypoints().isEmpty());
    }

    public Direction calculateDirection (Point start, Point end) {
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

    public Point stepTowards (Point point, Direction direction, int XStepSize, int YStepSize) {
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
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        int endXCoordinate = 0;
        int endYCoordinate = 0;
        int xCoordinate = start.getX();
        int yCoordinate = start.getY();
        DataClass dataClass = DataClass.getInstance();

        // friendly is not used for regular paths
        switch (rotation) {
            case UP:
                endYCoordinate = this.playableWindowMinHeight - 350;
                endXCoordinate = xCoordinate;
                break;
            case DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 350;
                endXCoordinate = xCoordinate;
                break;
            case LEFT:
                endYCoordinate = yCoordinate;
                endXCoordinate = 0 - 350;
                break;
            case RIGHT:
                endYCoordinate = yCoordinate;
                endXCoordinate = dataClass.getWindowWidth() + 350;
                break;
            case RIGHT_UP:
                endYCoordinate = this.playableWindowMinHeight -350;
                endXCoordinate = dataClass.getWindowWidth() + 350;
                break;
            case RIGHT_DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 350;
                endXCoordinate = dataClass.getWindowWidth() + 350;
                break;
            case LEFT_UP:
                endYCoordinate = this.playableWindowMinHeight -350;
                endXCoordinate = 0 - 350;
                break;
            case LEFT_DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 350;
                endXCoordinate = 0 - 350;
                break;
            default:
                endYCoordinate = yCoordinate;
                endXCoordinate = 0 + 350;
                break;
        }

        Point endPoint = new Point(endXCoordinate, endYCoordinate);
        return endPoint;
    }

    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed,
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