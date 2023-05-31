package game.spawner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.managers.TimerManager;

public class EnemySpawnTimer implements ActionListener {

	private TimerManager timerManager = TimerManager.getInstance();

	// Attributes required for spawning enemies
	private int amountOfSpawnAttempts;
	private int angleModuloDivider;
	private String enemyMovementDirection;
	private String timerEnemyType;
	private float enemyScale;

	// Attributes required for timing
	private int timeBeforeActivation;
	private boolean finished;
	private boolean loopable;
	private Timer timer;
	private String status;

	public EnemySpawnTimer(int timeBeforeActivation, int amountOfSpawnAttempts, String timerEnemyType, boolean loopable,
			String enemyMovementDirection, int angleModuloDivider, float enemyScale) {
		this.enemyScale = enemyScale;
		this.timerEnemyType = timerEnemyType;
		this.amountOfSpawnAttempts = amountOfSpawnAttempts;
		this.enemyMovementDirection = enemyMovementDirection;
		this.angleModuloDivider = angleModuloDivider;
		this.loopable = loopable;
		this.timeBeforeActivation = timeBeforeActivation;
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

	public int getAngleModuloDivider() {
		return this.angleModuloDivider;
	}

	public int getTimeBeforeActivation() {
		return this.timeBeforeActivation;
	}

	public float getEnemyScale() {
		return this.enemyScale;
	}

}
