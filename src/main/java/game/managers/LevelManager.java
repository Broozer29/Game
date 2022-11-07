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
		if (enemyManager.getEnemies().size() <= 0) {
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

			if (timesTriedToSpawn > 100) {
				break;
			}
			timesTriedToSpawn++;

			String direction = randomCoordinator.upOrDown();
			int xCoordinate = randomCoordinator.getRandomXBombEnemyCoordinate();
			int yCoordinate = 0;

			if (direction.equals("Up")) {
				yCoordinate = randomCoordinator.getRandomYUpBombEnemyCoordinate();
			} else if (direction.equals("Down")) {
				yCoordinate = randomCoordinator.getRandomYDownBombEnemyCoordinate();
			}

			if (randomCoordinator.checkValidEnemyXCoordinate(enemyManager.getEnemies(), xCoordinate, 5)
					&& randomCoordinator.checkValidEnemyYCoordinate(enemyManager.getEnemies(), yCoordinate, 5)) {
				enemyManager.addBombEnemy(xCoordinate, yCoordinate, "Alien bomb", direction);
				bombsSpawned++;
			}
		}
	}

	private void saturateLevelOne() {
		while (enemyManager.getEnemies().size() < 10) {
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();
			if (randomCoordinator.checkValidEnemyXCoordinate(enemyManager.getEnemies(), xCoordinate, 20)
					&& randomCoordinator.checkValidEnemyYCoordinate(enemyManager.getEnemies(), yCoordinate, 20)) {
				enemyManager.addEnemy(xCoordinate, yCoordinate, "Alien");
			}
		}
		
		System.out.println("Added bombs");
		timerManager.createTimer("SpawnBombs");
	}

	private void saturateLevelTwo() {
		for (int i = 0; i < 12; i++) {
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Alien");
		}
	}

	private void saturateLevelThree() {
		for (int i = 0; i < 14; i++) {
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();
			enemyManager.addEnemy(xCoordinate, yCoordinate, "Alien");
		}
	}

}
