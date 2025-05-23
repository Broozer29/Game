package net.riezebos.bruus.tbd.game.movement.pathfinders;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Path;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

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
    public Path findPath(GameObject gameObject) {

        Point start = new Point(gameObject.getXCoordinate(), gameObject.getYCoordinate());
        float xMovementSpeed = gameObject.getMovementConfiguration().getXMovementSpeed();
        float yMovementSpeed = gameObject.getMovementConfiguration().getYMovementSpeed();
        int zigzagWidth = gameObject.getMovementConfiguration().getDiamondWidth(); // Use the configured zigzag width
        int zigzagHeight = gameObject.getMovementConfiguration().getDiamondHeight(); // Use the configured zigzag height

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
        Path newPath = new Path(waypoints, gameObject.getMovementConfiguration().getRotation());
        return newPath;
    }

    @Override
    public Direction getNextStep (GameObject gameObject, Direction fallbackDirection) {
        //Not needed for ZigZag movement
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
        //Not needed for ZigZag movement
        return null;
    }

    public Point stepTowards(Point point, Direction direction, float XStepSize, float YStepSize) {
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