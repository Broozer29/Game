package game.spawner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.managers.TimerManager;
import game.movement.Direction;
import game.objects.enemies.EnemyEnums;

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
	private int additionalDelay;

	public EnemySpawnTimer(int timeBeforeActivation, int amountOfSpawnAttempts, EnemyEnums timerEnemyType, boolean loopable,
			Direction direction, float enemyScale, int additionalDelay) {
		this.enemyScale = enemyScale;
		this.timerEnemyType = timerEnemyType;
		this.amountOfSpawnAttempts = amountOfSpawnAttempts;
		this.direction = direction;
		this.loopable = loopable;
		this.timeBeforeActivation = timeBeforeActivation;
		//Disabled because of the memory/cpu fiasco, probably harmless
//		this.setAdditionalDelay(additionalDelay);
		this.finished = false;
		this.status = "primed";
		initTimer();
	}

	private void initTimer() {
		timer = new Timer(timeBeforeActivation + additionalDelay, this);
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
	
	public void removeDelay() {
		this.additionalDelay = 0;
		timer = new Timer(timeBeforeActivation + additionalDelay, this);
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

	public int getAdditionalDelay() {
		return additionalDelay;
	}

	public void setAdditionalDelay(int additionalDelay) {
		this.additionalDelay = additionalDelay;
	}

}