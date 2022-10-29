package gameManagers;

import java.util.ArrayList;
import java.util.List;

import Data.DataClass;
import gameObjects.BoardBlock;
import gameObjects.Enemy;
import java.awt.Rectangle;

public class EnemyManager {

	private static EnemyManager instance = new EnemyManager();
	private List<Enemy> enemyList = new ArrayList<Enemy>();
	private List<BoardBlock> boardBlockList = new ArrayList<BoardBlock>();

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
		DataClass dataClass = DataClass.getInstance();
		int widthPerBlock = dataClass.getWindowWidth() / 5;
		int heightPerBlock = dataClass.getWindowHeight();
		
		for (int i = 0; i < 5; i++) {
			BoardBlock newBoardBlock = new BoardBlock(widthPerBlock, heightPerBlock, (i*widthPerBlock), 0, i);
			boardBlockList.add(newBoardBlock);
		}
		
	}
	
	private void triggerEnemyAction() {
		for(Enemy enemy : enemyList) {
			enemy.fireAction();
		}
	}
	
	private void updateEnemyBoardBlocks() {
		for(BoardBlock boardBlock : boardBlockList) {
			for(Enemy enemy : enemyList) {
				if(boardBlock.getBounds().intersects(enemy.getBounds())) {
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
		return new Enemy(xCoordinate, yCoordinate, enemyType);
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
