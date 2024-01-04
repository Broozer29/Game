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
        System.out.println("Yo?");
    }

    @Override
    public void startOfTimer() {
        // No specific action on start for this timer
        status = TimerStatusEnums.Running;
    }

    @Override
    public void endOfTimer() {
        if (shouldActivate(currentTime)) {
            PowerUpCreator.getInstance().spawnPowerUp(powerUpType); // Actual spawning of the power-up

            if (this.loopable) {
                this.currentTime = 0;
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
