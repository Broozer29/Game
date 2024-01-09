package game.movement.pathfinders;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.DiamondShapePathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;

import java.util.ArrayList;
import java.util.List;

public class DiamondShapePathFinder implements PathFinder {

    private int loops;


    public DiamondShapePathFinder(int loopAmount){
        this.loops = loopAmount;
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
        return false;
    }

    @Override
    public Point calculateInitialEndpoint (Point start, Direction rotation, boolean friendly) {
        //Not needed for ZigZag movement
        return null;
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