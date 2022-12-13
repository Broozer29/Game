package game.objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.managers.TimerManager;

public class CustomTimer implements ActionListener {

	private TimerManager timerManager = TimerManager.getInstance();

	private int amountOfSpawnAttempts;
	private boolean loopable;
	private String timerEnemyType;
	private int timeBeforeActivation;
	private String timerPurpose;
	private String enemyMovementDirection;
	private boolean finished;
	private Timer timer;
	private String status;

	public CustomTimer(int timeBeforeActivation, String timerPurpose, int amountOfSpawnAttempts, String timerEnemyType,
			boolean loopable, String enemyMovementDirection) {
		this.amountOfSpawnAttempts = amountOfSpawnAttempts;
		this.enemyMovementDirection = enemyMovementDirection;
		this.loopable = loopable;
		this.timerEnemyType = timerEnemyType;
		this.timeBeforeActivation = timeBeforeActivation;
		this.timerPurpose = timerPurpose;
		this.finished = false;
		this.status = "primed";
		initTimer();
	}

	private void initTimer() {
		timer = new Timer(timeBeforeActivation, this);
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
		timerManager.activate(this);
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

	public String getTimerPurpose() {
		return this.timerPurpose;
	}

	public String getTimerEnemy() {
		return this.timerEnemyType;
	}
	
	public String getEnemyMovementDirection() {
		return this.enemyMovementDirection;
	}

	public int getTimerSpawnAttempts() {
		return this.amountOfSpawnAttempts;
	}

	public boolean getLoopable() {
		return this.loopable;
	}

	public int getTimeBeforeActivation() {
		return this.timeBeforeActivation;
	}

}
