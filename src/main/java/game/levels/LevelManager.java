package game.levels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.UnsupportedAudioFileException;

import game.managers.PlayerManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyEnums;
import game.objects.enemies.EnemyManager;
import game.objects.enemies.enemytypes.Alien;
import game.objects.enemies.enemytypes.AlienBomb;
import game.objects.enemies.enemytypes.Bomba;
import game.objects.enemies.enemytypes.Bulldozer;
import game.objects.enemies.enemytypes.Energizer;
import game.objects.enemies.enemytypes.Flamer;
import game.objects.enemies.enemytypes.Seeker;
import game.objects.enemies.enemytypes.Tazer;
import game.objects.friendlies.FriendlyManager;
import game.spawner.EnemySpawnTimer;
import game.spawner.SpawningCoordinator;
import gamedata.GameStateInfo;
import gamedata.GameStatusEnums;
import gamedata.audio.AudioEnums;
import gamedata.audio.AudioManager;

public class LevelManager {

	private static LevelManager instance = new LevelManager();
	private AudioManager audioManager = AudioManager.getInstance();
	private EnemyManager enemyManager = EnemyManager.getInstance();
	private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
	private TimerManager timerManager = TimerManager.getInstance();
	private GameStateInfo gameState = GameStateInfo.getInstance();

	private List<Level> levelsToPlay = new ArrayList<Level>();
	private Level currentLevel;

	private LevelManager() {
		Album newAlbum = new Album(AlbumEnums.Furi);
		this.setAlbum(newAlbum);
	}

	public static LevelManager getInstance() {
		return instance;
	}

	public void setAlbum(Album album) {
		for (Level level : album.getLevels()) {
			levelsToPlay.add(level);
		}

	}

	public void resetManager() {
		currentLevel = null;
		levelsToPlay = new ArrayList<Level>();
	}

	private void removeFinishedLevel() {
		if (levelsToPlay.contains(currentLevel)) {
			this.levelsToPlay.remove(currentLevel);
		}
	}
	
	private void advanceNextLevel() {
		if(levelsToPlay.size() > 0) {
			currentLevel = levelsToPlay.get(0);
			audioManager.resetManager();
		} else {
			gameState.setGameState(GameStatusEnums.Album_Completed);
		}
	}

	public void updateGameTick() {
		// Check if the song has ended, then create the moving out portal
		if (gameState.getMusicSeconds() >= gameState.getMaxMusicSeconds() && gameState.getGameState() == GameStatusEnums.Playing) {
			gameState.setGameState(GameStatusEnums.Song_Finished);
		}
		
		//NextLevelPortal spawns, now we wait for the player to enter the portal to set it to Level_Completed
		
		if(gameState.getGameState() == GameStatusEnums.Level_Completed) {
			gameState.setGameState(GameStatusEnums.Transitioning_To_Next_Level);
			
			removeFinishedLevel();
			advanceNextLevel();
			//Now the GameBoard completes the transition and zoning in to the next level
		}
	}

	// Called when a level starts, to saturate enemy list
	public void startLevel() {
		if (levelsToPlay.size() > 0) {
			currentLevel = levelsToPlay.get(0);
		}
		if(currentLevel == null) {
			currentLevel = new FuriWisdomOfRageLevel();
		}
		
		AudioManager audioManager = AudioManager.getInstance();
		for (EnemySpawnTimer timer : currentLevel.getTimers()) {
			timerManager.addEnemyTimerToList(timer);
		}

		try {
			AudioEnums currentMusic = currentLevel.getSong();
			audioManager.playMusicAudio(currentMusic);
			GameStateInfo.getInstance().setMaxMusicSeconds(audioManager.getBackgroundMusic());
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
		gameState.setGameState(GameStatusEnums.Playing);
//		
//		FormationCreator formCreator = new FormationCreator();
//		EnemySpawnTimer timer = null;
//		EnemyFormation formation = null;
//		EnemyEnums enemyType = EnemyEnums.Seeker;
//		boolean loopable = false;
//		float scale = 1;
//		int additionalDelay = 0;
//		DataClass dataClass = DataClass.getInstance();
//		int xMovementSpeed = 2;
//		int yMovementSpeed = 2;
//		int i = 1;
//		
//		timer = createSpawnTimer(EnemyEnums.Bulldozer, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//		formation = formCreator.createFormation(SpawnFormationEnums.Dot, 50, 50);
//		timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 100);
//		addSpawnTimer(timer);
		
//		for(int a = 0; a < 8; a++) {
//			FriendlyManager.getInstance().createMissileGuardianBot(FriendlyEnums.Missile_Guardian_Bot, 1);
//		}
		
//		for(int i = 0; i < 240; i += 30) {
//			TopBottomVRows pattern = new TopBottomVRows(i, enemyType, scale, EnemyEnums.Tazer, Direction.LEFT, xMovementSpeed, yMovementSpeed);
//			for(int iterator = 0; iterator < pattern.getTimers().size(); iterator++) {
//				addSpawnTimer(pattern.getTimers().get(iterator));
//			}
//		}
//		
//		yMovementSpeed = 1;
//		for(int i = 60; i < 300; i += 42) {
//			EnclosingFromAboveAndBelow pattern = new EnclosingFromAboveAndBelow(i, EnemyEnums.Flamer, scale, xMovementSpeed, yMovementSpeed);
//			for(int iterator = 0; iterator < pattern.getTimers().size(); iterator++) {
//				addSpawnTimer(pattern.getTimers().get(iterator));
//			}
//		}
//		
//		for(int i = 200; i < 600; i += 100) {
//			EnclosingFromAboveAndBelow pattern = new EnclosingFromAboveAndBelow(i, EnemyEnums.Flamer, scale, xMovementSpeed, yMovementSpeed);
//			for(int iterator = 0; iterator < pattern.getTimers().size(); iterator++) {
//				addSpawnTimer(pattern.getTimers().get(iterator));
//			}
//		}
//		
//		for(int i = 300; i < 600; i += 45) {
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 100);
//			addSpawnTimer(timer);
//			
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.LEFT_DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() / 2 - 400);
//			addSpawnTimer(timer);
//			
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 100);
//			addSpawnTimer(timer);
//			
//			timer = createSpawnTimer(EnemyEnums.Seeker, 1, i, loopable, Direction.RIGHT_DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, -550, dataClass.getWindowHeight() / 2 - 400);
//			addSpawnTimer(timer);
//		}
//		
//		for(int i = 300; i < 600; i += 30) {
//			timer = createSpawnTimer(EnemyEnums.Energizer, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, 150);
//			addSpawnTimer(timer);
//			
//			timer = createSpawnTimer(EnemyEnums.Energizer, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_greaterthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 600, 400);
//			addSpawnTimer(timer);
//			
//			timer = createSpawnTimer(EnemyEnums.Energizer, 1, i, loopable, Direction.LEFT, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 950, 750);
//			addSpawnTimer(timer);
//		} 
//		
//		for(int i = 300; i < 600; i += 45) {
//			timer = createSpawnTimer(EnemyEnums.Bulldozer, 1, i, loopable, Direction.LEFT_DOWN, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Small_smallerthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, -200);
//			addSpawnTimer(timer);
//			
//			timer = createSpawnTimer(EnemyEnums.Bulldozer, 1, i, loopable, Direction.LEFT_UP, scale, additionalDelay, xMovementSpeed, yMovementSpeed);
//			formation = formCreator.createFormation(SpawnFormationEnums.Large_smallerthen, 50, 50);
//			timer.setFormation(formation, dataClass.getWindowWidth() + 250, dataClass.getWindowHeight() + 250);
//			addSpawnTimer(timer);
//		}

	}

	// Called by CustomTimers when they have to spawn an enemy
	public void spawnEnemy(int xCoordinate, int yCoordinate, EnemyEnums enemyType, int amountOfAttempts,
			Direction direction, float scale, boolean random, int xMovementSpeed, int yMovementSpeed) {

		// Spawn random if there are no given X/Y coords
		if (random) {
			for (int i = 0; i < amountOfAttempts; i++) {
				List<Integer> coordinatesList = getSpawnCoordinatesByDirection(direction);

				xCoordinate = coordinatesList.get(0);
				yCoordinate = coordinatesList.get(1);

				if (enemyType.equals(EnemyEnums.Alien_Bomb)) {
					if (direction.equals(Direction.UP) || direction.equals(Direction.LEFT_UP)
							|| direction.equals(Direction.RIGHT_UP)) {
						yCoordinate = spawningCoordinator.getRandomYDownBombEnemyCoordinate();
					} else if (direction.equals(Direction.DOWN) || direction.equals(Direction.LEFT_DOWN)
							|| direction.equals(Direction.RIGHT_DOWN)) {
						yCoordinate = spawningCoordinator.getRandomYUpBombEnemyCoordinate();
					}
					xCoordinate = spawningCoordinator.getRandomXBombEnemyCoordinate();
					scale = 1;
				}

				Enemy enemy = createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed,
						yMovementSpeed);
				if (enemy != null && validCoordinates(enemy)) {
					enemyManager.addEnemy(enemy);
				}
			}
		} else {
			Enemy enemy = createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed,
					yMovementSpeed);
			enemyManager.addEnemy(enemy);
		}

	}

	private List<Integer> getSpawnCoordinatesByDirection(Direction direction) {
		List<Integer> coordinatesList = new ArrayList<Integer>();
		if (direction.equals(Direction.LEFT)) {
			coordinatesList.add(spawningCoordinator.getRightBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getRightBlockYCoordinate());
		} else if (direction.equals(Direction.RIGHT)) {
			coordinatesList.add(spawningCoordinator.getLeftBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getLeftBlockYCoordinate());
		} else if (direction.equals(Direction.DOWN)) {
			coordinatesList.add(spawningCoordinator.getUpBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getUpBlockYCoordinate());
		} else if (direction.equals(Direction.UP)) {
			coordinatesList.add(spawningCoordinator.getDownBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getDownBlockYCoordinate());
		}

		else if (direction.equals(Direction.LEFT_UP)) {
			coordinatesList.add(spawningCoordinator.getRightBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getBottomRightBlockYCoordinate());
//			System.out.println(
//					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.LEFT_DOWN)) {
			coordinatesList.add(spawningCoordinator.getRightBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getTopRightBlockYCoordinate());
//			System.out.println(
//					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.RIGHT_UP)) {
			coordinatesList.add(spawningCoordinator.getLeftBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getBottomLeftBlockYCoordinate());
//			System.out.println(
//					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		} else if (direction.equals(Direction.RIGHT_DOWN)) {
			coordinatesList.add(spawningCoordinator.getLeftBlockXCoordinate());
			coordinatesList.add(spawningCoordinator.getTopLeftBlockYCoordinate());
//			System.out.println(
//					"Tried spawning in a direction where the corresponding spawning block has nog been created yet!");
		}
		return coordinatesList;
	}

	private Enemy createEnemy(EnemyEnums type, int xCoordinate, int yCoordinate, Direction rotation, float scale,
			int xMovementSpeed, int yMovementSpeed) {

		// Allocate different pathfinders based on the enemytype
		PathFinder regularPathFinder = new RegularPathFinder();
		PathFinder homingPathFinder = new HomingPathFinder();
		Point currentPoint = new Point(xCoordinate, yCoordinate);
		Point regularDestination = regularPathFinder.calculateInitialEndpoint(currentPoint, rotation, false);
//		Point homingDestination = homingPathFinder.calculateInitialEndpoint(currentPoint, rotation);

		switch (type) {
		case Alien:
			return new Alien(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder,
					xMovementSpeed, yMovementSpeed);
		case Bomba:
			return new Bomba(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder,
					xMovementSpeed, yMovementSpeed);
		case Flamer:
			return new Flamer(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder,
					xMovementSpeed, yMovementSpeed);
		case Bulldozer:
			return new Bulldozer(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder,
					xMovementSpeed, yMovementSpeed);
		case Energizer:
			return new Energizer(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder,
					xMovementSpeed, yMovementSpeed);
		case Seeker:
			return new Seeker(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder,
					xMovementSpeed, yMovementSpeed);
		case Tazer:
			return new Tazer(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder,
					xMovementSpeed, yMovementSpeed);
		case Alien_Bomb:
			return new AlienBomb(xCoordinate, yCoordinate, regularDestination, rotation, scale, regularPathFinder,
					xMovementSpeed, yMovementSpeed);
		}
		return null;
	}

	// Called by all RANDOM spawn*Enemy* methods, returns true if there is no overlap
	// between enemies of the same type
	private boolean validCoordinates(Enemy enemy) {
		if (spawningCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), enemy.getXCoordinate(),
				enemy.getWidth())
				&& spawningCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(),
						enemy.getYCoordinate(), enemy.getHeight())) {
			return true;
		}
		return false;
	}

	// FOR TESTING PURPOSES only for methods below this!
	private EnemySpawnTimer createSpawnTimer(EnemyEnums enemyType, int spawnAttempts, int timeBeforeActivation,
			boolean loopable, Direction direction, float enemyScale, int additionalDelay, int xMovementSpeed,
			int yMovementSpeed) {

		if (enemyType == EnemyEnums.Random) {
			enemyType = selectRandomEnemy();
		}

		EnemySpawnTimer timer = new EnemySpawnTimer(timeBeforeActivation, spawnAttempts, enemyType, loopable, direction,
				enemyScale, additionalDelay, xMovementSpeed, yMovementSpeed);
		return timer;
	}

	// FOR TESTING PURPOSES ONLY AS WELL
	private void addSpawnTimer(EnemySpawnTimer timer) {
		this.timerManager.addEnemyTimerToList(timer);
	}

	private EnemyEnums selectRandomEnemy() {
		EnemyEnums[] enums = EnemyEnums.values();
		Random random = new Random();
		EnemyEnums randomValue = enums[random.nextInt(enums.length)];

		if (randomValue == EnemyEnums.Alien || randomValue == EnemyEnums.Alien_Bomb) {
			return selectRandomEnemy();
		}
		return randomValue;
	}
	
}
