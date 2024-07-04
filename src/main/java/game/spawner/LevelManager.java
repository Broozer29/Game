package game.spawner;

import java.io.IOException;
import java.util.List;

import VisualAndAudioData.audio.enums.LevelSongs;
import game.UI.GameUIManager;
import game.managers.ShopManager;
import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.*;
import game.objects.enemies.enums.EnemyEnums;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import VisualAndAudioData.audio.AudioManager;
import game.spawner.directors.DirectorManager;
import game.spawner.enums.LevelDifficulty;
import game.spawner.enums.LevelLength;

import javax.sound.sampled.UnsupportedAudioFileException;

public class LevelManager {

    private static LevelManager instance = new LevelManager();
    private AudioManager audioManager = AudioManager.getInstance();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
    private GameStateInfo gameState = GameStateInfo.getInstance();

    private LevelSongs currentLevelSong;
    private LevelDifficulty currentLevelDifficulty;
    private LevelLength currentLevelLength;
    private int enemiesSpawned;
    private int enemiesKilled;
    private int currentDifficultyCoeff;

    private LevelManager () {
        resetManager();
    }

    public static LevelManager getInstance () {
        return instance;
    }

    public void resetManager () {
        enemiesSpawned = 0;
        enemiesKilled = 0;
        currentLevelSong = null;
//        currentLevelDifficulty = LevelDifficulty.Easy;
//        currentLevelLength = LevelLength.Short;
    }

    public void updateGameTick () {
        // Check if the song has ended, then create the moving out portal
        if (gameState.getGameState() == GameStatusEnums.Playing && audioManager.getBackgroundMusic().getFramePosition() >= audioManager.getBackgroundMusic().getFrameLength()) {
            gameState.setGameState(GameStatusEnums.Level_Finished);
            ShopManager.getInstance().setLastLevelDifficulty(this.currentLevelDifficulty);
            ShopManager.getInstance().setLastLevelLength(this.currentLevelLength);
            ShopManager.getInstance().setLastLevelDifficultyCoeff(this.currentDifficultyCoeff);
            ShopManager.getInstance().setRowsUnlockedByDifficulty(this.currentDifficultyCoeff);
            DirectorManager.getInstance().setEnabled(false);
        }

        //NextLevelPortal spawns in friendlymanager, now we wait for the player to enter the portal to set it to Level_Completed
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            gameState.setGameState(GameStatusEnums.Transitioning_To_Next_Level);
            this.currentLevelLength = null;
            this.currentLevelDifficulty = null;
            this.currentDifficultyCoeff = 2;
            //Now the GameBoard completes the transition and zoning in to the next level
        }
//        if (gameState.getMusicSeconds() >= gameState.getMaxMusicSeconds() && gameState.getGameState() == GameStatusEnums.Playing) {
//            gameState.setGameState(GameStatusEnums.Level_Finished);
//        }
    }


    // Called when a level starts, to saturate enemy list
    public void startLevel () {
        if (currentLevelDifficulty == null) {
            currentLevelDifficulty = LevelDifficulty.getRandomDifficulty();
        }

        if (currentLevelLength == null) {
            currentLevelLength = LevelLength.getRandomLength();
        }

        AudioManager audioManager = AudioManager.getInstance();
        try {
            audioManager.playRandomBackgroundMusic(currentLevelDifficulty, currentLevelLength);
            this.currentLevelSong = audioManager.getCurrentSong();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }

        currentDifficultyCoeff = LevelSongs.getDifficultyScore(currentLevelDifficulty, currentLevelLength);
        GameUIManager.getInstance().createDifficultyWings();


        gameState.setGameState(GameStatusEnums.Playing);
//        DirectorManager.getInstance().setEnabled(true);
//        DirectorManager.getInstance().createMonsterCards();
//        DirectorManager.getInstance().createDirectors();


//        PlayerStats.getInstance().setBaseDamage(1);

//        Director testDirector = DirectorManager.getInstance().getTestDirector();
//        testDirector.spawnRegularFormation(SpawnFormationEnums.V, EnemyEnums.Scout);

        EnemyEnums enemyType = EnemyEnums.Bulldozer;
        Enemy enemy = EnemyCreator.createEnemy(enemyType, 1000, 100, Direction.LEFT, enemyType.getDefaultScale()
                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, enemyType.isBoxCollision());
//        enemy.getMovementConfiguration().setBoardBlockToHoverIn(5);
        enemy.getMovementConfiguration().setPathFinder(new RegularPathFinder());
        enemy.setAllowedVisualsToRotate(false);
        enemy.getMovementConfiguration().setXMovementSpeed(0);
        enemy.getMovementConfiguration().setYMovementSpeed(0);
//        enemy.getAnimation().changeImagetype(ImageEnums.Scout);
        EnemyManager.getInstance().addEnemy(enemy);
//
//        EnemyEnums enemyType2 = EnemyEnums.Energizer;
//        Enemy enemy2 = EnemyCreator.createEnemy(enemyType2, 800, 600, Direction.LEFT, enemyType2.getDefaultScale()
//                , enemyType2.getMovementSpeed(), enemyType2.getMovementSpeed(), MovementPatternSize.SMALL, enemyType2.isBoxCollision());
////        enemy2.getMovementConfiguration().setBoardBlockToHoverIn(5);
//        enemy2.getMovementConfiguration().setPathFinder(new RegularPathFinder());
//        enemy2.setAllowedVisualsToRotate(false);
//        enemy2.getMovementConfiguration().setXMovementSpeed(0);
//        enemy2.getMovementConfiguration().setYMovementSpeed(0);
//        EnemyManager.getInstance().addEnemy(enemy2);
//
//
//        Enemy enemy3 = EnemyCreator.createEnemy(EnemyEnums.Scout, 500, 200, Direction.LEFT, 1
//                , 2, 2, MovementPatternSize.SMALL, false);
//        enemy3.getMovementConfiguration().setPathFinder(new RegularPathFinder());
//        enemy3.getMovementConfiguration().setXMovementSpeed(0);
//        enemy3.getMovementConfiguration().setYMovementSpeed(0);
//        enemy3.setAllowedVisualsToRotate(false);
//        EnemyManager.getInstance().addEnemy(enemy3);


    }

    // Called by CustomTimers when they have to spawn an enemy
    public void spawnEnemy (int xCoordinate, int yCoordinate, EnemyEnums enemyType,
                            Direction direction, float scale, boolean random, int xMovementSpeed, int yMovementSpeed, boolean boxCollision) {
        // Spawn random if there are no given X/Y coords
        if (random) {
            List<Integer> coordinatesList = spawningCoordinator.getSpawnCoordinatesByDirection(direction);

            xCoordinate = coordinatesList.get(0);
            yCoordinate = coordinatesList.get(1);

            Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);
            if (validCoordinates(enemy)) {
                enemiesSpawned++;
                enemyManager.addEnemy(enemy);
            }
        } else {
            Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);
            enemy.setCenterCoordinates(xCoordinate, yCoordinate);
            enemy.resetMovementPath();
            if (validCoordinates(enemy)) {
                enemiesSpawned++;
                enemyManager.addEnemy(enemy);
            }
        }
    }

    private boolean validCoordinates (Enemy enemy) {
        if(enemy.getEnemyType().equals(EnemyEnums.CashCarrier)){
            return true;
        }

        if (spawningCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), enemy.getXCoordinate(),
                enemy.getWidth())
                || spawningCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(),
                enemy.getYCoordinate(), enemy.getHeight())) {
            return true;
        }
        return false;
    }

    public int getEnemiesSpawned () {
        return enemiesSpawned;
    }

    public void setEnemiesSpawned (int enemiesSpawned) {
        this.enemiesSpawned = enemiesSpawned;
    }

    public int getEnemiesKilled () {
        return enemiesKilled;
    }

    public void addEnemyKilled (int enemiesKilled) {
        this.enemiesKilled += enemiesKilled;
    }

    public LevelSongs getCurrentLevelSong () {
        return currentLevelSong;
    }

    public void setCurrentLevelSong (LevelSongs currentLevelSong) {
        this.currentLevelSong = currentLevelSong;
    }

    public LevelDifficulty getCurrentLevelDifficulty () {
        if (currentLevelDifficulty == null) {
            currentLevelDifficulty = LevelDifficulty.getRandomDifficulty();
        }
        return currentLevelDifficulty;
    }

    public void setCurrentLevelDifficulty (LevelDifficulty currentLevelDifficulty) {
        this.currentLevelDifficulty = currentLevelDifficulty;
    }

    public LevelLength getCurrentLevelLength () {
        if (currentLevelLength == null) {
            currentLevelLength = LevelLength.getRandomLength();
        }
        return currentLevelLength;
    }

    public void setCurrentLevelLength (LevelLength currentLevelLength) {
        this.currentLevelLength = currentLevelLength;
    }

    public int getCurrentDifficultyCoeff () {
        return currentDifficultyCoeff;
    }

    public void setCurrentDifficultyCoeff (int currentDifficultyCoeff) {
        this.currentDifficultyCoeff = currentDifficultyCoeff;
    }
}
