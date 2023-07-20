package game.objects.friendlies.powerups;

import game.managers.OnScreenTextManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.PowerUpDirectionBouncer;
import gamedata.image.ImageEnums;
import gamedata.image.ImageResizer;
import visual.objects.Sprite;

public class PowerUp extends Sprite {

	private Direction direction;

	private Point destination;
	private Path currentPath;
	private PowerUpEnums powerUpType;

	private int timeBeforeActivation;
	private boolean loopable;
	private PowerUpEffect powerUpEffect;

	public PowerUp(int x, int y, float scale, Direction direction, PowerUpEnums powerUpType, ImageEnums powerUpImage, int timeBeforeActivation,
			boolean loopable) {
		super(x, y, scale);
		this.direction = direction;
		this.setPowerUpType(powerUpType);
		this.timeBeforeActivation = timeBeforeActivation;
		this.loopable = loopable;
		this.loadImage(powerUpImage);
	}

	public void move() {
		if (currentPath == null || currentPath.getWaypoints().isEmpty()) {
			// calculate a new path if necessary
			currentPath = PowerUpDirectionBouncer.getInstance().calculateNewPath(this);
		}

		// Update current location on the path
		currentPath.updateCurrentLocation(currentLocation);

		// Get the next point from the path
		Point nextPoint = currentPath.getWaypoints().get(0);

		// Move towards the next point
		currentLocation = nextPoint;
		this.xCoordinate = nextPoint.getX();
		this.yCoordinate = nextPoint.getY();

		// If reached the next point, remove it from the path
		if (currentLocation.equals(nextPoint)) {
			currentPath.getWaypoints().remove(0);
		}
		
		bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
		updateCurrentBoardBlock();
		// Check for collision with walls and bounce if necessary
		Direction newDirection = PowerUpDirectionBouncer.getInstance().getNewDirection(this);
		if (newDirection != direction) {
			direction = newDirection;
			currentPath = PowerUpDirectionBouncer.getInstance().calculateNewPath(this);
		}

	}

	// Starts the timer of the powerUp. This method is called when the player collides with
	// the powerup
	public void startPowerUpTimer() {
		PowerUpTimer powerUpTimer = new PowerUpTimer(timeBeforeActivation, this, loopable);
		TimerManager timerManager = TimerManager.getInstance();
		PowerUpEffectFactory powerUpFactory = PowerUpEffectFactory.getInstance();
		this.powerUpEffect = powerUpFactory.createPowerUpEffect(powerUpType);
		this.powerUpEffect.activatePower();
		PowerUpAcquiredText text = new PowerUpAcquiredText(xCoordinate, yCoordinate, powerUpType);
		OnScreenTextManager textManager = OnScreenTextManager.getInstance();
		textManager.addPowerUpText(text);
		
		timerManager.addPowerUpTimerToList(powerUpTimer);
	}

	//Gets called at the end of the powerUp timer
	public void activateEndOfTimerEffect() {
		powerUpEffect.deactivatePower();
	}

	public Direction getDirection() {
		return direction;
	}

	public void setCurrentLocation(Point currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Point getDestination() {
		return destination;
	}

	public void setDestination(Point destination) {
		this.destination = destination;
	}

	public Path getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(Path currentPath) {
		this.currentPath = currentPath;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public PowerUpEnums getPowerUpType() {
		return powerUpType;
	}

	public void setPowerUpType(PowerUpEnums powerUpType) {
		this.powerUpType = powerUpType;
	}

	@Override
	public String toString() {
		return "PowerUp [direction=" + direction + ", currentLocation=" + currentLocation + ", destination="
				+ destination + ", currentPath=" + currentPath + ", powerUpType=" + powerUpType
				+ ", timeBeforeActivation=" + timeBeforeActivation + ", loopable=" + loopable + ", powerUpEffect="
				+ powerUpEffect + "]";
	}

}