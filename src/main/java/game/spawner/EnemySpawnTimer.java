package game.spawner;

import game.levels.LevelSpawnerManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.objects.enemies.EnemyEnums;

public class EnemySpawnTimer {

	// Attributes required for spawning enemies
	private int amountOfSpawnAttempts;
	private Direction direction;
	private EnemyEnums timerEnemyType;
	private float enemyScale;
	
	//Required for formation spawning
	private EnemyFormation formation = null;
	private int formationXCoordinate;
	private int formationYCoordinate;

	// Attributes required for timing
	private int timeBeforeActivation;
	private int originalTimeBeforeActivation;
	private boolean finished;
	private boolean loopable;
	private int additionalDelay;
	private float currentTime;

	public EnemySpawnTimer(int timeBeforeActivation, int amountOfSpawnAttempts, EnemyEnums timerEnemyType,
			boolean loopable, Direction direction, float enemyScale, int additionalDelay) {
		this.enemyScale = enemyScale;
		this.timerEnemyType = timerEnemyType;
		this.amountOfSpawnAttempts = amountOfSpawnAttempts;
		this.direction = direction;
		this.loopable = loopable;
		this.originalTimeBeforeActivation = timeBeforeActivation;
		this.timeBeforeActivation = timeBeforeActivation;
		this.setCurrentTime(0);
		this.setAdditionalDelay(additionalDelay);
		this.finished = false;
	}

	// Vuur event naar de timerManager dat deze timer voorbij is. Bijvoorbeeld om
	// bommen te spawnen.
	public void activateTimer() {
		if (formation == null) {
			LevelSpawnerManager.getInstance().spawnEnemy(0, 0, this.timerEnemyType, this.amountOfSpawnAttempts,
					this.direction, this.enemyScale);
		} else {
			formation.spawnFormation(formationXCoordinate, formationYCoordinate, timerEnemyType, direction, enemyScale);
		}
		if (this.loopable) {
			this.additionalDelay = 0;
			this.timeBeforeActivation = Math.round(currentTime + originalTimeBeforeActivation);
			this.finished = false;
		} else {
			this.finished = true;
		}
	}

	public void removeDelay() {
		this.additionalDelay = 0;
	}

	public boolean getFinished() {
		return this.finished;
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

	public float getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public boolean shouldActivate(float currentFrame) {
		this.currentTime = currentFrame;
		return (currentFrame >= (timeBeforeActivation + additionalDelay));
	}

	public EnemyFormation getFormation() {
		return formation;
	}

	public void setFormation(EnemyFormation formation, int formationXCoordinate, int formationYCoordinate) {
		this.formation = formation;
		this.formationXCoordinate = formationXCoordinate;
		this.formationYCoordinate = formationYCoordinate;
	}

}