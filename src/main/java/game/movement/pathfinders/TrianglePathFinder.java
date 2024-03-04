package game.movement.pathfinders;

import VisualAndAudioData.DataClass;
import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.TrianglePathFinderConfig;

import java.util.ArrayList;
import java.util.List;

public class TrianglePathFinder implements PathFinder {
    private final int windowWidth;
    private final int playableWindowMinHeight;
    private final int playableWindowMaxHeight;

    public TrianglePathFinder () {
        this.windowWidth = DataClass.getInstance().getWindowWidth();
        this.playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        this.playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();
    }
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
        int endXCoordinate = 0;
        int endYCoordinate = 0;
        int xCoordinate = start.getX();
        int yCoordinate = start.getY();
        DataClass dataClass = DataClass.getInstance();

        // friendly is not used for regular paths
        switch (rotation) {
            case UP:
                endYCoordinate = this.playableWindowMinHeight - 150;
                endXCoordinate = xCoordinate;
                break;
            case DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 150;
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
                endYCoordinate = this.playableWindowMinHeight -150;
                endXCoordinate = dataClass.getWindowWidth() + 150;
                break;
            case RIGHT_DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 150;
                endXCoordinate = dataClass.getWindowWidth() + 150;
                break;
            case LEFT_UP:
                endYCoordinate = this.playableWindowMinHeight -150;
                endXCoordinate = 0 - 150;
                break;
            case LEFT_DOWN:
                endYCoordinate = this.playableWindowMaxHeight + 150;
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
