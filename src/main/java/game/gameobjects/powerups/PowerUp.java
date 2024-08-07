package game.gameobjects.powerups;

import game.managers.OnScreenTextManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Path;
import game.movement.Point;
import game.movement.SpriteMover;
import game.gameobjects.GameObject;
import game.gameobjects.powerups.creation.PowerUpConfiguration;
import game.gameobjects.powerups.creation.PowerUpEffect;
import game.gameobjects.powerups.creation.PowerUpEffectFactory;
import game.util.OnScreenText;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class PowerUp extends GameObject {

    //Needs to extend gameobject, currently not properly doing so but in the middle of refactor so cant focus on it
    private PowerUpEnums powerUpType;
    private int timeBeforeActivation;
    private boolean loopable;
    private PowerUpEffect powerUpEffect;

    public PowerUp (SpriteConfiguration spriteConfiguration, PowerUpConfiguration powerUpConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, movementConfiguration);
        this.powerUpType = powerUpConfiguration.getPowerUpType();
        this.timeBeforeActivation = powerUpConfiguration.getTimeBeforeActivation();
        this.loopable = powerUpConfiguration.isLoopable();
        this.powerUpEffect =  PowerUpEffectFactory.getInstance().createPowerUpEffect(powerUpType);
        this.setObjectType("Power Up " + powerUpType);
        this.allowedVisualsToRotate = false;

        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
    }


    public void move () {
        SpriteMover.getInstance().moveGameObject(this, movementConfiguration);
        bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
        updateBoardBlock();
    }

    // Starts the timer of the powerUp. This method is called when the player collides with
    // the powerup
    public void startPowerUpTimer () {
        //DEPRECATED OLD TIMER SYSTEM
        //Use effects instead
//        PowerUpEffectTimer powerUpTimer = new PowerUpEffectTimer(timeBeforeActivation, this, loopable);
//        TimerManager.getInstance().addTimer(powerUpTimer);
//        powerUpTimer.startOfTimer(); // Start the timer, which will handle activating the power-up effect


        OnScreenText text = new OnScreenText(xCoordinate, yCoordinate, powerUpType);
        OnScreenTextManager textManager = OnScreenTextManager.getInstance();
        textManager.addTextObject(text);

    }

    //Gets called at the end of the powerUp timer
    public void activateEndOfTimerEffect () {
        powerUpEffect.deactivatePower();
    }

    public Direction getDirection () {
        return movementConfiguration.getRotation();
    }


    public Point getDestination () {
        return movementConfiguration.getDestination();
    }

    public void setDestination (Point destination) {
        this.movementConfiguration.setDestination(destination);
    }

    public Path getCurrentPath () {
        return movementConfiguration.getCurrentPath();
    }

    public void setCurrentPath (Path currentPath) {
        this.movementConfiguration.setCurrentPath(currentPath);
    }

    public void setDirection (Direction direction) {
        this.movementConfiguration.setRotation(direction);
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