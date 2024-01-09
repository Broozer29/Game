package game.movement.pathfinders;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.ZigZagPathFinderConfig;

import java.util.ArrayList;
import java.util.List;

public class ZigZagPathFinder implements PathFinder {

    private static final int MAX_WAYPOINTS = 200;

    @Override
    public Path findPath(PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof ZigZagPathFinderConfig)) {
            throw new IllegalArgumentException("Expected ZigZagPathFinderConfig");
        }

        ZigZagPathFinderConfig config = (ZigZagPathFinderConfig) pathFinderConfig;
        Point start = config.getStart();
        Direction direction = config.getDirection();
        int xMovementSpeed = config.getxMovementSpeed();
        int yMovementSpeed = config.getyMovementSpeed();
        int stepsBeforeBounce = config.getStepsBeforeBounceToOtherDirection();

        List<Point> waypoints = new ArrayList<>();
        Point currentPoint = start;
        boolean bounceDiagonally = true; // State to keep track of bouncing direction

        int bounces = 0;
        while (waypoints.size() < MAX_WAYPOINTS) {
            for (int step = 0; step < stepsBeforeBounce && waypoints.size() < MAX_WAYPOINTS; step++) {
                if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                    currentPoint = stepTowards(currentPoint, bounceDiagonally ? Direction.LEFT_UP : Direction.LEFT_DOWN,
                            xMovementSpeed, yMovementSpeed);
                } else {
                    currentPoint = stepTowards(currentPoint, bounceDiagonally ? Direction.RIGHT_UP : Direction.RIGHT_DOWN,
                            xMovementSpeed, yMovementSpeed);
                }
                waypoints.add(currentPoint);
            }
            bounceDiagonally = !bounceDiagonally;
            bounces++;
        }

        return new Path(waypoints, direction, false, config.isFriendly());
    }

    private Point stepTowards(Point point, Direction direction, int xStepSize, int yStepSize) {
        int x = point.getX();
        int y = point.getY();
        switch (direction) {
            case LEFT_UP:
                return new Point(x - xStepSize, y - yStepSize);
            case LEFT_DOWN:
                return new Point(x - xStepSize, y + yStepSize);
            case RIGHT_UP:
                return new Point(x + xStepSize, y - yStepSize);
            case RIGHT_DOWN:
                return new Point(x + xStepSize, y + yStepSize);
            default:
                throw new IllegalArgumentException("ZigZagPathFinder requires diagonal movement directions.");
        }
    }

    @Override
    public Direction getNextStep(Point currentLocation, Path path, Direction fallbackDirection) {
        // Implement logic to get the next direction from the current location
        // based on the path waypoints
        return fallbackDirection; // Placeholder
    }

    @Override
    public boolean shouldRecalculatePath(Path path) {
        // Return true if path is null, or waypoints are null or empty
        return path == null || path.getWaypoints() == null || path.getWaypoints().isEmpty();
    }

    @Override
    public Point calculateInitialEndpoint(Point start, Direction rotation, boolean friendly) {
        // Implement if needed
        return start;
    }

    @Override
    public Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementSpeed, int yMovementSpeed) {
        // Implement if needed
        return start;
    }
}
