package game.objects.enemies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.AnimationManager;
import game.managers.PlayerManager;
import game.movement.Point;
import game.objects.enemies.enemytypes.Alien;
import game.objects.enemies.enemytypes.AlienBomb;
import game.objects.enemies.enemytypes.Bomba;
import game.objects.enemies.enemytypes.Bulldozer;
import game.objects.enemies.enemytypes.Energizer;
import game.objects.enemies.enemytypes.Flamer;
import game.objects.enemies.enemytypes.Seeker;
import game.objects.enemies.enemytypes.Tazer;
import gamedata.DataClass;
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;

public class EnemyManager {

	private static EnemyManager instance = new EnemyManager();
	private AudioManager audioManager = AudioManager.getInstance();
	private PlayerManager friendlyManager = PlayerManager.getInstance();
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
	private DataClass dataClass = DataClass.getInstance();

	private EnemyManager() {
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
		seekerList = new ArrayList<Seeker>();
		bombaList = new ArrayList<Bomba>();
		flamerList = new ArrayList<Flamer>();
		bulldozerList = new ArrayList<Bulldozer>();
		tazerList = new ArrayList<Tazer>();
		energizerList = new ArrayList<Energizer>();
		friendlyManager = PlayerManager.getInstance();
		dataClass = DataClass.getInstance();
		audioManager = AudioManager.getInstance();
	}

	public void updateGameTick() {
		try {
			updateEnemies();
			checkSpaceshipCollisions();
			triggerEnemyActions();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}

	private void checkSpaceshipCollisions() throws UnsupportedAudioFileException, IOException {
		if (friendlyManager == null || animationManager == null) {
			this.animationManager = AnimationManager.getInstance();
			this.friendlyManager = PlayerManager.getInstance();
		}

		// Checks collision between spaceship and enemies
		for (Enemy enemy : enemyList) {
			if (isNearby(enemy, friendlyManager.getSpaceship())) {
				if (friendlyManager.getSpaceship().getBounds().intersects(enemy.getBounds())) {
					if (enemy instanceof AlienBomb) {
						detonateAlienBomb(enemy);
						friendlyManager.getSpaceship().takeHitpointDamage(20);
					} else {
						friendlyManager.getSpaceship().takeHitpointDamage(1);
					}
				}
			}
		}
	}

	private boolean isWithinBoardBlockThreshold(Sprite sprite1, Sprite sprite2) {
		int blockDifference = Math.abs(sprite1.getCurrentBoardBlock() - sprite2.getCurrentBoardBlock());
		return blockDifference <= 3;
	}

	private boolean isNearby(Sprite sprite1, Sprite sprite2) {
		if (!isWithinBoardBlockThreshold(sprite1, sprite2)) {
			return false;
		}

		double distance = Math.hypot(sprite1.getXCoordinate() - sprite2.getXCoordinate(),
				sprite1.getYCoordinate() - sprite2.getYCoordinate());
		return distance < 300;
	}

	private void detonateAlienBomb(Enemy enemy) throws UnsupportedAudioFileException, IOException {
		animationManager.createAndAddUpperAnimation(enemy.getXCoordinate(), enemy.getYCoordinate(),
				ImageEnums.Alien_Bomb_Explosion, false, 1);
		audioManager.addAudio(AudioEnums.Alien_Bomb_Impact);
		enemy.setVisible(false);
	}

	private void triggerEnemyActions() {
		for (Enemy enemy : enemyList) {
			enemy.fireAction();
		}
	}

	private void updateEnemies() throws UnsupportedAudioFileException, IOException {
		for (int i = 0; i < enemyList.size(); i++) {
			Enemy enemy = enemyList.get(i);
			if (enemy.isVisible()) {
				enemy.move();
				enemy.updateCurrentBoardBlock();
			} else {
				animationManager.deleteEnemyAnimations(enemy);
				removeEnemy(enemy);
			}
		}

	}

	// Called by LevelManager, creates an unambiguous enemy and adds it to enemies
	public void addEnemy(Enemy enemy) {
		if (animationManager == null) {
			animationManager = AnimationManager.getInstance();
		}

		switch (enemy.getEnemyType()) {
		case Alien_Bomb:
			alienBombList.add((AlienBomb) enemy);
			break;
		case Flamer:
			flamerList.add((Flamer) enemy);
			break;
		case Tazer:
			tazerList.add((Tazer) enemy);
			break;
		case Seeker:
			seekerList.add((Seeker) enemy);
			break;
		case Bomba:
			bombaList.add((Bomba) enemy);
			break;
		case Bulldozer:
			bulldozerList.add((Bulldozer) enemy);
			break;
		case Energizer:
			energizerList.add((Energizer) enemy);
			break;
		case Alien:
			alienList.add((Alien) enemy);
			break;
		}

		if (enemy != null) {
//			animationManager.addExhaustAnimation(enemy.getExhaustAnimation());
			this.enemyList.add(enemy);
		}
	}

	private void removeEnemy(Enemy enemy) {
		this.enemyList.remove(enemy);
		removeEnemyFromSpecialisedList(enemy);
	}

	private void removeEnemyFromSpecialisedList(Enemy enemy) {
		switch (enemy.getEnemyType()) {
		case Alien:
			alienList.remove(enemy);
			break;
		case Alien_Bomb:
			alienBombList.remove(enemy);
			break;
		case Tazer:
			tazerList.remove(enemy);
			break;
		case Energizer:
			energizerList.remove(enemy);
			break;
		case Seeker:
			seekerList.remove(enemy);
			break;
		case Bomba:
			bombaList.remove(enemy);
			break;
		case Flamer:
			flamerList.remove(enemy);
			break;
		case Bulldozer:
			bulldozerList.remove(enemy);
			break;
		}
	}

	public List<Enemy> getEnemies() {
		return this.enemyList;
	}

	public int getEnemyCount() {
		return enemyList.size();
	}

	public boolean enemiesToHomeTo() {
		if (enemyList.size() == 0) {
			return true;
		} else
			return false;
	}

	public Enemy getClosestEnemy() {
		int playerXCoordinate = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
		int playerYCoordinate = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();

		Enemy closestEnemy = null;
		double minDistance = Double.MAX_VALUE;

		for (Enemy enemy : enemyList) {
			int enemyXCoordinate = enemy.getCenterXCoordinate();
			int enemyYcoordinate = enemy.getCenterYCoordinate();

			// Compute the distance between player and enemy using Euclidean distance formula
			double distance = Math.sqrt(Math.pow((playerXCoordinate - enemyXCoordinate), 2)
					+ Math.pow((playerYCoordinate - enemyYcoordinate), 2));

			// If this enemy is closer than the previous closest enemy, update closestEnemy and
			// minDistance
			if (distance < minDistance) {
				minDistance = distance;
				closestEnemy = enemy;
			}
		}
		return closestEnemy;
	}


}