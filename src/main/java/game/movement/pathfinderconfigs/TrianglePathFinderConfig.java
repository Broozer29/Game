package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.objects.GameObject;

public class TrianglePathFinderConfig implements PathFinderConfig{
    private Point start;
    private int xMovementSpeed;
    private int yMovementSpeed;
    private boolean isFriendly;
    private Direction movementDirection;

    private int primaryDirectionStepAmount;
    private int firstDiagonalDirectionStepAmount;
    private int secondDiagonalDirectionStepAmount;


    public TrianglePathFinderConfig (GameObject gameObject, MovementConfiguration movementConfiguration) {
        start = gameObject.getCurrentLocation();
        xMovementSpeed = movementConfiguration.getXMovementSpeed();
        yMovementSpeed = movementConfiguration.getYMovementSpeed();
        isFriendly = gameObject.isFriendly();
        movementDirection = movementConfiguration.getRotation();
        primaryDirectionStepAmount = movementConfiguration.getPrimaryDirectionStepAmount();
        firstDiagonalDirectionStepAmount = movementConfiguration.getFirstDiagonalDirectionStepAmount();
        secondDiagonalDirectionStepAmount = movementConfiguration.getSecondDiagonalDirectionStepAmount();

    }

    public int getPrimaryDirectionStepAmount () {
        return primaryDirectionStepAmount;
    }

    public void setPrimaryDirectionStepAmount (int primaryDirectionStepAmount) {
        this.primaryDirectionStepAmount = primaryDirectionStepAmount;
    }

    public int getFirstDiagonalDirectionStepAmount () {
        return firstDiagonalDirectionStepAmount;
    }

    public void setFirstDiagonalDirectionStepAmount (int firstDiagonalDirectionStepAmount) {
        this.firstDiagonalDirectionStepAmount = firstDiagonalDirectionStepAmount;
    }

    public int getSecondDiagonalDirectionStepAmount () {
        return secondDiagonalDirectionStepAmount;
    }

    public void setSecondDiagonalDirectionStepAmount (int secondDiagonalDirectionStepAmount) {
        this.secondDiagonalDirectionStepAmount = secondDiagonalDirectionStepAmount;
    }

    @Override
    public Point getStart () {
        return start;
    }

    @Override
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

    @Override
    public boolean isFriendly () {
        return isFriendly;
    }

    @Override
    public void setFriendly (boolean friendly) {
        isFriendly = friendly;
    }

    @Override
    public Direction getMovementDirection () {
        return movementDirection;
    }

    @Override
    public void setMovementDirection (Direction movementDirection) {
        this.movementDirection = movementDirection;
    }
}
