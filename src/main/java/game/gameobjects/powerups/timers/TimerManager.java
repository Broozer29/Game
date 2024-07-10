package game.gameobjects.powerups.timers;

import java.util.ArrayList;
import java.util.List;
import game.gameobjects.timers.TimerInterface;
import game.gameobjects.timers.TimerStatusEnums;

public class TimerManager {

	//Literally only used for spawning power ups atm
	private static TimerManager instance = new TimerManager();
	private List<TimerInterface> allTimers = new ArrayList<>();

	private TimerManager() {
	}

	public static TimerManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		updateTimers();
	}

	public void resetManager() {
		allTimers.clear();
	}

	// Cycles and updates ALL timers
	public void updateTimers() {
		for (int i = 0; i < allTimers.size(); i++) {
			TimerInterface timer = allTimers.get(i);
			if (!timer.getStatus().equals(TimerStatusEnums.Finished)) {
				if (timer.shouldActivate()) {
					timer.endOfTimer();
				}
			}

			if (timer.getStatus().equals(TimerStatusEnums.Finished) && !timer.getLoopable()) {
				allTimers.remove(i);
				i--; // Adjust index after removal
			}
		}
	}

	public void addTimer(TimerInterface timer) {
		allTimers.add(timer);
	}
}