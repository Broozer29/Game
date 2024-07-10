package game.deprecated.pregeneratedlevels;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.gameobjects.powerups.timers.DeprecatedEnemySpawnTimer;
import VisualAndAudioData.audio.enums.AudioEnums;

public class BadAppleLevel implements Level{

	private AudioEnums song = AudioEnums.Apple_Holder_Remix;
	private List<DeprecatedEnemySpawnTimer> enemySpawnTimers = new ArrayList<DeprecatedEnemySpawnTimer>();

	public BadAppleLevel() {
		initBadAppleTimers();
	}

	private void initBadAppleTimers() {
		initRepeatableTimers();
		initDelayedTimers();
		initSingleFireTimers();
	}

	// Create the timers that repeat
	public void initRepeatableTimers() {
		boolean loopable = true;
		int spawnAttempts = 0;
		int timeBeforeActivation = 0;
		float enemyScale = 1;
		int additionalDelay = 0;
		Direction direction = Direction.LEFT;

		timeBeforeActivation = 12;
		direction = Direction.LEFT;
		spawnAttempts = 2;
		addSpawnTimer(EnemyEnums.Bomba, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 10;
		addSpawnTimer(EnemyEnums.Energizer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 14;
		addSpawnTimer(EnemyEnums.Tazer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 14;
		addSpawnTimer(EnemyEnums.Bulldozer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 8;
		addSpawnTimer(EnemyEnums.Flamer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 20;
		spawnAttempts = 3;
		direction = Direction.LEFT_UP;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.LEFT_DOWN;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

	}

	// Create the timers that have a delay, for things like the drop or beat increasing
	public void initDelayedTimers() {
//		boolean loopable = false;
//		int spawnAttempts = 100;
//		int timeBeforeActivation = 0;
//		float enemyScale = 1;
//		int additionalDelay = 0;
//		Direction direction = Direction.UP;
//		
//		timeBeforeActivation = 19000;
//		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
//		direction = Direction.DOWN;
//		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
	}

	// Create the timers that fire a single time
	public void initSingleFireTimers() {
		boolean loopable = false;
		int spawnAttempts = 100;
		int timeBeforeActivation = 0;
		float enemyScale = 1;
		int additionalDelay = 0;
		Direction direction = Direction.UP;

		// First real beat
		timeBeforeActivation = 14;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 15;
		spawnAttempts = 2;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Bomba, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		addSpawnTimer(EnemyEnums.Energizer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 16;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Tazer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		addSpawnTimer(EnemyEnums.Flamer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 17;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		addSpawnTimer(EnemyEnums.Bulldozer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		// Slight temporary increase
		timeBeforeActivation = 19;
		spawnAttempts = 100;
		direction = Direction.UP;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 21;
		spawnAttempts = 2;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Bulldozer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 24;
		spawnAttempts = 2;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Flamer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Energizer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 24;
		spawnAttempts = 2;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Bomba, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Tazer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		// Second drop
		spawnAttempts = 200;
		timeBeforeActivation = 48;
		direction = Direction.UP;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		spawnAttempts = 2;
		timeBeforeActivation = 48;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Bomba, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		addSpawnTimer(EnemyEnums.Energizer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 48;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Tazer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		addSpawnTimer(EnemyEnums.Flamer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		timeBeforeActivation = 48;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		addSpawnTimer(EnemyEnums.Bulldozer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		// Eerste pauze
		spawnAttempts = 100;
		timeBeforeActivation = 96;
		direction = Direction.UP;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

		// Cooling down
		timeBeforeActivation = 137;
		direction = Direction.UP;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale,
				additionalDelay);

	}

	private void addSpawnTimer(EnemyEnums enemyType, int spawnAttempts, int timeBeforeActivation, boolean loopable,
			Direction direction, float enemyScale, int additionalDelay) {

		DeprecatedEnemySpawnTimer timer = new DeprecatedEnemySpawnTimer(timeBeforeActivation, spawnAttempts, enemyType, loopable, direction,
				enemyScale, 2, 2);
		enemySpawnTimers.add(timer);
	}

	public AudioEnums getSong() {
		return song;
	}

	public List<DeprecatedEnemySpawnTimer> getTimers() {
		return this.enemySpawnTimers;
	}

}