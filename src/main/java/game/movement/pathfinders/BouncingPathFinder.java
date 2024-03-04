package game.movement.pathfinders;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.BouncingPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import VisualAndAudioData.DataClass;
import game.objects.GameObject;

import javax.xml.crypto.Data;

public class BouncingPathFinder implements PathFinder {

    private final int windowWidth;
    private final int playableWindowMinHeight;
    private final int playableWindowMaxHeight;

    public BouncingPathFinder () {
        this.windowWidth = DataClass.getInstance().getWindowWidth();
        this.playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        this.playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();

    }

    @Override
    public Path findPath (PathFinderConfig pathFinderConfig) {
        if (!(pathFinderConfig instanceof BouncingPathFinderConfig)) {
            throw new IllegalArgumentException("Expected BouncingPathFinderConfig");
        } else {

            int xCoordinate = ((BouncingPathFinderConfig) pathFinderConfig).getXCoordinate();
            int yCoordinate = ((BouncingPathFinderConfig) pathFinderConfig).getYCoordinate();
            int spriteWidth = ((BouncingPathFinderConfig) pathFinderConfig).getSpriteWidth();
            int spriteHeight = ((BouncingPathFinderConfig) pathFinderConfig).getSpriteHeight();
            Direction spriteRotation = ((BouncingPathFinderConfig) pathFinderConfig).getSpriteDirection();

            Direction newDirection = getNewDirection(((BouncingPathFinderConfig) pathFinderConfig).getSprite(), spriteRotation);
            List<Integer> endCoordinates = getNewEndpointCoordinates(xCoordinate, yCoordinate, spriteWidth, spriteHeight, newDirection);


            Point newEndpoint = new Point(endCoordinates.get(0), endCoordinates.get(1));
            RegularPathFinder regPathFinder = new RegularPathFinder();
            boolean isFriendly = true;

            RegularPathFinderConfig config = new RegularPathFinderConfig(((BouncingPathFinderConfig) pathFinderConfig).getCurrentLocation(), newEndpoint, ((BouncingPathFinderConfig) pathFinderConfig).getxMovementSpeed(),
                    ((BouncingPathFinderConfig) pathFinderConfig).getyMovementSpeed(), isFriendly, newDirection);
            Path newPath = regPathFinder.findPath(config);
            return newPath;
        }
    }

    @Override
    public Direction getNextStep (Point currentLocation, Path path, Direction fallbackDirection) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean shouldRecalculatePath (Path path) {
        // TODO Auto-generated method stub
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
                || (isDown(spriteDirection) && (yCoordinate + spriteHeight) >= playableWindowMinHeight);
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
                xCoordinate = 0;
                break;
            case RIGHT:
                xCoordinate = windowWidth;
                break;
            case UP:
                yCoordinate = 0;
                break;
            case DOWN:
                yCoordinate = playableWindowMinHeight - (spriteHeight);
                break;
            case RIGHT_UP:
                xCoordinate = windowWidth;
                yCoordinate = 0;
                break;
            case RIGHT_DOWN:
                xCoordinate = windowWidth;
                yCoordinate = playableWindowMinHeight - (spriteHeight);
                break;
            case LEFT_UP:
                xCoordinate = 0;
                yCoordinate = 0;
                break;
            case LEFT_DOWN:
                xCoordinate = 0;
                yCoordinate = playableWindowMinHeight - (spriteHeight);
                break;
            default:
                break;
        }

        newCoords.add(xCoordinate);
        newCoords.add(yCoordinate);
        return newCoords;
    }

}