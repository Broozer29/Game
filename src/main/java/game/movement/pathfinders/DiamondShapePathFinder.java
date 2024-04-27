package game.movement.pathfinders;

import VisualAndAudioData.DataClass;
import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.DiamondShapePathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;

import java.util.ArrayList;
import java.util.List;

public class DiamondShapePathFinder implements PathFinder {

    private int loops;
    private final int windowWidth;
    private final int playableWindowMinHeight;
    private final int playableWindowMaxHeight;


    public DiamondShapePathFinder(int loopAmount){
        this.loops = loopAmount;
        this.windowWidth = DataClass.getInstance().getWindowWidth();
        this.playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        this.playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();

    }

    @Override
    public Path findPath(PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof DiamondShapePathFinderConfig)) {
            throw new IllegalArgumentException("Expected DiamondShapePathFinderConfig");
        }

        DiamondShapePathFinderConfig config = (DiamondShapePathFinderConfig) pathFinderConfig;
        Point start = config.getStart();
        int xMovementSpeed = 1;
        int yMovementSpeed = 1;
        int zigzagWidth = config.getDiamondWidth(); // Use the configured zigzag width
        int zigzagHeight = config.getDiamondHeight(); // Use the configured zigzag height

        List<Point> waypoints = new ArrayList<>();
        Point currentPoint = start;
        int leftwardMovement = 0;
        waypoints.add(start);

        for (int i = 0; i < loops; i++) {
            // Upper left side of the diamond
            for (int step = 0; step < zigzagWidth; step++) {
                currentPoint = stepTowards(currentPoint, Direction.LEFT_UP, xMovementSpeed, yMovementSpeed);
                waypoints.add(new Point(currentPoint.getX() - leftwardMovement, currentPoint.getY()));
            }
            // Lower left side of the diamond
            for (int step = 0; step < zigzagHeight; step++) {
                currentPoint = stepTowards(currentPoint, Direction.LEFT_DOWN, xMovementSpeed, yMovementSpeed);
                waypoints.add(new Point(currentPoint.getX() - leftwardMovement, currentPoint.getY()));
            }

            // Lower right side of the diamond
            for (int step = 0; step < zigzagHeight; step++) {
                currentPoint = stepTowards(currentPoint, Direction.RIGHT_DOWN, xMovementSpeed, yMovementSpeed);
                waypoints.add(new Point(currentPoint.getX() - leftwardMovement, currentPoint.getY()));
            }
            // Upper right side of the diamond
            for (int step = 0; step < zigzagWidth; step++) {
                currentPoint = stepTowards(currentPoint, Direction.RIGHT_UP, xMovementSpeed, yMovementSpeed);
                waypoints.add(new Point(currentPoint.getX() - leftwardMovement, currentPoint.getY()));
            }
        }

        // Create the Path object with the waypoints
        Path newPath = new Path(waypoints, pathFinderConfig.getMovementDirection(), false, pathFinderConfig.isFriendly());
        return newPath;
    }

    @Override
    public Direction getNextStep (Point currentLocation, Path path, Direction fallbackDirection) {
        //Not needed for ZigZag movement
        return null;
    }

    @Override
    public boolean shouldRecalculatePath (Path path) {
        return(path == null || path.getWaypoints().isEmpty());
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
        //Not needed for ZigZag movement
        return null;
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

}