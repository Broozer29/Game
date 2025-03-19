package net.riezebos.bruus.tbd.game.gameobjects.enemies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyCategory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.level.LevelManager;
import net.riezebos.bruus.tbd.game.level.enums.LevelTypes;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class EnemyManager {

    private static EnemyManager instance = new EnemyManager();
    private AudioManager audioManager = AudioManager.getInstance();
    private PlayerManager friendlyManager = PlayerManager.getInstance();
    private AnimationManager animationManager = AnimationManager.getInstance();
    private CopyOnWriteArrayList<Enemy> enemyList = new CopyOnWriteArrayList<Enemy>();
    private boolean hasSpawnedABoss = false;
    private PerformanceLogger performanceLogger = null;


    private EnemyManager () {
        this.performanceLogger = new PerformanceLogger("Enemy Manager");
    }

    public static EnemyManager getInstance () {
        return instance;
    }

    public void resetManager () {
        for (Enemy enemy : enemyList) {
            enemy.setVisible(false);
        }
        updateEnemies();
        enemyList = new CopyOnWriteArrayList<Enemy>();
        friendlyManager = PlayerManager.getInstance();
        audioManager = AudioManager.getInstance();
        hasSpawnedABoss = false;
        performanceLogger.reset();
    }

    public void updateGameTick () {
//        PerformanceLoggerManager.timeAndLog(performanceLogger, "Total", () -> {
                    PerformanceLoggerManager.timeAndLog(performanceLogger, "Update Enemies", this::updateEnemies);
                    PerformanceLoggerManager.timeAndLog(performanceLogger, "Check Spaceship Collision", this::checkSpaceshipCollisions);
                    PerformanceLoggerManager.timeAndLog(performanceLogger, "Trigger Enemy Actions", this::triggerEnemyActions);
//                });
//        updateEnemies();
//        checkSpaceshipCollisions();
//        triggerEnemyActions();
    }


    public boolean isBossAlive () {
        if (!hasSpawnedABoss) {
            return true;
        } else if (hasSpawnedABoss) {
            return enemyList.stream().anyMatch(enemy -> enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Boss));
        }

        return true;
    }

    private void checkSpaceshipCollisions () {
        SpaceShip spaceship = friendlyManager.getSpaceship();
        for (Enemy enemy : enemyList) {
            if (enemy.isVisible()) {
                CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(enemy, spaceship);
                if (collisionInfo != null) {
                    if (enemy.isDetonateOnCollision()) {
                        detonateEnemy(enemy);
                        enemy.dealDamageToGameObject(spaceship);
                    } else {
                        spaceship.takeDamage(2.5f * (1 - PlayerStats.getInstance().getCollisionDamageReduction()));
                        spaceship.resetToPreviousPosition();
                        handleAdditionalKnockbackBehaviour(enemy);
                    }
                    spaceship.applyKnockback(collisionInfo, enemy.getKnockbackStrength());
                }
            }
        }
    }

    private void handleAdditionalKnockbackBehaviour (Enemy enemy) {
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.Thornweaver) != null) {
            ThornsDamageDealer.getInstance().addDelayedThornsDamageToObject(enemy, 1);
        }
    }

    public void detonateEnemy (Enemy enemy) {
        if (enemy.getDestructionAnimation() != null) {
            enemy.getDestructionAnimation().setOriginCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
            if (enemy.getEnemyType().equals(EnemyEnums.ZergScourge)) {
                enemy.getDestructionAnimation().changeImagetype(ImageEnums.ScourgeExplosion);
            }
            animationManager.addLowerAnimation(enemy.getDestructionAnimation());
        }

        AudioEnums impactSound = enemy.getDeathSound();
        switch (enemy.getEnemyType()) {
            case Needler, Alien_Bomb -> impactSound = AudioEnums.Alien_Bomb_Impact;
            case ZergScourge -> impactSound = AudioEnums.ScourgeCollision;
        }
        audioManager.addAudio(impactSound);
        enemy.setVisible(false);
    }

    private void triggerEnemyActions () {
        for (Enemy enemy : enemyList) {
            enemy.fireAction();
        }
    }

    private void updateEnemies () {
        for (Enemy en : enemyList) {
            if (en.isVisible()) {
                PerformanceLoggerManager.timeAndLog(performanceLogger, "Move Enemy", en::move);
                PerformanceLoggerManager.timeAndLog(performanceLogger, "Update Enemy Effects", en::updateGameObjectEffects);
//                en.move();
//                en.updateGameObjectEffects();
            } else {
                en.deleteObject();
                enemyList.remove(en); // Remove directly from CopyOnWriteArrayList
            }
        }
    }

    public void addEnemy (Enemy enemy) {
        if (enemy != null && !enemyList.contains(enemy)) {
            enemy.onCreationEffects();
            enemyList.add(enemy);

            increaseEnemySpawnedCount(enemy);
        }
    }

    private void increaseEnemySpawnedCount (Enemy enemy) {
        if (LevelManager.getInstance().getLevelType().equals(LevelTypes.Boss) &&
                enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Boss)) {
            GameStatsTracker.getInstance().addEnemySpawned(1);
        } else if (enemy.getEnemyType().getEnemyCategory() != EnemyCategory.Summon) {
            GameStatsTracker.getInstance().addEnemySpawned(1);
        }
    }

    public List<Enemy> getEnemies () {
        return this.enemyList;
    }

    public int getEnemyCount () {
        return enemyList.size();
    }

    public boolean enemiesToHomeTo () {
        if (enemyList.isEmpty()) {
            return true;
        } else
            return false;
    }

    public Enemy getClosestEnemy(int xCoordinate, int yCoordinate) {
        int boardBlockThreshold = 3;

        // Filter out enemies that are too far away based on board block
        List<Enemy> nearbyEnemies = enemyList.stream()
                .filter(enemy -> this.isWithinBoardBlockThreshold(enemy, xCoordinate, boardBlockThreshold))
                .collect(Collectors.toList());

        // Initialize variables to track the closest enemy
        Enemy closestEnemy = null;
        double minDistanceSquared = 320000; //400 X AND Y coordinates away

        // Iterate over the filtered list of enemies to find the closest one
        for (Enemy enemy : nearbyEnemies) {
            int enemyXCoordinate = enemy.getCenterXCoordinate();
            int enemyYCoordinate = enemy.getCenterYCoordinate();

            // Compute the squared distance between the player and enemy
            double deltaX = xCoordinate - enemyXCoordinate;
            double deltaY = yCoordinate - enemyYCoordinate;
            double distanceSquared = deltaX * deltaX + deltaY * deltaY;  // No need for sqrt()

            // If this enemy is closer, update closestEnemy and minDistanceSquared
            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                closestEnemy = enemy;
            }
        }
        return closestEnemy;
    }


    public Enemy getClosestEnemyWithinDistance(int xCoordinate, int yCoordinate, double attackRange) {
        double minDistance = attackRange; // Directly use attackRange without √2 adjustment
        Enemy closestEnemy = null;

        for (Enemy enemy : EnemyManager.getInstance().getEnemies()) {
            Rectangle enemyBounds = enemy.getBounds(); // Get enemy's bounding box
            double distance = ProtossUtils.getDistanceToRectangle(xCoordinate, yCoordinate, enemyBounds);

            // If the enemy is within attackRange and closer than the previous closest enemy, update
            if (distance < minDistance) {
                minDistance = distance;
                closestEnemy = enemy;
            }
        }
        return closestEnemy;
    }



    //Helper method
    private boolean isWithinBoardBlockThreshold(Enemy enemy, int xCoordinate, int boardBlockThreshold) {
        enemy.updateBoardBlock();  // Ensure the enemy's board block is updated
        int enemyBoardBlock = enemy.getCurrentBoardBlock();

        // Get the board block of the provided xCoordinate
        int playerBoardBlock = BoardBlockUpdater.getBoardBlock(xCoordinate);

        // Check if the enemy's board block is within the threshold of the player
        int blockDifference = Math.abs(playerBoardBlock - enemyBoardBlock);
        return blockDifference <= boardBlockThreshold;
    }


    public GameObject findEnemyForMissileToBounceTo (GameObject gameObject, List<GameObject> objectsToIgnore) {
        GameObject closestEnemy = null;
        double minDistance = Double.MAX_VALUE;

        for (GameObject enemy : enemyList) {
            if (!enemy.equals(gameObject)) {
                if (objectsToIgnore != null && !objectsToIgnore.contains(enemy)) {
                    int enemyXCoordinate = enemy.getCenterXCoordinate();
                    int enemyYcoordinate = enemy.getCenterYCoordinate();

                    // Compute the distance between player and enemy using Euclidean distance formula
                    double distance = Math.sqrt(Math.pow((gameObject.getCenterXCoordinate() - enemyXCoordinate), 2)
                            + Math.pow((gameObject.getCenterYCoordinate() - enemyYcoordinate), 2));

                    // If this enemy is closer than the previous closest enemy, update closestEnemy and
                    // minDistance
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestEnemy = enemy;
                    }
                }
            }
        }

        return closestEnemy;
    }

    public boolean isHasSpawnedABoss () {
        return hasSpawnedABoss;
    }

    public void setHasSpawnedABoss (boolean hasSpawnedABoss) {
        this.hasSpawnedABoss = hasSpawnedABoss;
    }

    public void removeOutOfBoundsEnemies () {
        for (Enemy enemy : enemyList) {
            if (enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Small) ||
                    enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Medium) ||
                    enemy.getEnemyType().getEnemyCategory().equals(EnemyCategory.Special)) {
                if (!WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
                    enemy.deleteObject();
                }
            }
        }
    }

    public void detonateAllEnemies () {
        for (Enemy enemy : enemyList) {
            enemy.deleteObject();
        }
    }

    public int getAmountOfEnemyTypesAlive(EnemyEnums enemyToCheck){
        return enemyList.stream().filter(enemy -> enemy.getEnemyType().equals(enemyToCheck)).collect(Collectors.toList()).size();
    }

    public PerformanceLogger getPerformanceLogger () {
        return this.performanceLogger;
    }
}