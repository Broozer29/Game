package game.movement.deprecatedpathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.gameobjects.GameObject;

public class ZigZagPathFinderConfig implements PathFinderConfig {

    private Direction direction;
    private int xMovementSpeed;
    private int yMovementSpeed;
    private int stepsBeforeBounceToOtherDirection;

    private Point startPoint;

    public ZigZagPathFinderConfig (GameObject gameObject, MovementConfiguration movementConfiguration) {
        this.direction = movementConfiguration.getRotation();
        this.xMovementSpeed = movementConfiguration.getXMovementSpeed();
        this.yMovementSpeed = movementConfiguration.getYMovementSpeed();
        this.stepsBeforeBounceToOtherDirection = movementConfiguration.getStepsBeforeBounceInOtherDirection();
        this.startPoint = gameObject.getCurrentLocation();
    }

    public Direction getDirection () {
        return direction;
    }

    public void setDirection (Direction direction) {
        this.direction = direction;
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

    public int getStepsBeforeBounceToOtherDirection () {
        return stepsBeforeBounceToOtherDirection;
    }

    public void setStepsBeforeBounceToOtherDirection (int stepsBeforeBounceToOtherDirection) {
        this.stepsBeforeBounceToOtherDirection = stepsBeforeBounceToOtherDirection;
    }

    @Override
    public void setStart (Point start) {
    this.startPoint = start;
    }

    @Override
    public Point getStart () {
        return this.startPoint;
    }

    @Override
    public Direction getMovementDirection () {
        return null;
    }

    @Override
    public void setMovementDirection (Direction movementDirection) {

    }

    @Override
    public boolean isFriendly () {
        return false;
    }

    @Override
    public void setFriendly (boolean isFriendly) {

    }
}
