package game.objects.friendlies;

import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.PathFinder;

public class FriendlyObjectConfiguration {

    private FriendlyObjectEnums friendlyType;
    private int attackSpeedCooldown;
    private PathFinder pathFinder;
    private Direction movementDirection;
    private int xMovementSpeed;
    private int yMovementSpeed;

    private MovementPatternSize movementPatternSize;

    private boolean boxCollision;

    public FriendlyObjectConfiguration (FriendlyObjectEnums friendlyType, int attackSpeedCooldown, PathFinder pathFinder, Direction movementDirection, int xMovementSpeed, int yMovementSpeed, MovementPatternSize movementPatternSize
    , boolean boxCollision) {
        this.friendlyType = friendlyType;
        this.attackSpeedCooldown = attackSpeedCooldown;
        this.pathFinder = pathFinder;
        this.movementDirection = movementDirection;
        this.xMovementSpeed = xMovementSpeed;
        this.yMovementSpeed = yMovementSpeed;
        this.movementPatternSize = movementPatternSize;
        this.boxCollision = boxCollision;
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

//    public FriendlyObjectConfiguration(){
//    }

    public FriendlyObjectEnums getFriendlyType () {
        return friendlyType;
    }

    public void setFriendlyType (FriendlyObjectEnums friendlyType) {
        this.friendlyType = friendlyType;
    }

    public int getAttackSpeedCooldown () {
        return attackSpeedCooldown;
    }

    public void setAttackSpeedCooldown (int attackSpeedCooldown) {
        this.attackSpeedCooldown = attackSpeedCooldown;
    }

    public MovementPatternSize getMovementPatternSize () {
        return movementPatternSize;
    }

    public void setMovementPatternSize (MovementPatternSize movementPatternSize) {
        this.movementPatternSize = movementPatternSize;
    }

    public boolean isBoxCollision () {
        return boxCollision;
    }

    public void setBoxCollision (boolean boxCollision) {
        this.boxCollision = boxCollision;
    }
}
