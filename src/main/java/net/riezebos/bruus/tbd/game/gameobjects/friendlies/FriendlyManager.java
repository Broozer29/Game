package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.util.OrbitingObjectsFormatter;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLogger;
import net.riezebos.bruus.tbd.game.util.performancelogger.PerformanceLoggerManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class FriendlyManager {

    private static FriendlyManager instance = new FriendlyManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private List<FriendlyObject> friendlyObjects = new ArrayList<FriendlyObject>();
    private List<Drone> allPlayerDrones = new ArrayList<>();
    private Portal finishedLevelPortal;
    private GameStateInfo gameState = GameStateInfo.getInstance();
    private PerformanceLogger performanceLogger = null;

    private FriendlyManager () {
        initPortal();
        this.performanceLogger = new PerformanceLogger("Friendly Manager");
    }

    public void addDrone () {
        Drone drone = FriendlyCreator.createDrone();
        PlayerManager.getInstance().getSpaceship().getObjectOrbitingThis().add(drone);
        this.friendlyObjects.add(drone);
        this.allPlayerDrones.add(drone);

        OrbitingObjectsFormatter.reformatOrbitingObjects(PlayerManager.getInstance().getSpaceship(), 85);
    }

    public static FriendlyManager getInstance () {
        return instance;
    }


    public void updateGameTick () {
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Activate Friendly Objects", this::activateFriendlyObjects);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Check Friendly Object Collision", this::checkFriendlyObjectCollision);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Move Friendly Objects", this::moveFriendlyObjects);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Remove Invisible Objects", this::removeInvisibleObjects);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Spawn Finished Level Portal", this::spawnFinishedLevelPortal);

//        activateFriendlyObjects();
//        checkFriendlyObjectCollision();
//        moveFriendlyObjects();
//        removeInvisibleObjects();
//        spawnFinishedLevelPortal();
    }


    public void resetManager () {
        for (GameObject object : friendlyObjects) {
            object.setVisible(false);
        }

        removeInvisibleObjects();
        friendlyObjects = new ArrayList<>();
        allPlayerDrones = new ArrayList<>();
        initPortal();
        performanceLogger.reset();
    }


    private void spawnFinishedLevelPortal () {
        if (gameState.getGameState() == GameStatusEnums.Level_Finished && !finishedLevelPortal.isSpawned()) {
            if (finishedLevelPortal == null) {
                initPortal();
            }
            finishedLevelPortal.setSpawned(true);
            finishedLevelPortal.setVisible(true);
            finishedLevelPortal.setTransparancyAlpha(true, 0.0f, 0.01f);
            AnimationManager.getInstance().addUpperAnimation(finishedLevelPortal.getAnimation());
        }
    }

    private void moveFriendlyObjects () {
        for (FriendlyObject friendlyObject : friendlyObjects) {
            if (friendlyObject.getAnimation().isVisible()) {
                friendlyObject.move();
                friendlyObject.updateGameObjectEffects();
            }
        }
    }

    private void activateFriendlyObjects () {
        for (FriendlyObject object : friendlyObjects) {
            object.activateObject();
        }
    }

    private void removeInvisibleObjects () {
        for (int i = 0; i < friendlyObjects.size(); i++) {
            if (!friendlyObjects.get(i).isVisible()) {
                friendlyObjects.get(i).deleteObject();
                friendlyObjects.remove(i);
                i--;
            }
        }
    }

    // Checks collision between friendly objects and enemies
    private void checkFriendlyObjectCollision () {
        if (enemyManager == null) {
            enemyManager = EnemyManager.getInstance();
        }

        for (FriendlyObject drone : friendlyObjects) {
            if (drone.isVisible()) {
                for (Enemy enemy : enemyManager.getEnemies()) {
                    CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(drone, enemy);
                    if (collisionInfo != null) { //old way of handling collision, check other managers
                        // Collision logic here
                    }
                }
            }
        }

        // Checks collision between the finished level portal and player
        if (gameState.getGameState() == GameStatusEnums.Level_Finished && finishedLevelPortal.isVisible()) {
            CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(PlayerManager.getInstance().getSpaceship(), finishedLevelPortal);
            if (collisionInfo != null) {
                if (finishedLevelPortal.getTransparancyAlpha() >= 0.5f) {
                    gameState.setGameState(GameStatusEnums.Level_Completed);
                    finishedLevelPortal.setTransparancyAlpha(true, 1.0f, -0.02f);
                    finishedLevelPortal.setSpawned(false);
                }
            }
        }
    }


    public void addFriendlyObject (FriendlyObject friendlyObject) {
        if (!this.friendlyObjects.contains(friendlyObject)) {
            this.friendlyObjects.add(friendlyObject);
        }
    }

    public List<FriendlyObject> getFriendlyObjects () {
        return this.friendlyObjects;
    }


    public void resetPortal () {
        initPortal();
    }

    private void initPortal () {
        int portalXCoordinate = (int) Math.floor(DataClass.getInstance().getWindowWidth() * 0.55);
        int portalYCoordinate = (DataClass.getInstance().getWindowHeight() / 2);

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(portalXCoordinate);
        spriteConfiguration.setyCoordinate(portalYCoordinate);
        spriteConfiguration.setImageType(ImageEnums.Portal5);
        spriteConfiguration.setScale(1);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);

        finishedLevelPortal = new Portal(spriteAnimationConfiguration);
        finishedLevelPortal.setCenterCoordinates(portalXCoordinate, portalYCoordinate);
    }

    public List<Drone> getAllPlayerDrones () {
        return allPlayerDrones;
    }

    public PerformanceLogger getPerformanceLogger () {
        return performanceLogger;
    }
}