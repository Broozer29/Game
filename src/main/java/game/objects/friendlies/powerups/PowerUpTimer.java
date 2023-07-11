package game.objects.friendlies.powerups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.managers.TimerManager;

public class PowerUpTimer {

	private TimerManager timerManager = TimerManager.getInstance();
	private PowerUp powerUp;
	// Attributes required for timing
	private int timeBeforeActivation;
	private boolean finished;
	private boolean loopable;
	private String status;
	private int currentTime;

	public PowerUpTimer(int timeBeforeActivation, PowerUp powerUp, boolean loopOrNot) {
		this.status = "primed";
		this.timeBeforeActivation = timeBeforeActivation;
		this.loopable = loopOrNot;
		this.powerUp = powerUp;
		setCurrentTime(0);
	}

	public void startTimer() {
		this.status = "running";
		this.finished = false;
	}

	public void stopTimer() {
		this.finished = true;
	}

	public void refreshTimer() {
		this.setCurrentTime(0);
		this.status = "running";
		this.finished = false;
	}

	// This method gets executed by the end of the Timer
	public void activateTimer() {
		this.finished = true;
		this.status = "finished";
		
		if (this.loopable) {
			this.currentTime = 0;
			this.finished = false;
			startTimer();
		} else {
			this.finished = true;
			this.status = "finished";
			powerUp.activateEndOfTimerEffect();
		}
	}
	
	
	public boolean getFinished() {
		return this.finished;
	}

	public String getStatus() {
		return this.status;
	}

	public boolean getLoopable() {
		return this.loopable;
	}

	public int getTimeBeforeActivation() {
		return this.timeBeforeActivation;
	}

	public PowerUp getPowerUp() {
		return powerUp;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public boolean shouldActivate() {
		return (currentTime >= timeBeforeActivation);
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public void increaseTimerTick() {
		currentTime++;
	}

}