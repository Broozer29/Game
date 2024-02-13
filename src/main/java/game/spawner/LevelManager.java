package game.spawner;

import java.util.List;

import game.managers.TimerManager;
import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.objects.enemies.*;
import game.objects.enemies.enums.EnemyEnums;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import VisualAndAudioData.audio.AudioManager;
import game.objects.player.PlayerManager;
import game.objects.powerups.creation.PowerUpCreator;
import game.spawner.directors.DirectorManager;

public class LevelManager {

    private static LevelManager instance = new LevelManager();
    private AudioManager audioManager = AudioManager.getInstance();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private SpawningCoordinator spawningCoordinator = SpawningCoordinator.getInstance();
    private TimerManager timerManager = TimerManager.getInstance();
    private GameStateInfo gameState = GameStateInfo.getInstance();

    private int enemiesSpawned;
    private int enemiesKilled;

    private LevelManager () {
    }

    public static LevelManager getInstance () {
        return instance;
    }

    public void resetManager () {
    }

    public void updateGameTick () {
        // Check if the song has ended, then create the moving out portal
        if (gameState.getGameState() == GameStatusEnums.Playing && audioManager.getBackgroundMusic().getFramePosition() >= audioManager.getBackgroundMusic().getFrameLength()) {
            gameState.setGameState(GameStatusEnums.Level_Finished);
        }

        //NextLevelPortal spawns, now we wait for the player to enter the portal to set it to Level_Completed
        if (gameState.getGameState() == GameStatusEnums.Level_Completed) {
            gameState.setGameState(GameStatusEnums.Transitioning_To_Next_Level);
            //Now the GameBoard completes the transition and zoning in to the next level
        }
//        if (gameState.getMusicSeconds() >= gameState.getMaxMusicSeconds() && gameState.getGameState() == GameStatusEnums.Playing) {
//            gameState.setGameState(GameStatusEnums.Level_Finished);
//        }
    }

    // Called when a level starts, to saturate enemy list
    public void startLevel () {
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.playRandomBackgroundMusic();

        gameState.setGameState(GameStatusEnums.Playing);

//        PowerUpCreator.getInstance().initializePowerUpSpawnTimers();
//        DirectorManager.getInstance().createMonsterCards();
//        DirectorManager.getInstance().createDirectors();

        Enemy enemy = EnemyCreator.createEnemy(EnemyEnums.Seeker, 800, 600, Direction.LEFT, 1
                , 1, 1, MovementPatternSize.SMALL, false);
        enemy.getMovementConfiguration().setXMovementSpeed(0);
        enemy.getMovementConfiguration().setYMovementSpeed(0);
        EnemyManager.getInstance().addEnemy(enemy);

//        Enemy enemy2 = EnemyCreator.createEnemy(EnemyEnums.Seeker, 800, 600, Direction.RIGHT_DOWN, 1
//                , 1, 1, MovementPatternSize.SMALL, false);
//
//        EnemyManager.getInstance().addEnemy(enemy2);
//        Enemy enemy3 = EnemyCreator.createEnemy(EnemyEnums.Seeker, 800, 600, Direction.RIGHT_UP, 1
//                , 1, 1, MovementPatternSize.SMALL, false);
//
//        EnemyManager.getInstance().addEnemy(enemy3);
//
//        Enemy enemy4 = EnemyCreator.createEnemy(EnemyEnums.Seeker, 800, 600, Direction.LEFT, 1
//                , 1, 1, MovementPatternSize.SMALL, false);
//        EnemyManager.getInstance().addEnemy(enemy4);
//
//        Enemy enemy5 = EnemyCreator.createEnemy(EnemyEnums.Seeker, 800, 600, Direction.LEFT_UP, 1
//                , 1, 1, MovementPatternSize.SMALL, false);
//        EnemyManager.getInstance().addEnemy(enemy5);
//
//        Enemy enemy6 = EnemyCreator.createEnemy(EnemyEnums.Seeker, 800, 600, Direction.LEFT_DOWN, 1
//                , 1, 1, MovementPatternSize.SMALL, false);
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

    private Enemy increaseEnemyStrengthByDifficulty(Enemy enemy){
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
}
