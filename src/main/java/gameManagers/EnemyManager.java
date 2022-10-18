package gameManagers;

import java.util.ArrayList;
import java.util.List;

import gameObjectes.Enemy;

public class EnemyManager {

	private static EnemyManager instance = new EnemyManager();
	private List<Enemy> enemyList = new ArrayList<Enemy>();

	private EnemyManager() {
	}

	public static EnemyManager getInstance() {
		return instance;
	}
	
	public void updateGameTick() {
		updateEnemies();
	}

	private void updateEnemies() {
		for (int i = 0; i < enemyList.size(); i++) {
			Enemy enemy = enemyList.get(i);
			if (enemy.isVisible()) {
				enemy.move();
			} else {
				removeEnemy(enemy);
			}
		}
	}

	public void addEnemy(Enemy enemy) {
		enemy.setVisible(true);
		this.enemyList.add(enemy);
	}

	public void removeEnemy(Enemy enemy) {
		this.enemyList.remove(enemy);
	}

	public List<Enemy> getEnemies() {
		return this.enemyList;
	}

}
