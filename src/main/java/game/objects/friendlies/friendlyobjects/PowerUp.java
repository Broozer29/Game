package game.objects.friendlies.friendlyobjects;

import data.image.enums.ImageEnums;
import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.PowerUpDirectionBouncer;
import game.objects.friendlies.PowerUpEffect;
import game.objects.friendlies.PowerUps;
import game.spawner.PowerUpTimer;
import visual.objects.Sprite;

public class PowerUp extends Sprite {

	private Direction direction;
	private Point currentLocation;
	private Point destination;
	private Path currentPath;
	private PowerUps powerUpType;

	private int timeBeforeActivation;
	private boolean loopable;
	private PowerUpEffect powerUpEffect;

	public PowerUp(int x, int y, float scale, Direction direction, PowerUps powerUpType, int timeBeforeActivation,
			boolean loopable) {
		super(x, y, scale);
		this.direction = direction;
		this.setPowerUpType(powerUpType);
		this.timeBeforeActivation = timeBeforeActivation;
		this.loopable = loopable;
		this.loadImage(ImageEnums.MultiShotIcon);
	}

	public void move() {
		if (currentPath == null || currentPath.getWaypoints().isEmpty()) {
			// calculate a new path if necessary
			PowerUpDirectionBouncer bouncer = new PowerUpDirectionBouncer();
			currentPath = bouncer.calculateNewPath(this);
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

		// Check for collision with walls and bounce if necessary
		PowerUpDirectionBouncer bouncer = new PowerUpDirectionBouncer();
		Direction newDirection = bouncer.getNewDirection(this);
		if (newDirection != direction) {
			direction = newDirection;
			currentPath = bouncer.calculateNewPath(this);
		}
//	    System.out.println(newDirection);

	}

	// Starts the timer of the powerUp. This method is called when the player collides with
	// the powerup
	public void startPowerUpTimer() {
		PowerUpTimer powerUpTimer = new PowerUpTimer(timeBeforeActivation, this, loopable);
		TimerManager timerManager = TimerManager.getInstance();
		this.powerUpEffect = new PowerUpEffect(powerUpType);
		this.powerUpEffect.activateImmediatePowerEffect();
		timerManager.addPowerUpTimerToList(powerUpTimer);
	}

	//Gets called at the end of the powerUp timer
	public void activateEndOfTimerEffect() {
		powerUpEffect.activateEndOfPowerEffect();
	}

	public Direction getDirection() {
		return direction;
	}

	public Point getCurrentLocation() {
		return currentLocation;
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

	public PowerUps getPowerUpType() {
		return powerUpType;
	}

	public void setPowerUpType(PowerUps powerUpType) {
		this.powerUpType = powerUpType;
	}

}
