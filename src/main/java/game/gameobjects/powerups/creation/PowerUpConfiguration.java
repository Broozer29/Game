package game.gameobjects.powerups.creation;

import game.gameobjects.powerups.PowerUpEnums;

public class PowerUpConfiguration {

    private PowerUpEnums powerUpType;
    private int timeBeforeActivation;

    private boolean loopable;

    public PowerUpConfiguration (PowerUpEnums powerUpType, int timeBeforeActivation, boolean loopable) {
        this.powerUpType = powerUpType;
        this.timeBeforeActivation = timeBeforeActivation;
        this.loopable = loopable;
    }

    public PowerUpEnums getPowerUpType () {
        return powerUpType;
    }

    public void setPowerUpType (PowerUpEnums powerUpType) {
        this.powerUpType = powerUpType;
    }

    public int getTimeBeforeActivation () {
        return timeBeforeActivation;
    }

    public void setTimeBeforeActivation (int timeBeforeActivation) {
        this.timeBeforeActivation = timeBeforeActivation;
    }

    public boolean isLoopable () {
        return loopable;
    }

    public void setLoopable (boolean loopable) {
        this.loopable = loopable;
    }
}
