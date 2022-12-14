package data;

import java.util.List;
import java.util.Random;

import game.objects.enemies.Enemy;
import image.objects.BackgroundObject;

public class SpawningCoordinator {

	private static SpawningCoordinator instance = new SpawningCoordinator();
	private Random random = new Random();

	// Al deze ranges moeten eigenlijk dynamisch berekend worden, want nu is het
	// niet resizable


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
	
	//Left Spawning block
	private int leftEnemyMaxHeightRange = DataClass.getInstance().getWindowHeight() - 100;
	private int leftEnemyMinHeightRange = 100;
	private int leftEnemyMaxWidthRange = 0 - 500;
	private int leftEnemyMinWidthRange = 0 - 100;
	
	//Right Spawning block
	private int rightEnemyMaxWidthRange = DataClass.getInstance().getWindowWidth() + 500;
	private int rightEnemyMinWidthRange = DataClass.getInstance().getWindowWidth();
	private int rightEnemyMaxHeightRange = DataClass.getInstance().getWindowHeight() - 100;
	private int rightEnemyMinHeightRange = 100;
	
	//Up spawning block
	private int upEnemyMaxWidthRange = DataClass.getInstance().getWindowWidth() - 50;
	private int upEnemyMinWidthRange = 50;
	private int upEnemyMaxHeightRange = -200;
	private int upEnemyMinHeightRange = -100;
	
	//Down spawning block
	private int downEnemyMaxWidthRange = DataClass.getInstance().getWindowWidth() - 50;
	private int downEnemyMinWidthRange = 50;
	private int downEnemyMaxHeightRange = DataClass.getInstance().getWindowHeight() + 200;
	private int downEnemyMinHeightRange = DataClass.getInstance().getWindowHeight() + 100;

	private SpawningCoordinator() {

	}

	public static SpawningCoordinator getInstance() {
		return instance;
	}

	//Function used to prevent enemies of the same type of stacking on top of each other when being spawned in
	public boolean checkValidEnemyXCoordinate(Enemy enemyType, List<Enemy> listToCheck, int xCoordinate, int minimumRange) {
		for (Enemy enemy : listToCheck) {
			if (enemy.getClass().equals(enemyType.getClass())) {
				if (Math.abs(enemy.getXCoordinate() - xCoordinate) < minimumRange) {
					return false;
				}
			}
		}
		return true;
	}

	//Function used to prevent enemies of the same type of stacking on top of each other when being spawned in
	public boolean checkValidEnemyYCoordinate(Enemy enemyType, List<Enemy> listToCheck, int yCoordinate, int minimumRange) {
		for (Enemy enemy : listToCheck) {
			if (enemy.getClass().equals(enemyType.getClass())) {
				if (Math.abs(enemy.getYCoordinate() - yCoordinate) < minimumRange) {
					return false;
				}
			}
		}
		return true;
	}
	

	public int getRightBlockXCoordinate() {
		return random.nextInt((rightEnemyMaxWidthRange - rightEnemyMinWidthRange) + 1) + rightEnemyMinWidthRange;
	}

	public int getRightBlockYCoordinate() {
		return random.nextInt((rightEnemyMaxHeightRange - rightEnemyMinHeightRange) + 1) + rightEnemyMinHeightRange;
	}
	
	public int getLeftBlockXCoordinate() {
		return random.nextInt((leftEnemyMaxWidthRange - leftEnemyMinWidthRange) + 1) + leftEnemyMinWidthRange;
	}

	public int getLeftBlockYCoordinate() {
		return random.nextInt((leftEnemyMaxHeightRange - leftEnemyMinHeightRange) + 1) + leftEnemyMinHeightRange;
	}
	
	public int getUpBlockXCoordinate() {
		return random.nextInt((upEnemyMaxWidthRange - upEnemyMinWidthRange) + 1) + upEnemyMinWidthRange;
	}

	public int getUpBlockYCoordinate() {
		return random.nextInt((upEnemyMaxHeightRange - upEnemyMinHeightRange) + 1) + upEnemyMinHeightRange;
	}
	
	public int getDownBlockXCoordinate() {
		return random.nextInt((downEnemyMaxWidthRange - downEnemyMinWidthRange) + 1) + downEnemyMinWidthRange;
	}

	public int getDownBlockYCoordinate() {
		return random.nextInt((downEnemyMaxHeightRange - downEnemyMinHeightRange) + 1) + downEnemyMinHeightRange;
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
	public boolean checkValidBGOXCoordinate(List<BackgroundObject> listToCheck, int xCoordinate, int size) {
		for (BackgroundObject bgObject : listToCheck) {
			if (Math.abs(bgObject.getXCoordinate() - xCoordinate) < size) {
				return false;
			}
		}
		return true;
	}

	public boolean checkValidBGOYCoordinate(List<BackgroundObject> listToCheck, int yCoordinate, int size) {
		for (BackgroundObject bgObject : listToCheck) {
			if (Math.abs(bgObject.getYCoordinate() - yCoordinate) < size) {
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
