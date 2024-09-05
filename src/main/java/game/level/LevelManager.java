package game.level;

import java.io.IOException;
import java.util.List;

import VisualAndAudioData.audio.enums.LevelSongs;
import game.UI.GameUICreator;
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
import game.movement.Point;
import game.movement.pathfinders.DestinationPathFinder;
import game.movement.pathfinders.HoverPathFinder;
import game.movement.pathfinders.RegularPathFinder;

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
    private int currentLevelDifficultyScore;

    private LevelManager () {
        resetManager();
    }

    public static LevelManager getInstance () {
        return instance;
    }

    public void resetManager () {
        currentLevelSong = null;
//        currentLevelDifficulty = LevelDifficulty.Easy;
//        currentLevelLength = LevelLength.Short;
    }

    public void updateGameTick () {
        // Check if the song has ended, then create the moving out portal
        if (gameState.getGameState() == GameStatusEnums.Playing && audioManager.getBackgroundMusic().getFramePosition() >= audioManager.getBackgroundMusic().getFrameLength()) {
            gameState.setGameState(GameStatusEnums.Level_Finished);
            ShopManager shopManager = ShopManager.getInstance();
            shopManager.setLastLevelDifficulty(this.currentLevelDifficulty);
            shopManager.setLastLevelLength(this.currentLevelLength);
            shopManager.setLastLevelDifficultyCoeff(this.currentLevelDifficultyScore);
            shopManager.setRowsUnlockedByDifficulty(this.currentLevelDifficultyScore);
            DirectorManager.getInstance().setEnabled(false);
            shopManager.calculateRerollCost();
        }

        //NextLevelPortal spawns in friendlymanager, now we wait for the player to enter the portal to set it to Level_Completed
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            gameState.setGameState(GameStatusEnums.Show_Level_Score_Card);
            this.currentLevelLength = null;
            this.currentLevelDifficulty = null;
            this.currentLevelDifficultyScore = 2;
            //Now the GameBoard handles the following transition into "going to shop" or "back to main menu"
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
//            audioManager.testMode = true;
            audioManager.muteMode = true;
            audioManager.playRandomBackgroundMusic(currentLevelDifficulty, currentLevelLength);
            this.currentLevelSong = audioManager.getCurrentSong();
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }

        currentLevelDifficultyScore = LevelSongs.getDifficultyScore(currentLevelDifficulty, currentLevelLength);
        GameUICreator.getInstance().createDifficultyWings();


        gameState.setGameState(GameStatusEnums.Playing);
        DirectorManager directorManager = DirectorManager.getInstance();
//        directorManager.setEnabled(true);
//        directorManager.createMonsterCards();
//        directorManager.createDirectors();


//        PlayerStats.getInstance().setShopRerollDiscount(99);
//        PlayerInventory.getInstance().addMinerals(1000000);


//        for(int i = 0; i < 150; i++){
//            PlayerInventory.getInstance().addItem(ItemEnums.getRandomItemByRarity(ItemRarityEnums.Common));
//        }
//        directorManager.testingRichMode = true;


//        PlayerStats.getInstance().setBaseDamage(1);

//        Director testDirector = DirectorManager.getInstance().getTestDirector();
//        testDirector.spawnRegularFormation(SpawnFormationEnums.V, EnemyEnums.Scout);

        EnemyEnums enemyType = EnemyEnums.RedBoss;
        Enemy enemy = EnemyCreator.createEnemy(enemyType, 1200, 300, Direction.LEFT, enemyType.getDefaultScale()
                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
        enemy.getMovementConfiguration().setBoardBlockToHoverIn(4);
        enemy.getMovementConfiguration().setPathFinder(new HoverPathFinder());
//        enemy.getMovementConfiguration().setDestination(new Point(800, 300));
//        enemy.setAllowedVisualsToRotate(false);
//        enemy.getMovementConfiguration().setXMovementSpeed(2f);
//        enemy.getMovementConfiguration().setYMovementSpeed(2f);
//        enemy.getAnimation().changeImagetype(ImageEnums.Scout);
        EnemyManager.getInstance().addEnemy(enemy);
//////
//        EnemyEnums enemyType2 = EnemyEnums.Bomba;
//        Enemy enemy2 = EnemyCreator.createEnemy(enemyType2, 800, 600, Direction.LEFT, enemyType2.getDefaultScale()
//                , enemyType2.getMovementSpeed(), enemyType2.getMovementSpeed(), MovementPatternSize.SMALL, false);
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
        if (enemyType.equals(EnemyEnums.CashCarrier)) {
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

    public int getCurrentLevelDifficultyScore () {
        return currentLevelDifficultyScore;
    }

    public void setCurrentLevelDifficultyScore (int currentLevelDifficultyScore) {
        this.currentLevelDifficultyScore = currentLevelDifficultyScore;
    }
}
