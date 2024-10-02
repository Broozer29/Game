package game.gameobjects.missiles.laserbeams;

import game.gameobjects.GameObject;
import game.movement.Direction;
import game.movement.Point;

public class LaserbeamConfiguration {

    private int xOffset;
    private int yOffset;

    private Point originPoint;
    private Point aimingPoint;
    private GameObject originObject;
    private GameObject targetToAimAt;
    private Direction direction;

    private GameObject owner;
    private boolean blocksMovement;
    private float damage;
    private int amountOfLaserbeamSegments;
    private double angleDegrees;

    public LaserbeamConfiguration(boolean blocksMovement, float damage){
        this.blocksMovement = blocksMovement;
        this.damage = damage;
    }

    public int getxOffset () {
        return xOffset;
    }

    public void setxOffset (int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset () {
        return yOffset;
    }

    public void setyOffset (int yOffset) {
        this.yOffset = yOffset;
    }

    public Point getOriginPoint () {
        return originPoint;
    }

    public void setOriginPoint (Point originPoint) {
        this.originPoint = originPoint;
    }

    public GameObject getOriginObject () {
        return originObject;
    }

    public void setOriginObject (GameObject originObject) {
        this.originObject = originObject;
    }

    public Direction getDirection () {
        return direction;
    }

    public GameObject getOwner () {
        return owner;
    }

    public void setOwner (GameObject owner) {
        this.owner = owner;
    }

    public void setDirection (Direction direction) {
        this.direction = direction;
    }

    public boolean isBlocksMovement () {
        return blocksMovement;
    }

    public void setBlocksMovement (boolean blocksMovement) {
        this.blocksMovement = blocksMovement;
    }

    public float getDamage () {
        return damage;
    }

    public void setDamage (float damage) {
        this.damage = damage;
    }

    public int getAmountOfLaserbeamSegments () {
        return amountOfLaserbeamSegments;
    }

    public void setAmountOfLaserbeamSegments (int amountOfLaserbeamSegments) {
        this.amountOfLaserbeamSegments = amountOfLaserbeamSegments;
    }

    public GameObject getTargetToAimAt () {
        return targetToAimAt;
    }

    public void setTargetToAimAt (GameObject targetToAimAt) {
        this.targetToAimAt = targetToAimAt;
    }

    public Point getAimingPoint () {
        return aimingPoint;
    }

    public void setAimingPoint (Point aimingPoint) {
        this.aimingPoint = aimingPoint;
    }

    public double getAngleDegrees () {
        return angleDegrees;
    }

    public void setAngleDegrees (double angleDegrees) {
        this.angleDegrees = angleDegrees;
    }
}
