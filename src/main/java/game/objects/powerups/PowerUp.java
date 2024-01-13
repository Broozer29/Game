package game.objects.powerups;

import game.managers.OnScreenTextManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Path;
import game.movement.Point;
import game.movement.SpriteMover;
import game.objects.GameObject;
import game.objects.powerups.creation.PowerUpConfiguration;
import game.objects.powerups.creation.PowerUpEffect;
import game.objects.powerups.creation.PowerUpEffectFactory;
import game.objects.powerups.timers.PowerUpEffectTimer;
import game.util.OnScreenText;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class PowerUp extends GameObject {

    //Needs to extend gameobject, currently not properly doing so but in the middle of refactor so cant focus on it
    private PowerUpEnums powerUpType;
    private int timeBeforeActivation;
    private boolean loopable;
    private PowerUpEffect powerUpEffect;
    private MovementConfiguration moveConfig;

    public PowerUp (SpriteConfiguration spriteConfiguration, PowerUpConfiguration powerUpConfiguration) {
        super(spriteConfiguration);
        this.powerUpType = powerUpConfiguration.getPowerUpType();
        this.timeBeforeActivation = powerUpConfiguration.getTimeBeforeActivation();
        this.loopable = powerUpConfiguration.isLoopable();
        initMoveConfig(powerUpConfiguration);
        this.powerUpEffect =  PowerUpEffectFactory.getInstance().createPowerUpEffect(powerUpType);
        this.setObjectType("Power Up " + powerUpType);
    }

    private void initMoveConfig (PowerUpConfiguration powerUpConfiguration) {
        moveConfig = new MovementConfiguration();
        moveConfig.setXMovementSpeed(powerUpConfiguration.getxMovementSpeed());
        moveConfig.setYMovementSpeed(powerUpConfiguration.getyMovementSpeed());
        moveConfig.setRotation(powerUpConfiguration.getMovementDirection());
        moveConfig.setPathFinder(powerUpConfiguration.getPathFinder());
    }

    public void move () {
        SpriteMover.getInstance().moveSprite(this, moveConfig);
        bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
        updateBoardBlock();
    }

    // Starts the timer of the powerUp. This method is called when the player collides with
    // the powerup
    public void startPowerUpTimer () {
        PowerUpEffectTimer powerUpTimer = new PowerUpEffectTimer(timeBeforeActivation, this, loopable);
        TimerManager.getInstance().addTimer(powerUpTimer);
        powerUpTimer.startOfTimer(); // Start the timer, which will handle activating the power-up effect


        OnScreenText text = new OnScreenText(xCoordinate, yCoordinate, powerUpType);
        OnScreenTextManager textManager = OnScreenTextManager.getInstance();
        textManager.addPowerUpText(text);

    }

    //Gets called at the end of the powerUp timer
    public void activateEndOfTimerEffect () {
        powerUpEffect.deactivatePower();
    }

    public Direction getDirection () {
        return moveConfig.getRotation();
    }

    public void setCurrentLocation (Point currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Point getDestination () {
        return moveConfig.getDestination();
    }

    public void setDestination (Point destination) {
        this.moveConfig.setDestination(destination);
    }

    public Path getCurrentPath () {
        return moveConfig.getCurrentPath();
    }

    public void setCurrentPath (Path currentPath) {
        this.moveConfig.setCurrentPath(currentPath);
    }

    public void setDirection (Direction direction) {
        this.moveConfig.setRotation(direction);
    }

    public PowerUpEnums getPowerUpType () {
        return powerUpType;
    }

    public void setPowerUpType (PowerUpEnums powerUpType) {
        this.powerUpType = powerUpType;
    }

    public PowerUpEffect getPowerUpEffect(){
        return this.powerUpEffect;
    }

}