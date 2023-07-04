package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.levels.LevelSpawnerManager;
import game.movement.Direction;
import game.objects.enemies.EnemyEnums;
import game.objects.friendlies.powerups.PowerUp;
import game.spawner.EnemySpawnTimer;
import game.spawner.PowerUpTimer;

public class TimerManager {

	private static TimerManager instance = new TimerManager();
	private LevelSpawnerManager levelManager = LevelSpawnerManager.getInstance();
	private List<EnemySpawnTimer> allEnemyTimers = new ArrayList<EnemySpawnTimer>();
	private List<PowerUpTimer> allPowerUpTimers = new ArrayList<PowerUpTimer>();

	private TimerManager() {

	}

	public static TimerManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		updateTimers();
	}

	public void resetManager() {
		for (int i = 0; i < allEnemyTimers.size(); i++) {
			allEnemyTimers.get(i).stopTimer();
			removeEnemyTimerFromList(allEnemyTimers.get(i));
		}
	}

	// Creates timers for different purposes
	// int duration, int timedelay (zelfde als game delay), waar de timer voor is
	public EnemySpawnTimer createTimer(EnemyEnums enemyType, int amountOfSpawnAttempts, int timeBeforeActivation,
			boolean loopable, Direction direction, float enemyScale, int additionalDelay) {
		return new EnemySpawnTimer(timeBeforeActivation, amountOfSpawnAttempts, enemyType, loopable,
				direction, enemyScale, additionalDelay);
	}

	// Timers die afgelopen zijn, verwijderen
	public void updateTimers() {
		for (int i = 0; i < allEnemyTimers.size(); i++) {
			if (!allEnemyTimers.get(i).getFinished()) {
				switch (allEnemyTimers.get(i).getStatus()) {
				case ("primed"):
					allEnemyTimers.get(i).startTimer();
					break;
				case ("finished"):
					allEnemyTimers.get(i).stopTimer();
					removeEnemyTimerFromList(allEnemyTimers.get(i));
					break;
				case ("running"):
					break;
				}
			}
			if (allEnemyTimers.get(i).getFinished() && !allEnemyTimers.get(i).getLoopable()) {
				removeEnemyTimerFromList(allEnemyTimers.get(i));
			}
		}

		for (int i = 0; i < allPowerUpTimers.size(); i++) {
			if (!allPowerUpTimers.get(i).getFinished()) {
				switch (allPowerUpTimers.get(i).getStatus()) {
				case ("primed"):
					allPowerUpTimers.get(i).startTimer();
					break;
				case ("finished"):
					allPowerUpTimers.get(i).stopTimer();
					removePowerUpTimerFromList(allPowerUpTimers.get(i));
					break;
				case ("running"):
					break;
				}
			}
			if (allPowerUpTimers.get(i).getFinished() && !allPowerUpTimers.get(i).getLoopable()) {
				removePowerUpTimerFromList(allPowerUpTimers.get(i));
			}
		}
	}

	private void removeEnemyTimerFromList(EnemySpawnTimer timerToRemove) {
		allEnemyTimers.remove(timerToRemove);
	}

	private void removePowerUpTimerFromList(PowerUpTimer timerToRemove) {
		allPowerUpTimers.remove(timerToRemove);
	}

	public void addEnemyTimerToList(EnemySpawnTimer timerToAdd) {
		allEnemyTimers.add(timerToAdd);

	}

	public void addPowerUpTimerToList(PowerUpTimer timerToAdd) {
		allPowerUpTimers.add(timerToAdd);
	}

	// Called by the timer itself when it's time to activate
	public void activateSpawnTimer(EnemySpawnTimer timer) {
		if (levelManager == null) {
			levelManager = LevelSpawnerManager.getInstance();
		}
		levelManager.spawnEnemy(timer.getTimerEnemy(), timer.getTimerSpawnAttempts(), timer.getDirection(), timer.getEnemyScale());

		if (timer.getLoopable()) {
			timer.removeDelay();
			timer.refreshTimer();
		} else {
			timer.stopTimer();
		}
	}

	public void activatePowerUpTimer(PowerUpTimer timer, PowerUp powerUp) {
		powerUp.activateEndOfTimerEffect();
		
		if (timer.getLoopable()) {
			timer.refreshTimer();
		} else {
			timer.stopTimer();
		}
	}
}