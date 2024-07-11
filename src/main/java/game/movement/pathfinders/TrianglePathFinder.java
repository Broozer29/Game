package game.movement.pathfinders;

import VisualAndAudioData.DataClass;
import game.gameobjects.GameObject;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Path;
import game.movement.Point;

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
    public Path findPath (GameObject gameObject) {

        MovementConfiguration config = gameObject.getMovementConfiguration();
        int xMovementSpeed = config.getXMovementSpeed(); // Horizontal step size
        int yMovementSpeed = config.getYMovementSpeed(); // Vertical step size
        Point start = new Point(gameObject.getXCoordinate(), gameObject.getYCoordinate());

        List<Point> waypoints = new ArrayList<>();
        waypoints.add(start);
        Point currentPoint = start;
        Direction primaryDirection = config.getRotation();
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

        return new Path(waypoints, primaryDirection);
    }

    @Override
    public Direction getNextStep (GameObject gameObject, Direction fallbackDirection) {
        return null;
    }

    public boolean shouldRecalculatePath (GameObject gameObject) {
        MovementConfiguration configuration = gameObject.getMovementConfiguration();
        return(configuration.getCurrentPath() == null || configuration.getCurrentPath().getWaypoints().isEmpty());
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
