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
		friendlyManager = FriendlyManager.getInstance();
		dataClass = DataClass.getInstance();
		audioManager = AudioManager.getInstance();
	}

	public void updateGameTick() {
		try {
			updateEnemies();
			checkSpaceshipCollisions();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		triggerEnemyActions();
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
					detonateAlienBomb(enemy);
				} else {
					friendlyManager.getSpaceship().takeHitpointDamage(1);
				}
			}
		}
	}

	private void detonateAlienBomb(Enemy enemy) throws UnsupportedAudioFileException, IOException {
		friendlyManager.getSpaceship().takeHitpointDamage(20);
		animationManager.addUpperAnimation(enemy.getXCoordinate(), enemy.getYCoordinate(), "Alien Bomb Explosion",
				false);
		audioManager.addAudio("Alien Bomb Impact");
		enemy.setVisible(false);
	}

	private void triggerEnemyActions() {
		for (Alien alien : alienList) {
			alien.fireAction();
		}
		for (Seeker seeker : seekerList) {
			seeker.fireAction();
		}
		for (Tazer tazer : tazerList) {
			tazer.fireAction();
		}
		for (Bulldozer bulldozer : bulldozerList) {
			bulldozer.fireAction();
		}
		for (Energizer energizer : energizerList) {
			energizer.fireAction();
		}
		for (Flamer flamer : flamerList) {
			flamer.fireAction();
		}
		for (Bomba bomba : bombaList) {
			bomba.fireAction();
		}
	}

	private void updateEnemies() throws UnsupportedAudioFileException, IOException {
		for (int i = 0; i < enemyList.size(); i++) {
			Enemy enemy = enemyList.get(i);
			if (enemy.isVisible()) {
				enemy.move();
				enemy.updateBoardBlock();
				if (enemy.getExhaustAnimation() != null) {
					enemy.getExhaustAnimation().setX(enemy.getCenterXCoordinate() + (enemy.getWidth() / 2));
					enemy.getExhaustAnimation()
							.setY(enemy.getCenterYCoordinate() - (enemy.getExhaustAnimation().getHeight() / 2));
				}
			} else {
				if (enemy.getCurrentHitpoints() < 0) {
					triggerEnemyDeathSound(enemy);
				}
				animationManager.deleteEnemyAnimations(enemy);
				removeEnemy(enemy);
			}
		}

	}

	private void triggerEnemyDeathSound(Enemy enemy) throws UnsupportedAudioFileException, IOException {
		audioManager.addAudio(enemy.getDeathSound());
	}

	// Called by LevelManager, creates an unambiguous enemy and adds it to enemies
	public void addEnemy(Enemy enemy) {
		if (animationManager == null) {
			animationManager = AnimationManager.getInstance();
		}
		
		switch(enemy.getEnemyType()) {
		case("Alien Bomb"):
			alienBombList.add((AlienBomb) enemy);
			break;
		case("Flamer"):
			flamerList.add((Flamer) enemy);
			break;
		case("Tazer"):
			tazerList.add((Tazer) enemy);
			break;
		case("Seeker"):
			seekerList.add((Seeker) enemy);
			break;
		case("Bomba"):
			bombaList.add((Bomba) enemy);
			break;
		case("Bulldozer"):
			bulldozerList.add((Bulldozer) enemy);
			break;
		case("Energizer"):
			energizerList.add((Energizer) enemy);
			break;
		case("Alien"):
			alienList.add((Alien) enemy);
			break;
		}
		
		if (enemy != null) {
			animationManager.addExhaustAnimation(enemy.getExhaustAnimation());
			this.enemyList.add(enemy);
		}
	}

	private void removeEnemy(Enemy enemy) {
		this.enemyList.remove(enemy);
		removeEnemyFromSpecialisedList(enemy);
	}

	private void removeEnemyFromSpecialisedList(Enemy enemy) {
		if (enemy instanceof Alien) {
			alienList.remove(enemy);
		} else if (enemy instanceof AlienBomb) {
			alienBombList.remove(enemy);
		} else if (enemy instanceof Tazer) {
			tazerList.remove(enemy);
		} else if (enemy instanceof Energizer) {
			energizerList.remove(enemy);
		} else if (enemy instanceof Seeker) {
			seekerList.remove(enemy);
		} else if (enemy instanceof Bomba) {
			bombaList.remove(enemy);
		} else if (enemy instanceof Flamer) {
			flamerList.remove(enemy);
		} else if (enemy instanceof Bulldozer) {
			bulldozerList.remove(enemy);
		}
	}

	public List<Enemy> getEnemies() {
		return this.enemyList;
	}

	public int getDefaultAlienSpaceshipCount() {
		return alienList.size();
	}

	public int getAlienBombCount() {
		return alienBombList.size();
	}

	public int getSeekerCount() {
		return seekerList.size();
	}

	public int getBulldozerCount() {
		return this.bulldozerList.size();
	}

	public int getEnergizerCount() {
		return this.energizerList.size();
	}

	public int getFlamerCount() {
		return this.flamerList.size();
	}

	public int getBombaCount() {
		return this.bombaList.size();
	}

	public int getTazerCount() {
		return this.tazerList.size();
	}

}
