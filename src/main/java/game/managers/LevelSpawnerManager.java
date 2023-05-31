package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.DataClass;
import data.movement.Direction;
import data.movement.InitialEndPointCalculator;
import data.movement.Point;
import data.movement.RegularPathFinder;
import game.objects.enemies.Alien;
import game.objects.enemies.AlienBomb;
import game.objects.enemies.Bomba;
import game.objects.enemies.Bulldozer;
import game.objects.enemies.Enemy;
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
		checkLevelUpdate();
	}

	// Called when all aliens are dead
	public void levelUp() {
		this.level += 1;
	}

	// Called when a level starts, to saturate enemy list
	public void startLevel() {
		this.setLevel(this.level);
	}

	public void spawnBombs(int bombTriesAmount) {
		for (int i = 0; i < bombTriesAmount; i++) {
			int randomXCoordinate = spawningCoordinator.getRandomXBombEnemyCoordinate();
			spawnAlienBomb(randomXCoordinate);
		}
	}

	// Called every gameloop to see if enough aliens are dead to advance to the next
	// level
	private void checkLevelUpdate() {
//		if (enemyManager.getDefaultAlienSpaceshipCount() <= 0) {
//			levelUp();
//			startLevel();
//		}
	}

	private void setLevel(int level) {
		switch (level) {
		case (1):
			this.saturateLevelOne();
			return;
		}
	}

	private void saturateLevelOne() {
		int angleModuloDivider = 2;
		EnemySpawnTimer timer = null;
//		timer = timerManager.createTimer("Alien Bomb", 20, 10000, true, "NaN", angleModuloDivider, 1);
//		timerManager.addTimerToList(timer);

		timer = timerManager.createTimer("Bomba", 1, 100, false, Direction.LEFT, 1);
		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer("Flamer", 1, 4500, true, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer("Tazer", 2, 5000, true, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer("Seeker", 2, 15000, true, Direction.DOWN, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer("Seeker", 2, 15000, true, Direction.UP, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer("Seeker", 2, 15000, true, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer("Seeker", 2, 15000, true, Direction.RIGHT, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer("Bulldozer", 1, 6000, true, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
//		timer = timerManager.createTimer("Energizer", 1, 5500, true, Direction.LEFT, 1);
//		timerManager.addTimerToList(timer);
	}

	// Called by CustomTimers when they have to spawn an enemy
	public void spawnEnemy(String enemyType, int amountOfAttempts, Direction direction, float scale) {
		for (int i = 0; i < amountOfAttempts; i++) {
			List<Integer> coordinatesList = getSpawnCoordinatesByDirection(direction);
			int xCoordinate = coordinatesList.get(0);
			int yCoordinate = coordinatesList.get(1);

			if (enemyType.equals("Alien Bomb")) {
				xCoordinate = spawningCoordinator.getRandomXBombEnemyCoordinate();
				Direction bombDirection = spawningCoordinator.upOrDown();
				yCoordinate = bombDirection.equals(Direction.UP) ? spawningCoordinator.getRandomYUpBombEnemyCoordinate()
						: spawningCoordinator.getRandomYDownBombEnemyCoordinate();
				enemyType = "Alien";
				scale = 1;
			}

			Enemy enemy = createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale);
			if (enemy != null && validCoordinates(enemy)) {
				enemyManager.addEnemy(enemy);
			}
		}
	}

	private List<Integer> getSpawnCoordinatesByDirection(Direction direction) {
		int xCoordinate = 0;
		int yCoordinate = 0;
		List<Integer> coordinatesList = new ArrayList<Integer>();

		if (direction.equals(Direction.LEFT)) {
			xCoordinate = spawningCoordinator.getRightBlockXCoordinate();
			yCoordinate = spawningCoordinator.getRightBlockYCoordinate();
		} else if (direction.equals(Direction.RIGHT)) {
			xCoordinate = spawningCoordinator.getLeftBlockXCoordinate();
			yCoordinate = spawningCoordinator.getLeftBlockYCoordinate();
		} else if (direction.equals(Direction.DOWN)) {
			xCoordinate = spawningCoordinator.getUpBlockXCoordinate();
			yCoordinate = spawningCoordinator.getUpBlockYCoordinate();
		} else if (direction.equals(Direction.UP)) {
			xCoordinate = spawningCoordinator.getDownBlockXCoordinate();
			yCoordinate = spawningCoordinator.getDownBlockYCoordinate();
		}

		else if (direction.equals(Direction.LEFT_UP)) {
			xCoordinate = spawningCoordinator.getRightBlockXCoordinate();
			yCoordinate = spawningCoordinator.getRightBlockYCoordinate();
			System.out.println(
					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.LEFT_DOWN)) {
			xCoordinate = spawningCoordinator.getRightBlockXCoordinate();
			yCoordinate = spawningCoordinator.getRightBlockYCoordinate();
			System.out.println(
					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.RIGHT_UP)) {
			xCoordinate = spawningCoordinator.getLeftBlockXCoordinate();
			yCoordinate = spawningCoordinator.getLeftBlockYCoordinate();
			System.out.println(
					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.RIGHT_DOWN)) {
			xCoordinate = spawningCoordinator.getLeftBlockXCoordinate();
			yCoordinate = spawningCoordinator.getLeftBlockYCoordinate();
			System.out.println(
					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		}

		coordinatesList.add(xCoordinate);
		coordinatesList.add(yCoordinate);

		return coordinatesList;
	}

	private void spawnAlienBomb(int xCoordinate) {
		Direction direction = spawningCoordinator.upOrDown();
		int yCoordinate = 0;
		float scale = 1;

		if (direction.equals(Direction.UP)) {
			yCoordinate = spawningCoordinator.getRandomYUpBombEnemyCoordinate();
		} else if (direction.equals(Direction.DOWN)) {
			yCoordinate = spawningCoordinator.getRandomYDownBombEnemyCoordinate();
		}
		RegularPathFinder regularPathFinder = new RegularPathFinder();
		InitialEndPointCalculator initEndPointCalc = new InitialEndPointCalculator();
		Point destination = initEndPointCalc.calculateEndPoint(xCoordinate, yCoordinate, direction);
		Enemy enemy = new AlienBomb(xCoordinate, yCoordinate, destination, direction, scale, regularPathFinder);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private Enemy createEnemy(String type, int xCoordinate, int yCoordinate, Direction rotation, float scale) {
		RegularPathFinder regularPathFinder = new RegularPathFinder();
		InitialEndPointCalculator initEndPointCalc = new InitialEndPointCalculator();
		Point destination = initEndPointCalc.calculateEndPoint(xCoordinate, yCoordinate, rotation);

		switch (type) {
		case "Alien":
			return new Alien(xCoordinate, yCoordinate, destination, rotation, scale, regularPathFinder);
		case "Bomba":
			return new Bomba(xCoordinate, yCoordinate, destination, rotation, scale, regularPathFinder);
		case "Flamer":
			return new Flamer(xCoordinate, yCoordinate, destination, rotation, scale, regularPathFinder);
		case "Bulldozer":
			return new Bulldozer(xCoordinate, yCoordinate, destination, rotation, scale, regularPathFinder);
		case "Energizer":
			return new Energizer(xCoordinate, yCoordinate, destination, rotation, scale, regularPathFinder);
		case "Seeker":
			return new Seeker(xCoordinate, yCoordinate, destination, rotation, scale, regularPathFinder);
		case "Tazer":
			return new Tazer(xCoordinate, yCoordinate, destination, rotation, scale, regularPathFinder);
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
