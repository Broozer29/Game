package game.spawner;

import game.movement.Direction;
import game.objects.enemies.enums.EnemyEnums;
import game.objects.timers.TimerInterface;
import game.objects.timers.TimerStatusEnums;

public class EnemySpawnTimer implements TimerInterface {
	private float timerLength;
	private boolean loopable;
	private float currentTime;
	private EnemyEnums timerEnemyType;
	private TimerStatusEnums status;

	// Attributes for enemy spawning
	private int amountOfSpawnAttempts;
	private Direction direction;
	private float enemyScale;
	private int xMovementSpeed;
	private int yMovementSpeed;
	private EnemyFormation formation;
	private int formationXCoordinate;
	private int formationYCoordinate;

	public EnemySpawnTimer(float timeBeforeActivation, int amountOfSpawnAttempts, EnemyEnums timerEnemyType,
						   boolean loopable, Direction direction, float enemyScale, int xMovementSpeed, int yMovementSpeed) {
		this.timerLength = timeBeforeActivation;
		this.timerEnemyType = timerEnemyType;
		this.amountOfSpawnAttempts = amountOfSpawnAttempts;
		this.direction = direction;
		this.enemyScale = enemyScale;
		this.loopable = loopable;
		this.xMovementSpeed = xMovementSpeed;
		this.yMovementSpeed = yMovementSpeed;
		this.currentTime = 0;
		this.status = TimerStatusEnums.Waiting_To_Start;
	}

	@Override
	public void startOfTimer() {
		// No specific action on start for this timer
		status = TimerStatusEnums.Running;
	}

	@Override
	public void endOfTimer() {
		if (shouldActivate(currentTime)) {
			// Enemy spawning logic
			if (formation == null) {
				LevelManager.getInstance().spawnEnemy(0, 0, timerEnemyType, amountOfSpawnAttempts, direction,
						enemyScale, true, xMovementSpeed, yMovementSpeed, false);
			} else {
				formation.spawnFormation(formationXCoordinate, formationYCoordinate, timerEnemyType, direction, enemyScale,
						xMovementSpeed, yMovementSpeed);
			}
			if (this.loopable) {
				this.currentTime = 0;
				this.status = TimerStatusEnums.Waiting_To_Start;
			} else {
				this.status = TimerStatusEnums.Finished;
			}
		}
	}

	@Override
	public boolean shouldActivate(float currentTime) {
		this.currentTime = currentTime;
		return currentTime >= timerLength;
	}

	@Override
	public void setCurrentTime(float currentTime) {
		this.currentTime = currentTime;
	}

	@Override
	public boolean getLoopable() {
		return loopable;
	}

	@Override
	public TimerStatusEnums getStatus() {
		return status;
	}

	// Additional methods related to enemy formation
	public void setFormation(EnemyFormation formation, int formationXCoordinate, int formationYCoordinate) {
		this.formation = formation;
		this.formationXCoordinate = formationXCoordinate;
		this.formationYCoordinate = formationYCoordinate;
	}
}