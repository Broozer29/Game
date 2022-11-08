package game.objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.managers.TimerManager;

public class CustomTimer implements ActionListener {

	private TimerManager timerManager = TimerManager.getInstance();
	private int delay;
	private String timerPurpose;
	private boolean finished;
	private Timer timer;
	private String status;

	public CustomTimer(int delay, String timerPurpose) {
		this.delay = delay;
		this.timerPurpose = timerPurpose;
		this.finished = false;
		this.status = "primed";
		initTimer();
	}

	private void initTimer() {
		timer = new Timer(delay, this);
	}

	public void startTimer() {
		timer.start();
		this.status = "running";
	}

	public void stopTimer() {
		this.finished = true;
		timer.stop();
	}

	// Vuur event naar de timerManager dat deze timer voorbij is. Bijvoorbeeld om
	// bommen te spawnen.
	@Override
	public void actionPerformed(ActionEvent e) {
		timerManager.activate(timerPurpose);
		this.finished = true;
		this.status = "finished";
		stopTimer();
	}

	public boolean getFinished() {
		return this.finished;
	}

	public String getStatus() {
		return this.status;
	}

}
