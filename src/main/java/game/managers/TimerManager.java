package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.levels.LevelManager;
import game.objects.friendlies.powerups.PowerUpManager;
import game.objects.friendlies.powerups.PowerUpSpawnTimer;
import game.objects.friendlies.powerups.PowerUpTimer;
import game.objects.friendlies.powerups.PowerUpCreator;
import game.objects.friendlies.powerups.PowerUpEnums;
import game.spawner.EnemySpawnTimer;

public class TimerManager {

	private static TimerManager instance = new TimerManager();
	private LevelManager levelManager = LevelManager.getInstance();
	private List<EnemySpawnTimer> allEnemyTimers = new ArrayList<EnemySpawnTimer>();
	private List<PowerUpTimer> activePowerUpTimers = new ArrayList<PowerUpTimer>();
	private List<PowerUpSpawnTimer> powerUpSpawnerTimers = new ArrayList<PowerUpSpawnTimer>();

	private TimerManager() {

	}

	public static TimerManager getInstance() {
		return instance;
	}

	public void updateGameTick(float currentSongFrame) {
		updateTimers(currentSongFrame);
	}

	public void resetManager() {
		allEnemyTimers = new ArrayList<EnemySpawnTimer>();
		activePowerUpTimers = new ArrayList<PowerUpTimer>();
	}

	//Cycles and updates ALL timers
	public void updateTimers(float currentSongFrame) {
		if(powerUpSpawnerTimers.size() < 1) {
			powerUpSpawnerTimers.add(PowerUpCreator.getInstance().createPowerUpTimer(PowerUpEnums.RANDOM, true, 0));
		}
		
		for (int i = 0; i < allEnemyTimers.size(); i++) {
			if (!allEnemyTimers.get(i).getFinished()) {
				if (allEnemyTimers.get(i).shouldActivate(currentSongFrame)) {
					allEnemyTimers.get(i).activateTimer();
				}
			}
			if (allEnemyTimers.get(i).getFinished() && !allEnemyTimers.get(i).getLoopable()) {
				allEnemyTimers.remove(allEnemyTimers.get(i));
			}
		}

		for (int i = 0; i < activePowerUpTimers.size(); i++) {
			if (!activePowerUpTimers.get(i).getFinished()) {
				switch (activePowerUpTimers.get(i).getStatus()) {
				case ("primed"):
					activePowerUpTimers.get(i).startTimer();
					break;
				case ("running"):
					activePowerUpTimers.get(i).increaseTimerTick();
					break;
				}
				if (activePowerUpTimers.get(i).shouldActivate()) {
					activePowerUpTimers.get(i).activateTimer();
				}
			}
			if (activePowerUpTimers.get(i).getFinished() && !activePowerUpTimers.get(i).getLoopable()) {
				activePowerUpTimers.remove(activePowerUpTimers.get(i));
			}
		}
		
		for (int i = 0; i < powerUpSpawnerTimers.size(); i++) {
			if (!powerUpSpawnerTimers.get(i).getFinished()) {
				switch (powerUpSpawnerTimers.get(i).getStatus()) {
				case ("primed"):
					powerUpSpawnerTimers.get(i).startTimer();
					break;
				case ("running"):
					powerUpSpawnerTimers.get(i).increaseTimerTick();
					break;
				}
				if (powerUpSpawnerTimers.get(i).shouldActivate()) {
					powerUpSpawnerTimers.get(i).activateTimer();
				}
			}
			if (powerUpSpawnerTimers.get(i).getFinished() && !powerUpSpawnerTimers.get(i).getLoopable()) {
				powerUpSpawnerTimers.remove(powerUpSpawnerTimers.get(i));
			}
		}
	}


	public void addEnemyTimerToList(EnemySpawnTimer timerToAdd) {
		allEnemyTimers.add(timerToAdd);
	}

	public void addPowerUpTimerToList(PowerUpTimer timerToAdd) {
		activePowerUpTimers.add(timerToAdd);
	}



}