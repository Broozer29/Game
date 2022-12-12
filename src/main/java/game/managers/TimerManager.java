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
	public void createTimer(String timerPurpose, int amountOfSpawnAttempts, int timeBeforeActivation) {
		switch (timerPurpose) {
		case ("Spawn Bombs"):
			CustomTimer bombTimer = new CustomTimer(timeBeforeActivation, "Spawn Bombs", amountOfSpawnAttempts,
					"Alien Bomb");
			addTimerToList(bombTimer);
			break;
		case ("Spawn Bomba"):
			CustomTimer bombaTimer = new CustomTimer(timeBeforeActivation, "Spawn Bomba", amountOfSpawnAttempts,
					"Bomba");
			addTimerToList(bombaTimer);
			break;
		case ("Spawn Flamer"):
			CustomTimer flamerTimer = new CustomTimer(timeBeforeActivation, "Spawn Flamer", amountOfSpawnAttempts,
					"Flamer");
			addTimerToList(flamerTimer);
			break;
		case ("Spawn Energizer"):
			CustomTimer energizerTimer = new CustomTimer(timeBeforeActivation, "Spawn Energizer", amountOfSpawnAttempts,
					"Energizer");
			addTimerToList(energizerTimer);
			break;
		case ("Spawn Tazer"):
			CustomTimer tazerTimer = new CustomTimer(timeBeforeActivation, "Spawn Tazer", amountOfSpawnAttempts,
					"Tazer");
			addTimerToList(tazerTimer);
			break;
		case ("Spawn Seeker"):
			CustomTimer seekerTimer = new CustomTimer(timeBeforeActivation, "Spawn Seeker", amountOfSpawnAttempts,
					"Seeker");
			addTimerToList(seekerTimer);
			break;
		case ("Spawn Bulldozer"):
			CustomTimer bulldozerTimer = new CustomTimer(timeBeforeActivation, "Spawn Bulldozer", amountOfSpawnAttempts,
					"Bulldozer");
			addTimerToList(bulldozerTimer);
			break;
		}
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
		this.allTimers.add(timerToAdd);
	}

	//Called by the timer itself when it's time to activate
	public void activate(CustomTimer timer) {
		if (levelManager == null) {
			levelManager = LevelManager.getInstance();
		}
		levelManager.spawnEnemy(timer.getTimerEnemy(), timer.getTimerSpawnAttempts());
	}
}
