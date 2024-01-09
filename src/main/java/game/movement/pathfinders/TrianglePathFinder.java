package game.movement.pathfinders;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.TrianglePathFinderConfig;

import java.util.ArrayList;
import java.util.List;

public class TrianglePathFinder implements PathFinder {

    @Override
    public Path findPath (PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof TrianglePathFinderConfig)) {
            throw new IllegalArgumentException("Expected TrianglePathFinderConfig");
        }

        TrianglePathFinderConfig config = (TrianglePathFinderConfig) pathFinderConfig;
        int xMovementSpeed = config.getxMovementSpeed(); // Horizontal step size
        int yMovementSpeed = config.getyMovementSpeed(); // Vertical step size
        Point start = config.getStart();

        List<Point> waypoints = new ArrayList<>();
        Point currentPoint = start;
        Direction primaryDirection = config.getMovementDirection();
        Direction[] diagonalDirections = calculateDiagonalDirections(primaryDirection);

        // Move left for a fixed number of steps
        for (int i = 0; i < config.getPrimaryDirectionStepAmount(); i++) {
            currentPoint = stepTowards(currentPoint, primaryDirection, xMovementSpeed, 0);
            waypoints.add(currentPoint);
        }
        // Move right and up for a fixed number of steps
        for (int i = 0; i < config.getFirstDiagonalDirectionStepAmount(); i++) {
            currentPoint = stepTowards(currentPoint, diagonalDirections[0], xMovementSpeed, yMovementSpeed);
            waypoints.add(currentPoint);
        }
        // Move right and down for a fixed number of steps
        for (int i = 0; i < config.getSecondDiagonalDirectionStepAmount(); i++) {
            currentPoint = stepTowards(currentPoint, diagonalDirections[1], xMovementSpeed, yMovementSpeed);
            waypoints.add(currentPoint);
        }

        return new Path(waypoints, primaryDirection, false, config.isFriendly());
    }

    @Override
    public Direction getNextStep (Point currentLocation, Path path, Direction fallbackDirection) {
        return null;
    }

    @Override
    public boolean shouldRecalculatePath (Path path) {
        if (path == null || path.getWaypoints().isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        return null;
    }

    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed) {
        return null;
    }

    // Utility method for stepping towards a direction
    private Point stepTowards (Point point, Direction direction, int XStepSize, int YStepSize) {
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

    private Direction[] calculateDiagonalDirections (Direction primaryDirection) {
        Direction[] diagonalDirections = new Direction[2];
        switch (primaryDirection) {
            case LEFT:
                diagonalDirections[0] = Direction.RIGHT_UP;
                diagonalDirections[1] = Direction.RIGHT_DOWN;
                break;
            case RIGHT:
                diagonalDirections[0] = Direction.LEFT_UP;
                diagonalDirections[1] = Direction.LEFT_DOWN;
                break;
            case UP:
                diagonalDirections[0] = Direction.LEFT_DOWN;
                diagonalDirections[1] = Direction.RIGHT_DOWN;
                break;
            case DOWN:
                diagonalDirections[0] = Direction.RIGHT_UP;
                diagonalDirections[1] = Direction.LEFT_UP;
                break;
            default:
                throw new IllegalArgumentException("Invalid primary direction for triangular pathfinding: " + primaryDirection);
        }
        return diagonalDirections;
    }
}
