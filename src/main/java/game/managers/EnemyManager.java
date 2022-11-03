package game.managers;

import java.util.ArrayList;
import java.util.List;

import data.DataClass;
import game.objects.BoardBlock;
import game.objects.Enemy;

import java.awt.Rectangle;

public class EnemyManager {

	private static EnemyManager instance = new EnemyManager();
	private List<Enemy> enemyList = new ArrayList<Enemy>();
	private List<BoardBlock> boardBlockList = new ArrayList<BoardBlock>();
	private int maxBoardBlocks = 8;
	private DataClass dataClass = DataClass.getInstance();


	private EnemyManager() {
		saturateBoardBlockList();
	}

	public static EnemyManager getInstance() {
		return instance;
	}

	public void updateGameTick() {
		updateEnemies();
		updateEnemyBoardBlocks();
		triggerEnemyAction();
	}

	private void saturateBoardBlockList() {
		int widthPerBlock = dataClass.getWindowWidth() / maxBoardBlocks;
		int heightPerBlock = dataClass.getWindowHeight();

		// i < total amount of board blocks. The amount of board block speed settings in
		// Enemy.java HAS TO REFLECT THIS.
		for (int i = 0; i < maxBoardBlocks; i++) {
			BoardBlock newBoardBlock = new BoardBlock(widthPerBlock, heightPerBlock, (i * widthPerBlock), 0, i);
			boardBlockList.add(newBoardBlock);
		}

	}

	private void triggerEnemyAction() {
		for (Enemy enemy : enemyList) {
			enemy.fireAction();
		}
	}

	private void updateEnemyBoardBlocks() {
		for (BoardBlock boardBlock : boardBlockList) {
			for (Enemy enemy : enemyList) {
				if (boardBlock.getBounds().intersects(enemy.getBounds())) {
					enemy.updateBoardBlock(boardBlock.getBoardBlockNumber());
				}
			}
		}
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

	private Enemy createEnemy(int xCoordinate, int yCoordinate, String enemyType) {
		return new Enemy(xCoordinate, yCoordinate, enemyType, maxBoardBlocks);
	}

	public void addBombEnemy(int xCoordinte, int yCoordinate, String enemyType, String direction) {
		Enemy enemy = createEnemy(xCoordinte, yCoordinate, enemyType);
		enemy.setRotation(direction);
		enemy.setVisible(true);
		this.enemyList.add(enemy);
	}

	public void addEnemy(int xCoordinate, int yCoordinate, String enemyType) {
		Enemy enemy = createEnemy(xCoordinate, yCoordinate, enemyType);
		enemy.setVisible(true);
		this.enemyList.add(enemy);
	}

	private void removeEnemy(Enemy enemy) {
		this.enemyList.remove(enemy);
	}

	public List<Enemy> getEnemies() {
		return this.enemyList;
	}

}
