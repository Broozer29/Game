package game.level;

import java.io.IOException;
import java.util.List;

import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.audio.enums.LevelSongs;
import VisualAndAudioData.image.ImageEnums;
import game.UI.GameUICreator;
import game.gameobjects.missiles.MissileManager;
import game.gameobjects.missiles.specialAttacks.Laserbeam;
import game.gameobjects.missiles.specialAttacks.LaserbeamConfiguration;
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
import game.movement.Point;
import game.movement.pathfinders.HoverPathFinder;
import game.movement.pathfinders.RegularPathFinder;

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
            if (levelType == LevelTypes.Regular || levelType == LevelTypes.Special) {
                if (audioManager.getBackgroundMusic().getFramePosition() >= audioManager.getBackgroundMusic().getFrameLength()) {
                    gameState.setGameState(GameStatusEnums.Level_Finished);
                }
            } else if (levelType == LevelTypes.Boss) {
                if (!EnemyManager.getInstance().isBossAlive()) {
                    gameState.setGameState(GameStatusEnums.Level_Finished);
                }
            }
        }

        //NextLevelPortal spawns in friendlymanager, now we wait for the player to enter the portal to set it to Level_Completed
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            ShopManager shopManager = ShopManager.getInstance();
            shopManager.setLastLevelDifficulty(this.currentLevelDifficulty);
            shopManager.setLastLevelLength(this.currentLevelLength);
            shopManager.setLastLevelDifficultyCoeff(this.currentLevelDifficultyScore);
            shopManager.setRowsUnlockedByDifficulty(this.currentLevelDifficultyScore);
            DirectorManager.getInstance().setEnabled(false);
            shopManager.calculateRerollCost();

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

        boolean nextLevelABossLevel = isNextLevelABossLevel();
        if(nextLevelABossLevel){
            this.levelType = LevelTypes.Boss;
            currentLevelDifficulty = LevelDifficulty.Hard;
            currentLevelLength = LevelLength.Long;
        } else {
            this.levelType = LevelTypes.Regular;
        }


        currentLevelDifficultyScore = LevelSongs.getDifficultyScore(currentLevelDifficulty, currentLevelLength);
        GameUICreator.getInstance().createDifficultyWings(nextLevelABossLevel, currentLevelDifficultyScore);
        gameState.setGameState(GameStatusEnums.Playing);

        AudioManager audioManager = AudioManager.getInstance();
        audioManager.testMode = true;
        audioManager.muteMode = true;


//        activateDirectors(this.levelType);
        try {
            activateMusic(this.levelType);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
//        PlayerStats.getInstance().setShopRerollDiscount(99);
//        PlayerInventory.getInstance().addMinerals(1000000);


//        for(int i = 0; i < 150; i++){
//            PlayerInventory.getInstance().addItem(ItemEnums.getRandomItemByRarity(ItemRarityEnums.Common));
//        }
//        directorManager.testingRichMode = true;


//        PlayerStats.getInstance().setBaseDamage(1);

//        Director testDirector = DirectorManager.getInstance().getTestDirector();
//        testDirector.spawnRegularFormation(SpawnFormationEnums.V, EnemyEnums.Scout);

//        EnemyEnums enemyType = EnemyEnums.Scout;
//        Enemy enemy = EnemyCreator.createEnemy(enemyType, 300, 300, Direction.LEFT, enemyType.getDefaultScale()
//                , enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
////        enemy.getMovementConfiguration().setBoardBlockToHoverIn(4);
//        enemy.getMovementConfiguration().setPathFinder(new RegularPathFinder());
////        enemy.getMovementConfiguration().setDestination(new Point(100, 300));
//        enemy.setAllowedVisualsToRotate(false);
//        enemy.getMovementConfiguration().setXMovementSpeed(0f);
//        enemy.getMovementConfiguration().setYMovementSpeed(0f);
////        enemy.getAnimation().changeImagetype(ImageEnums.Scout);
//        EnemyManager.getInstance().addEnemy(enemy);

        LaserbeamConfiguration config = new LaserbeamConfiguration( 0,0,
                PlayerManager.getInstance().getSpaceship(), Direction.LEFT_UP, PlayerManager.getInstance().getSpaceship());
        Laserbeam laserBeam = new Laserbeam(config);
        MissileManager.getInstance().addLaserBeam(laserBeam);

        config.setDirection(Direction.LEFT_DOWN);
        Laserbeam laserBeam4 = new Laserbeam(config);
        MissileManager.getInstance().addLaserBeam(laserBeam4);

        config.setDirection(Direction.RIGHT_UP);
        Laserbeam laserBeam2 = new Laserbeam(config);
        MissileManager.getInstance().addLaserBeam(laserBeam2);

        config.setDirection(Direction.RIGHT_DOWN);
        Laserbeam laserBeam3 = new Laserbeam(config);
        MissileManager.getInstance().addLaserBeam(laserBeam3);

        config.setDirection(Direction.LEFT);
        Laserbeam laserBeam5 = new Laserbeam(config);
        MissileManager.getInstance().addLaserBeam(laserBeam5);

        config.setDirection(Direction.UP);
        Laserbeam laserBeam6 = new Laserbeam(config);
        MissileManager.getInstance().addLaserBeam(laserBeam6);

        config.setDirection(Direction.RIGHT);
        Laserbeam laserBeam7 = new Laserbeam(config);
        MissileManager.getInstance().addLaserBeam(laserBeam7);

        config.setDirection(Direction.DOWN);
        Laserbeam laserBeam8 = new Laserbeam(config);
        MissileManager.getInstance().addLaserBeam(laserBeam8);

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
                audioManager.playRandomBackgroundMusic(currentLevelDifficulty, currentLevelLength, false);
                this.currentLevelSong = audioManager.getCurrentSong();
            }
            case Boss -> {
                audioManager.playBackgroundMusic(LevelSongs.getRandomBossSong(),true);
                this.currentLevelSong = audioManager.getCurrentSong();
                //to implement
            }
            case Special -> {
                //to implement
            }
            default -> {
                audioManager.playRandomBackgroundMusic(currentLevelDifficulty, currentLevelLength, false);
                this.currentLevelSong = audioManager.getCurrentSong();
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
        if (enemyType.equals(EnemyEnums.CashCarrier) || enemyType.equals(EnemyEnums.RedBoss)) {
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
