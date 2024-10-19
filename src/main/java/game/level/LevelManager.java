package game.level;

import java.io.IOException;
import java.util.List;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.enums.LevelSongs;
import VisualAndAudioData.audio.enums.MusicMediaPlayer;
import game.UI.GameUICreator;
import game.gameobjects.enemies.enums.EnemyCategory;
import game.gameobjects.player.PlayerManager;
import game.level.enums.LevelTypes;
import game.managers.ShopManager;
import game.movement.Direction;
import game.movement.MovementPatternSize;
import game.gameobjects.enemies.*;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import VisualAndAudioData.audio.AudioManager;
import game.level.directors.DirectorManager;
import game.level.enums.LevelDifficulty;
import game.level.enums.LevelLength;

import javax.sound.sampled.UnsupportedAudioFileException;

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
            if(AudioManager.getInstance().getMusicMediaPlayer().equals(MusicMediaPlayer.MacOS)){
                currentLevelLength = LevelLength.getLevelLengthByDuration(audioManager.getTotalPlaybackLengthInSeconds());
            }

            if (levelType == LevelTypes.Regular || levelType == LevelTypes.Special) {
                if(audioManager.isBackgroundMusicInitializing()){
                    //We are still initializing the audio it seems
                    return;
                }
                if (audioManager.isBackgroundMusicFinished()) {
                    gameState.setGameState(GameStatusEnums.Level_Finished);
                    DirectorManager.getInstance().setEnabled(false);
                    enemyManager.removeOutOfBoundsEnemies();
                }
            } else if (levelType == LevelTypes.Boss) {
                if (!EnemyManager.getInstance().isBossAlive()) {
                    gameState.setGameState(GameStatusEnums.Level_Finished);
                    enemyManager.detonateAllEnemies();
                }
            }
        }



        //NextLevelPortal spawns in friendlymanager, now we wait for the player to enter the portal to set it to Level_Completed to show the score card
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
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
            //Now the GameBoard handles the following transition into "going to shop" or "back to main menu"
        }
    }


    // Called when a level starts, to saturate enemy list
    public void startLevel () {
        if (currentLevelDifficulty == null) {

            if(AudioManager.getInstance().getMusicMediaPlayer().equals(MusicMediaPlayer.MacOS)){
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
        if(nextLevelABossLevel){
            this.levelType = LevelTypes.Boss;
            currentLevelDifficulty = LevelDifficulty.Hard;
            currentLevelLength = LevelLength.Long;
        } else {
            this.levelType = LevelTypes.Regular;
        }


        currentLevelDifficultyScore = LevelSongs.getDifficultyScore(currentLevelDifficulty, currentLevelLength);
        GameUICreator.getInstance().createDifficultyWings(nextLevelABossLevel, currentLevelDifficultyScore);


        AudioManager audioManager = AudioManager.getInstance();

        PlayerManager.getInstance().getSpaceship().allowMovementBeyondBoundaries = false;
//        audioManager.testMode = true;
        audioManager.muteMode = true;

//        activateDirectors(this.levelType);
        try {
            activateMusic(this.levelType);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }

        gameState.setGameState(GameStatusEnums.Playing);


//        EnemyEnums enemyType = EnemyEnums.RedBoss;
//        Enemy enemy = EnemyCreator.createEnemy(enemyType, 800, 400, Direction.LEFT, enemyType.getDefaultScale()
//                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
//        enemy.getMovementConfiguration().setBoardBlockToHoverIn(4);
//        enemy.getMovementConfiguration().setPathFinder(new DestinationPathFinder());
//        enemy.getMovementConfiguration().setDestination(new Point(100, 99));
//        enemy.setAllowedVisualsToRotate(false);
//        enemy.getMovementConfiguration().setXMovementSpeed(1f);
//        enemy.getMovementConfiguration().setYMovementSpeed(1f);
//        enemy.getAnimation().changeImagetype(ImageEnums.Scout);
//        EnemyManager.getInstance().addEnemy(enemy);

        EnemyEnums enemyType = EnemyEnums.SpaceStationBoss;
        Enemy enemy = EnemyCreator.createEnemy(enemyType, 500, 200, Direction.RIGHT, enemyType.getDefaultScale()
                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
//        enemy.setAllowedToDealDamage(false);
//        enemy.setAllowedToMove(false);
//        enemy.setAllowedToFire(false);
        EnemyManager.getInstance().addEnemy(enemy);

//        Enemy enemy2 = EnemyCreator.createEnemy(enemyType, 300, 200, Direction.LEFT, enemyType.getDefaultScale()
//                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
//        enemy2.setAllowedToDealDamage(false);
//        enemy2.setAllowedToMove(false);
//        enemy2.setAllowedToFire(false);
//        EnemyManager.getInstance().addEnemy(enemy2);
//
//        Enemy enemy3 = EnemyCreator.createEnemy(enemyType, 200, 230, Direction.LEFT, enemyType.getDefaultScale()
//                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
//        enemy3.setAllowedToDealDamage(false);
//        enemy3.setAllowedToMove(false);
//        enemy3.setAllowedToFire(false);
//        EnemyManager.getInstance().addEnemy(enemy3);

    }

    private void activateDirectors (LevelTypes levelType) {
        DirectorManager directorManager = DirectorManager.getInstance();
        directorManager.setEnabled(true);
        directorManager.createMonsterCards();
        directorManager.createDirectors(levelType);
    }

    private void activateMusic (LevelTypes levelType) throws UnsupportedAudioFileException, IOException {
        switch (levelType) {
            case Regular -> {
                audioManager.playDefaultBackgroundMusic(currentLevelDifficulty, currentLevelLength, false);
                this.currentLevelSong = audioManager.getCurrentSong();
            }
            case Boss -> {
                audioManager.playDefaultBackgroundMusic(LevelSongs.getRandomBossSong(),true);
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

            if (validCoordinates(xCoordinate, yCoordinate, enemyType, scale)) {
                Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);
                enemyManager.addEnemy(enemy);
            }
        } else {
            if (validCoordinates(xCoordinate, yCoordinate, enemyType, scale)) {
                Enemy enemy = EnemyCreator.createEnemy(enemyType, xCoordinate, yCoordinate, direction, scale, xMovementSpeed, yMovementSpeed, MovementPatternSize.SMALL, boxCollision);
                enemy.setCenterCoordinates(xCoordinate, yCoordinate);
                enemy.resetMovementPath();
                enemyManager.addEnemy(enemy);
            }
        }
    }

    private boolean validCoordinates (int xCoordinate, int yCoordinate, EnemyEnums enemyType, float scale) {
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

    public boolean isNextLevelABossLevel(){
        int stagesCompleted = GameStateInfo.getInstance().getStagesCompleted();
        if(stagesCompleted == 0){
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
        if(!isNextLevelABossLevel()) {
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
        if(!isNextLevelABossLevel()) {
            this.currentLevelLength = currentLevelLength;
        }
    }

    public int getCurrentLevelDifficultyScore () {
        return currentLevelDifficultyScore;
    }

    public void setCurrentLevelDifficultyScore (int currentLevelDifficultyScore) {
        this.currentLevelDifficultyScore = currentLevelDifficultyScore;
    }


}
