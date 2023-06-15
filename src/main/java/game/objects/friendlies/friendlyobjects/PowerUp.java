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
import image.objects.Sprite;

public class PowerUp extends Sprite{

	private Direction direction;
	private Point currentLocation;
	private Point destination;
	private Path currentPath;
	private PowerUps powerUpType;
	
	public PowerUp(int x, int y, float scale, Direction direction, PowerUps powerUpType) {
		super(x, y, scale);
		this.direction = direction;
		this.setPowerUpType(powerUpType);
		this.loadImage(ImageEnums.Test_Image);
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
	
	public void activatePowerUpTimer() {
		PowerUpTimer powerUpTimer = new PowerUpTimer(10, this, false);
		TimerManager timerManager = TimerManager.getInstance();
		timerManager.addPowerUpTimerToList(powerUpTimer);
	}
	
	public void activatePowerEffect() {
		PowerUpEffect powerUpEffect = new PowerUpEffect(powerUpType);
		powerUpEffect.activatePowerEffect();
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
