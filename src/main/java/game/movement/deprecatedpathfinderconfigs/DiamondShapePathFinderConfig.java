package game.movement.deprecatedpathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.gameobjects.GameObject;

public class DiamondShapePathFinderConfig implements PathFinderConfig {
    private Point start;
    private int xMovementSpeed;
    private int yMovementSpeed;
    private boolean isFriendly;
    private Direction fallbackDirection;

    private int diamondWidth;
    private int diamondHeight;

    public DiamondShapePathFinderConfig (GameObject gameObject, MovementConfiguration movementConfiguration) {
        super();
        this.start = gameObject.getCurrentLocation();
        this.xMovementSpeed = movementConfiguration.getXMovementSpeed();
        this.yMovementSpeed = movementConfiguration.getYMovementSpeed();
        this.isFriendly = gameObject.isFriendly();
        this.fallbackDirection = movementConfiguration.getRotation();
        this.diamondWidth = movementConfiguration.getDiamondWidth();
        this.diamondHeight = movementConfiguration.getDiamondHeight();
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

    public int getDiamondWidth () {
        return diamondWidth;
    }

    public void setDiamondWidth (int diamondWidth) {
        this.diamondWidth = diamondWidth;
    }

    public int getDiamondHeight () {
        return diamondHeight;
    }

    public void setDiamondHeight (int diamondHeight) {
        this.diamondHeight = diamondHeight;
    }
}