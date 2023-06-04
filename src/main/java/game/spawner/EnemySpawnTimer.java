package game.spawner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import data.image.enums.EnemyEnums;
import game.managers.TimerManager;
import game.movement.Direction;

public class EnemySpawnTimer implements ActionListener {

	private TimerManager timerManager = TimerManager.getInstance();

	// Attributes required for spawning enemies
	private int amountOfSpawnAttempts;
	private Direction direction;
	private EnemyEnums timerEnemyType;
	private float enemyScale;

	// Attributes required for timing
	private int timeBeforeActivation;
	private boolean finished;
	private boolean loopable;
	private Timer timer;
	private String status;

	public EnemySpawnTimer(int timeBeforeActivation, int amountOfSpawnAttempts, EnemyEnums timerEnemyType, boolean loopable,
			Direction direction, float enemyScale) {
		this.enemyScale = enemyScale;
		this.timerEnemyType = timerEnemyType;
		this.amountOfSpawnAttempts = amountOfSpawnAttempts;
		this.direction = direction;
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

	// Vuur event naar de timerManager dat deze timer voorbij is. Bijvoorbeeld om
	// bommen te spawnen.
	@Override
	public void actionPerformed(ActionEvent e) {
		timerManager.activateSpawnTimer(this);
		this.finished = true;
		this.status = "finished";
	}

	public boolean getFinished() {
		return this.finished;
	}

	public String getStatus() {
		return this.status;
	}

	public EnemyEnums getTimerEnemy() {
		return this.timerEnemyType;
	}

	public Direction getDirection() {
		return this.direction;
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

	public float getEnemyScale() {
		return this.enemyScale;
	}

}
