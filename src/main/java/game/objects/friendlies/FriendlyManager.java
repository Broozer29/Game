package game.objects.friendlies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.managers.AnimationManager;
import game.managers.CollisionDetector;
import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.objects.friendlies.Drones.DroneConfiguration;
import game.objects.friendlies.Drones.DroneEnums;
import game.objects.friendlies.Drones.GuardianDrone;
import gamedata.DataClass;
import gamedata.GameStateInfo;
import gamedata.GameStatusEnums;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class FriendlyManager {

    private static FriendlyManager instance = new FriendlyManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private List<GuardianDrone> guardianDroneList = new ArrayList<GuardianDrone>();
    private Portal finishedLevelPortal;
    private GameStateInfo gameState = GameStateInfo.getInstance();

    private FriendlyManager () {
        initPortal();
    }

    public static FriendlyManager getInstance () {
        return instance;
    }

    public void updateGameTick () {
        cycleActiveFriendlyObjects();
        checkFriendlyObjectCollision();
        moveFriendlyObjects();
        spawnFinishedLevelPortal();
    }

    private void spawnFinishedLevelPortal () {
        if (gameState.getGameState() == GameStatusEnums.Song_Finished && finishedLevelPortal.getSpawned() == false) {
            finishedLevelPortal.setSpawned(true);
            finishedLevelPortal.setVisible(true);
            finishedLevelPortal.setTransparancyAlpha(true, 0.0f, 0.01f);
            AnimationManager.getInstance().addUpperAnimation(finishedLevelPortal.getAnimation());
        }
    }

    private void moveFriendlyObjects () {
        for (GuardianDrone drone : guardianDroneList) {
            if (drone.getAnimation().isVisible()) {
                drone.move();
                drone.updateCurrentBoardBlock();
            }
        }
    }

    private void cycleActiveFriendlyObjects () {
        guardianDroneList.removeIf(friendlyObject -> !friendlyObject.getAnimation().isVisible());

        for (GuardianDrone drone : guardianDroneList) {
            drone.activateGuardianDrone();
        }
    }

    // Checks collision between friendly objects and enemies
    // Intentionally broken, fix manually
    private void checkFriendlyObjectCollision () {
        if (enemyManager == null) {
            enemyManager = EnemyManager.getInstance();
        }

        for (GuardianDrone drone : guardianDroneList) {
            if (drone.isVisible()) {
                for (Enemy enemy : enemyManager.getEnemies()) {
                    if (CollisionDetector.getInstance().detectCollision(drone, enemy)) {
                        // Collision logic here
                    }
                }
            }
        }

        // Checks collision between the finished level portal and player
        if (gameState.getGameState() == GameStatusEnums.Song_Finished && finishedLevelPortal.isVisible()) {
            if (CollisionDetector.getInstance().detectCollision(PlayerManager.getInstance().getSpaceship(), finishedLevelPortal)) {
                gameState.setGameState(GameStatusEnums.Level_Completed);
                finishedLevelPortal.setTransparancyAlpha(true, 1.0f, -0.02f);
                finishedLevelPortal.setSpawned(false);
            }
        }
    }

    public void createMissileGuardianBot (DroneEnums droneType, float scale) {
        createAndAddDrone(scale, droneType);
        reOrganizeOrbitingDroneFormation();
    }

    private void createAndAddDrone (float scale, DroneEnums droneType) {
        double meanX = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
        double meanY = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();
        double nextAngle = 0;
        if (!guardianDroneList.isEmpty()) {
            List<Double> angles = new ArrayList<>();
            for (GuardianDrone drone : guardianDroneList) {
                double angle = Math.atan2(drone.getYCoordinate() - meanY, drone.getXCoordinate() - meanX);
                angles.add(angle);
            }
            Collections.sort(angles);
            double maxGap = 0;
            for (int i = 0; i < angles.size(); i++) {
                double gap = angles.get((i + 1) % angles.size()) - angles.get(i);
                if (gap < 0) {
                    gap += Math.PI * 2;
                }
                if (gap > maxGap) {
                    maxGap = gap;
                    nextAngle = angles.get(i) + gap / 2;
                }
            }
        }

        int radius = 75; // Example radius
        int x = (int) (meanX + Math.cos(nextAngle) * radius);
        int y = (int) (meanY + Math.sin(nextAngle) * radius);

        PathFinder pathFinder = new OrbitPathFinder(PlayerManager.getInstance().getSpaceship(), radius, 300, nextAngle);


        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(x);
        spriteConfiguration.setyCoordinate(y);
        spriteConfiguration.setImageType(ImageEnums.Guardian_Bot);
        spriteConfiguration.setScale(scale);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        DroneConfiguration droneConfiguration = new DroneConfiguration(
                DroneEnums.Missile_Guardian_Bot, 200, pathFinder, Direction.RIGHT, 3, 3
        );

        GuardianDrone friendlyObject = new GuardianDrone(spriteAnimationConfiguration, droneConfiguration);
        addActiveFriendlyObject(friendlyObject);
    }

    //Add the logic of this method to a seperate class, which takes a rotating target and the list of objects as a parameter
    private void reOrganizeOrbitingDroneFormation () {
        // The center around which the GuardianDrones will orbit
        double meanX = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
        double meanY = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();

        // Counting only the GuardianDrones
        int numberOfDrones = (int) guardianDroneList.stream().filter(obj -> obj instanceof GuardianDrone).count();
        double angleIncrement = 2 * Math.PI / numberOfDrones;

        int totalFrames = 300; // You may replace this with an appropriate value
        int radius = 75; // Example radius

        int iterator = 0;
        for (GuardianDrone friendlyObject : guardianDroneList) {
            double nextAngle = angleIncrement * iterator;

            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            GuardianDrone drone = (GuardianDrone) friendlyObject;
            drone.setX(x);
            drone.setY(y);

            // Create a new OrbitPathFinder with the correct offset angle
            OrbitPathFinder newOrbit = new OrbitPathFinder(PlayerManager.getInstance().getSpaceship(), radius,
                    totalFrames, nextAngle);
            // Update the GuardianDrone's path finder
            drone.setPathFinder(newOrbit);

            iterator++;
        }
    }

    public void addActiveFriendlyObject (GuardianDrone friendlyObject) {
        this.guardianDroneList.add(friendlyObject);
    }

    public List<GuardianDrone> getGuardianDroneList () {
        return this.guardianDroneList;
    }

    public void resetManager () {
        guardianDroneList = new ArrayList<GuardianDrone>();
        initPortal();
    }

    public void resetManagerForNextLevel () {
        guardianDroneList = new ArrayList<GuardianDrone>();
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
}