package game.managers;

import data.RandomCoordinator;

public class LevelManager {

	private static LevelManager instance = new LevelManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private RandomCoordinator randomCoordinator = RandomCoordinator.getInstance();
	private TimerManager timerManager = TimerManager.getInstance();
	private int bombQuota = 20;
	private int level = 1;

	private LevelManager() {
	}

	public static LevelManager getInstance() {
		return instance;
	}

	public void resetManager() {
		bombQuota = 20;
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

	public void spawnBombs() {
		saturateBombList();
	}

	private void checkLevelUpdate() {
		if (enemyManager.getDefaultAlienSpaceshipCount() <= 0) {
			levelUp();
			startLevel();
		}
	}

	private void setLevel(int level) {
		switch (level) {
		case (1):
			this.saturateLevelOne();
			return;
		case (2):
			this.saturateLevelTwo();
			return;
		case (3):
			this.saturateLevelThree();
			return;
		}
	}

	private void saturateBombList() {
		int bombsSpawned = 0;
		int timesTriedToSpawn = 0;
		while (bombsSpawned < bombQuota) {
			timesTriedToSpawn++;
			if (timesTriedToSpawn > 100) {
				break;
			}
			String direction = randomCoordinator.upOrDown();
			int xCoordinate = randomCoordinator.getRandomXBombEnemyCoordinate();
			int yCoordinate = 0;

			if (direction.equals("Up")) {
				yCoordinate = randomCoordinator.getRandomYUpBombEnemyCoordinate();
			} else if (direction.equals("Down")) {
				yCoordinate = randomCoordinator.getRandomYDownBombEnemyCoordinate();
			}

			if (randomCoordinator.checkValidEnemyXCoordinate("Alien Bomb", enemyManager.getEnemies(), xCoordinate, 5)
					&& randomCoordinator.checkValidEnemyYCoordinate("Alien Bomb", enemyManager.getEnemies(), yCoordinate, 5)) {
				enemyManager.addEnemy(xCoordinate, yCoordinate, "Alien Bomb", direction);
				bombsSpawned++;
			}
		}
	}

	private void saturateLevelOne() {
		while (enemyManager.getDefaultAlienSpaceshipCount() < 10) {
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();

			if (randomCoordinator.checkValidEnemyXCoordinate("Default Alien Spaceship", enemyManager.getEnemies(),
					xCoordinate, 20)
					&& randomCoordinator.checkValidEnemyYCoordinate("Default Alien Spaceship",
							enemyManager.getEnemies(), yCoordinate, 20)) {
				enemyManager.addEnemy(xCoordinate, yCoordinate, "Default Alien Spaceship", "Left");
			}
		}
		timerManager.createTimer("SpawnBombs");
	}

	private void saturateLevelTwo() {
		while (enemyManager.getDefaultAlienSpaceshipCount() < 12) {
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();

			if (randomCoordinator.checkValidEnemyXCoordinate("Default Alien Spaceship", enemyManager.getEnemies(),
					xCoordinate, 20)
					&& randomCoordinator.checkValidEnemyYCoordinate("Default Alien Spaceship",
							enemyManager.getEnemies(), yCoordinate, 20)) {
				enemyManager.addEnemy(xCoordinate, yCoordinate, "Default Alien Spaceship", "Left");
			}
		}
	}

	private void saturateLevelThree() {
		while (enemyManager.getDefaultAlienSpaceshipCount() < 14) {
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();
			if (randomCoordinator.checkValidEnemyXCoordinate("Default Alien Spaceship", enemyManager.getEnemies(),
					xCoordinate, 20)
					&& randomCoordinator.checkValidEnemyYCoordinate("Default Alien Spaceship",
							enemyManager.getEnemies(), yCoordinate, 20)) {
				enemyManager.addEnemy(xCoordinate, yCoordinate, "Default Alien Spaceship", "Left");
			}
		}
	}

}
