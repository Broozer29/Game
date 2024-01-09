package game.objects.powerups.timers;

import game.objects.powerups.PowerUp;
import game.objects.timers.TimerInterface;
import game.objects.timers.TimerStatusEnums;

public class PowerUpEffectTimer implements TimerInterface {
    private float timerLength;
    private boolean loopable;
    private float currentTime;
    private PowerUp powerUp;
    private TimerStatusEnums status;

    public PowerUpEffectTimer (float timeBeforeActivation, PowerUp powerUp, boolean loopable) {
        this.timerLength = timeBeforeActivation;
        this.powerUp = powerUp;
        this.loopable = loopable;
        this.currentTime = 0;
        this.status = TimerStatusEnums.Waiting_To_Start;
    }

    @Override
    public void startOfTimer() {
        // Start power-up effect logic
        powerUp.getPowerUpEffect().activatePower();
        // Add on-screen text or other initial actions
        status = TimerStatusEnums.Running;
    }

    @Override
    public void endOfTimer() {
        if (shouldActivate(currentTime)) {
            // End power-up effect logic
            powerUp.getPowerUpEffect().deactivatePower();
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
