package game.movement.pathfinders;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.StraightLinePathFinderConfig;
import game.util.OutOfBoundsCalculator;

public class StraightLinePathFinder implements PathFinder {

    @Override
    public Path findPath(PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof StraightLinePathFinderConfig)) {
            throw new IllegalArgumentException("Expected StraightLinePathFinderConfig");
        }

        Point start = ((StraightLinePathFinderConfig) pathFinderConfig).getStart();
        Point end = ((StraightLinePathFinderConfig) pathFinderConfig).getEnd();
        int maxStepSizeX = ((StraightLinePathFinderConfig) pathFinderConfig).getxMovementSpeed();
        int maxStepSizeY = ((StraightLinePathFinderConfig) pathFinderConfig).getyMovementSpeed();

        List<Point> pathList = new ArrayList<>();
        pathList.add(start);

        int totalDistanceX = end.getX() - start.getX();
        int totalDistanceY = end.getY() - start.getY();
        int stepsToEndpoint = Math.max(Math.abs(totalDistanceX) / maxStepSizeX, Math.abs(totalDistanceY) / maxStepSizeY);

        double stepSizeX = totalDistanceX / (double) stepsToEndpoint;
        double stepSizeY = totalDistanceY / (double) stepsToEndpoint;

        double accumulatedStepX = 0.0;
        double accumulatedStepY = 0.0;

        int currentX = start.getX();
        int currentY = start.getY();
        boolean endpointReached = false;
        boolean outOfBoundsAdded = false; // Flag to track if the out-of-bounds point has been added
        Direction direction = calculateDirection(start, end);

        for (int i = 0; i < 500 && !endpointReached; i++) {
            accumulatedStepX += stepSizeX;
            accumulatedStepY += stepSizeY;

            int nextX = (int) Math.round(accumulatedStepX + start.getX());
            int nextY = (int) Math.round(accumulatedStepY + start.getY());

            Point newPoint = new Point(nextX, nextY);
            if (!OutOfBoundsCalculator.isOutOfBounds(nextX, nextY, direction) || !outOfBoundsAdded) {
                pathList.add(newPoint);
                if (OutOfBoundsCalculator.isOutOfBounds(nextX, nextY, direction)) {
                    outOfBoundsAdded = true; // Mark that an out-of-bounds point has been added
                    break; // Stop adding more points after adding one out-of-bounds point
                }
            }

            // Update the accumulated steps
            accumulatedStepX = nextX - start.getX();
            accumulatedStepY = nextY - start.getY();
        }

        return new Path(pathList, direction, false, pathFinderConfig.isFriendly());
    }



    // Method to calculate the direction based on start and end points
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
                return Direction.NONE; // start and end are the same point
            }
        }
    }


    @Override
    public Direction getNextStep (Point currentLocation, Path path, Direction fallbackDirection) {
        return null;
    }

    @Override
    public boolean shouldRecalculatePath (Path path) {
        if (path == null || path.getWaypoints().isEmpty()) {
            return true;
        } else return false;
    }

    @Override
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        return null;
    }

    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed) {
        return null;
    }

    // Other overridden methods and additional private helper methods as needed...
}