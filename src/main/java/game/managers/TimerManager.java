package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.objects.CustomTimer;

public class TimerManager {

	private static TimerManager instance = new TimerManager();
	private LevelManager levelManager = LevelManager.getInstance();
	private List<CustomTimer> allTimers = new ArrayList<CustomTimer>();

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
			CustomTimer selectedTimer = allTimers.get(i);
			selectedTimer.stopTimer();
			removeTimerFromList(selectedTimer);
		}
	}

	// Creates timers for different purposes
	// int duration, int timedelay (zelfde als game delay), waar de timer voor is
	public void createTimer(String timerPurpose, int amountOfSpawnAttempts, int timeBeforeActivation,
			boolean loopable, String direction) {

		String enemyType = "";
		switch (timerPurpose) {
		case ("Spawn Bombs"):
			enemyType = "Alien Bomb";
			break;
		case ("Spawn Bomba"):
			enemyType = "Bomba";
			break;
		case ("Spawn Flamer"):
			enemyType = "Flamer";
			break;
		case ("Spawn Energizer"):
			enemyType = "Energizer";
			break;
		case ("Spawn Tazer"):
			enemyType = "Tazer";
			break;
		case ("Spawn Seeker"):
			enemyType = "Seeker";
			break;
		case ("Spawn Bulldozer"):
			enemyType = "Bulldozer";
			break;
		}

		CustomTimer timer = new CustomTimer(timeBeforeActivation, timerPurpose, amountOfSpawnAttempts, enemyType,
				loopable, direction);
		addTimerToList(timer);
	}

	// Timers die afgelopen zijn, verwijderen
	public void updateTimers() {
		for (int i = 0; i < allTimers.size(); i++) {
			CustomTimer selectedTimer = allTimers.get(i);
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

	private void removeTimerFromList(CustomTimer timerToRemove) {
		allTimers.remove(timerToRemove);
	}

	private void addTimerToList(CustomTimer timerToAdd) {
		allTimers.add(timerToAdd);
	}

	// Called by the timer itself when it's time to activate
	public void activate(CustomTimer timer) {
		if (levelManager == null) {
			levelManager = LevelManager.getInstance();
		}
		levelManager.spawnEnemy(timer.getTimerEnemy(), timer.getTimerSpawnAttempts(), timer.getEnemyMovementDirection());

		if (timer.getLoopable()) {
			createTimer(timer.getTimerPurpose(), timer.getTimerSpawnAttempts(), timer.getTimeBeforeActivation(),
					timer.getLoopable(), timer.getEnemyMovementDirection());
		}

	}
}
