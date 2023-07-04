package game.spawner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.levels.LevelSpawnerManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.objects.enemies.EnemyEnums;

public class EnemySpawnTimer {

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
	private String status;
	private int additionalDelay;

	private int currentTime;

	public EnemySpawnTimer(int timeBeforeActivation, int amountOfSpawnAttempts, EnemyEnums timerEnemyType,
			boolean loopable, Direction direction, float enemyScale, int additionalDelay) {
		this.enemyScale = enemyScale;
		this.timerEnemyType = timerEnemyType;
		this.amountOfSpawnAttempts = amountOfSpawnAttempts;
		this.direction = direction;
		this.loopable = loopable;
		this.timeBeforeActivation = timeBeforeActivation;
		this.setCurrentTime(0);
		this.setAdditionalDelay(additionalDelay);
		this.finished = false;
		this.status = "primed";
	}

	public void increaseTimerTick() {
		currentTime++;
	}

	public void startTimer() {
		this.status = "running";
		this.finished = false;
	}

	// Vuur event naar de timerManager dat deze timer voorbij is. Bijvoorbeeld om
	// bommen te spawnen.
	public void activateTimer() {
		LevelSpawnerManager.getInstance().spawnEnemy(this.timerEnemyType, this.amountOfSpawnAttempts, this.direction,
				this.enemyScale);

		if (this.loopable) {
			this.additionalDelay = 0;
			this.currentTime = 0;
			this.finished = false;
			startTimer();
		} else {
			this.finished = true;
			this.status = "finished";
		}
	}

	public void removeDelay() {
		this.additionalDelay = 0;
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

	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public boolean shouldActivate() {
		return (currentTime >= (timeBeforeActivation + additionalDelay));
	}

	@Override
	public String toString() {
		return "EnemySpawnTimer [timerManager=" + timerManager + ", amountOfSpawnAttempts=" + amountOfSpawnAttempts
				+ ", direction=" + direction + ", timerEnemyType=" + timerEnemyType + ", enemyScale=" + enemyScale
				+ ", timeBeforeActivation=" + timeBeforeActivation + ", finished=" + finished + ", loopable=" + loopable
				+ ", status=" + status + ", additionalDelay=" + additionalDelay + ", currentTime=" + currentTime + "]";
	}

}