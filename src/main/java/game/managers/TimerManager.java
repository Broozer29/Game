package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.movement.Direction;
import game.spawner.EnemySpawnTimer;

public class TimerManager {

	private static TimerManager instance = new TimerManager();
	private LevelSpawnerManager levelManager = LevelSpawnerManager.getInstance();
	private List<EnemySpawnTimer> allTimers = new ArrayList<EnemySpawnTimer>();

	private TimerManager() {

	}

	public static TimerManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		updateTimers();
	}

	public void resetManager() {
		for (int i = 0; i < allTimers.size(); i++) {
			EnemySpawnTimer selectedTimer = allTimers.get(i);
			selectedTimer.stopTimer();
			removeTimerFromList(selectedTimer);
		}
	}

	// Creates timers for different purposes
	// int duration, int timedelay (zelfde als game delay), waar de timer voor is
	public EnemySpawnTimer createTimer(String enemyType, int amountOfSpawnAttempts, int timeBeforeActivation,
			boolean loopable, Direction direction, float enemyScale) {
		EnemySpawnTimer timer = new EnemySpawnTimer(timeBeforeActivation, amountOfSpawnAttempts, enemyType, loopable,
				direction, enemyScale);
		return timer;
	}

	// Timers die afgelopen zijn, verwijderen
	public void updateTimers() {
		for (int i = 0; i < allTimers.size(); i++) {
			EnemySpawnTimer selectedTimer = allTimers.get(i);
			if (!selectedTimer.getFinished()) {
				switch (selectedTimer.getStatus()) {
				case ("primed"):
					selectedTimer.startTimer();
					break;
				case ("finished"):
					selectedTimer.stopTimer();
					removeTimerFromList(selectedTimer);
					break;
				case ("running"):
					break;
				}
			}
			if (selectedTimer.getFinished()) {
				removeTimerFromList(selectedTimer);
			}
		}
	}

	private void removeTimerFromList(EnemySpawnTimer timerToRemove) {
		allTimers.remove(timerToRemove);
	}

	public void addTimerToList(EnemySpawnTimer timerToAdd) {
		allTimers.add(timerToAdd);
	}

	// Called by the timer itself when it's time to activate
	public void activateSpawnTimer(EnemySpawnTimer timer) {
		if (levelManager == null) {
			levelManager = LevelSpawnerManager.getInstance();
		}

		String enemyType = timer.getTimerEnemy();
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
}
