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
import game.objects.enemies.Bomba;
import game.objects.enemies.Bulldozer;
import game.objects.enemies.Enemy;
import game.objects.enemies.Energizer;
import game.objects.enemies.Flamer;
import game.objects.enemies.Seeker;
import game.objects.enemies.Tazer;

public class EnemyManager {

	private static EnemyManager instance = new EnemyManager();
	private AudioManager audioManager = AudioManager.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();
	private AnimationManager animationManager = AnimationManager.getInstance();
	private List<Enemy> enemyList = new ArrayList<Enemy>();
	private List<Alien> alienList = new ArrayList<Alien>();
	private List<Seeker> seekerList = new ArrayList<Seeker>();
	private List<Bomba> bombaList = new ArrayList<Bomba>();
	private List<Flamer> flamerList = new ArrayList<Flamer>();
	private List<Bulldozer> bulldozerList = new ArrayList<Bulldozer>();
	private List<Tazer> tazerList = new ArrayList<Tazer>();
	private List<Energizer> energizerList = new ArrayList<Energizer>();
	private List<AlienBomb> alienBombList = new ArrayList<AlienBomb>();
	private List<BoardBlock> boardBlockList = new ArrayList<BoardBlock>();
	private int maxBoardBlocks = 8;
	private DataClass dataClass = DataClass.getInstance();

	private int defaultAlienSpaceshipCount;
	private int alienBombCount;
	private int seekerCount;

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
		seekerList = new ArrayList<Seeker>();
		bombaList = new ArrayList<Bomba>();
		flamerList = new ArrayList<Flamer>();
		bulldozerList = new ArrayList<Bulldozer>();
		tazerList = new ArrayList<Tazer>();
		energizerList = new ArrayList<Energizer>();
		friendlyManager = FriendlyManager.getInstance();
		dataClass = DataClass.getInstance();
		audioManager = AudioManager.getInstance();
		maxBoardBlocks = 8;
		saturateBoardBlockList();
	}

	public void updateGameTick() {
		try {
			updateEnemies();
			checkSpaceshipCollisions();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		updateEnemyBoardBlocks();
		triggerEnemyAction();
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
		int seekerCounter = 0;

		for (Enemy enemy : enemyList) {
			if (enemy instanceof Alien) {
				defaultSpaceShipCounter++;
			}
			if (enemy instanceof AlienBomb) {
				alienBombCounter++;
			}
			if (enemy instanceof Seeker) {
				seekerCounter++;
			}
		}
		
		defaultAlienSpaceshipCount = defaultSpaceShipCounter;
		alienBombCount = alienBombCounter;
		seekerCount = seekerCounter;
	}

	private void checkSpaceshipCollisions() throws UnsupportedAudioFileException, IOException {
		if (friendlyManager == null || animationManager == null) {
			this.animationManager = AnimationManager.getInstance();
			this.friendlyManager = FriendlyManager.getInstance();
		}
		Rectangle spaceshipBounds = friendlyManager.getSpaceship().getBounds();

		// Checks collision between spaceship and enemies
		for (Enemy enemy : enemyList) {
			Rectangle enemyBounds = enemy.getBounds();
			if (spaceshipBounds.intersects(enemyBounds)) {
				if (enemy instanceof AlienBomb) {
					friendlyManager.getSpaceship().takeHitpointDamage(20);
					animationManager.addUpperAnimation(enemy.getXCoordinate(), enemy.getYCoordinate(),
							"Alien Bomb Explosion", false);
					audioManager.addAudio("Alien Bomb Impact");
					enemy.setVisible(false);
				} else {
					friendlyManager.getSpaceship().takeHitpointDamage(1);
				}
			}
		}
	}

	private void triggerEnemyAction() {
		for (Alien alien : alienList) {
			if (alien.getHasAttack()) {
				alien.fireAction();
			}
		}
		for (Seeker seeker : seekerList) {
			if (seeker.getHasAttack()) {
				seeker.fireAction();
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

	private void updateAliens() throws UnsupportedAudioFileException, IOException {
		for (int i = 0; i < alienList.size(); i++) {
			Alien alien = alienList.get(i);
			if (alien.isVisible()) {
				alien.move();
				for (BoardBlock boardBlock : boardBlockList) {
					if (boardBlock.getBounds().intersects(alien.getBounds())) {
						if (boardBlock.getBoardBlockNumber() != alien.getBoardBlockNumber()) {
							alien.updateBoardBlock(boardBlock.getBoardBlockNumber());
						}
					}
				}

			} else {
				if (alien.getCurrentHitpoints() < 0) {
					triggerEnemyDeathSound(alien);
				}
				removeAlienSpaceship(alien);
				removeEnemy(alien);
			}
		}
	}

	private void updateAlienBombs() throws UnsupportedAudioFileException, IOException {
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

	private void updateSeekers() throws UnsupportedAudioFileException, IOException {
		for (int i = 0; i < seekerList.size(); i++) {
			Seeker seeker = seekerList.get(i);
			if (seeker.isVisible()) {
				seeker.move();
				for (BoardBlock boardBlock : boardBlockList) {
					if (boardBlock.getBounds().intersects(seeker.getBounds())) {
						if (boardBlock.getBoardBlockNumber() != seeker.getBoardBlockNumber()) {
							seeker.updateBoardBlock(boardBlock.getBoardBlockNumber());
						}
					}
				}
			} else {
				if (seeker.getCurrentHitpoints() < 0) {
					triggerEnemyDeathSound(seeker);
				}
				removeSeeker(seeker);
				removeEnemy(seeker);
			}
		}
	}

	private void updateEnemies() throws UnsupportedAudioFileException, IOException {
		updateAliens();
		updateAlienBombs();
		updateSeekers();

	}

	private void triggerEnemyDeathSound(Enemy enemy) throws UnsupportedAudioFileException, IOException {
		audioManager.addAudio(enemy.getDeathSound());
	}

	// Called by LevelManager, creates an unambiguous enemy and adds it to enemies
	public void addEnemy(int xCoordinate, int yCoordinate, String enemyType, String direction) {
		Enemy enemy = null;
		switch (enemyType) {
		case ("Alien Bomb"):
			AlienBomb alienBomb = new AlienBomb(xCoordinate, yCoordinate, direction);
			enemy = alienBomb;
			alienBombList.add(alienBomb);
			break;
		case ("Default Alien Spaceship"):
			Alien alien = new Alien(xCoordinate, yCoordinate, direction);
			enemy = alien;
			alienList.add(alien);
			break;
		case ("Seeker"):
			Seeker seeker = new Seeker(xCoordinate, yCoordinate, direction);
			enemy = seeker;
			seekerList.add(seeker);
		}

		if (enemy != null) {
			this.enemyList.add(enemy);
		}
		keepTrackOfEnemies();
	}

	private void removeAlienSpaceship(Alien alien) {
		this.alienList.remove(alien);
	}

	private void removeAlienBomb(AlienBomb alienBomb) {
		this.alienBombList.remove(alienBomb);
	}

	private void removeSeeker(Seeker seeker) {
		this.seekerList.remove(seeker);
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

	public int getSeekerCount() {
		return seekerCount;
	}

}
