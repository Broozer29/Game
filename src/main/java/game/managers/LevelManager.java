package game.managers;

import java.util.ArrayList;
import java.util.List;

import game.objects.enemies.Alien;
import game.objects.enemies.AlienBomb;
import game.objects.enemies.Bomba;
import game.objects.enemies.Bulldozer;
import game.objects.enemies.Enemy;
import game.objects.enemies.Energizer;
import game.objects.enemies.Flamer;
import game.objects.enemies.Seeker;
import game.objects.enemies.Tazer;
import game.spawner.CustomTimer;

public class LevelManager {

	private static LevelManager instance = new LevelManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
	private TimerManager timerManager = TimerManager.getInstance();
	private int level = 1;

	private LevelManager() {
	}

	public static LevelManager getInstance() {
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
		CustomTimer timer = null;
		timer = timerManager.createTimer("Alien Bomb", 20, 5000, true, "NaN", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Bomba", 1, 6000, true, "Left", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Flamer", 1, 5500, true, "Left", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Tazer", 2, 8000, true, "Left", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Seeker", 2, 10000, true, "Down", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Seeker", 2, 10000, true, "Up", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Seeker", 2, 10000, true, "Left", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Seeker", 2, 10000, true, "Right", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Bulldozer", 1, 5000, true, "Left", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Energizer", 1, 4500, true, "Left", angleModuloDivider, 1);
		timerManager.addTimerToList(timer);
	}

	// Called by CustomTimers when they have to spawn an enemy
	public void spawnEnemy(String enemyType, int amountOfAttempts, String direction, int angleModuloDivider,
			float scale) {
		for (int i = 0; i < amountOfAttempts; i++) {

			// Hier afvangen waar hij moet spawnen
			List<Integer> coordinatesList = getSpawnCoordinatesByDirection(direction);

			int xCoordinate = coordinatesList.get(0);
			int yCoordinate = coordinatesList.get(1);

			switch (enemyType) {
			case ("Alien Bomb"):
				xCoordinate = spawningCoordinator.getRandomXBombEnemyCoordinate();
				spawnAlienBomb(xCoordinate);
				break;
			case ("Alien"):
				spawnAlien(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
				break;
			case ("Bomba"):
				spawnBomba(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
				break;
			case ("Flamer"):
				spawnFlamer(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
				break;
			case ("Bulldozer"):
				spawnBulldozer(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
				break;
			case ("Energizer"):
				spawnEnergizer(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
				break;
			case ("Seeker"):
				spawnSeeker(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
				break;
			case ("Tazer"):
				spawnTazer(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
				break;
			}
		}
	}

	private List<Integer> getSpawnCoordinatesByDirection(String direction) {
		int xCoordinate = 0;
		int yCoordinate = 0;
		List<Integer> coordinatesList = new ArrayList<Integer>();

		if (direction.equals("Left")) {
			xCoordinate = spawningCoordinator.getRightBlockXCoordinate();
			yCoordinate = spawningCoordinator.getRightBlockYCoordinate();
		} else if (direction.equals("Right")) {
			xCoordinate = spawningCoordinator.getLeftBlockXCoordinate();
			yCoordinate = spawningCoordinator.getLeftBlockYCoordinate();
		} else if (direction.equals("Down")) {
			xCoordinate = spawningCoordinator.getUpBlockXCoordinate();
			yCoordinate = spawningCoordinator.getUpBlockYCoordinate();
		} else if (direction.equals("Up")) {
			xCoordinate = spawningCoordinator.getDownBlockXCoordinate();
			yCoordinate = spawningCoordinator.getDownBlockYCoordinate();
		}

		else if (direction.equals("LeftUp")) {
			xCoordinate = spawningCoordinator.getRightBlockXCoordinate();
			yCoordinate = spawningCoordinator.getRightBlockYCoordinate();
			System.out.println(
					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals("LeftDown")) {
			xCoordinate = spawningCoordinator.getRightBlockXCoordinate();
			yCoordinate = spawningCoordinator.getRightBlockYCoordinate();
			System.out.println(
					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals("RightUp")) {
			xCoordinate = spawningCoordinator.getLeftBlockXCoordinate();
			yCoordinate = spawningCoordinator.getLeftBlockYCoordinate();
			System.out.println(
					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals("RightDown")) {
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
		String direction = spawningCoordinator.upOrDown();
		int yCoordinate = 0;
		float scale = 1;

		if (direction.equals("Up")) {
			yCoordinate = spawningCoordinator.getRandomYUpBombEnemyCoordinate();
		} else if (direction.equals("Down")) {
			yCoordinate = spawningCoordinator.getRandomYDownBombEnemyCoordinate();
		}

		Enemy enemy = new AlienBomb(xCoordinate, yCoordinate, direction, 0, scale);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnSeeker(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider, float scale) {
		Enemy enemy = new Seeker(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnTazer(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider, float scale) {
		Enemy enemy = new Tazer(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnFlamer(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider, float scale) {
		Enemy enemy = new Flamer(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnBomba(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider, float scale) {
		Enemy enemy = new Bomba(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnBulldozer(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider,
			float scale) {
		Enemy enemy = new Bulldozer(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnEnergizer(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider,
			float scale) {
		Enemy enemy = new Energizer(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnAlien(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider, float scale) {
		Enemy enemy = new Alien(xCoordinate, yCoordinate, direction, angleModuloDivider, scale);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
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
