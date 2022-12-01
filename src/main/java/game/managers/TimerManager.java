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
	public void createTimer(String timerPurpose) {
		switch (timerPurpose) {
		case ("SpawnBombs"):
			CustomTimer timer = new CustomTimer(10000, "SpawnBombs");
			addTimerToList(timer);
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
			if(selectedTimer.getFinished()) {
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

	public void activate(String timerPurpose) {
		if (levelManager == null) {
			levelManager = LevelManager.getInstance();
		}

		switch (timerPurpose) {
		case ("SpawnBombs"):
			levelManager.spawnBombs(20);
			createTimer("SpawnBombs");
			break;
		}
	}
}
