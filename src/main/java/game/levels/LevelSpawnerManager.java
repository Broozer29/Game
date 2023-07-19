package game.levels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import data.audio.AudioEnums;
import game.managers.AudioManager;
import game.managers.TimerManager;
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
import game.objects.enemies.EnemyManager;
import game.objects.enemies.Energizer;
import game.objects.enemies.Flamer;
import game.objects.enemies.Seeker;
import game.objects.enemies.Tazer;
import game.spawner.EnemyFormation;
import game.spawner.EnemySpawnTimer;
import game.spawner.FormationCreator;
import game.spawner.SpawnFormationEnums;
import game.spawner.SpawningCoordinator;

public class LevelSpawnerManager {

	private static LevelSpawnerManager instance = new LevelSpawnerManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
	private TimerManager timerManager = TimerManager.getInstance();

	private Level currentLevel;

	private LevelSpawnerManager() {

	}

	public static LevelSpawnerManager getInstance() {
		return instance;
	}

	public void resetManager() {
		currentLevel = null;
	}

	public void updateGameTick() {
	}

	// Called when a level starts, to saturate enemy list
	public void startLevel() {
//		currentLevel = new FuriWisdomOfRageLevel();
//		AudioManager audioManager = AudioManager.getInstance();
//		for (EnemySpawnTimer timer : currentLevel.getTimers()) {
//			timerManager.addEnemyTimerToList(timer);
//		}
//
//		try {
//			AudioEnums currentMusic = currentLevel.getSong();
//			audioManager.playMusicAudio(currentMusic);
//		} catch (UnsupportedAudioFileException | IOException e) {
//			e.printStackTrace();
//		}

		FormationCreator formCreator = new FormationCreator();
		EnemySpawnTimer timer = null;
		EnemyFormation formation = null;
		boolean loopable = false;
		float scale = 1;
		int additionalDelay = 0;
		DataClass dataClass = DataClass.getInstance();
		
		
		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 0, loopable, Direction.LEFT, scale, additionalDelay);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
		timer.setFormation(formation, dataClass.getWindowWidth() / 2, dataClass.getWindowHeight() / 2);
		addSpawnTimer(timer);
		
		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 0, loopable, Direction.LEFT_DOWN, scale, additionalDelay);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
		timer.setFormation(formation, dataClass.getWindowWidth() / 2, dataClass.getWindowHeight() / 2);
		addSpawnTimer(timer);
		
		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 0, loopable, Direction.LEFT_UP, scale, additionalDelay);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
		timer.setFormation(formation, dataClass.getWindowWidth() / 2, dataClass.getWindowHeight() / 2);
		addSpawnTimer(timer);
		
		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 0, loopable, Direction.RIGHT, scale, additionalDelay);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
		timer.setFormation(formation, dataClass.getWindowWidth() / 2, dataClass.getWindowHeight() / 2);
		addSpawnTimer(timer);
		
		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 0, loopable, Direction.RIGHT_DOWN, scale, additionalDelay);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
		timer.setFormation(formation, dataClass.getWindowWidth() / 2, dataClass.getWindowHeight() / 2);
		addSpawnTimer(timer);
		
		timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, 0, loopable, Direction.RIGHT_UP, scale, additionalDelay);
		formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
		timer.setFormation(formation, dataClass.getWindowWidth() / 2, dataClass.getWindowHeight() / 2);
		addSpawnTimer(timer);
		
//		for(int i = 0; i < 50; i +=5) {
//			timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT_UP, scale, additionalDelay);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 100);
//			addSpawnTimer(timer);
//			
//			timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT_DOWN, scale, additionalDelay);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 400);
//			addSpawnTimer(timer);
//			
//			timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT_UP, scale, additionalDelay);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 100);
//			addSpawnTimer(timer);
//			
//			timer = createRandomSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT_DOWN, scale, additionalDelay);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 400);
//			addSpawnTimer(timer);
//		}

	}

	// Called by CustomTimers when they have to spawn an enemy
	public void spawnEnemy(int xCoordinate, int yCoordinate, EnemyEnums enemyType, int amountOfAttempts,
			Direction direction, float scale) {

		// Spawn random if there are no given X/Y coords
		if (xCoordinate == 0 && yCoordinate == 0) {
			for (int i = 0; i < amountOfAttempts; i++) {
				List<Integer> coordinatesList = getSpawnCoordinatesByDirection(direction);

				xCoordinate = coordinatesList.get(0);
				yCoordinate = coordinatesList.get(1);

				if (enemyType.equals(EnemyEnums.Alien_Bomb)) {
					if (direction.equals(Direction.UP) || direction.equals(Direction.LEFT_UP)
							|| direction.equals(Direction.RIGHT_UP)) {
						yCoordinate = spawningCoordinator.getRandomYDownBombEnemyCoordinate();
					} else if (direction.equals(Direction.DOWN) || direction.equals(Direction.LEFT_DOWN)
							|| direction.equals(Direction.RIGHT_DOWN)) {
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
		} else {
			Enemy enemy = createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale);
			enemyManager.addEnemy(enemy);
		}

	}

	private List<Integer> getSpawnCoordinatesByDirection(Direction direction) {
		List<Integer> coordinatesList = new ArrayList<Integer>();
		if (direction.equals(Direction.LEFT)) {
			coordinatesList.add(spawningCoordinator.getRightBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getRightBlockYCoordinate());
		} else if (direction.equals(Direction.RIGHT)) {
			coordinatesList.add(spawningCoordinator.getLeftBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getLeftBlockYCoordinate());
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
		// Can be refactored to be more efficient
		Point currentPoint = new Point(xCoordinate, yCoordinate);
		Point regularDestination = regularPathFinder.calculateInitialEndpoint(currentPoint, rotation, false);
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

	// FOR TESTING PURPOSES only for methods below this!
	private EnemySpawnTimer createRandomSpawnTimer(EnemyEnums enemyType, int spawnAttempts, int timeBeforeActivation,
			boolean loopable, Direction direction, float enemyScale, int additionalDelay) {

		if (enemyType == EnemyEnums.Random) {
			enemyType = selectRandomEnemy();
		}

		EnemySpawnTimer timer = new EnemySpawnTimer(timeBeforeActivation, spawnAttempts, enemyType, loopable, direction,
				enemyScale, additionalDelay);
		return timer;
	}

	// FOR TESTING PURPOSES ONLY AS WELL
	private void addSpawnTimer(EnemySpawnTimer timer) {
		this.timerManager.addEnemyTimerToList(timer);
	}

	private EnemyEnums selectRandomEnemy() {
		EnemyEnums[] enums = EnemyEnums.values();
		Random random = new Random();
		EnemyEnums randomValue = enums[random.nextInt(enums.length)];

		if (randomValue == EnemyEnums.Alien || randomValue == EnemyEnums.Alien_Bomb) {
			return selectRandomEnemy();
		}
		return randomValue;
	}
}
