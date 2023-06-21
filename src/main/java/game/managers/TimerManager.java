package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.image.enums.EnemyEnums;
import game.movement.Direction;
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
			EnemySpawnTimer selectedTimer = allEnemyTimers.get(i);
			selectedTimer.stopTimer();
			removeEnemyTimerFromList(selectedTimer);
		}
	}

	// Creates timers for different purposes
	// int duration, int timedelay (zelfde als game delay), waar de timer voor is
	public EnemySpawnTimer createTimer(EnemyEnums enemyType, int amountOfSpawnAttempts, int timeBeforeActivation,
			boolean loopable, Direction direction, float enemyScale) {
		EnemySpawnTimer timer = new EnemySpawnTimer(timeBeforeActivation, amountOfSpawnAttempts, enemyType, loopable,
				direction, enemyScale);
		return timer;
	}

	// Timers die afgelopen zijn, verwijderen
	public void updateTimers() {
		for (int i = 0; i < allEnemyTimers.size(); i++) {
			EnemySpawnTimer selectedTimer = allEnemyTimers.get(i);
			if (!selectedTimer.getFinished()) {
				switch (selectedTimer.getStatus()) {
				case ("primed"):
					selectedTimer.startTimer();
					break;
				case ("finished"):
					selectedTimer.stopTimer();
					removeEnemyTimerFromList(selectedTimer);
					break;
				case ("running"):
					break;
				}
			}
			if (selectedTimer.getFinished()) {
				removeEnemyTimerFromList(selectedTimer);
			}
		}

		for (int i = 0; i < allPowerUpTimers.size(); i++) {
			PowerUpTimer selectedTimer = allPowerUpTimers.get(i);
			if (!selectedTimer.getFinished()) {
				switch (selectedTimer.getStatus()) {
				case ("primed"):
					selectedTimer.startTimer();
					break;
				case ("finished"):
					selectedTimer.stopTimer();
					removePowerUpTimerFromList(selectedTimer);
					break;
				case ("running"):
					break;
				}
			}
			if (selectedTimer.getFinished()) {
				removePowerUpTimerFromList(selectedTimer);
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

		EnemyEnums enemyType = timer.getTimerEnemy();
		int spawnAttempts = timer.getTimerSpawnAttempts();
		Direction direction = timer.getDirection();
		float enemyScale = timer.getEnemyScale();

		levelManager.spawnEnemy(enemyType, spawnAttempts, direction, enemyScale);

		if (timer.getLoopable()) {
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
