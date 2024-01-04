package game.objects.powerups.creation;

import game.movement.Direction;
import game.movement.pathfinders.BouncingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.powerups.PowerUpEnums;

public class PowerUpConfiguration {

    private PowerUpEnums powerUpType;
    private int timeBeforeActivation;

    private boolean loopable;

    private int xMovementSpeed;
    private int yMovementSpeed;
    private Direction movementDirection;
    private PathFinder pathFinder;

    public PowerUpConfiguration (PowerUpEnums powerUpType, int timeBeforeActivation, boolean loopable, int xMovementSpeed, int yMovementSpeed, Direction movementDirection, PathFinder pathFinder) {
        this.powerUpType = powerUpType;
        this.timeBeforeActivation = timeBeforeActivation;
        this.loopable = loopable;
        this.xMovementSpeed = xMovementSpeed;
        this.yMovementSpeed = yMovementSpeed;
        this.movementDirection = movementDirection;
        this.pathFinder = pathFinder;
    }

    public PowerUpEnums getPowerUpType () {
        return powerUpType;
    }

    public void setPowerUpType (PowerUpEnums powerUpType) {
        this.powerUpType = powerUpType;
    }

    public int getTimeBeforeActivation () {
        return timeBeforeActivation;
    }

    public void setTimeBeforeActivation (int timeBeforeActivation) {
        this.timeBeforeActivation = timeBeforeActivation;
    }

    public boolean isLoopable () {
        return loopable;
    }

    public void setLoopable (boolean loopable) {
        this.loopable = loopable;
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

    public Direction getMovementDirection () {
        return movementDirection;
    }

    public void setMovementDirection (Direction movementDirection) {
        this.movementDirection = movementDirection;
    }

    public PathFinder getPathFinder () {
        if(pathFinder == null){
            pathFinder = new BouncingPathFinder();
        }
        return pathFinder;
    }

    public void setPathFinder (PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }
}
