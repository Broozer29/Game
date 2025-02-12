package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.game.UI.GameUICreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.level.directors.DirectorManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelLength;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.LevelSongs;

import java.util.List;

public class LevelManager {

    private static LevelManager instance = new LevelManager();
    private AudioManager audioManager = AudioManager.getInstance();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
    private GameStateInfo gameState = GameStateInfo.getInstance();


    private AudioEnums currentLevelSong;
    private LevelDifficulty currentLevelDifficulty;
    private LevelLength currentLevelLength;
    private int currentLevelDifficultyScore;
    private LevelTypes levelType;
    private EnemyTribes currentEnemyTribe;
    private int stagesBeforeBoss = 3;
    private PerformanceLogger performanceLogger = null;

    private LevelManager () {
        this.performanceLogger = new PerformanceLogger("Level Manager");
        resetManager();
    }

    public static LevelManager getInstance () {
        return instance;
    }

    public void resetManager () {
        currentLevelSong = null;
        levelType = LevelTypes.Regular;
        currentLevelDifficulty = LevelDifficulty.Medium;
        currentLevelLength = LevelLength.Medium;
        currentEnemyTribe = EnemyTribes.Pirates;
        performanceLogger.reset();
    }


    private int tickerCount = 0;
    public void updateGameTick () {
        // Check if the song has ended, then create the moving out portal
        if (gameState.getGameState() == GameStatusEnums.Playing) {
//            if (tickerCount % 5 == 0) {
                PerformanceLoggerManager.timeAndLog(performanceLogger, "Handle End Of Song Behaviour", this::handleEndOfSongBehaviour);
//                tickerCount = 0;
//            }
//            tickerCount++; // Increment the counter
        }

        //NextLevelPortal spawns in friendlymanager, now we wait for the player to enter the portal to set it to Level_Completed to show the score card
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Finish Level", this::finishLevel);
            //Now the GameBoard handles the following transition into "going to shop" or "back to main menu"
        }
    }

    private void handleEndOfSongBehaviour () {
        if (levelType == LevelTypes.Regular || levelType == LevelTypes.Special) {
            if (audioManager.isBackgroundMusicInitializing()) {
                //We are still initializing the audio it seems
                return;
            }
            if (audioManager.isLevelMusicFinished()) {
                gameState.setGameState(GameStatusEnums.Level_Finished);
                DirectorManager.getInstance().setEnabled(false);
                enemyManager.removeOutOfBoundsEnemies();
            }
        } else if (levelType == LevelTypes.Boss) {
            boolean bossAlive = EnemyManager.getInstance().isBossAlive();
            if (audioManager.isLevelMusicFinished() && bossAlive) {
                audioManager.playDefaultBackgroundMusic(LevelSongs.getBossTheme(GameStateInfo.getInstance().getNextBoss()), false);
            }

            if (!bossAlive) {
                gameState.setGameState(GameStatusEnums.Level_Finished);
                gameState.setBossesDefeated(gameState.getBossesDefeated() + 1);
                DirectorManager.getInstance().setEnabled(false);
                enemyManager.detonateAllEnemies();
            }
        }
    }

    private void finishLevel () {
        ShopManager shopManager = ShopManager.getInstance();
        shopManager.setLastLevelDifficulty(this.currentLevelDifficulty);
        shopManager.setLastLevelLength(this.currentLevelLength);
        shopManager.setLastLevelDifficultyScore(this.currentLevelDifficultyScore);
        shopManager.setRowsUnlockedByDifficulty(this.currentLevelDifficultyScore);
        shopManager.calculateRerollCost();

        PlayerManager.getInstance().getSpaceship().setImmune(true);
        gameState.setGameState(GameStatusEnums.Show_Level_Score_Card);
        this.currentLevelLength = null;
        this.currentLevelDifficulty = null;
        this.currentLevelDifficultyScore = 2;

    }


    // Called when a level starts, to saturate enemy list
    public void startLevel () {
        initDifficulty();
//        this.levelType = LevelTypes.Boss;

        GameUICreator.getInstance().createDifficultyWings(this.levelType.equals(LevelTypes.Boss), currentLevelDifficultyScore);


//        PlayerManager.getInstance().getSpaceship().allowMovementBeyondBoundaries = true;
//        audioManager.devTestShortLevelMode = true;
//        audioManager.devTestmuteMode = true;

        activateDirectors(this.levelType);
        activateMusic(this.levelType);
        gameState.setGameState(GameStatusEnums.Playing);

//        EnemyEnums enemyType = EnemyEnums.Scout;
//        Enemy enemy = EnemyCreator.createEnemy(enemyType, 1200, 300, Direction.LEFT, enemyType.getDefaultScale()
//                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
//        EnemyManager.getInstance().addEnemy(enemy);
    }

    private void initDifficulty () {
        boolean controlledByThirdPartyApp = AudioManager.getInstance().isMusicControlledByThirdPartyApp();
        if (controlledByThirdPartyApp) {
            currentLevelLength = LevelLength.Short;
            if (currentLevelDifficulty == null) {
                currentLevelDifficulty = LevelDifficulty.getRandomDifficulty();
            }
        } else {
            if (currentLevelDifficulty == null) {
                currentLevelDifficulty = LevelDifficulty.getRandomDifficulty();
            }
            if (currentLevelLength == null) {
                currentLevelLength = LevelLength.getRandomLength();
            }
        }

        boolean nextLevelABossLevel = isNextLevelABossLevel();
//        nextLevelABossLevel = true;
        if (nextLevelABossLevel) {
            this.levelType = LevelTypes.Boss;
            currentLevelDifficulty = LevelDifficulty.Hard;
            currentLevelLength = LevelLength.Long;
        } else {
            this.levelType = LevelTypes.Regular;
        }

        if (controlledByThirdPartyApp && !nextLevelABossLevel) {
            currentLevelDifficultyScore = LevelSongs.getDifficultyScoreByDifficultyOnly(currentLevelDifficulty);
        } else {
            currentLevelDifficultyScore = LevelSongs.getDifficultyScore(currentLevelDifficulty, currentLevelLength);
        }
        selectEnemyTribe();

    }

    private void selectEnemyTribe () {
        if (GameStateInfo.getInstance().getBossesDefeated() >= 2) {
            this.currentEnemyTribe = EnemyTribes.Zerg;
        } else {
            this.currentEnemyTribe = EnemyTribes.Pirates;
        }
//        this.currentEnemyTribe = EnemyTribes.Zerg;
    }


    private void activateDirectors (LevelTypes levelType) {
        DirectorManager directorManager = DirectorManager.getInstance();
        directorManager.setEnabled(true);
        directorManager.createMonsterCards();
        directorManager.createDirectors(levelType);
    }

    private void activateMusic (LevelTypes levelType) {
        switch (levelType) {
            case Regular -> {
                audioManager.playDefaultBackgroundMusic(currentLevelDifficulty, currentLevelLength, false);
                this.currentLevelSong = audioManager.getCurrentSong();
            }
            case Boss -> {
                audioManager.playDefaultBackgroundMusic(LevelSongs.getBossTheme(GameStateInfo.getInstance().getNextBoss()), false); //False because looping streams is not a thing, we need to recreate one instead
                this.currentLevelSong = audioManager.getCurrentSong();
                //to implement
            }
            case Special -> {
                //to implement
            }
        }
    }

    public void spawnEnemy (int xCoordinate, int yCoordinate, EnemyEnums enemyType,
                            Direction direction, float scale, boolean random, float xMovementSpeed, float yMovementSpeed, boolean boxCollision) {
        Point coordinates = new Point(xCoordinate, yCoordinate);

        //Placed in a wrapper for logging
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Spawn Enemy", () -> {
            if (random) {
                List<Integer> coordinatesList = spawningCoordinator.getSpawnCoordinatesByDirection(direction);
                coordinates.setX(coordinatesList.get(0));
                coordinates.setY(coordinatesList.get(1));
            }

            if (validSpawnCoordinates(coordinates.getX(), coordinates.getY(), enemyType, scale)) {
                Enemy enemy = EnemyCreator.createEnemy(enemyType, coordinates.getX(), coordinates.getY(), direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);

                if (!random) {
                    Point originalDestination = enemy.getMovementConfiguration().getDestination();
                    enemy.setCenterCoordinates(coordinates.getX(), coordinates.getY());
                    enemy.resetMovementPath();

                    if (originalDestination != null) {
                        enemy.getMovementConfiguration().setDestination(originalDestination);
                    }
                }

                enemyManager.addEnemy(enemy);
            }
        });
    }


    private boolean validSpawnCoordinates (int xCoordinate, int yCoordinate, EnemyEnums enemyType, float scale) {
        if (enemyType.getEnemyCategory().equals(EnemyCategory.Boss) ||
                enemyType.getEnemyCategory().equals(EnemyCategory.Special) ||
                enemyType.getEnemyCategory().equals(EnemyCategory.Summon)) {
            return true;
        }

        int actualWidth = Math.round(enemyType.getBaseWidth() * scale);
        int actualHeight = Math.round(enemyType.getBaseHeight() * scale);

        if (spawningCoordinator.checkValidEnemyXCoordinate(enemyManager.getEnemies(), xCoordinate,
                actualWidth)
                || spawningCoordinator.checkValidEnemyYCoordinate(enemyManager.getEnemies(),
                yCoordinate, actualHeight)) {
            return true;
        }
        return false;
    }

    public AudioEnums getCurrentLevelSong () {
        return currentLevelSong;
    }

    public void setCurrentLevelSong (AudioEnums currentLevelSong) {
        this.currentLevelSong = currentLevelSong;
    }

    public boolean isNextLevelABossLevel () {
        int stagesCompleted = GameStateInfo.getInstance().getStagesCompleted();
        if (stagesCompleted == 0) {
            return false;
        }
        return stagesCompleted % stagesBeforeBoss == 0;
    }

    public LevelDifficulty getCurrentLevelDifficulty () {
        if (currentLevelDifficulty == null) {
            currentLevelDifficulty = LevelDifficulty.getRandomDifficulty();
        }
        return currentLevelDifficulty;
    }

    public void setCurrentLevelDifficulty (LevelDifficulty currentLevelDifficulty) {
        if (!isNextLevelABossLevel()) {
            this.currentLevelDifficulty = currentLevelDifficulty;
        }
    }

    public LevelLength getCurrentLevelLength () {
        if (currentLevelLength == null) {
            currentLevelLength = LevelLength.getRandomLength();
        }
        return currentLevelLength;
    }

    public void setCurrentLevelLength (LevelLength currentLevelLength) {
        if (!isNextLevelABossLevel()) {
            this.currentLevelLength = currentLevelLength;
        }
    }

    public int getCurrentLevelDifficultyScore () {
        return currentLevelDifficultyScore;
    }

    public void setCurrentLevelDifficultyScore (int currentLevelDifficultyScore) {
        this.currentLevelDifficultyScore = currentLevelDifficultyScore;
    }

    public LevelTypes getLevelType () {
        return levelType;
    }

    public EnemyTribes getCurrentEnemyTribe () {
        return currentEnemyTribe;
    }

    public PerformanceLogger getPerformanceLogger () {
        return this.performanceLogger;
    }
}
