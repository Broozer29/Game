package game.objects.powerups.timers;

import game.objects.powerups.creation.PowerUpCreator;
import game.objects.powerups.PowerUpEnums;
import game.objects.timers.TimerInterface;
import game.objects.timers.TimerStatusEnums;

public class PowerUpSpawnTimer implements TimerInterface {
    private float timerLength;
    private boolean loopable;
    private float currentTime;
    private PowerUpEnums powerUpType;
    private TimerStatusEnums status;

    public PowerUpSpawnTimer(float timeBeforeActivation, PowerUpEnums powerUpType, boolean loopable) {
        this.timerLength = timeBeforeActivation;
        this.powerUpType = powerUpType;
        this.loopable = loopable;
        this.currentTime = 0;
        this.status = TimerStatusEnums.Waiting_To_Start;
    }

    @Override
    public void startOfTimer() {
        status = TimerStatusEnums.Running;
    }

    @Override
    public void endOfTimer() {
        if (shouldActivate(currentTime)) {
            PowerUpCreator.getInstance().spawnPowerUp(powerUpType); // Actual spawning of the power-up

            if (this.loopable) {
                this.timerLength = currentTime + PowerUpCreator.getInstance().getRandomTimeForSpawner();
                this.status = TimerStatusEnums.Waiting_To_Start;

            } else {
                this.status = TimerStatusEnums.Finished;
            }
        }
    }

    @Override
    public boolean shouldActivate(float currentTime) {
        this.currentTime = currentTime;
        return currentTime >= timerLength;
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
