package game.managers;

import java.util.ArrayList;
import java.util.List;
import game.objects.timers.TimerInterface;
import game.objects.timers.TimerStatusEnums;

public class TimerManager {

	private static TimerManager instance = new TimerManager();
	private List<TimerInterface> allTimers = new ArrayList<>();

	private TimerManager() {
	}

	public static TimerManager getInstance() {
		return instance;
	}

	public void updateGameTick(float currentSongFrame) {
		updateTimers(20);
	}

	public void resetManager() {
		allTimers.clear();
	}

	// Cycles and updates ALL timers
	public void updateTimers(float currentSongFrame) {
		for (int i = 0; i < allTimers.size(); i++) {
			TimerInterface timer = allTimers.get(i);
			timer.setCurrentTime(currentSongFrame);

			if (!timer.getStatus().equals(TimerStatusEnums.Finished)) {
				if (timer.shouldActivate(currentSongFrame)) {
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