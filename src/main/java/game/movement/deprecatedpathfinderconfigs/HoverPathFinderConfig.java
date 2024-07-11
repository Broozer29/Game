package game.movement.deprecatedpathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.gameobjects.GameObject;

public class HoverPathFinderConfig implements PathFinderConfig{


    private Point start;
    private Point end;
    private int width;
    private int height;
    private int boardBlockToHoverIn;
    private int xMovementSpeed;
    private int yMovementSpeed;
    private boolean isFriendly;
    private Direction fallbackDirection;

    public HoverPathFinderConfig (GameObject gameObject, MovementConfiguration movementConfiguration) {
        super();
        this.start = gameObject.getCurrentLocation();
        this.end = movementConfiguration.getDestination();
        this.xMovementSpeed = movementConfiguration.getXMovementSpeed();
        this.yMovementSpeed = movementConfiguration.getYMovementSpeed();
        this.isFriendly = gameObject.isFriendly();
        this.fallbackDirection = movementConfiguration.getRotation();
        this.boardBlockToHoverIn = movementConfiguration.getBoardBlockToHoverIn();


        if(gameObject.getAnimation() != null){
            this.width = gameObject.getAnimation().getWidth();
            this.height = gameObject.getAnimation().getHeight();
        } else {
            this.width = gameObject.getWidth();
            this.height = gameObject.getHeight();
        }

    }

    public HoverPathFinderConfig (Point start, Point end, int xMovementSpeed, int yMovementSpeed, boolean isFriendly,
                                         Direction fallbackDirection) {
        super();
        this.start = start;
        this.end = end;
        this.xMovementSpeed = xMovementSpeed;
        this.yMovementSpeed = yMovementSpeed;
        this.isFriendly = isFriendly;
        this.fallbackDirection = fallbackDirection;
    }



    public Point getStart () {
        return start;
    }

    public void setStart (Point start) {
        this.start = start;
    }

    public int getxMovementSpeed () {
        return xMovementSpeed;
    }

    public void setxMovementSpeed (int xMovementSpeed) {
        this.xMovementSpeed = xMovementSpeed;
    }

    public int getyMovementSpeed () {
        return yMovementSpeed;
    }

    public void setyMovementSpeed (int yMovementSpeed) {
        this.yMovementSpeed = yMovementSpeed;
    }

    public boolean isFriendly () {
        return isFriendly;
    }

    public void setFriendly (boolean isFriendly) {
        this.isFriendly = isFriendly;
    }

    public Direction getMovementDirection () {
        return fallbackDirection;
    }

    public void setMovementDirection (Direction movementDirection) {
        this.fallbackDirection = movementDirection;
    }

    public Point getEnd () {
        return end;
    }

    public void setEnd (Point end) {
        this.end = end;
    }

    public int getWidth () {
        return width;
    }

    public void setWidth (int width) {
        this.width = width;
    }

    public int getHeight () {
        return height;
    }

    public void setHeight (int height) {
        this.height = height;
    }

    public int getBoardBlockToHoverIn () {
        return boardBlockToHoverIn;
    }

    public void setBoardBlockToHoverIn (int boardBlockToHoverIn) {
        this.boardBlockToHoverIn = boardBlockToHoverIn;
    }

    public Direction getFallbackDirection () {
        return fallbackDirection;
    }

    public void setFallbackDirection (Direction fallbackDirection) {
        this.fallbackDirection = fallbackDirection;
    }
}
