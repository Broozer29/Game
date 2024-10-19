package net.riezebos.bruus.tbd.game.movement.pathfinders;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Path;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visuals.audiodata.DataClass;

import java.util.ArrayList;
import java.util.List;

public class DestinationPathFinder implements PathFinder {
    //Pathfinder that goes to the destination and stays there, unlike straighline pathfinders

    private Point destination;

    @Override
    public Path findPath (GameObject gameObject) {
        MovementConfiguration config = gameObject.getMovementConfiguration();

        Point start = new Point(gameObject.getXCoordinate(), gameObject.getYCoordinate());
        Point end = config.getDestination();
        float maxStepSizeX = config.getXMovementSpeed();
        float maxStepSizeY = config.getYMovementSpeed();

        List<Point> pathList = new ArrayList<>();
        pathList.add(start);

        int totalDistanceX = end.getX() - start.getX();
        int totalDistanceY = end.getY() - start.getY();
        int stepsToEndpoint = Math.round(Math.max(Math.abs(totalDistanceX) / maxStepSizeX, Math.abs(totalDistanceY) / maxStepSizeY));

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

        destination = pathList.get(pathList.size() - 1);
        config.setDestination(pathList.get(pathList.size() - 1));
        return new Path(pathList, direction);
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
    public Direction getNextStep (GameObject gameObject, Direction fallbackDirection) {
        return null; //The path already contains this, no need
    }

    @Override
    public boolean shouldRecalculatePath (GameObject gameObject) {
        MovementConfiguration configuration = gameObject.getMovementConfiguration();

        if (gameObject.getCurrentLocation().getX() == destination.getX()
                && gameObject.getCurrentLocation().getY() == destination.getY()) {
            return false;
        }

        return configuration.getCurrentPath().getWaypoints().isEmpty();
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
                endYCoordinate = DataClass.getInstance().getPlayableWindowMinHeight();
                endXCoordinate = xCoordinate;
                break;
            case DOWN:
                endYCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight();
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
                endYCoordinate = DataClass.getInstance().getPlayableWindowMinHeight();
                endXCoordinate = dataClass.getWindowWidth() + 150;
                break;
            case RIGHT_DOWN:
                endYCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight();
                endXCoordinate = dataClass.getWindowWidth() + 150;
                break;
            case LEFT_UP:
                endYCoordinate = DataClass.getInstance().getPlayableWindowMinHeight();
                endXCoordinate = 0 - 150;
                break;
            case LEFT_DOWN:
                endYCoordinate = DataClass.getInstance().getPlayableWindowMaxHeight();
                endXCoordinate = 0 - 150;
                break;
            default:
                endYCoordinate = yCoordinate;
                endXCoordinate = 0 - 150;
                break;
        }

        Point endPoint = new Point(endXCoordinate, endYCoordinate);
        return endPoint;
    }


    @Override
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed) {
        return null;
    }
}
