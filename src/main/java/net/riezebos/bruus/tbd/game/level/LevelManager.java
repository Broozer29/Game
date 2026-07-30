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
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.BoonManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.boons.boonimplementations.BoonActivationEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.level.directors.DirectorManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.game.level.enums.MiniBossConfig;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

import java.util.ArrayList;
import java.util.Arrays;
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
    private LevelDifficulty lastSelectedDifficulty = null;
    private MiniBossConfig lastSelectedMiniBossConfig = null;

    private int currentLevelDifficultyScore;
    private LevelTypes levelType;
    private EnemyTribes currentEnemyTribe;
    private int stagesBeforeBoss = 3;
    private PerformanceLogger performanceLogger = null;
    private EnemyEnums selectedBoss = null;

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
        loopBreaker = 0;
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
                audioManager.tryToDisposeBackgroundMusic();
            }
        } else if (levelType == LevelTypes.Boss) {
            boolean bossAlive = EnemyManager.getInstance().isBossAlive();

            if (audioManager.isLevelMusicFinished() && bossAlive) {
                audioManager.playDefaultBackgroundMusicForALevel(AudioEnums.getBossTheme(selectedBoss), false);
            }

            if (!bossAlive) {
                gameState.setGameState(GameStatusEnums.Level_Finished);
                DirectorManager.getInstance().setEnabled(false);
                enemyManager.deleteAllEnemies();
            }
        }
    }

    public void finishLevel() {
        ShopManager shopManager = ShopManager.getInstance();
        shopManager.setLastLevelDifficulty(this.currentLevelDifficulty);
        shopManager.setLastMiniBossConfig(this.currentMiniBossConfig);
        shopManager.setLastLevelDifficultyScore(this.currentLevelDifficultyScore);
        shopManager.setRowsUnlockedByDifficulty(this.currentLevelDifficultyScore);
        shopManager.calculateRerollCost();

        for (SpaceShip spaceShip : PlayerManager.getInstance().getAllSpaceShips()) {
            spaceShip.setImmune(true);
        }

        gameState.setGameState(GameStatusEnums.Show_Level_Score_Card);
    }


    public void startLevel() {
        BoonManager.getInstance().activateBoons(BoonActivationEnums.Start_of_Level);
        GameState.getInstance().setLevelStartTime(GameState.getInstance().getGameSeconds());

        initDifficulty();

        if (DevTestSettings.onlyBossLevels) {
            this.levelType = LevelTypes.Boss;
        }

        if (this.levelType.equals(LevelTypes.Boss)) {
            selectedBoss = getNextBoss();
        }

        GameUICreator.getInstance().createDifficultyWings(this.levelType.equals(LevelTypes.Boss), getDifficultyScore());
        GameUICreator.getInstance().createProgressBar();
        GameUICreator.getInstance().createMineralIcon();

        for (SpaceShip spaceShip : PlayerManager.getInstance().getAllSpaceShips()) {
            spaceShip.allowMovementBeyondBoundaries = DevTestSettings.enablePlayerMovingPastBoundaries;
        }

        if (!DevTestSettings.blockDirectors) {
            activateDirectors(this.levelType);
        }
        if (!DevTestSettings.blockMusic) {
            activateMusic(this.levelType);
        }
        gameState.setGameState(GameStatusEnums.Playing);

        if (DevTestSettings.spawnTargetDummy) {
            EnemyEnums enemyType = EnemyEnums.RoyalGuardCaptain;
            Enemy dummy = EnemyCreator.createEnemy(enemyType, 1600, 500, Direction.LEFT, enemyType.getDefaultScale()
                    , enemyType.getMovementSpeed());
//            dummy.setXCoordinate(600);
            dummy.setMaxHitPoints(1000);
            dummy.setCurrentHitpoints(1000);
//            dummy.setAllowedToFire(false);
//            dummy.setAllowedToMove(false);
            EnemyManager.getInstance().addEnemy(dummy);
        }

        if (DevTestSettings.instantlySpawnPortal) {
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
            currentLevelDifficultyScore = getDifficultyScore();
        } else {
            currentLevelDifficultyScore = 6; //safety guard for boss levels
        }
        selectEnemyTribe();

    }

    private void selectEnemyTribe() {
        if (this.isNextLevelABossLevel()) {
            //Theoretically, this attribute is never used in boss levels, but keeping it for now
            this.currentEnemyTribe = EnemyTribes.Pirates;
            return;
        }

        switch (this.currentLevelDifficulty) {
            case Easy -> {
                this.currentEnemyTribe = EnemyTribes.Pirates;
            }
            case Medium -> {
                this.currentEnemyTribe = EnemyTribes.Zerg;
            }
            case Hard -> {
                this.currentEnemyTribe = EnemyTribes.RoyalGuard;
            }
        }
    }


    private List<EnemyEnums> lastSpawnedBosses = new ArrayList<>();
    private int loopBreaker = 0;

    public EnemyEnums getNextBoss() {
//        return EnemyEnums.TwinBoss;

        List<EnemyEnums> eligibleBosses = Arrays.stream(EnemyEnums.values()).filter(enemyEnums ->
                enemyEnums.getEnemyCategory().equals(EnemyCategory.Boss) &&
                        GameState.getInstance().getBossesDefeated() >= enemyEnums.getBossKillCountRequiredBeforeAllowedToSpawn()
        ).toList();
        int randomlySelectedBoss = (int) (Math.random() * eligibleBosses.size());

        if (lastSpawnedBosses.contains(eligibleBosses.get(randomlySelectedBoss))) {
            loopBreaker++;
            if (loopBreaker > 1000) {
                lastSpawnedBosses.clear(); //if no bosses can be spawned, clear last used so we can re-use them again
            }
            return getNextBoss();
        }
        lastSpawnedBosses.add(eligibleBosses.get(randomlySelectedBoss));
        return eligibleBosses.get(randomlySelectedBoss);
    }

    public EnemyEnums getSelectedBoss() {
        return selectedBoss;
    }

    public void setSelectedBoss(EnemyEnums selectedBoss) {
        this.selectedBoss = selectedBoss;
    }

    //Returns the bossDifficultyLevel which is basically the bosses killed so far, creative enough?
    public int getBossDifficultyLevel() {
        return GameState.getInstance().getBossesDefeated();
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
                audioManager.playDefaultBackgroundMusicForALevel();
                this.currentLevelSong = audioManager.getCurrentSong();
            }
            case Boss -> {
                audioManager.playDefaultBackgroundMusicForALevel(AudioEnums.getBossTheme(selectedBoss), true);
                this.currentLevelSong = audioManager.getCurrentSong();
                //to implement
            }
            case Special -> {
                //to implement
            }
        }
    }

    public void spawnEnemy(int xCoordinate, int yCoordinate, EnemyEnums enemyType,
                           Direction direction, float scale, boolean random, float xMovementSpeed) {
        Point coordinates = new Point(xCoordinate, yCoordinate);

        PerformanceLoggerManager.timeAndLog(performanceLogger, "Spawn Enemy", () -> {
            if (random) {
                List<Integer> coordinatesList = spawningCoordinator.getSpawnCoordinatesByDirection(direction);
                coordinates.setX(coordinatesList.get(0));
                coordinates.setY(coordinatesList.get(1));
            }
            if (validSpawnCoordinates(coordinates.getX(), coordinates.getY(), enemyType, scale) || !random) {
                Enemy enemy = EnemyCreator.createEnemy(enemyType, coordinates.getX(), coordinates.getY(), direction, scale, xMovementSpeed);
                if (!random) {
                    Point originalDestination = enemy.getMovementConfiguration().getDestination();
                    enemy.setCenterCoordinates(coordinates.getX(), coordinates.getY());
                    enemy.resetMovementPath();

                    if (originalDestination != null) {
                        if ((direction.equals(Direction.LEFT) || direction.equals(Direction.RIGHT)) && originalDestination.getY() != enemy.getYCoordinate()) {
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
        if (GameState.getInstance().getGameMode().equals(GameMode.ManMode) || DevTestSettings.onlyBossLevels) {
            return true;
        }

        int stagesCompleted = GameState.getInstance().getStagesCompleted();
        if (stagesCompleted == 0) {
            return false;
        }

        //Initially 3 levels between bosses, then after the second boss every other level is a boss level
        if (stagesCompleted >= 6) {
            stagesBeforeBoss = 2;
        } else {
            stagesBeforeBoss = 3;
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

    public MiniBossConfig getLastSelectedMiniBossConfig() {
        if (lastSelectedMiniBossConfig == null) {
            return getCurrentMiniBossConfig();
        }
        return lastSelectedMiniBossConfig;
    }

    public void setLastSelectedMiniBossConfig(MiniBossConfig lastSelectedMiniBossConfig) {
        this.lastSelectedMiniBossConfig = lastSelectedMiniBossConfig;
    }

    public LevelDifficulty getLastSelectedDifficulty() {
        if (lastSelectedDifficulty == null) {
            return getCurrentLevelDifficulty();
        }

        return lastSelectedDifficulty;
    }

    public void setLastSelectedDifficulty(LevelDifficulty lastSelectedDifficulty) {
        this.lastSelectedDifficulty = lastSelectedDifficulty;
    }

    public int getDifficultyScore() {
        LevelDifficulty difficulty = this.lastSelectedDifficulty == null ? getCurrentLevelDifficulty() : this.lastSelectedDifficulty;
        MiniBossConfig length = this.lastSelectedMiniBossConfig == null ? getCurrentMiniBossConfig() : this.lastSelectedMiniBossConfig;


        int difficultyWeight = difficulty.ordinal() + 1; // Assuming Enum order is EASY, MEDIUM, HARD
        int lengthWeight = length.ordinal() + 1; // Assuming Enum order is SHORT, MEDIUM, LONG
        return difficultyWeight + lengthWeight;
    }

    public int getSelectedDifficultyScore() {
        LevelDifficulty difficulty = this.lastSelectedDifficulty == null ? getCurrentLevelDifficulty() : this.lastSelectedDifficulty;
        MiniBossConfig length = this.lastSelectedMiniBossConfig == null ? getCurrentMiniBossConfig() : this.lastSelectedMiniBossConfig;


        int difficultyWeight = difficulty.ordinal() + 1; // Assuming Enum order is EASY, MEDIUM, HARD
        int lengthWeight = length.ordinal() + 1; // Assuming Enum order is SHORT, MEDIUM, LONG
        return difficultyWeight + lengthWeight;
    }

    public ImageEnums getImageEnumByDifficultyScore(int difficultyScore) {
        if (difficultyScore <= 2) return ImageEnums.PurpleWings1; // Image 1
        if (difficultyScore == 3) return ImageEnums.PurpleWings2; // Image 2
        if (difficultyScore == 4) return ImageEnums.PurpleWings3; // Image 3
        if (difficultyScore == 5) return ImageEnums.PurpleWings4; // Image 4
        return ImageEnums.PurpleWings5; // Image 5
    }

    public void resetBossUsed() {
        lastSpawnedBosses.clear();
    }
}
