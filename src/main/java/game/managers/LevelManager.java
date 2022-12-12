package game.managers;

import data.RandomCoordinator;
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
	private RandomCoordinator randomCoordinator = RandomCoordinator.getInstance();
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

	//Spawn command, spawn tries, amount of time required to pass
	private void saturateLevelOne() {
		timerManager.createTimer("Spawn Bombs", 20, 1000);
		timerManager.createTimer("Spawn Bomba", 3, 5000);
	}

	// Called by CustomTimers when they have to spawn an enemy
	public void spawnEnemy(String enemyType, int amountOfAttempts) {
		for (int i = 0; i < amountOfAttempts; i++) {
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();
			
			switch(enemyType) {
			case("Alien Bomb"):
				xCoordinate = randomCoordinator.getRandomXBombEnemyCoordinate();
				spawnAlienBomb(xCoordinate);
				break;
			case("Alien"):
				spawnAlien(xCoordinate, yCoordinate);
				break;
			case("Bomba"):
				spawnBomba(xCoordinate, yCoordinate);
				break;
			case("Flamer"):
				spawnFlamer(xCoordinate, yCoordinate);
				break;
			case("Bulldozer"):
				spawnBulldozer(xCoordinate, yCoordinate);
				break;
			case("Energizer"):
				spawnEnergizer(xCoordinate, yCoordinate);
				break;
			case("Seeker"):
				spawnSeeker(xCoordinate, yCoordinate);
				break;
			case("Tazer"):
				spawnTazer(xCoordinate, yCoordinate);
				break;
			}
		}
	}

	private void spawnAlien(int xCoordinate, int yCoordinate) {
		Enemy enemy = new Alien(xCoordinate, yCoordinate, "Left");
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), xCoordinate, enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(), yCoordinate, enemy.getHeight())) {
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Alien", "Left");
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

		Enemy enemy = new AlienBomb(xCoordinate, yCoordinate, direction);
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), xCoordinate, enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(), yCoordinate, enemy.getHeight())) {
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Alien Bomb", direction);
		}
	}

	private void spawnSeeker(int xCoordinate, int yCoordinate) {
		Enemy enemy = new Seeker(xCoordinate, yCoordinate, "Left");
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), xCoordinate, enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(), yCoordinate, enemy.getHeight())) {
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Seeker", "Left");
		}
	}

	private void spawnTazer(int xCoordinate, int yCoordinate) {
		Enemy enemy = new Tazer(xCoordinate, yCoordinate, "Left");
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), xCoordinate, enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(), yCoordinate, enemy.getHeight())) {
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Tazer", "Left");
		}
	}

	private void spawnFlamer(int xCoordinate, int yCoordinate) {
		Enemy enemy = new Flamer(xCoordinate, yCoordinate, "Left");
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), xCoordinate, enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(), yCoordinate, enemy.getHeight())) {
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Flamer", "Left");
		}
	}

	private void spawnBomba(int xCoordinate, int yCoordinate) {
		Enemy enemy = new Bomba(xCoordinate, yCoordinate, "Left");
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), xCoordinate, enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(), yCoordinate, enemy.getHeight())) {
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Bomba", "Left");
		}
	}

	private void spawnBulldozer(int xCoordinate, int yCoordinate) {
		Enemy enemy = new Bomba(xCoordinate, yCoordinate, "Left");
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), xCoordinate, enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(), yCoordinate, enemy.getHeight())) {
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Bulldozer", "Left");
		}
	}

	private void spawnEnergizer(int xCoordinate, int yCoordinate) {
		Enemy enemy = new Bomba(xCoordinate, yCoordinate, "Left");
		if (randomCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), xCoordinate, enemy.getWidth())
				&& randomCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(), yCoordinate, enemy.getHeight())) {
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Energizer", "Left");
		}
	}

}
