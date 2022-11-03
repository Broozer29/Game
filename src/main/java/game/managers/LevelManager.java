package game.managers;

import java.util.List;
import java.util.Random;

import data.RandomCoordinator;
import game.objects.Enemy;

public class LevelManager {

	private static LevelManager instance = new LevelManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private RandomCoordinator randomCoordinator = RandomCoordinator.getInstance();

	private int level = 1;

	private LevelManager() {
		level = 1;
	}

	public static LevelManager getInstance() {
		return instance;
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

	private void saturateLevelOne() {
		while (enemyManager.getEnemies().size() < 10) {
			int xCoordinate = randomCoordinator.getRandomXEnemyCoordinate();
			int yCoordinate = randomCoordinator.getRandomYEnemyCoordinate();
			if (randomCoordinator.checkValidEnemyXCoordinate(enemyManager.getEnemies(), xCoordinate)
					&& randomCoordinator.checkValidEnemyYCoordinate(enemyManager.getEnemies(), yCoordinate)) {
				enemyManager.addEnemy(xCoordinate, yCoordinate, "Alien");
			}

		}
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
