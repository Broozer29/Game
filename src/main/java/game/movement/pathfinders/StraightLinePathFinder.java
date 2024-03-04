package game.movement.pathfinders;

import java.util.ArrayList;
import java.util.List;

import VisualAndAudioData.DataClass;
import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.StraightLinePathFinderConfig;
import game.util.OutOfBoundsCalculator;

public class StraightLinePathFinder implements PathFinder {

    private final int windowWidth;
    private final int playableWindowMinHeight;
    private final int playableWindowMaxHeight;

    public StraightLinePathFinder () {
        this.windowWidth = DataClass.getInstance().getWindowWidth();
        this.playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        this.playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();
    }

    @Override
    public Path findPath (PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof StraightLinePathFinderConfig)) {
            throw new IllegalArgumentException("Expected StraightLinePathFinderConfig");
        }

        StraightLinePathFinderConfig config = (StraightLinePathFinderConfig) pathFinderConfig;
        Point start = config.getStart();
        Point end = config.getEnd();
        int maxStepSizeX = config.getxMovementSpeed();
        int maxStepSizeY = config.getyMovementSpeed();

        List<Point> pathList = new ArrayList<>();
        pathList.add(start);

        int totalDistanceX = end.getX() - start.getX();
        int totalDistanceY = end.getY() - start.getY();
        int stepsToEndpoint = Math.max(Math.abs(totalDistanceX) / maxStepSizeX, Math.abs(totalDistanceY) / maxStepSizeY);

        double stepSizeX = totalDistanceX / (double) stepsToEndpoint;
        double stepSizeY = totalDistanceY / (double) stepsToEndpoint;

        double accumulatedStepX = 0.0;
        double accumulatedStepY = 0.0;
        Direction direction = calculateDirection(start, end);

        for (int i = 0; i <= stepsToEndpoint; i++) {
            // Ensure we directly add the end point as part of the path
            if (i == stepsToEndpoint) {
                pathList.add(end);
                accumulatedStepX = end.getX() - start.getX();
                accumulatedStepY = end.getY() - start.getY();
            } else {
                accumulatedStepX += stepSizeX;
                accumulatedStepY += stepSizeY;

                int nextX = (int) Math.round(accumulatedStepX + start.getX());
                int nextY = (int) Math.round(accumulatedStepY + start.getY());
                pathList.add(new Point(nextX, nextY));
            }
        }

        // Optionally, add extra steps beyond the destination if needed
        boolean breakLoop = false;
        for (int i = 1; i <= 500; i++) {
            if(breakLoop){
                break;
            }
            int nextX = (int) Math.round(end.getX() + i * stepSizeX);
            int nextY = (int) Math.round(end.getY() + i * stepSizeY);
            pathList.add(new Point(nextX, nextY));

            if(OutOfBoundsCalculator.isOutOfBounds(nextX, nextY, direction)){
                breakLoop = true;
            }

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

    // Other overridden methods and additional private helper methods as needed...
}