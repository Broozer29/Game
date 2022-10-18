package gameManagers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Data.DataClass;
import gameObjectes.Alien;
import gameObjectes.Enemy;

public class EnemyManager {

	private int maximumWidthRange = DataClass.getInstance().getWindowWidth();
	private int minimumWidthRange = DataClass.getInstance().getWindowWidth() / 3;
	private int maximumHeightRange = DataClass.getInstance().getWindowHeight() - 100;
	private int minimumHeightRange = 100;
	private int level = 1;
	
	private static EnemyManager instance = new EnemyManager();
	private List<Enemy> enemyList = new ArrayList<Enemy>();

	private EnemyManager() {
	}

	public static EnemyManager getInstance() {
		return instance;
	}

	

	public void addEnemy(Enemy enemy) {
		enemy.setVisible(true);
		this.enemyList.add(enemy);
	}

	public void removeEnemy(Enemy enemy) {
		this.enemyList.remove(enemy);
	}
	
	public List<Enemy> getEnemies(){
		return this.enemyList;
	}
	
	//Called when all aliens are dead
	public void levelUp() {
		this.level += 1;
	}
	
	//Called when a level starts, to saturate enemy list
	public void startLevel() {
		this.setLevel(this.level);
	}
	
	
	private void setLevel(int level) {
		switch(level) {
			case(1):
				this.setLevelOne();
				return;
			case(2):
				return;
			case(3):
				return;
		}
	}

	private void setLevelOne() {
		Random random = new Random();
		for (int i = 0; i < 30; i++) {
			int xCoordinate = random.nextInt((maximumWidthRange - minimumWidthRange) + 1) + minimumWidthRange;
			int yCoordinate = random.nextInt((maximumHeightRange - minimumHeightRange) + 1) + minimumHeightRange;
			Enemy newEnemy = new Alien(xCoordinate, yCoordinate, "Alien");
			addEnemy(newEnemy);
		}
	}
	
	private void setLevelTwo() {
		Random random = new Random();
		for (int i = 0; i < 40; i++) {
			int xCoordinate = random.nextInt((maximumWidthRange - minimumWidthRange) + 1) + minimumWidthRange;
			int yCoordinate = random.nextInt((maximumHeightRange - minimumHeightRange) + 1) + minimumHeightRange;

			Enemy newEnemy = new Alien(xCoordinate, yCoordinate, "Alien");
			addEnemy(newEnemy);
		}
	}
	
	private void setLevelThree() {
		Random random = new Random();
		for (int i = 0; i < 50; i++) {
			int xCoordinate = random.nextInt((maximumWidthRange - minimumWidthRange) + 1) + minimumWidthRange;
			int yCoordinate = random.nextInt((maximumHeightRange - minimumHeightRange) + 1) + minimumHeightRange;
			Enemy newEnemy = new Alien(xCoordinate, yCoordinate, "Alien");
			addEnemy(newEnemy);
		}
	}

}
