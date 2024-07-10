package game.gameobjects.powerups.timers;

import game.gamestate.GameStateInfo;
import game.gameobjects.powerups.creation.PowerUpCreator;
import game.gameobjects.powerups.PowerUpEnums;
import game.gameobjects.timers.TimerInterface;
import game.gameobjects.timers.TimerStatusEnums;

public class PowerUpSpawnTimer implements TimerInterface {
    private double activateAfterThisTime;
    private double timeBeforeActivationInSeconds;
    private boolean loopable;
    private double currentTime;
    private PowerUpEnums powerUpType;
    private TimerStatusEnums status;

    public PowerUpSpawnTimer(double timeBeforeActivationInSeconds, PowerUpEnums powerUpType, boolean loopable) {
        this.currentTime = GameStateInfo.getInstance().getGameSeconds();
        this.activateAfterThisTime = currentTime + timeBeforeActivationInSeconds;
        this.timeBeforeActivationInSeconds = timeBeforeActivationInSeconds;
        this.powerUpType = powerUpType;
        this.loopable = loopable;
        this.status = TimerStatusEnums.Waiting_To_Start;
    }

    @Override
    public void startOfTimer() {
        status = TimerStatusEnums.Running;
    }

    @Override
    public void endOfTimer() {
        if (shouldActivate()) {
            PowerUpCreator.getInstance().spawnPowerUp(powerUpType); // Actual spawning of the power-up
            if (this.loopable) {
                this.activateAfterThisTime = GameStateInfo.getInstance().getGameSeconds() + timeBeforeActivationInSeconds;
                this.status = TimerStatusEnums.Waiting_To_Start;
            } else {
                this.status = TimerStatusEnums.Finished;
            }
        }
    }

    @Override
    public boolean shouldActivate() {
        return GameStateInfo.getInstance().getGameSeconds() >= activateAfterThisTime;
    }

    @Override
    public void setCurrentTime(float currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public boolean getLoopable() {
        return loopable;
    }

    @Override
    public TimerStatusEnums getStatus() {
        return status;
    }
}
