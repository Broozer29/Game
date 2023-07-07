package game.spawner;

import java.util.Random;

import game.objects.friendlies.powerups.PowerUpCreator;
import game.objects.friendlies.powerups.PowerUpEnums;
import game.objects.friendlies.powerups.PowerUpManager;

public class PowerUpSpawnTimer{

	PowerUpManager powerUpManager = PowerUpManager.getInstance();
	
	private int timeBeforeActivation;
	private PowerUpEnums powerUpType;
	private PowerUpEnums[] powerupEnums = PowerUpEnums.values();
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
		setPowerUp();
	}
	
	public void activateTimer() {
		PowerUpCreator.getInstance().spawnPowerUp(powerUpType);
		
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
		if(this.originalPowerUpType == PowerUpEnums.RANDOM) {
			this.powerUpType = selectRandomPowerUp();
		}
	}
	
	private PowerUpEnums selectRandomPowerUp() {
		PowerUpEnums randomValue = powerupEnums[random.nextInt(powerupEnums.length)];
//	    if (randomValue == PowerUps.DOUBLE_SHOT || 
//	    		randomValue == PowerUps.TRIPLE_SHOT) {
//	        return selectRandomPowerUp();
//	    }
		if (randomValue == PowerUpEnums.DUMMY_DO_NOT_USE || 
				randomValue == PowerUpEnums.RANDOM) {
			return selectRandomPowerUp();
		}
		
//		return PowerUpEnums.Guardian_Drone_Homing_Missile;
		return randomValue;
	}

}