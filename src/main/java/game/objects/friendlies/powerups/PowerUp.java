package game.objects.friendlies.powerups;

import game.managers.OnScreenTextManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Path;
import game.movement.Point;
import game.movement.PowerUpDirectionBouncer;
import game.movement.SpriteMover;
import game.movement.pathfinderconfigs.BouncingPathFinderConfig;
import game.movement.pathfinders.BouncingPathFinder;
import game.movement.pathfinders.PathFinder;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;

public class PowerUp extends Sprite {

	private PowerUpEnums powerUpType;
	private int timeBeforeActivation;
	private boolean loopable;
	private PowerUpEffect powerUpEffect;
	private MovementConfiguration moveConfig;

	public PowerUp(int x, int y, float scale, Direction direction, PowerUpEnums powerUpType, ImageEnums powerUpImage, int timeBeforeActivation,
			boolean loopable) {
		super(x, y, scale);
		this.setPowerUpType(powerUpType);
		this.timeBeforeActivation = timeBeforeActivation;
		this.loopable = loopable;
		this.loadImage(powerUpImage);
		initMoveConfig(direction);
	}
	
	private void initMoveConfig(Direction direction) {
		moveConfig = new MovementConfiguration();
		moveConfig.setXMovementSpeed(2);
		moveConfig.setYMovementSpeed(2);
		moveConfig.setRotation(direction);
		PathFinder pathFinder = new BouncingPathFinder();
		moveConfig.setPathFinder(pathFinder);
	}

	
	//Uses a bouncer which makes it incompatible with spritemover?
	public void move() {
		
		SpriteMover.getInstance().moveSprite(this, moveConfig);
		
		bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
		updateCurrentBoardBlock();


	}

	// Starts the timer of the powerUp. This method is called when the player collides with
	// the powerup
	public void startPowerUpTimer() {
		PowerUpTimer powerUpTimer = new PowerUpTimer(timeBeforeActivation, this, loopable);
		TimerManager timerManager = TimerManager.getInstance();
		PowerUpEffectFactory powerUpFactory = PowerUpEffectFactory.getInstance();
		this.powerUpEffect = powerUpFactory.createPowerUpEffect(powerUpType);
		this.powerUpEffect.activatePower();
		OnScreenText text = new OnScreenText(xCoordinate, yCoordinate, powerUpType);
		OnScreenTextManager textManager = OnScreenTextManager.getInstance();
		textManager.addPowerUpText(text);
		
		timerManager.addPowerUpTimerToList(powerUpTimer);
	}

	//Gets called at the end of the powerUp timer
	public void activateEndOfTimerEffect() {
		powerUpEffect.deactivatePower();
	}

	public Direction getDirection() {
		return moveConfig.getRotation();
	}

	public void setCurrentLocation(Point currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Point getDestination() {
		return moveConfig.getDestination();
	}

	public void setDestination(Point destination) {
		this.moveConfig.setDestination(destination);
	}

	public Path getCurrentPath() {
		return moveConfig.getCurrentPath();
	}

	public void setCurrentPath(Path currentPath) {
		this.moveConfig.setCurrentPath(currentPath);
	}

	public void setDirection(Direction direction) {
		this.moveConfig.setRotation(direction);
	}

	public PowerUpEnums getPowerUpType() {
		return powerUpType;
	}

	public void setPowerUpType(PowerUpEnums powerUpType) {
		this.powerUpType = powerUpType;
	}

}