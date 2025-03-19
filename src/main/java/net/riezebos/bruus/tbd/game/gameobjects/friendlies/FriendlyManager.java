package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossShuttle;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
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
import java.util.Iterator;
import java.util.List;

public class FriendlyManager {

    private static FriendlyManager instance = new FriendlyManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private List<Drone> drones = new ArrayList<Drone>();
    private Portal finishedLevelPortal;
    private GameState gameState = GameState.getInstance();
    private PerformanceLogger performanceLogger = null;

    private FriendlyManager() {
        initPortal();
        this.performanceLogger = new PerformanceLogger("Friendly Manager");
    }

    public void addDrone() {
        Drone drone = FriendlyCreator.createDrone();
        PlayerManager.getInstance().getSpaceship().getObjectOrbitingThis().add(drone);
        this.drones.add(drone);

        OrbitingObjectsFormatter.reformatOrbitingObjects(PlayerManager.getInstance().getSpaceship(), 85);
    }

    public void addProtossShip(DroneTypes droneTypes) {
        Drone protosShip = FriendlyCreator.createProtossShip(droneTypes);
        if (protosShip != null) {
            this.drones.add(protosShip);
        }
    }

    public static FriendlyManager getInstance() {
        return instance;
    }


    public void updateGameTick() {
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Activate Friendly Objects", this::activateFriendlyObjects);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Check Friendly Object Collision", this::checkFriendlyObjectCollision);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Move Friendly Objects", this::updateFriendlyObjects);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Remove Invisible Objects", this::removeInvisibleObjects);
        PerformanceLoggerManager.timeAndLog(performanceLogger, "Spawn Finished Level Portal", this::spawnFinishedLevelPortal);

//        activateFriendlyObjects();
//        checkFriendlyObjectCollision();
//        moveFriendlyObjects();
//        removeInvisibleObjects();
//        spawnFinishedLevelPortal();
    }


    public void resetManager() {
        for (GameObject object : drones) {
            object.setVisible(false);
        }

        removeInvisibleObjects();
        drones = new ArrayList<>();
        initPortal();
        performanceLogger.reset();
    }


    private void spawnFinishedLevelPortal() {
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

    private void updateFriendlyObjects() {
        Iterator<Drone> iterator = drones.iterator();
        while (iterator.hasNext()) {
            Drone friendlyObject =  iterator.next();

            if(friendlyObject.getCurrentHitpoints() <= 0){
                friendlyObject.setVisible(false);
            }

            if (friendlyObject.isVisible()) {
                friendlyObject.move();
                friendlyObject.updateGameObjectEffects();
                friendlyObject.setShowHealthBar(friendlyObject.getCurrentHitpoints() < friendlyObject.getMaxHitPoints());
            } else {
                iterator.remove();
            }
        }

    }


    private void activateFriendlyObjects() {
        for (Drone drone : drones) {
            drone.activateObject();
        }
    }

    private void removeInvisibleObjects() {
        for (int i = 0; i < drones.size(); i++) {
            if (!drones.get(i).isVisible()) {
                drones.get(i).deleteObject();
                drones.remove(i);
                i--;
            }
        }
    }

    // Checks collision between friendly objects and enemies
    private void checkFriendlyObjectCollision() {
        if (enemyManager == null) {
            enemyManager = EnemyManager.getInstance();
        }

        for (Drone drones : drones) {
            if (drones.isVisible()) {
                for (Enemy enemy : enemyManager.getEnemies()) {
                    CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(drones, enemy);
                    if (collisionInfo != null) {
                        if(drones instanceof ProtossShuttle shuttle){
                            shuttle.detonate();
                            if(enemy.isDetonateOnCollision()){
                                EnemyManager.getInstance().detonateEnemy(enemy);
                            }
                        } else if(enemy.isDetonateOnCollision()) {
                            EnemyManager.getInstance().detonateEnemy(enemy);
                            enemy.dealDamageToGameObject(drones);
                        }
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



    public List<Drone> getDrones() {
        return this.drones;
    }


    public void resetPortal() {
        initPortal();
    }

    private void initPortal() {
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

    public List<Drone> getAllPlayerDrones() {
        return drones.stream().filter(drone -> !drone.isProtoss()).toList();
    }

    public List<Drone> getAllProtossDrones() {
        return drones.stream().filter(Drone::isProtoss).toList();
    }

    public List<Drone> getDronesByDroneType(DroneTypes droneType) {
        return drones.stream().filter(drone -> drone.getDroneType().equals(droneType)).toList();
    }

    public PerformanceLogger getPerformanceLogger() {
        return performanceLogger;
    }
}