package game.managers;

import data.SpawningCoordinator;
import game.objects.CustomTimer;
import game.objects.enemies.Alien;
import game.objects.enemies.AlienBomb;
import game.objects.enemies.Bomba;
import game.objects.enemies.Enemy;
import game.objects.enemies.Flamer;
import game.objects.enemies.Seeker;
import game.objects.enemies.Tazer;

public class LevelManager {

	private static LevelManager instance = new LevelManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private SpawningCoordinator randomCoordinator = SpawningCoordinator.getInstance();
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
			int randomXCoordinate = randomCoordinator.getRandomXBombEnemyCoordinate();
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

	// Spawn command, spawn tries, amount of time required to pass
	private void saturateLevelOne() {
		int angleModuloDivider = 0;
		CustomTimer timer = null;
		timer = timerManager.createTimer("Spawn Bombs", 20, 10000, true, "Left", angleModuloDivider);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Spawn Bomba", 3, 2000, true, "LeftUp", angleModuloDivider);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Spawn Flamer", 3, 7500, true, "Left", angleModuloDivider);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Spawn Tazer", 3, 10000, true, "Left", angleModuloDivider);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Spawn Seeker", 3, 5000, true, "Left", angleModuloDivider);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Spawn Bulldozer", 3, 10000, true, "Left", angleModuloDivider);
		timerManager.addTimerToList(timer);
		timer = timerManager.createTimer("Spawn Energizer", 3, 10000, true, "Left", angleModuloDivider);
		timerManager.addTimerToList(timer);
	}

	// Called by CustomTimers when they have to spawn an enemy
	public void spawnEnemy(String enemyType, int amountOfAttempts, String direction, int angleModuloDivider) {
		for (int i = 0; i < amountOfAttempts; i++) {
			
			//Hier afvangen waar hij moet spawnen op basis van schuine lijn
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();

			switch (enemyType) {
			case ("Alien Bomb"):
				xCoordinate = randomCoordinator.getRandomXBombEnemyCoordinate();
				spawnAlienBomb(xCoordinate);
				break;
			case ("Alien"):
				spawnAlien(xCoordinate, yCoordinate, direction, angleModuloDivider);
				break;
			case ("Bomba"):
				spawnBomba(xCoordinate, yCoordinate, direction, angleModuloDivider);
				break;
			case ("Flamer"):
				spawnFlamer(xCoordinate, yCoordinate, direction, angleModuloDivider);
				break;
			case ("Bulldozer"):
				spawnBulldozer(xCoordinate, yCoordinate, direction, angleModuloDivider);
				break;
			case ("Energizer"):
				spawnEnergizer(xCoordinate, yCoordinate, direction, angleModuloDivider);
				break;
			case ("Seeker"):
				spawnSeeker(xCoordinate, yCoordinate, direction, angleModuloDivider);
				break;
			case ("Tazer"):
				spawnTazer(xCoordinate, yCoordinate, direction, angleModuloDivider);
				break;
			}
		}
	}

	private void spawnAlienBomb(int xCoordinate) {
		String direction = randomCoordinator.upOrDown();
		int yCoordinate = 0;

		if (direction.equals("Up")) {
			yCoordinate = randomCoordinator.getRandomYUpBombEnemyCoordinate();
		} else if (direction.equals("Down")) {
			yCoordinate = randomCoordinator.getRandomYDownBombEnemyCoordinate();
		}

		Enemy enemy = new AlienBomb(xCoordinate, yCoordinate, direction, 0);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnSeeker(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider) {
		Enemy enemy = new Seeker(xCoordinate, yCoordinate, direction, angleModuloDivider);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnTazer(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider) {
		Enemy enemy = new Tazer(xCoordinate, yCoordinate, direction, angleModuloDivider);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnFlamer(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider) {
		Enemy enemy = new Flamer(xCoordinate, yCoordinate, direction, angleModuloDivider);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnBomba(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider) {
		Enemy enemy = new Bomba(xCoordinate, yCoordinate, direction, angleModuloDivider);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnBulldozer(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider) {
		Enemy enemy = new Bomba(xCoordinate, yCoordinate, direction, angleModuloDivider);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnEnergizer(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider) {
		Enemy enemy = new Bomba(xCoordinate, yCoordinate, direction, angleModuloDivider);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	private void spawnAlien(int xCoordinate, int yCoordinate, String direction, int angleModuloDivider) {
		Enemy enemy = new Alien(xCoordinate, yCoordinate, direction, 0);
		if (validCoordinates(enemy)) {
			enemyManager.addEnemy(enemy);
		}
	}

	// Called by all spawn*Enemy* methods, returns true if there is no overlap
	// between enemies of the same type
	private boolean validCoordinates(Enemy enemy) {
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), enemy.getXCoordinate(),
				enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(),
						enemy.getYCoordinate(), enemy.getHeight())) {
			return true;
		}
		return false;
	}

}
