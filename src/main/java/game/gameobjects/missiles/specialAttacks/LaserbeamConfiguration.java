package game.gameobjects.missiles.specialAttacks;

import game.gameobjects.GameObject;
import game.movement.Direction;
import game.movement.Point;

public class LaserbeamConfiguration {

    private int xOffset;
    private int yOffset;

    private Point originPoint;
    private GameObject originObject;
    private Direction direction;

    private GameObject owner;
    public LaserbeamConfiguration (int xOffset, int yOffset, Point originPoint, Direction direction, GameObject owner) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.originPoint = originPoint;
        this.direction = direction;
        this.owner = owner;
    }

    public LaserbeamConfiguration (int xOffset, int yOffset, GameObject originObject, Direction direction, GameObject owner) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.originObject = originObject;
        this.direction = direction;
        this.owner = owner;
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
}
