package game.levels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import VisualAndAudioData.audio.AudioEnums;
import game.directors.DirectorManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.objects.enemies.*;
import game.objects.enemies.enums.EnemyEnums;
import game.objects.powerups.creation.PowerUpCreator;
import game.spawner.*;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import VisualAndAudioData.audio.AudioManager;

import javax.sound.sampled.UnsupportedAudioFileException;

public class LevelManager {

    private static LevelManager instance = new LevelManager();
    private AudioManager audioManager = AudioManager.getInstance();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
    private TimerManager timerManager = TimerManager.getInstance();
    private GameStateInfo gameState = GameStateInfo.getInstance();

    private List<Level> levelsToPlay = new ArrayList<Level>();
    private Level currentLevel;

    private LevelManager () {
        Album newAlbum = new Album(AlbumEnums.Furi);
        this.setAlbum(newAlbum);
    }

    public static LevelManager getInstance () {
        return instance;
    }

    public void setAlbum (Album album) {
        for (Level level : album.getLevels()) {
            levelsToPlay.add(level);
        }

    }

    public void resetManager () {
        currentLevel = null;
        levelsToPlay = new ArrayList<Level>();
    }

    private void removeFinishedLevel () {
        if (levelsToPlay.contains(currentLevel)) {
            this.levelsToPlay.remove(currentLevel);
        }
    }

    private void advanceNextLevel () {
        if (levelsToPlay.size() > 0) {
            currentLevel = levelsToPlay.get(0);
            audioManager.resetManager();
        } else {
            gameState.setGameState(GameStatusEnums.Album_Completed);
        }
    }

    public void updateGameTick () {
        // Check if the song has ended, then create the moving out portal
        if (gameState.getMusicSeconds() >= gameState.getMaxMusicSeconds() && gameState.getGameState() == GameStatusEnums.Playing) {
            gameState.setGameState(GameStatusEnums.Level_Finished);
        }
        //NextLevelPortal spawns, now we wait for the player to enter the portal to set it to Level_Completed
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            gameState.setGameState(GameStatusEnums.Transitioning_To_Next_Level);

            removeFinishedLevel();
            advanceNextLevel();
            //Now the GameBoard completes the transition and zoning in to the next level
        }
    }

    // Called when a level starts, to saturate enemy list
    public void startLevel () {
//        if (levelsToPlay.size() > 0) {
//            currentLevel = levelsToPlay.get(0);
//        }
//        if (currentLevel == null) {
//            currentLevel = new FuriWisdomOfRageLevel();
//        }
//
//        for (EnemySpawnTimer timer : currentLevel.getTimers()) {
//            timerManager.addTimer(timer);
//        }
        AudioManager audioManager = AudioManager.getInstance();
//        try {
//            AudioEnums currentMusic = currentLevel.getSong();
//            audioManager.playMusicAudio(currentMusic);
//            audioManager.playRandomBackgroundMusic();
//            GameStateInfo.getInstance().setMaxMusicSeconds(audioManager.getBackgroundMusic());
//        } catch (UnsupportedAudioFileException | IOException e) {
//            e.printStackTrace();
//        }

        gameState.setGameState(GameStatusEnums.Playing);

//        PowerUpCreator.getInstance().initializePowerUpSpawnTimers();
//        DirectorManager.getInstance().createMonsterCards();
//        DirectorManager.getInstance().createDirectors();

        Enemy enemy = EnemyCreator.createEnemy(EnemyEnums.Bomba, 1800, 600, game.movement.Direction.LEFT, 1
        , 2, 2, MovementPatternSize.SMALL, false);
        EnemyManager.getInstance().addEnemy(enemy);


    }

    // Called by CustomTimers when they have to spawn an enemy
    public void spawnEnemy (int xCoordinate, int yCoordinate, EnemyEnums enemyType, int amountOfAttempts,
                            Direction direction, float scale, boolean random, int xMovementSpeed, int yMovementSpeed, boolean boxCollision) {
        // Spawn random if there are no given X/Y coords
        if (random) {
            for (int i = 0; i < amountOfAttempts; i++) {
                List<Integer> coordinatesList = spawningCoordinator.getSpawnCoordinatesByDirection(direction);

                xCoordinate = coordinatesList.get(0);
                yCoordinate = coordinatesList.get(1);

                Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);
                if (validCoordinates(enemy)) {
                    enemyManager.addEnemy(enemy);

                }
            }
        } else {
            Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);

            if (validCoordinates(enemy)) {
                enemyManager.addEnemy(enemy);

            }
        }

    }


    private boolean validCoordinates (Enemy enemy) {
        if (spawningCoordinator.checkValidEnemyXCoordinate(enemy, enemyManager.getEnemies(), enemy.getXCoordinate(),
                enemy.getWidth())
                && spawningCoordinator.checkValidEnemyYCoordinate(enemy, enemyManager.getEnemies(),
                enemy.getYCoordinate(), enemy.getHeight())) {
            return true;
        }
        return false;
    }




}
