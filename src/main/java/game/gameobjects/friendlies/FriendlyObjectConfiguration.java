package game.gameobjects.friendlies;

import game.movement.Direction;
import game.movement.MovementPatternSize;
import game.movement.pathfinders.PathFinder;

public class FriendlyObjectConfiguration {

    private FriendlyObjectEnums friendlyType;
    private float attackSpeedCooldown;
    private PathFinder pathFinder;
    private Direction movementDirection;
    private int xMovementSpeed;
    private int yMovementSpeed;

    private MovementPatternSize movementPatternSize;

    private boolean boxCollision;
    private boolean permanentObject;

    public FriendlyObjectConfiguration (FriendlyObjectEnums friendlyType, float attackSpeedCooldown, PathFinder pathFinder, Direction movementDirection, int xMovementSpeed, int yMovementSpeed, MovementPatternSize movementPatternSize
    , boolean boxCollision, boolean permanentObject) {
        this.friendlyType = friendlyType;
        this.attackSpeedCooldown = attackSpeedCooldown;
        this.pathFinder = pathFinder;
        this.movementDirection = movementDirection;
        this.xMovementSpeed = xMovementSpeed;
        this.yMovementSpeed = yMovementSpeed;
        this.movementPatternSize = movementPatternSize;
        this.boxCollision = boxCollision;
        this.permanentObject = permanentObject;
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

    public float getAttackSpeedCooldown () {
        return attackSpeedCooldown;
    }

    public void setAttackSpeedCooldown (float attackSpeedCooldown) {
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

    public boolean isPermanentObject () {
        return permanentObject;
    }

    public void setPermanentObject (boolean permanentObject) {
        this.permanentObject = permanentObject;
    }
}
