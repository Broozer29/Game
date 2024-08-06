package game.movement.pathfinders;

import VisualAndAudioData.DataClass;
import game.gameobjects.GameObject;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Path;
import game.movement.Point;

import java.util.ArrayList;
import java.util.List;

public class ZigZagPathFinder implements PathFinder {

    private static final int MAX_WAYPOINTS = 200;
    private final int windowWidth;
    private final int playableWindowMinHeight;
    private final int playableWindowMaxHeight;

    public ZigZagPathFinder() {
        this.windowWidth = DataClass.getInstance().getWindowWidth();
        this.playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        this.playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();
    }

    @Override
    public Path findPath(GameObject gameObject) {

        MovementConfiguration config = gameObject.getMovementConfiguration();
        Point start = new Point(gameObject.getXCoordinate(), gameObject.getYCoordinate());
        Direction direction = config.getRotation();
        float xMovementSpeed = config.getXMovementSpeed();
        float yMovementSpeed = config.getYMovementSpeed();
        int stepsBeforeBounce = config.getStepsBeforeBounceInOtherDirection();

        List<Point> waypoints = new ArrayList<>();
        Point currentPoint = start;
        waypoints.add(start);
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

        return new Path(waypoints, direction);
    }

    private Point stepTowards(Point point, Direction direction, float xStepSize, float yStepSize) {
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
    public Direction getNextStep(GameObject gameObject, Direction fallbackDirection) {
        // Implement logic to get the next direction from the current location
        // based on the path waypoints
        return fallbackDirection; // Placeholder
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
    public Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementSpeed, int yMovementSpeed) {
        // Implement if needed
        return start;
    }
}
