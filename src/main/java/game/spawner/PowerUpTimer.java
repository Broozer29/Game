package game.spawner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.managers.TimerManager;
import game.objects.friendlies.powerups.PowerUp;

public class PowerUpTimer implements ActionListener {

	private TimerManager timerManager = TimerManager.getInstance();
	private PowerUp powerUp;
	// Attributes required for timing
	private int timeBeforeActivation;
	private boolean finished;
	private boolean loopable;
	private Timer timer;
	private String status;

	public PowerUpTimer(int timeBeforeActivation, PowerUp powerUp, boolean loopOrNot) {
		this.status = "primed";
		this.timeBeforeActivation = timeBeforeActivation;
		this.loopable = loopOrNot;
		this.powerUp = powerUp;
		initTimer();
	}

	private void initTimer() {
		timer = new Timer(timeBeforeActivation, this);
	}

	public void startTimer() {
		timer.start();
		this.status = "running";
		this.finished = false;
	}

	public void stopTimer() {
		this.finished = true;
		timer.stop();
	}

	public void refreshTimer() {
		timer.restart();
		this.status = "running";
		this.finished = false;
	}

	//This method gets executed by the end of the Timer
	@Override
	public void actionPerformed(ActionEvent e) {
		timerManager.activatePowerUpTimer(this, this.powerUp);
		this.finished = true;
		this.status = "finished";
	}
	
	public boolean getFinished() {
		return this.finished;
	}

	public String getStatus() {
		return this.status;
	}

	public boolean getLoopable() {
		return this.loopable;
	}

	public int getTimeBeforeActivation() {
		return this.timeBeforeActivation;
	}
	
	public PowerUp getPowerUp() {
		return powerUp;
	}

}