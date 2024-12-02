package net.riezebos.bruus.tbd.game.level;

import net.riezebos.bruus.tbd.game.UI.GameUICreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.level.directors.DirectorManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelDifficulty;
import net.riezebos.bruus.tbd.game.level.enums.LevelLength;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.game.gamestate.ShopManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.LevelSongs;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.MusicMediaPlayer;

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
    private int stagesBeforeBoss = 3;

    private LevelManager () {
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
    }


    public void updateGameTick () {
        // Check if the song has ended, then create the moving out portal
        if (gameState.getGameState() == GameStatusEnums.Playing) {
            if (AudioManager.getInstance().getMusicMediaPlayer().equals(MusicMediaPlayer.MacOS)) {
                updateLevelLengthForMacOSMediaPlayer();
            }
            handleEndOfSongBehaviour();
        }

        //NextLevelPortal spawns in friendlymanager, now we wait for the player to enter the portal to set it to Level_Completed to show the score card
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            finishLevel();
            //Now the GameBoard handles the following transition into "going to shop" or "back to main menu"
        }
    }

    private void updateLevelLengthForMacOSMediaPlayer () {
        if (currentLevelLength != LevelLength.getLevelLengthByDuration(audioManager.getTotalPlaybackLengthInSeconds())) {
            boolean isBossLevel = this.levelType == LevelTypes.Boss;
            currentLevelLength = LevelLength.getLevelLengthByDuration(audioManager.getTotalPlaybackLengthInSeconds());
            currentLevelDifficultyScore = LevelSongs.getDifficultyScore(currentLevelDifficulty, currentLevelLength);
            GameUICreator.getInstance().createDifficultyWings(isBossLevel, currentLevelDifficultyScore);
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
            if(audioManager.isLevelMusicFinished() && bossAlive){
                audioManager.playDefaultBackgroundMusic(LevelSongs.getBossTheme(GameStateInfo.getInstance().getNextBoss()), false);
            }

            if (!bossAlive) {
                gameState.setGameState(GameStatusEnums.Level_Finished);
                gameState.setBossesDefeated(gameState.getBossesDefeated() + 1);
                enemyManager.detonateAllEnemies();
            }
        }
    }

    private void finishLevel () {
        ShopManager shopManager = ShopManager.getInstance();
        shopManager.setLastLevelDifficulty(this.currentLevelDifficulty);
        shopManager.setLastLevelLength(this.currentLevelLength);
        shopManager.setLastLevelDifficultyCoeff(this.currentLevelDifficultyScore);
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

        PlayerManager.getInstance().getSpaceship().allowMovementBeyondBoundaries = false;
//        audioManager.devTestShortLevelMode = true;
//        audioManager.devTestmuteMode = true
        activateDirectors(this.levelType);
        activateMusic(this.levelType);
        gameState.setGameState(GameStatusEnums.Playing);

        EnemyEnums enemyType = EnemyEnums.Seeker;
        Enemy enemy = EnemyCreator.createEnemy(enemyType, 1000, 300, Direction.LEFT, enemyType.getDefaultScale()
                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
//        enemy.setAllowedVisualsToRotate(false);
//        enemy.getMovementConfiguration().setBoardBlockToHoverIn(4);
//        enemy.getMovementConfiguration().setPathFinder(new DestinationPathFinder());
//        enemy.getMovementConfiguration().setDestination(new Point(100, 99));
//        enemy.setAllowedVisualsToRotate(false);
        enemy.getMovementConfiguration().setXMovementSpeed(0f);
        enemy.getMovementConfiguration().setYMovementSpeed(0f);
//        enemy.setCenterCoordinates(DataClass.getInstance().getWindowWidth() / 2 , DataClass.getInstance().getPlayableWindowMaxHeight() / 2);
//        enemy.getAnimation().changeImagetype(ImageEnums.Scout);
//        EnemyManager.getInstance().addEnemy(enemy);


        EnemyEnums enemyType2 = EnemyEnums.Tazer;
        Enemy enemy2 = EnemyCreator.createEnemy(enemyType2, 1050, 300, Direction.LEFT, enemyType2.getDefaultScale()
                , enemyType2.getMovementSpeed(), enemyType2.getMovementSpeed(), MovementPatternSize.SMALL, false);
//        enemy2.setAllowedVisualsToRotate(false);
        enemy2.getMovementConfiguration().setXMovementSpeed(0f);
        enemy2.getMovementConfiguration().setYMovementSpeed(0f);
//        EnemyManager.getInstance().addEnemy(enemy2);

    }

    private void initDifficulty(){
        if (currentLevelDifficulty == null) {
            if (AudioManager.getInstance().getMusicMediaPlayer().equals(MusicMediaPlayer.MacOS)) {
                currentLevelDifficulty = LevelDifficulty.Medium;
            } else {
                currentLevelDifficulty = LevelDifficulty.getRandomDifficulty();
            }
        }

        if (currentLevelLength == null) {
            currentLevelLength = LevelLength.getRandomLength();
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

        currentLevelDifficultyScore = LevelSongs.getDifficultyScore(currentLevelDifficulty, currentLevelLength);
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
        // Spawn random if there are no given X/Y coords
        if (random) {
            List<Integer> coordinatesList = spawningCoordinator.getSpawnCoordinatesByDirection(direction);

            xCoordinate = coordinatesList.get(0);
            yCoordinate = coordinatesList.get(1);

            if (validSpawnCoordinates(xCoordinate, yCoordinate, enemyType, scale)) {
                Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);
                enemyManager.addEnemy(enemy);
            }
        } else {
            if (validSpawnCoordinates(xCoordinate, yCoordinate, enemyType, scale)) {
                Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);

                Point originalDestination = enemy.getMovementConfiguration().getDestination();
                enemy.setCenterCoordinates(xCoordinate, yCoordinate);
                enemy.resetMovementPath();

                if (originalDestination != null) {
                    enemy.getMovementConfiguration().setDestination(originalDestination);
                }
                enemyManager.addEnemy(enemy);
            }
        }
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
}
