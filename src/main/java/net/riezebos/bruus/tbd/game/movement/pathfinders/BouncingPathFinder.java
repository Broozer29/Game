package net.riezebos.bruus.tbd.game.movement.pathfinders;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Path;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visuals.audiodata.DataClass;

import java.util.ArrayList;
import java.util.List;

public class BouncingPathFinder implements PathFinder {

    private final int windowWidth;
    private final int playableWindowMinHeight;
    private final int playableWindowMaxHeight;

    private int maxBounces = 20;
    private int currentBounces = 0;

    public BouncingPathFinder () {
        this.windowWidth = DataClass.getInstance().getWindowWidth();
        this.playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        this.playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();

    }

    @Override
    public Path findPath (GameObject gameObject) {
            int xCoordinate = gameObject.getXCoordinate();
            int yCoordinate = gameObject.getYCoordinate();
            int spriteWidth = gameObject.getWidth();
            int spriteHeight = gameObject.getHeight();
            Direction spriteRotation = gameObject.getMovementConfiguration().getRotation();

            Direction newDirection = getNewDirection(gameObject, spriteRotation);
            List<Integer> endCoordinates = getNewEndpointCoordinates(xCoordinate, yCoordinate, spriteWidth, spriteHeight, newDirection);


            Point newEndpoint = new Point(endCoordinates.get(0), endCoordinates.get(1));
            gameObject.getMovementConfiguration().setDestination(newEndpoint);

            RegularPathFinder regPathFinder = new RegularPathFinder();
            Path newPath = regPathFinder.findPath(gameObject);
            return newPath;
    }

    @Override
    public Direction getNextStep (GameObject gameObject, Direction fallbackDirection) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
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
    public Point calculateEndPointBySteps (Point start, Direction rotation, int steps, int xMovementspeed,
                                           int yMovementspeed) {
        // TODO Auto-generated method stub
        return null;
    }

    public Direction getNewDirection (GameObject gameObject, Direction spriteDirection) {

        int xCoordinate = 0;
        int yCoordinate = 0;
        int spriteWidth = 0;
        int spriteHeight = 0;

        if (gameObject.getAnimation() != null) {
            xCoordinate = gameObject.getAnimation().getXCoordinate();
            yCoordinate = gameObject.getAnimation().getYCoordinate();
            spriteWidth = gameObject.getAnimation().getWidth();
            spriteHeight = gameObject.getAnimation().getHeight();
        } else {
            xCoordinate = gameObject.getXCoordinate();
            yCoordinate = gameObject.getYCoordinate();
            spriteWidth = gameObject.getWidth();
            spriteHeight = gameObject.getHeight();
        }

        boolean changeX = (isLeft(spriteDirection) && xCoordinate <= 0)
                || (isRight(spriteDirection) && (xCoordinate + spriteWidth) >= windowWidth);
        boolean changeY = (isUp(spriteDirection) && yCoordinate <= 0)
                || (isDown(spriteDirection) && (yCoordinate + spriteHeight) >= playableWindowMaxHeight);
        if (changeX && changeY)
            return flipDirection(spriteDirection);
        else if (changeX)
            return flipXDirection(spriteDirection);
        else if (changeY)
            return flipYDirection(spriteDirection);

        return spriteDirection;
    }

    private Direction flipDirection (Direction direction) {
        switch (direction) {
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT_UP:
                return Direction.RIGHT_DOWN;
            case LEFT_DOWN:
                return Direction.RIGHT_UP;
            case RIGHT_UP:
                return Direction.LEFT_DOWN;
            case RIGHT_DOWN:
                return Direction.LEFT_UP;
            default:
                return direction;
        }
    }

    private Direction flipXDirection (Direction direction) {
        switch (direction) {
            case LEFT:
                return Direction.RIGHT;
            case RIGHT:
                return Direction.LEFT;
            case LEFT_UP:
                return Direction.RIGHT_UP;
            case LEFT_DOWN:
                return Direction.RIGHT_DOWN;
            case RIGHT_UP:
                return Direction.LEFT_UP;
            case RIGHT_DOWN:
                return Direction.LEFT_DOWN;
            default:
                return direction;
        }
    }

    private Direction flipYDirection (Direction direction) {
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT_UP:
                return Direction.LEFT_DOWN;
            case LEFT_DOWN:
                return Direction.LEFT_UP;
            case RIGHT_UP:
                return Direction.RIGHT_DOWN;
            case RIGHT_DOWN:
                return Direction.RIGHT_UP;
            default:
                return direction;
        }
    }

    private boolean isLeft (Direction direction) {
        return direction == Direction.LEFT || direction == Direction.LEFT_UP || direction == Direction.LEFT_DOWN;
    }

    private boolean isRight (Direction direction) {
        return direction == Direction.RIGHT || direction == Direction.RIGHT_UP || direction == Direction.RIGHT_DOWN;
    }

    private boolean isUp (Direction direction) {
        return direction == Direction.UP || direction == Direction.LEFT_UP || direction == Direction.RIGHT_UP;
    }

    private boolean isDown (Direction direction) {
        return direction == Direction.DOWN || direction == Direction.LEFT_DOWN || direction == Direction.RIGHT_DOWN;
    }

    private List<Integer> getNewEndpointCoordinates (int xCoordinate, int yCoordinate, int spriteWidth, int spriteHeight, Direction newDirection) {
        List<Integer> newCoords = new ArrayList<Integer>();
        switch (newDirection) {
            case LEFT:
                xCoordinate = 0 - spriteWidth;
                break;
            case RIGHT:
                xCoordinate = windowWidth;
                break;
            case UP:
                yCoordinate = 0 - spriteHeight;
                break;
            case DOWN:
                yCoordinate = playableWindowMaxHeight + (spriteHeight);
                break;
            case RIGHT_UP:
                xCoordinate = windowWidth;
                yCoordinate = 0 - spriteHeight;
                break;
            case RIGHT_DOWN:
                xCoordinate = windowWidth;
                yCoordinate = playableWindowMaxHeight + (spriteHeight);
                break;
            case LEFT_UP:
                xCoordinate = 0 - spriteWidth;
                yCoordinate = 0 - spriteHeight;
                break;
            case LEFT_DOWN:
                xCoordinate = 0 - spriteWidth;
                yCoordinate = playableWindowMaxHeight + (spriteHeight);
                break;
            default:
                break;
        }

        newCoords.add(xCoordinate);
        newCoords.add(yCoordinate);
        return newCoords;
    }



    public void increaseBounce(){
        this.currentBounces++;
    }

    public boolean isAllowedToBounce(){
        return this.currentBounces <= maxBounces;
    }

    public int getMaxBounces () {
        return maxBounces;
    }

    public void setMaxBounces (int maxBounces) {
        this.maxBounces = maxBounces;
    }
}