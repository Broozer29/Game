package game.objects.friendlies.Drones;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.pathfinders.PathFinder;

public class DroneConfiguration {
    private DroneEnums droneType;

    private int attackSpeedCooldown;

    private PathFinder pathFinder;

    private Direction movementDirection;
    private int xMovementSpeed;
    private int yMovementSpeed;


    public DroneConfiguration (DroneEnums droneType, int attackSpeedCooldown, PathFinder pathFinder, Direction movementDirection, int xMovementSpeed, int yMovementSpeed) {
        this.droneType = droneType;
        this.attackSpeedCooldown = attackSpeedCooldown;
        this.pathFinder = pathFinder;
        this.movementDirection = movementDirection;
        this.xMovementSpeed = xMovementSpeed;
        this.yMovementSpeed = yMovementSpeed;
    }

    public PathFinder getPathFinder () {
        return pathFinder;
    }

    public void setPathFinder (PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public Direction getMovementDirection () {
        return movementDirection;
    }

    public void setMovementDirection (Direction movementDirection) {
        this.movementDirection = movementDirection;
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

    public DroneEnums getDroneType () {
        return droneType;
    }

    public void setDroneType (DroneEnums droneType) {
        this.droneType = droneType;
    }

    public int getAttackSpeedCooldown () {
        return attackSpeedCooldown;
    }

    public void setAttackSpeedCooldown (int attackSpeedCooldown) {
        this.attackSpeedCooldown = attackSpeedCooldown;
    }

}
