package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.Point;
import game.objects.GameObject;
import game.objects.powerups.PowerUp;

public class BouncingPathFinderConfig implements PathFinderConfig {

	private PowerUp powerUp;
	private int xCoordinate;
	private int yCoordinate;
	private int spriteWidth;
	private int spriteHeight;
	private Direction spriteCurrentDirection;
	private GameObject bouncingSprite;

	public BouncingPathFinderConfig(GameObject bouncingSprite, Direction currentDirection) {
		this.xCoordinate = bouncingSprite.getXCoordinate();
		this.yCoordinate = bouncingSprite.getYCoordinate();
		this.spriteWidth = bouncingSprite.getWidth();
		this.spriteHeight = bouncingSprite.getHeight();
		this.spriteCurrentDirection = currentDirection;
		this.bouncingSprite = bouncingSprite;
	}

	public PowerUp getPowerUp() {
		return powerUp;
	}

	public void setPowerUp(PowerUp powerUp) {
		this.powerUp = powerUp;
	}

	public int getXCoordinate() {
		return xCoordinate;
	}

	public void setXCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getYCoordinate() {
		return yCoordinate;
	}

	public void setYCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public int getSpriteWidth() {
		return spriteWidth;
	}

	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}

	public int getSpriteHeight() {
		return spriteHeight;
	}

	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}

	public Direction getSpriteDirection() {
		return spriteCurrentDirection;
	}

	public void setSpriteDirection(Direction spriteDirection) {
		this.spriteCurrentDirection = spriteDirection;
	}

	public Point getCurrentLocation() {
		return bouncingSprite.getCurrentLocation();
	}

	public void setCurrentLocation(Point currentLocation) {
		bouncingSprite.setX(currentLocation.getX());
		bouncingSprite.setY(currentLocation.getY());
	}
	
	public GameObject getSprite() {
		return bouncingSprite;
	}

	@Override
	public void setStart(Point start) {
		// TODO Auto-generated method stub

	}

	@Override
	public Point getStart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Direction getFallbackDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFallbackDirection(Direction fallbackDirection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isFriendly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFriendly(boolean isFriendly) {
		// TODO Auto-generated method stub

	}

}
