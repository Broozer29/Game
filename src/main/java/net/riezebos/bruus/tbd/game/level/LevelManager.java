package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.DevTestSettings;
import net.riezebos.bruus.tbd.game.UI.GameUICreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyTribes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.StuiversBestFriend;
import net.riezebos.bruus.tbd.game.level.directors.DirectorManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.MiniBossConfig;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
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
    private GameState gameState = GameState.getInstance();


    private AudioEnums currentLevelSong;
    private LevelDifficulty currentLevelDifficulty;
    private MiniBossConfig currentMiniBossConfig;
    private int currentLevelDifficultyScore;
    private LevelTypes levelType;
    private EnemyTribes currentEnemyTribe;
    private int stagesBeforeBoss = 3;
    private PerformanceLogger performanceLogger = null;

    private LevelManager() {
        this.performanceLogger = new PerformanceLogger("Level Manager");
        resetManager();
    }

    public static LevelManager getInstance() {
        return instance;
    }

    public void resetManager() {
        currentLevelSong = null;
        levelType = LevelTypes.Regular;
        currentLevelDifficulty = LevelDifficulty.Easy;
        currentMiniBossConfig = MiniBossConfig.Easy;
        currentEnemyTribe = EnemyTribes.Pirates;
        performanceLogger.reset();
    }



    public void updateGameTick() {
        // Check if the song has ended, then create the moving out portal
        if (gameState.getGameState() == GameStatusEnums.Playing) {
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Handle End Of Song Behaviour", this::handleEndOfSongBehaviour);
        }

        //NextLevelPortal spawns in friendlymanager, now we wait for the player to enter the portal to set it to Level_Completed to show the score card
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            PerformanceLoggerManager.timeAndLog(performanceLogger, "Finish Level", this::finishLevel);
            //Now the GameBoard handles the following transition into "going to shop" or "back to main menu"
        }
    }

    private void handleEndOfSongBehaviour() {
        if (levelType == LevelTypes.Regular || levelType == LevelTypes.Special) {
            if (audioManager.isBackgroundMusicInitializing()) {
                //We are still initializing the audio it seems
                return;
            }
            if (audioManager.isLevelMusicFinished()) {
                gameState.setGameState(GameStatusEnums.Level_Finished);
                DirectorManager.getInstance().setEnabled(false);
                enemyManager.removeOutOfBoundsEnemies();
                enemyManager.startBurningEnemies();
            }
        } else if (levelType == LevelTypes.Boss) {
            boolean bossAlive = EnemyManager.getInstance().isBossAlive();
            if (audioManager.isLevelMusicFinished() && bossAlive) {
                audioManager.playDefaultBackgroundMusic(LevelSongs.getBossTheme(getNextBoss()), false);
            }

            if (!bossAlive) {
                gameState.setGameState(GameStatusEnums.Level_Finished);
                gameState.setBossesDefeated(gameState.getBossesDefeated() + 1);
                DirectorManager.getInstance().setEnabled(false);
                enemyManager.detonateAllEnemies();
            }
        }
    }

    private void finishLevel() {
        ShopManager shopManager = ShopManager.getInstance();
        shopManager.setLastLevelDifficulty(this.currentLevelDifficulty);
        shopManager.setLastMiniBossConfig(this.currentMiniBossConfig);
        shopManager.setLastLevelDifficultyScore(this.currentLevelDifficultyScore);
        shopManager.setRowsUnlockedByDifficulty(this.currentLevelDifficultyScore);
        shopManager.calculateRerollCost();

        PlayerManager.getInstance().getSpaceship().setImmune(true);
        gameState.setGameState(GameStatusEnums.Show_Level_Score_Card);
        //disabling this causes the game to remember the players last selected option
//        this.currentLevelLength = null;
//        this.currentLevelDifficulty = null;
//        this.currentLevelDifficultyScore = 2;

    }


    // Called when a level starts, to saturate enemy list
    public void startLevel() {
        BoonManager.getInstance().activateBoons(BoonActivationEnums.Start_of_Level);
        GameState.getInstance().setLevelStartTime(GameState.getInstance().getGameSeconds());

        initDifficulty();

        if (DevTestSettings.onlyBossLevels) {
            this.levelType = LevelTypes.Boss;
        }

        GameUICreator.getInstance().createDifficultyWings(this.levelType.equals(LevelTypes.Boss), currentLevelDifficultyScore);

        if (DevTestSettings.enablePlayerMovingPastBoundaries) {
            PlayerManager.getInstance().getSpaceship().allowMovementBeyondBoundaries = true;
        }

        if (DevTestSettings.devTestShortLevelMode) {
            audioManager.devTestShortLevelMode = true;
        }

        if (!DevTestSettings.blockDirectors) {
            activateDirectors(this.levelType);
        }
        if (!DevTestSettings.blockMusic) {
            activateMusic(this.levelType);
        }
        gameState.setGameState(GameStatusEnums.Playing);

        if (DevTestSettings.spawnTargetDummy) {
            EnemyEnums enemyType = EnemyEnums.Bomba;
            Enemy dummy = EnemyCreator.createEnemy(enemyType, 1600, 500, Direction.LEFT, enemyType.getDefaultScale()
                    , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
            dummy.setXCoordinate(600);
            dummy.setMaxHitPoints(1000);
            dummy.setCurrentHitpoints(1000);
            dummy.setAllowedToFire(false);
            dummy.setAllowedToMove(false);
            EnemyManager.getInstance().addEnemy(dummy);
        }

        if(DevTestSettings.instantlySpawnPortal) {
            gameState.setGameState(GameStatusEnums.Level_Finished);
        }
    }

    private void initDifficulty() {
        if (currentLevelDifficulty == null) {
            currentLevelDifficulty = LevelDifficulty.Medium;
        }
        if (currentMiniBossConfig == null) {
            currentMiniBossConfig = MiniBossConfig.Medium;
        }

        boolean nextLevelABossLevel = isNextLevelABossLevel();
        if (nextLevelABossLevel) {
            this.levelType = LevelTypes.Boss;
            currentLevelDifficulty = LevelDifficulty.Hard;
            currentMiniBossConfig = MiniBossConfig.Hard;
        } else {
            this.levelType = LevelTypes.Regular;
        }

        if (!nextLevelABossLevel) {
            currentLevelDifficultyScore = LevelSongs.getDifficultyScore(currentLevelDifficulty, currentMiniBossConfig);
        } else {
            currentLevelDifficultyScore = 6; //safety guard for boss levels
        }
        selectEnemyTribe();

    }

    private void selectEnemyTribe() {
        if (GameState.getInstance().getBossesDefeated() >= 3) {
            this.currentEnemyTribe = EnemyTribes.Zerg;
        } else {
            this.currentEnemyTribe = EnemyTribes.Pirates;
        }
//        this.currentEnemyTribe = EnemyTribes.Zerg;
    }

    public EnemyEnums getNextBoss() {
//        return EnemyEnums.YellowBoss;
        int bossesDefeated = GameState.getInstance().getBossesDefeated();

        // Use modulo to cycle through the bosses
        switch (bossesDefeated % EnemyEnums.getAmountOfBossEnemies()) {
            case 0:
                return EnemyEnums.RedBoss;
            case 1:
                return EnemyEnums.SpaceStationBoss;
            case 2:
                return EnemyEnums.CarrierBoss;
            case 3:
                return EnemyEnums.YellowBoss;
            default:
                return EnemyEnums.RedBoss;
        }
    }


    //This needs to be reworked if infinite boss scaling abilities is to be achieved
    public int getBossDifficultyLevel(){
        if(GameState.getInstance().getBossesDefeated() > 4 || GameState.getInstance().getGameMode().equals(GameMode.Nightmare)){ //cycled through all bosses, keep this number updated manually for now
            return 1;
        }
        return 0;
    }


    private void activateDirectors(LevelTypes levelType) {
        DirectorManager directorManager = DirectorManager.getInstance();
        directorManager.setEnabled(true);
        directorManager.createMonsterCards();
        directorManager.createDirectors(levelType);
    }

    private void activateMusic(LevelTypes levelType) {
        switch (levelType) {
            case Regular -> {
                audioManager.playDefaultBackgroundMusic(currentLevelDifficulty, currentMiniBossConfig, false);
                this.currentLevelSong = audioManager.getCurrentSong();
            }
            case Boss -> {
                audioManager.playDefaultBackgroundMusic(LevelSongs.getBossTheme(getNextBoss()), true);
                this.currentLevelSong = audioManager.getCurrentSong();
                //to implement
            }
            case Special -> {
                //to implement
            }
        }
    }

    public void spawnEnemy(int xCoordinate, int yCoordinate, EnemyEnums enemyType,
                           Direction direction, float scale, boolean random, float xMovementSpeed, float yMovementSpeed, boolean boxCollision) {
        Point coordinates = new Point(xCoordinate, yCoordinate);

        //Placed in a wrapper for logging
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Spawn Enemy", () -> {
            if (random) {
                List<Integer> coordinatesList = spawningCoordinator.getSpawnCoordinatesByDirection(direction);
                coordinates.setX(coordinatesList.get(0));
                coordinates.setY(coordinatesList.get(1));
            }

            if (validSpawnCoordinates(coordinates.getX(), coordinates.getY(), enemyType, scale) || !random) {
                Enemy enemy = EnemyCreator.createEnemy(enemyType, coordinates.getX(), coordinates.getY(), direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);

                if (!random) {
                    Point originalDestination = enemy.getMovementConfiguration().getDestination();
                    enemy.setCenterCoordinates(coordinates.getX(), coordinates.getY());
                    enemy.resetMovementPath();

                    if (originalDestination != null) {

                        if((direction.equals(Direction.LEFT) || direction.equals(Direction.RIGHT)) && originalDestination.getY() != enemy.getYCoordinate()){
                            originalDestination.setY(enemy.getYCoordinate()); //er is een verschil van minder dan 1, corrigeer het anders gaan visuals trillen
                        }
                        enemy.getMovementConfiguration().setDestination(originalDestination);
                    }
                }

                enemyManager.addEnemy(enemy);
            }
        });
    }


    private boolean validSpawnCoordinates(int xCoordinate, int yCoordinate, EnemyEnums enemyType, float scale) {
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

    public AudioEnums getCurrentLevelSong() {
        return currentLevelSong;
    }

    public void setCurrentLevelSong(AudioEnums currentLevelSong) {
        this.currentLevelSong = currentLevelSong;
    }

    public boolean isNextLevelABossLevel() {
        if (GameState.getInstance().getGameMode().equals(GameMode.ManMode)) {
            return true;
        }

        int stagesCompleted = GameState.getInstance().getStagesCompleted();
        if (stagesCompleted == 0) {
            return false;
        }
        return stagesCompleted % stagesBeforeBoss == 0;
    }

    public LevelDifficulty getCurrentLevelDifficulty() {
        if (currentLevelDifficulty == null) {
            currentLevelDifficulty = LevelDifficulty.getRandomDifficulty();
        }
        return currentLevelDifficulty;
    }

    public void setCurrentLevelDifficulty(LevelDifficulty currentLevelDifficulty) {
        if (!isNextLevelABossLevel()) {
            this.currentLevelDifficulty = currentLevelDifficulty;
        }
    }

    public MiniBossConfig getCurrentMiniBossConfig() {
        if (currentMiniBossConfig == null) {
            currentMiniBossConfig = MiniBossConfig.getRandomMiniBossConfig();
        }
        return currentMiniBossConfig;
    }

    public void setCurrentMiniBossConfig(MiniBossConfig currentMiniBossConfig) {
        if (!isNextLevelABossLevel()) {
            this.currentMiniBossConfig = currentMiniBossConfig;
        }
    }

    public int getCurrentLevelDifficultyScore() {
        return currentLevelDifficultyScore;
    }

    public void setCurrentLevelDifficultyScore(int currentLevelDifficultyScore) {
        this.currentLevelDifficultyScore = currentLevelDifficultyScore;
    }

    public LevelTypes getLevelType() {
        return levelType;
    }

    public EnemyTribes getCurrentEnemyTribe() {
        return currentEnemyTribe;
    }

    public PerformanceLogger getPerformanceLogger() {
        return this.performanceLogger;
    }
}
