package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.spawner.EnemySpawnTimer;

public class TimerManager {

	private static TimerManager instance = new TimerManager();
	private LevelManager levelManager = LevelManager.getInstance();
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
			boolean loopable, String direction, int angleModuloDivider, float enemyScale) {
		EnemySpawnTimer timer = new EnemySpawnTimer(timeBeforeActivation, amountOfSpawnAttempts, enemyType,
				loopable, direction, angleModuloDivider, enemyScale);
		return timer;
//		addTimerToList(timer);
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
	public void activate(EnemySpawnTimer timer) {
		if (levelManager == null) {
			levelManager = LevelManager.getInstance();
		}
		levelManager.spawnEnemy(timer.getTimerEnemy(), timer.getTimerSpawnAttempts(), timer.getEnemyMovementDirection(), timer.getAngleModuloDivider(), timer.getEnemyScale());
		if (timer.getLoopable()) {
			EnemySpawnTimer renewedTimer = createTimer(timer.getTimerEnemy(), timer.getTimerSpawnAttempts(), timer.getTimeBeforeActivation(),
					timer.getLoopable(), timer.getEnemyMovementDirection(), timer.getAngleModuloDivider(), timer.getEnemyScale());
			addTimerToList(renewedTimer);
		}

	}
}
