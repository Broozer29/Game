package game.managers;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import data.DataClass;
import game.objects.BoardBlock;
import game.objects.enemies.Alien;
import game.objects.enemies.AlienBomb;
import game.objects.enemies.Enemy;

public class EnemyManager {

	private static EnemyManager instance = new EnemyManager();
	private AudioManager audioManager = AudioManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private List<Enemy> enemyList = new ArrayList<Enemy>();
	private List<Alien> alienList = new ArrayList<Alien>();
	private List<AlienBomb> alienBombList = new ArrayList<AlienBomb>();
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
		alienBombList = new ArrayList<AlienBomb>();
		alienList = new ArrayList<Alien>();
		boardBlockList = new ArrayList<BoardBlock>();
		friendlyManager = FriendlyManager.getInstance();
		dataClass = DataClass.getInstance();
		audioManager = AudioManager.getInstance();
		maxBoardBlocks = 8;
		saturateBoardBlockList();
	}

	public void updateGameTick() {
		try {
			updateEnemies();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
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
				friendlyManager.getSpaceship().takeHitpointDamage(1);
			}
		}
	}

	private void triggerEnemyAction() {
		for (Alien alien : alienList) {
			if (alien.getHasAttack()) {
				alien.fireAction();
			}
		}
	}

	private void updateEnemyBoardBlocks() {
		for (BoardBlock boardBlock : boardBlockList) {
			for (Alien alien : alienList) {
				if (boardBlock.getBounds().intersects(alien.getBounds())) {
					if (boardBlock.getBoardBlockNumber() != alien.getBoardBlockNumber()) {
						alien.updateBoardBlock(boardBlock.getBoardBlockNumber());
					}
				}
			}
		}
	}

	private void updateEnemies() throws UnsupportedAudioFileException, IOException {
		for (int i = 0; i < alienList.size(); i++) {
			Alien alien = alienList.get(i);
			if (alien.isVisible()) {
				alien.move();
			} else {
				if (alien.getCurrentHitpoints() < 0) {
					triggerEnemyDeathSound(alien);
				}
				removeAlienSpaceship(alien);
				removeEnemy(alien);
			}
		}
		
		for (int i = 0; i < alienBombList.size(); i++) {
			AlienBomb alienBomb = alienBombList.get(i);
			if (alienBomb.isVisible()) {
				alienBomb.move();
			} else {
				if (alienBomb.getCurrentHitpoints() < 0) {
					triggerEnemyDeathSound(alienBomb);
				}
				removeAlienBomb(alienBomb);
				removeEnemy(alienBomb);
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

	// Called by LevelManager, creates an unambiguous enemy and adds it to enemies
	public void addEnemy(int xCoordinate, int yCoordinate, String enemyType, String direction) {
		Enemy enemy = null;
		switch (enemyType) {
		case ("Alien Bomb"):
			AlienBomb alienBomb = new AlienBomb(xCoordinate, yCoordinate, enemyType, direction);
			enemy = alienBomb;
			alienBombList.add(alienBomb);
			break;
		case ("Default Alien Spaceship"):
			Alien alien = new Alien(xCoordinate, yCoordinate, enemyType, direction);
			enemy = alien;
			alienList.add(alien);
			break;
		}

		this.enemyList.add(enemy);
		keepTrackOfEnemies();
	}

	private void removeAlienSpaceship(Alien alien) {
		this.alienList.remove(alien);
	}

	private void removeAlienBomb(AlienBomb alienBomb) {
		this.alienBombList.remove(alienBomb);
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
