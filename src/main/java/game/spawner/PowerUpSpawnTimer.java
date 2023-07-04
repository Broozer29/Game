package game.spawner;

import java.util.Random;

import game.objects.friendlies.powerups.PowerUpManager;
import game.objects.friendlies.powerups.PowerUps;

public class PowerUpSpawnTimer{

	PowerUpManager powerUpManager = PowerUpManager.getInstance();
	
	private int timeBeforeActivation;
	private PowerUps powerUpType;
	private PowerUps[] powerupEnums = PowerUps.values();
	private Random random = new Random();
	
	private int currentTime;
	private boolean finished;
	private boolean loopable;
	private String status;
	private int additionalDelay;
	private PowerUps originalPowerUpType;
	
	
	public PowerUpSpawnTimer(int timeBeforeActivation, PowerUps powerUpType, boolean loopable, int additionalDelay) {
		this.timeBeforeActivation = timeBeforeActivation;
		this.powerUpType = powerUpType;
		this.originalPowerUpType = powerUpType;
		this.currentTime = 0;
		this.loopable = loopable;
		this.additionalDelay = additionalDelay;
		this.finished = false;
		this.setStatus("primed");
		setPowerUp();
	}
	
	public void activateTimer() {
		PowerUpManager.getInstance().spawnPowerUp(powerUpType);
		
		if (this.loopable) {
			this.additionalDelay = 0;
			this.currentTime = 0;
			this.finished = false;
			setPowerUp();
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
	
	private void setPowerUp() {
		if(this.originalPowerUpType == PowerUps.RANDOM) {
			this.powerUpType = selectRandomPowerUp();
		}
	}
	
	private PowerUps selectRandomPowerUp() {
		PowerUps randomValue = powerupEnums[random.nextInt(powerupEnums.length)];
//	    if (randomValue == PowerUps.DOUBLE_SHOT || 
//	    		randomValue == PowerUps.TRIPLE_SHOT) {
//	        return selectRandomPowerUp();
//	    }
		if (randomValue == PowerUps.DUMMY_DO_NOT_USE || 
				randomValue == PowerUps.RANDOM) {
			return selectRandomPowerUp();
		}
		return randomValue;
	}

}