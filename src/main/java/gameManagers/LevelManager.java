package gameManagers;

import java.util.Random;

import Data.DataClass;
import gameObjectes.Alien;
import gameObjectes.Enemy;

public class LevelManager {

	
	private static LevelManager instance = new LevelManager();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private int maximumWidthRange = DataClass.getInstance().getWindowWidth();
	private int minimumWidthRange = DataClass.getInstance().getWindowWidth() / 3;
	private int maximumHeightRange = DataClass.getInstance().getWindowHeight() - 100;
	private int minimumHeightRange = 100;
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
	

	//Called when all aliens are dead
	public void levelUp() {
		this.level += 1;
	}
	
	//Called when a level starts, to saturate enemy list
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
		switch(level) {
			case(1):
				this.setLevelOne();
				return;
			case(2):
				this.setLevelTwo();
				return;
			case(3):
				this.setLevelThree();
				return;
		}
	}

	private void setLevelOne() {
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			int xCoordinate = random.nextInt((maximumWidthRange - minimumWidthRange) + 1) + minimumWidthRange;
			int yCoordinate = random.nextInt((maximumHeightRange - minimumHeightRange) + 1) + minimumHeightRange;
			Enemy newEnemy = new Alien(xCoordinate, yCoordinate, "Alien");
			enemyManager.addEnemy(newEnemy);
		}
	}
	
	private void setLevelTwo() {
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			int xCoordinate = random.nextInt((maximumWidthRange - minimumWidthRange) + 1) + minimumWidthRange;
			int yCoordinate = random.nextInt((maximumHeightRange - minimumHeightRange) + 1) + minimumHeightRange;

			Enemy newEnemy = new Alien(xCoordinate, yCoordinate, "Alien");
			enemyManager.addEnemy(newEnemy);
		}
	}
	
	private void setLevelThree() {
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			int xCoordinate = random.nextInt((maximumWidthRange - minimumWidthRange) + 1) + minimumWidthRange;
			int yCoordinate = random.nextInt((maximumHeightRange - minimumHeightRange) + 1) + minimumHeightRange;
			Enemy newEnemy = new Alien(xCoordinate, yCoordinate, "Alien");
			enemyManager.addEnemy(newEnemy);
		}
	}

}
