package data;

import java.util.List;
import java.util.Random;

import game.objects.Enemy;
import image.objects.BackgroundObject;

public class RandomCoordinator {

	private static RandomCoordinator instance = new RandomCoordinator();
	private Random random = new Random();

	private int maximumEnemyWidthRange = DataClass.getInstance().getWindowWidth() + 500;
	private int minimumEnemyWidthRange = DataClass.getInstance().getWindowWidth();
	private int maximumEnemyHeightRange = DataClass.getInstance().getWindowHeight() - 100;
	private int minimumEnemyHeightRange = 100;

	private int maximumBGOWidthRange = DataClass.getInstance().getWindowWidth() + 200;
	private int minimumBGOWidthRange = -200;
	private int maximumBGOHeightRange = DataClass.getInstance().getWindowHeight() + 50;
	private int minimumBGOHeightRange = -50;

	private RandomCoordinator() {

	}

	public static RandomCoordinator getInstance() {
		return instance;
	}

	// Random functions used for ENEMIES //
	public boolean checkValidEnemyXCoordinate(List<Enemy> listToCheck, int xCoordinate) {
		for (Enemy enemy : listToCheck) {
			if (Math.abs(enemy.getXCoordinate() - xCoordinate) < 20) {
				return false;
			}
		}
		return true;
	}

	public boolean checkValidEnemyYCoordinate(List<Enemy> listToCheck, int yCoordinate) {
		for (Enemy enemy : listToCheck) {
			if (Math.abs(enemy.getYCoordinate() - yCoordinate) < 20) {
				return false;
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
