package game.levels;

import java.util.ArrayList;
import java.util.List;

import data.audio.AudioEnums;
import game.managers.TimerManager;
import game.movement.Direction;
import game.objects.enemies.EnemyEnums;
import game.spawner.EnemySpawnTimer;

public class BadAppleLevel {

	private AudioEnums song = AudioEnums.Apple_Holder_Remix;
	private TimerManager timerManager = TimerManager.getInstance();
	private List<EnemySpawnTimer> enemySpawnTimers = new ArrayList<EnemySpawnTimer>();

	public BadAppleLevel() {
		initBadAppleTimers();
	}

	private void initBadAppleTimers() {
//		initRepeatableTimers();
		initDelayedTimers();
		initSingleFireTimers();
	}

	// Create the timers that repeat
	private void initRepeatableTimers() {
		boolean loopable = true;
		int spawnAttempts = 0;
		int timeBeforeActivation = 0;
		float enemyScale = 1;
		int additionalDelay = 0;
		Direction direction = Direction.LEFT;
		
		
		timeBeforeActivation = 7500;
		direction = Direction.LEFT;
		spawnAttempts = 2	;
		addSpawnTimer(EnemyEnums.Bomba, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 12000;
		addSpawnTimer(EnemyEnums.Energizer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 16000;
		addSpawnTimer(EnemyEnums.Tazer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 13000;
		addSpawnTimer(EnemyEnums.Bulldozer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 15000;
		addSpawnTimer(EnemyEnums.Flamer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 10000;
		spawnAttempts = 3;
		direction = Direction.LEFT_UP;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.LEFT_DOWN;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
	}

	// Create the timers that have a delay, for things like the drop or beat increasing
	private void initDelayedTimers() {
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
	private void initSingleFireTimers() {
		boolean loopable = false;
		int spawnAttempts = 100;
		int timeBeforeActivation = 0;
		float enemyScale = 1;
		int additionalDelay = 0;
		Direction direction = Direction.UP;
		
		//First real beat
		timeBeforeActivation = 19000;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 24000;
		spawnAttempts = 2;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Bomba, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		addSpawnTimer(EnemyEnums.Energizer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 29000;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Tazer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		addSpawnTimer(EnemyEnums.Flamer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 35000;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		addSpawnTimer(EnemyEnums.Bulldozer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		//Slight temporary increase
		timeBeforeActivation = 48000;
		spawnAttempts = 100;
		direction = Direction.UP;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		
		timeBeforeActivation = 54000;
		spawnAttempts = 2;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Bulldozer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 60000;
		spawnAttempts = 2;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Flamer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Energizer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 70000;
		spawnAttempts = 2;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Bomba, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Tazer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		//Second drop
		spawnAttempts = 200;
		timeBeforeActivation = 90000;
		direction = Direction.UP;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		
		spawnAttempts = 2;
		timeBeforeActivation = 95000;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Bomba, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		addSpawnTimer(EnemyEnums.Energizer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 100000;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Tazer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		addSpawnTimer(EnemyEnums.Flamer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		timeBeforeActivation = 110000;
		direction = Direction.LEFT;
		addSpawnTimer(EnemyEnums.Seeker, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		addSpawnTimer(EnemyEnums.Bulldozer, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		//Eerste pauze
		spawnAttempts = 100;
		timeBeforeActivation = 118000;
		direction = Direction.UP;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
		//Cooling down
		timeBeforeActivation = 125000;
		direction = Direction.UP;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		direction = Direction.DOWN;
		addSpawnTimer(EnemyEnums.Alien_Bomb, spawnAttempts, timeBeforeActivation, loopable, direction, enemyScale, additionalDelay);
		
	}
	
	private void addSpawnTimer(EnemyEnums enemyType, int spawnAttempts, int timeBeforeActivation,
		    boolean loopable, Direction direction, float enemyScale, int additionalDelay) {
		    
		    EnemySpawnTimer timer = timerManager.createTimer(enemyType, spawnAttempts, timeBeforeActivation,
		        loopable, direction, enemyScale, additionalDelay);
		    enemySpawnTimers.add(timer);
		}

	public AudioEnums getSong() {
		return song;
	}
	
	public List<EnemySpawnTimer> getTimers() {
		return this.enemySpawnTimers;
	}

}