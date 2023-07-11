package game.objects.friendlies.powerups;

import java.util.Random;

public class PowerUpSpawnTimer{

	PowerUpManager powerUpManager = PowerUpManager.getInstance();
	
	private int timeBeforeActivation;
	private PowerUpEnums powerUpType;
	private Random random = new Random();
	
	private int currentTime;
	private boolean finished;
	private boolean loopable;
	private String status;
	private int additionalDelay;
	private PowerUpEnums originalPowerUpType;
	
	
	public PowerUpSpawnTimer(int timeBeforeActivation, PowerUpEnums powerUpType, boolean loopable, int additionalDelay) {
		this.timeBeforeActivation = timeBeforeActivation;
		this.powerUpType = powerUpType;
		this.originalPowerUpType = powerUpType;
		this.currentTime = 0;
		this.loopable = loopable;
		this.additionalDelay = additionalDelay;
		this.finished = false;
		this.setStatus("primed");
		this.powerUpType = powerUpType;
	}
	
	public void activateTimer() {
		PowerUpCreator.getInstance().spawnPowerUp(powerUpType);
		
		if (this.loopable) {
			this.additionalDelay = 0;
			this.currentTime = 0;
			this.finished = false;
			startTimer();
		} else {
			this.finished = true;
			this.setStatus("finished");
		}
	}
	
	public void increaseTimerTick() {
		currentTime++;
	}

	public void startTimer() {
		this.setStatus("running");
		this.finished = false;
	}
	
	public boolean shouldActivate() {
		return (currentTime >= (timeBeforeActivation + additionalDelay));
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getFinished() {
		return finished;
	}

	public boolean getLoopable() {
		return loopable;
	}
	
}