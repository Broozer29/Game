package game.managers;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import game.objects.BoardBlock;
import game.objects.Enemy;

import java.awt.Rectangle;
import java.io.IOException;

public class EnemyManager {

	private static EnemyManager instance = new EnemyManager();
	private AudioManager audioManager = AudioManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private List<Enemy> enemyList = new ArrayList<Enemy>();
	private List<BoardBlock> boardBlockList = new ArrayList<BoardBlock>();
	private int maxBoardBlocks = 8;
	private DataClass dataClass = DataClass.getInstance();

	private int defaultAlienSpaceshipCount;
	private int alienBombCount;

	private EnemyManager() {
		saturateBoardBlockList();
	}

	public static EnemyManager getInstance() {
		return instance;
	}

	// Called when a game instance needs to be deleted and the manager needs to be
	// reset.
	public void resetManager() {
		enemyList = new ArrayList<Enemy>();
		boardBlockList = new ArrayList<BoardBlock>();
		friendlyManager = FriendlyManager.getInstance();
		dataClass = DataClass.getInstance();
		audioManager = AudioManager.getInstance();
		maxBoardBlocks = 8;
		saturateBoardBlockList();
	}

	public void updateGameTick() {
		updateEnemies();
		updateEnemyBoardBlocks();
		triggerEnemyAction();
		checkSpaceshipCollisions();
		keepTrackOfEnemies();
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

	public void keepTrackOfEnemies() {
		int defaultSpaceShipCounter = 0;
		int alienBombCounter = 0;

		for (Enemy enemy : enemyList) {
			if (enemy.getEnemyType().equals("Default Alien Spaceship")) {
				defaultSpaceShipCounter++;
			}
			if (enemy.getEnemyType().equals("Alien Bomb")) {
				alienBombCounter++;
			}
		}

		defaultAlienSpaceshipCount = defaultSpaceShipCounter;
		alienBombCount = alienBombCounter;

	}

	private void checkSpaceshipCollisions() {
		if (friendlyManager == null) {
			this.friendlyManager = FriendlyManager.getInstance();
		}
		Rectangle spaceshipBounds = friendlyManager.getSpaceship().getBounds();

		// Checks collision between spaceship and enemies
		for (Enemy enemy : enemyList) {
			Rectangle alienBounds = enemy.getBounds();
			if (spaceshipBounds.intersects(alienBounds)) {
				friendlyManager.getSpaceship().takeHitpointDamage(5);
			}
		}
	}

	private void triggerEnemyAction() {
		for (Enemy enemy : enemyList) {
			if (enemy.getHasAttack()) {
				enemy.fireAction();
			}
		}
	}

	private void updateEnemyBoardBlocks() {
		for (BoardBlock boardBlock : boardBlockList) {
			for (Enemy enemy : enemyList) {
				if (boardBlock.getBounds().intersects(enemy.getBounds())) {
					if (boardBlock.getBoardBlockNumber() != enemy.getBoardBlockNumber()) {
						enemy.updateBoardBlock(boardBlock.getBoardBlockNumber());
					}
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
				if (enemy.getCurrentHitpoints() <= 0) {
					try {
						triggerEnemyDeathSound(enemy);
					} catch (UnsupportedAudioFileException | IOException e) {
						e.printStackTrace();
					}
				}
				removeEnemy(enemy);
			}
		}
	}

	private void triggerEnemyDeathSound(Enemy enemy) throws UnsupportedAudioFileException, IOException {
		switch (enemy.getEnemyType()) {
		case ("Alien Bomb"):
			audioManager.addAudio("Destroyed Explosion");
			break;
		case ("Default Alien Spaceship"):
			audioManager.addAudio("Alien Spaceship Destroyed");
			break;

		}

	}

	private Enemy createEnemy(int xCoordinate, int yCoordinate, String enemyType, String direction) {
		return new Enemy(xCoordinate, yCoordinate, enemyType, maxBoardBlocks, direction);
	}

	// Called by LevelManager, creates an unambiguous enemy and adds it to enemies
	public void addEnemy(int xCoordinate, int yCoordinate, String enemyType, String direction) {
		Enemy enemy = createEnemy(xCoordinate, yCoordinate, enemyType, direction);
		enemy.setVisible(true);
		this.enemyList.add(enemy);
		keepTrackOfEnemies();
	}

	private void removeEnemy(Enemy enemy) {
		this.enemyList.remove(enemy);
	}

	public List<Enemy> getEnemies() {
		return this.enemyList;
	}

	public int getDefaultAlienSpaceshipCount() {
		return defaultAlienSpaceshipCount;
	}

	public int getAlienBombCount() {
		return alienBombCount;
	}

}
