package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.objects.GameObject;

public class SpiralPathFinderConfig implements PathFinderConfig {


    private Point start;
    private Direction fallbackDirection;
    private boolean isFriendly;
    private int curveDistance;
    private double angleStep;
    private double radius;
    private double radiusIncrement;

    public SpiralPathFinderConfig (GameObject gameObject, MovementConfiguration movementConfiguration) {
        this.start = new Point(gameObject.getXCoordinate(), gameObject.getYCoordinate());
        this.fallbackDirection = movementConfiguration.getRotation();
        this.isFriendly = gameObject.isFriendly();
        this.curveDistance = movementConfiguration.getCurveDistance();
        this.angleStep = movementConfiguration.getAngleStep();
        this.radius = movementConfiguration.getSpiralRadius();
        this.radiusIncrement = movementConfiguration.getRadiusIncrement();
    }

    @Override
    public Point getStart () {
        return start;
    }

    @Override
    public void setStart (Point start) {
        this.start = start;
    }

    @Override
    public Direction getMovementDirection () {
        return fallbackDirection;
    }

    @Override
    public void setMovementDirection (Direction movementDirection) {
        this.fallbackDirection = movementDirection;
    }

    @Override
    public boolean isFriendly () {
        return isFriendly;
    }

    @Override
    public void setFriendly (boolean friendly) {
        isFriendly = friendly;
    }

    public int getCurveDistance () {
        return curveDistance;
    }

    public void setCurveDistance (int curveDistance) {
        this.curveDistance = curveDistance;
    }

    public double getAngleStep () {
        return angleStep;
    }

    public void setAngleStep (double angleStep) {
        this.angleStep = angleStep;
    }

    public double getRadius () {
        return radius;
    }

    public void setRadius (double radius) {
        this.radius = radius;
    }

    public double getRadiusIncrement () {
        return radiusIncrement;
    }

    public void setRadiusIncrement (double radiusIncrement) {
        this.radiusIncrement = radiusIncrement;
    }
}
