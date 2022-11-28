package data;

import java.util.List;
import java.util.Random;

import game.objects.enemies.Enemy;
import image.objects.BackgroundObject;

public class RandomCoordinator {

	private static RandomCoordinator instance = new RandomCoordinator();
	private Random random = new Random();

	// Al deze ranges moeten eigenlijk dynamisch berekend worden, want nu is het
	// niet resizable
	private int maximumEnemyWidthRange = DataClass.getInstance().getWindowWidth() + 500;
	private int minimumEnemyWidthRange = DataClass.getInstance().getWindowWidth();
	private int maximumEnemyHeightRange = DataClass.getInstance().getWindowHeight() - 100;
	private int minimumEnemyHeightRange = 100;

	private int maximumBGOWidthRange = DataClass.getInstance().getWindowWidth() + 200;
	private int minimumBGOWidthRange = -200;
	private int maximumBGOHeightRange = DataClass.getInstance().getWindowHeight() + 50;
	private int minimumBGOHeightRange = -50;

	private int maximumBombEnemyWidthRange = DataClass.getInstance().getWindowWidth() - 250;
	private int minimumBombEnemyWidthRange = 250;
	private int maximumBombEnemyHeightUpRange = DataClass.getInstance().getWindowHeight() + 500;
	private int minimumBombEnemyHeightUpRange = DataClass.getInstance().getWindowHeight();

	private int maximumBombEnemyHeightDownRange = 0;
	private int minimumBombEnemyHeightDownRange = -500;

	private RandomCoordinator() {

	}

	public static RandomCoordinator getInstance() {
		return instance;
	}

	//Function used to prevent enemies of the same type of stacking on top of each other when being spawned in
	public boolean checkValidEnemyXCoordinate(String enemyType, List<Enemy> listToCheck, int xCoordinate, int minimumRange) {
		for (Enemy enemy : listToCheck) {
			if (enemy.getEnemyType().equals(enemyType)) {
				if (Math.abs(enemy.getXCoordinate() - xCoordinate) < minimumRange) {
					return false;
				}
			}
		}
		return true;
	}

	//Function used to prevent enemies of the same type of stacking on top of each other when being spawned in
	public boolean checkValidEnemyYCoordinate(String enemyType, List<Enemy> listToCheck, int yCoordinate, int minimumRange) {
		for (Enemy enemy : listToCheck) {
			if (enemy.getEnemyType().equals(enemyType)) {
				if (Math.abs(enemy.getYCoordinate() - yCoordinate) < minimumRange) {
					return false;
				}
			}
		}
		return true;
	}

	public int getRandomXEnemyCoordinate() {
		return random.nextInt((maximumEnemyWidthRange - minimumEnemyWidthRange) + 1) + minimumEnemyWidthRange;
	}

	public int getRandomYEnemyCoordinate() {
		return random.nextInt((maximumEnemyHeightRange - minimumEnemyHeightRange) + 1) + minimumEnemyHeightRange;
	}

	public int getRandomXBombEnemyCoordinate() {
		return random.nextInt((maximumBombEnemyWidthRange - minimumBombEnemyWidthRange) + 1)
				+ minimumBombEnemyWidthRange;
	}

	public int getRandomYUpBombEnemyCoordinate() {
		return random.nextInt((maximumBombEnemyHeightUpRange - minimumBombEnemyHeightUpRange) + 1)
				+ minimumBombEnemyHeightUpRange;
	}

	public int getRandomYDownBombEnemyCoordinate() {
		return random.nextInt((maximumBombEnemyHeightDownRange - minimumBombEnemyHeightDownRange) + 1)
				+ minimumBombEnemyHeightDownRange;
	}

	public String upOrDown() {
		int randInt = random.nextInt((1 - 0) + 1) + 0;
		switch (randInt) {
		case (0):
			return "Down";
		case (1):
			return "Up";
		}
		return "Up";
	}

	// Random functions used for Background objects //
	public boolean checkValidBGOXCoordinate(List<BackgroundObject> listToCheck, int xCoordinate) {
		for (BackgroundObject bgObject : listToCheck) {
			if (Math.abs(bgObject.getXCoordinate() - xCoordinate) < 20) {
				return false;
			}
		}
		return true;
	}

	public boolean checkValidBGOYCoordinate(List<BackgroundObject> listToCheck, int yCoordinate) {
		for (BackgroundObject bgObject : listToCheck) {
			if (Math.abs(bgObject.getYCoordinate() - yCoordinate) < 20) {
				return false;
			}
		}
		return true;
	}

	public int getRandomXBGOCoordinate() {
		return random.nextInt((maximumBGOWidthRange - minimumBGOWidthRange) + 1) + minimumBGOWidthRange;
	}

	public int getRandomYBGOCoordinate() {
		return random.nextInt((maximumBGOHeightRange - minimumBGOHeightRange) + 1) + minimumBGOHeightRange;
	}

}
