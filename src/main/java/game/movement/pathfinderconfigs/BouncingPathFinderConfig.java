package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.gameobjects.GameObject;
import game.gameobjects.powerups.PowerUp;

public class BouncingPathFinderConfig implements PathFinderConfig {

    private PowerUp powerUp;
    private int xCoordinate;
    private int yCoordinate;
    private int spriteWidth;
    private int spriteHeight;
    private int xMovementSpeed;
    private int yMovementSpeed;
    private Direction spriteCurrentDirection;
    private GameObject bouncingSprite;

    public BouncingPathFinderConfig (GameObject bouncingSprite, MovementConfiguration movementConfiguration) {
        this.xCoordinate = bouncingSprite.getXCoordinate();
        this.yCoordinate = bouncingSprite.getYCoordinate();
        this.spriteWidth = bouncingSprite.getWidth();
        this.spriteHeight = bouncingSprite.getHeight();
        this.spriteCurrentDirection = movementConfiguration.getRotation();
        this.bouncingSprite = bouncingSprite;
        this.xMovementSpeed = movementConfiguration.getXMovementSpeed();
        this.yMovementSpeed = movementConfiguration.getYMovementSpeed();

    }

    public PowerUp getPowerUp () {
        return powerUp;
    }

    public void setPowerUp (PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public int getXCoordinate () {
        return xCoordinate;
    }

    public void setXCoordinate (int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate () {
        return yCoordinate;
    }

    public void setYCoordinate (int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getSpriteWidth () {
        return spriteWidth;
    }

    public void setSpriteWidth (int spriteWidth) {
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteHeight () {
        return spriteHeight;
    }

    public void setSpriteHeight (int spriteHeight) {
        this.spriteHeight = spriteHeight;
    }

    public Direction getSpriteDirection () {
        return spriteCurrentDirection;
    }

    public void setSpriteDirection (Direction spriteDirection) {
        this.spriteCurrentDirection = spriteDirection;
    }

    public Point getCurrentLocation () {
        return bouncingSprite.getCurrentLocation();
    }

    public void setCurrentLocation (Point currentLocation) {
        bouncingSprite.setX(currentLocation.getX());
        bouncingSprite.setY(currentLocation.getY());
    }

    public GameObject getSprite () {
        return bouncingSprite;
    }

    @Override
    public void setStart (Point start) {
        // TODO Auto-generated method stub

    }

    @Override
    public Point getStart () {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Direction getMovementDirection () {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMovementDirection (Direction movementDirection) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isFriendly () {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setFriendly (boolean isFriendly) {
        // TODO Auto-generated method stub

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
}
