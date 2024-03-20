package game.spawner;

import java.io.IOException;
import java.util.List;

import VisualAndAudioData.audio.enums.LevelSongs;
import game.UI.GameUIManager;
import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.objects.enemies.*;
import game.objects.enemies.enums.EnemyEnums;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import VisualAndAudioData.audio.AudioManager;
import game.spawner.enums.LevelDifficulty;
import game.spawner.enums.LevelLength;

import javax.sound.sampled.UnsupportedAudioFileException;

public class LevelManager {

    private static LevelManager instance = new LevelManager();
    private AudioManager audioManager = AudioManager.getInstance();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
    private TimerManager timerManager = TimerManager.getInstance();
    private GameStateInfo gameState = GameStateInfo.getInstance();

    private LevelSongs currentLevelSong;
    private LevelDifficulty currentLevelDifficulty;
    private LevelLength currentLevelLength;
    private int enemiesSpawned;
    private int enemiesKilled;
    private int difficultyModifier;
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
        }

        //NextLevelPortal spawns, now we wait for the player to enter the portal to set it to Level_Completed
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            gameState.setGameState(GameStatusEnums.Transitioning_To_Next_Level);
            this.currentLevelLength = null;
            this.currentLevelDifficulty = null;
            //Now the GameBoard completes the transition and zoning in to the next level
        }
//        if (gameState.getMusicSeconds() >= gameState.getMaxMusicSeconds() && gameState.getGameState() == GameStatusEnums.Playing) {
//            gameState.setGameState(GameStatusEnums.Level_Finished);
//        }
    }


    // Called when a level starts, to saturate enemy list
    public void startLevel () {
        if(currentLevelDifficulty == null){
            currentLevelDifficulty = LevelDifficulty.getRandomDifficulty();
        }

        if(currentLevelLength == null){
            currentLevelLength = LevelLength.getRandomLength();
        }

        AudioManager audioManager = AudioManager.getInstance();
        try {
            audioManager.playRandomBackgroundMusic(currentLevelDifficulty, currentLevelLength);
            this.currentLevelSong = audioManager.getCurrentSong();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }

        difficultyModifier = LevelSongs.getDifficultyImageIndex(currentLevelDifficulty, currentLevelLength);
        if(difficultyModifier < 2){
            difficultyModifier = 2;
        }
        GameUIManager.getInstance().createDifficultyWings();

        gameState.setGameState(GameStatusEnums.Playing);

//        PowerUpCreator.getInstance().initializePowerUpSpawnTimers();
//        DirectorManager.getInstance().createMonsterCards();
//        DirectorManager.getInstance().createDirectors();

        Enemy enemy = EnemyCreator.createEnemy(EnemyEnums.Seeker, 800, 600, Direction.LEFT, 1
                , 1, 1, MovementPatternSize.SMALL, false);
        enemy.getMovementConfiguration().setXMovementSpeed(0);
        enemy.getMovementConfiguration().setYMovementSpeed(0);

        EnemyManager.getInstance().addEnemy(enemy);

//        Enemy enemy2 = EnemyCreator.createEnemy(EnemyEnums.Seeker, 900, 600, Direction.RIGHT_DOWN, 1
//                , 0, 0, MovementPatternSize.SMALL, false);
//
//        EnemyManager.getInstance().addEnemy(enemy2);
//        Enemy enemy3 = EnemyCreator.createEnemy(EnemyEnums.Tazer, 700, 600, Direction.RIGHT_UP, 1
//                , 0, 0, MovementPatternSize.SMALL, false);
//
//        EnemyManager.getInstance().addEnemy(enemy3);
//
//        Enemy enemy4 = EnemyCreator.createEnemy(EnemyEnums.Bomba, 600, 600, Direction.LEFT, 1
//                , 0, 0, MovementPatternSize.SMALL, false);
//        EnemyManager.getInstance().addEnemy(enemy4);
//
//        Enemy enemy5 = EnemyCreator.createEnemy(EnemyEnums.Energizer, 500, 600, Direction.LEFT_UP, 1
//                , 0, 0, MovementPatternSize.SMALL, false);
//        EnemyManager.getInstance().addEnemy(enemy5);
//
//        Enemy enemy6 = EnemyCreator.createEnemy(EnemyEnums.Bulldozer, 400, 600, Direction.LEFT_DOWN, 1
//                , 0, 0, MovementPatternSize.SMALL, false);
//        EnemyManager.getInstance().addEnemy(enemy6);
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
                    enemy = increaseEnemyStrengthByDifficulty(enemy);
                    enemiesSpawned++;
                    enemyManager.addEnemy(enemy);
                }
            }
        } else {
            Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);
            if (validCoordinates(enemy)) {
                enemy = increaseEnemyStrengthByDifficulty(enemy);
                enemiesSpawned++;
                enemyManager.addEnemy(enemy);
            }
        }
    }

    private Enemy increaseEnemyStrengthByDifficulty (Enemy enemy) {
        float difficultyCoeff = GameStateInfo.getInstance().getDifficultyCoefficient();
        enemy.setMaxHitPoints(enemy.getMaxHitPoints() * difficultyCoeff);
        enemy.setMaxShieldPoints(enemy.getMaxShieldPoints() * difficultyCoeff);
        enemy.setDamage(enemy.getDamage() * difficultyCoeff);
        enemy.setBaseArmor(enemy.getBaseArmor() * (1 + (difficultyCoeff / 4)));
        enemy.setCurrentHitpoints(enemy.getMaxHitPoints());
        enemy.setCurrentShieldPoints(enemy.getMaxShieldPoints());
        return enemy;
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

    public int getEnemiesSpawned () {
        return enemiesSpawned;
    }

    public void setEnemiesSpawned (int enemiesSpawned) {
        this.enemiesSpawned = enemiesSpawned;
    }

    public int getEnemiesKilled () {
        return enemiesKilled;
    }

    public void setEnemiesKilled (int enemiesKilled) {
        this.enemiesKilled += enemiesKilled;
    }

    public LevelSongs getCurrentLevelSong () {
        return currentLevelSong;
    }

    public void setCurrentLevelSong (LevelSongs currentLevelSong) {
        this.currentLevelSong = currentLevelSong;
    }

    public LevelDifficulty getCurrentLevelDifficulty () {
        return currentLevelDifficulty;
    }

    public void setCurrentLevelDifficulty (LevelDifficulty currentLevelDifficulty) {
        this.currentLevelDifficulty = currentLevelDifficulty;
    }

    public LevelLength getCurrentLevelLength () {
        return currentLevelLength;
    }

    public void setCurrentLevelLength (LevelLength currentLevelLength) {
        this.currentLevelLength = currentLevelLength;
    }

    public int getDifficultyModifier () {
        return difficultyModifier;
    }

    public void setDifficultyModifier (int difficultyModifier) {
        this.difficultyModifier = difficultyModifier;
    }
}
