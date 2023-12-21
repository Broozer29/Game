package game.objects.friendlies.powerups;

import game.managers.OnScreenTextManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Path;
import game.movement.Point;
import game.movement.SpriteMover;
import game.movement.pathfinders.BouncingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class PowerUp extends GameObject {

	//Needs to extend gameobject, currently not properly doing so but in the middle of refactor so cant focus on it
	private PowerUpEnums powerUpType;
	private int timeBeforeActivation;
	private boolean loopable;
	private PowerUpEffect powerUpEffect;
	private MovementConfiguration moveConfig;

	public PowerUp(SpriteConfiguration spriteConfiguration, PowerUpConfiguration powerUpConfiguration) {
		super(spriteConfiguration);
		this.powerUpType = powerUpConfiguration.getPowerUpType();
		this.timeBeforeActivation = powerUpConfiguration.getTimeBeforeActivation();
		this.loopable = powerUpConfiguration.isLoopable();
		initMoveConfig(powerUpConfiguration);
		this.setObjectType("Power Up " + powerUpType);
	}
	
	private void initMoveConfig(PowerUpConfiguration powerUpConfiguration) {
		moveConfig = new MovementConfiguration();
		moveConfig.setXMovementSpeed(powerUpConfiguration.getxMovementSpeed());
		moveConfig.setYMovementSpeed(powerUpConfiguration.getyMovementSpeed());
		moveConfig.setRotation(powerUpConfiguration.getMovementDirection());
		moveConfig.setPathFinder(powerUpConfiguration.getPathFinder());
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