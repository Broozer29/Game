package game.objects.friendlies;

import java.util.ArrayList;
import java.util.List;

import game.managers.AnimationManager;
import game.managers.CollisionDetector;
import game.objects.player.PlayerManager;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import VisualAndAudioData.DataClass;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class FriendlyManager {

    private static FriendlyManager instance = new FriendlyManager();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private List<FriendlyObject> friendlyObjects = new ArrayList<FriendlyObject>();
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
        if (gameState.getGameState() == GameStatusEnums.Level_Finished && finishedLevelPortal.getSpawned() == false) {
            finishedLevelPortal.setSpawned(true);
            finishedLevelPortal.setVisible(true);
            finishedLevelPortal.setTransparancyAlpha(true, 0.0f, 0.01f);
            AnimationManager.getInstance().addUpperAnimation(finishedLevelPortal.getAnimation());
        }
    }

    private void moveFriendlyObjects () {
        for (FriendlyObject drone : friendlyObjects) {
            if (drone.getAnimation().isVisible()) {
                drone.move();
            }
        }
    }

    private void cycleActiveFriendlyObjects () {
        friendlyObjects.removeIf(friendlyObject -> !friendlyObject.getAnimation().isVisible());

        for (FriendlyObject object : friendlyObjects) {
            object.activateObjectAction();
        }
    }

    // Checks collision between friendly objects and enemies
    // Intentionally broken, fix manually
    private void checkFriendlyObjectCollision () {
        if (enemyManager == null) {
            enemyManager = EnemyManager.getInstance();
        }

        for (FriendlyObject drone : friendlyObjects) {
            if (drone.isVisible()) {
                for (Enemy enemy : enemyManager.getEnemies()) {
                    if (CollisionDetector.getInstance().detectCollision(drone, enemy)) {
                        // Collision logic here
                    }
                }
            }
        }

        // Checks collision between the finished level portal and player
        if (gameState.getGameState() == GameStatusEnums.Level_Finished && finishedLevelPortal.isVisible()) {
            if (CollisionDetector.getInstance().detectCollision(PlayerManager.getInstance().getSpaceship(), finishedLevelPortal)) {
                gameState.setGameState(GameStatusEnums.Level_Completed);
                finishedLevelPortal.setTransparancyAlpha(true, 1.0f, -0.02f);
                finishedLevelPortal.setSpawned(false);
            }
        }
    }

//    public void createMissileGuardianBot (FriendlyObjectEnums droneType, float scale) {
//        createAndAddDrone(scale, droneType);
//        reOrganizeOrbitingDroneFormation();
//    }

//    private void createAndAddDrone (float scale, DroneEnums droneType) {
//        double meanX = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
//        double meanY = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();
//        double nextAngle = 0;
//        if (!guardianDroneList.isEmpty()) {
//            List<Double> angles = new ArrayList<>();
//            for (GuardianDrone drone : guardianDroneList) {
//                double angle = Math.atan2(drone.getYCoordinate() - meanY, drone.getXCoordinate() - meanX);
//                angles.add(angle);
//            }
//            Collections.sort(angles);
//            double maxGap = 0;
//            for (int i = 0; i < angles.size(); i++) {
//                double gap = angles.get((i + 1) % angles.size()) - angles.get(i);
//                if (gap < 0) {
//                    gap += Math.PI * 2;
//                }
//                if (gap > maxGap) {
//                    maxGap = gap;
//                    nextAngle = angles.get(i) + gap / 2;
//                }
//            }
//        }
//
//        int radius = 75; // Example radius
//        int x = (int) (meanX + Math.cos(nextAngle) * radius);
//        int y = (int) (meanY + Math.sin(nextAngle) * radius);
//
//        PathFinder pathFinder = new OrbitPathFinder(PlayerManager.getInstance().getSpaceship(), radius, 300, nextAngle);
//
//
//        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
//        spriteConfiguration.setxCoordinate(x);
//        spriteConfiguration.setyCoordinate(y);
//        spriteConfiguration.setImageType(ImageEnums.Guardian_Bot);
//        spriteConfiguration.setScale(scale);
//
//        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
//        DroneConfiguration droneConfiguration = new DroneConfiguration(
//                DroneEnums.Missile_Guardian_Bot, 200, pathFinder, Direction.RIGHT, 3, 3
//        );
//
//        GuardianDrone friendlyObject = new GuardianDrone(spriteAnimationConfiguration, droneConfiguration);
//        addActiveFriendlyObject(friendlyObject);
//    }

    public void addFriendlyObject(FriendlyObject friendlyObject){
        if(!this.friendlyObjects.contains(friendlyObject)){
            this.friendlyObjects.add(friendlyObject);
        }
    }

    public List<FriendlyObject> getFriendlyObjects () {
        return this.friendlyObjects;
    }

    public void resetManager () {
        friendlyObjects = new ArrayList<FriendlyObject>();
        initPortal();
    }

    public void resetManagerForNextLevel () {
        friendlyObjects = new ArrayList<FriendlyObject>();
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