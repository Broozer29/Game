package game.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.audio.AudioEnums;
import game.levels.BadAppleLevel;
import game.movement.Direction;
import game.movement.HomingPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.movement.RegularPathFinder;
import game.objects.enemies.Alien;
import game.objects.enemies.AlienBomb;
import game.objects.enemies.Bomba;
import game.objects.enemies.Bulldozer;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyEnums;
import game.objects.enemies.Energizer;
import game.objects.enemies.Flamer;
import game.objects.enemies.Seeker;
import game.objects.enemies.Tazer;
import game.spawner.EnemySpawnTimer;

public class LevelSpawnerManager {

	private static LevelSpawnerManager instance = new LevelSpawnerManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
	private TimerManager timerManager = TimerManager.getInstance();
	private int level = 1;

	private LevelSpawnerManager() {
	}

	public static LevelSpawnerManager getInstance() {
		return instance;
	}

	public void resetManager() {
		level = 1;
	}

	public void updateGameTick() {
	}

	// Called when all aliens are dead
	public void levelUp() {
		this.level += 1;
	}

	// Called when a level starts, to saturate enemy list
	public void startLevel() {
//		BadAppleLevel baddAppleSong = new BadAppleLevel();
//		AudioManager audioManager = AudioManager.getInstance();
//		for(EnemySpawnTimer timer : baddAppleSong.getTimers()) {
//			timerManager.addEnemyTimerToList(timer);
//		}
//		
//		try {
//			AudioEnums currentMusic = baddAppleSong.getSong();
//			audioManager.playMusicAudio(currentMusic);
//		} catch (UnsupportedAudioFileException | IOException e) {
//			e.printStackTrace();
//		}
//		timer = timerManager.createTimer(EnemyEnums.Alien_Bomb, 100, 100, false, Direction.DOWN, 1);


		EnemySpawnTimer timer = timerManager.createTimer(EnemyEnums.Bomba, 1, 3000, false, Direction.LEFT, 1, 1);
		timerManager.addEnemyTimerToList(timer);
//		timer = timerManager.createTimer(EnemyEnums.Flamer, 1, 100, false, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
//		EnemySpawnTimer timer = timerManager.createTimer(EnemyEnums.Alien_Bomb, 100, 3000, true, Direction.LEFT, 1, 1);
//		timerManager.addEnemyTimerToList(timer);
//		timer = timerManager.createTimer(EnemyEnums.Seeker, 1, 100, false, Direction.UP, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer(EnemyEnums.Seeker, 1, 100, false, Direction.DOWN, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer(EnemyEnums.Seeker, 2, 100, true, Direction.UP, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer(EnemyEnums.Seeker, 2, 15000, true, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer(EnemyEnums.Seeker, 2, 15000, true, Direction.RIGHT, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer(EnemyEnums.Bulldozer, 1, 6000, true, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer(EnemyEnums.Energizer, 1, 5500, true, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
	}

	// Called by CustomTimers when they have to spawn an enemy
	public void spawnEnemy(EnemyEnums enemyType, int amountOfAttempts, Direction direction, float scale) {
		for (int i = 0; i < amountOfAttempts; i++) {
			List<Integer> coordinatesList = getSpawnCoordinatesByDirection(direction);
			int xCoordinate = coordinatesList.get(0);
			int yCoordinate = coordinatesList.get(1);

			if (enemyType.equals(EnemyEnums.Alien_Bomb)) {
				if (direction.equals(Direction.UP) || direction.equals(Direction.LEFT_UP )|| direction.equals(Direction.RIGHT_UP)) {
					yCoordinate = spawningCoordinator.getRandomYDownBombEnemyCoordinate();
				} else if (direction.equals(Direction.DOWN) || direction.equals(Direction.LEFT_DOWN )|| direction.equals(Direction.RIGHT_DOWN)) {
					yCoordinate = spawningCoordinator.getRandomYUpBombEnemyCoordinate();
				}
				xCoordinate = spawningCoordinator.getRandomXBombEnemyCoordinate();
				scale = 1;
			}

			Enemy enemy = createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale);
			if (enemy != null && validCoordinates(enemy)) {
				enemyManager.addEnemy(enemy);
			}
		}

	}

	private List<Integer> getSpawnCoordinatesByDirection(Direction direction) {
		List<Integer> coordinatesList = new ArrayList<Integer>();

		if (direction.equals(Direction.LEFT)) {
			coordinatesList.add(spawningCoordinator.getRightBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getRightBlockYCoordinate());
		} else if (direction.equals(Direction.RIGHT)) {
			coordinatesList.add(spawningCoordinator.getLeftBlockXCoordinate());
			coordinatesList.add( spawningCoordinator.getLeftBlockYCoordinate());
		} else if (direction.equals(Direction.DOWN)) {
			coordinatesList.add(spawningCoordinator.getUpBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getUpBlockYCoordinate());
		} else if (direction.equals(Direction.UP)) {
			coordinatesList.add(spawningCoordinator.getDownBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getDownBlockYCoordinate());
		}

		else if (direction.equals(Direction.LEFT_UP)) {
			coordinatesList.add(spawningCoordinator.getRightBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getBottomRightBlockYCoordinate());
//			System.out.println(
//					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.LEFT_DOWN)) {
			coordinatesList.add(spawningCoordinator.getRightBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getTopRightBlockYCoordinate());
//			System.out.println(
//					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.RIGHT_UP)) {
			coordinatesList.add(spawningCoordinator.getLeftBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getBottomLeftBlockYCoordinate());
//			System.out.println(
//					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.RIGHT_DOWN)) {
			coordinatesList.add(spawningCoordinator.getLeftBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getTopLeftBlockYCoordinate());
//			System.out.println(
//					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		}

		return coordinatesList;
	}

	private Enemy createEnemy(EnemyEnums type, int xCoordinate, int yCoordinate, Direction rotation, float scale) {

		PathFinder regularPathFinder = new RegularPathFinder();
		PathFinder homingPathFinder = new HomingPathFinder();
		
		//Can be refactored to be more efficient
		Point currentPoint = new Point(xCoordinate, yCoordinate);
		Point regularDestination = regularPathFinder.calculateInitialEndpoint(currentPoint, rotation);
//		Point homingDestination = homingPathFinder.calculateInitialEndpoint(currentPoint, rotation);
		
		switch (type) {
		case Alien:
			return new Alien(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder);
		case Bomba:
			return new Bomba(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder);
		case Flamer:
			return new Flamer(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder);
		case Bulldozer:
			return new Bulldozer(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder);
		case Energizer:
			return new Energizer(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder);
		case Seeker:
			return new Seeker(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder);
		case Tazer:
			return new Tazer(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder);
		case Alien_Bomb:
			return new AlienBomb(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder);
		}
		return null;
	}

	// Called by all spawn*Enemy* methods, returns true if there is no overlap
	// between enemies of the same type
	private boolean validCoordinates(Enemy enemy) {
		if (spawningCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), enemy.getXCoordinate(),
				enemy.getWidth())
				&& spawningCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(),
						enemy.getYCoordinate(), enemy.getHeight())) {
			return true;
		}
		return false;
	}

}