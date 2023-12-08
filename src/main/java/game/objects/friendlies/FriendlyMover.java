package game.objects.friendlies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.managers.AnimationManager;
import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyManager;
import game.utils.BoundsCalculator;
import gamedata.DataClass;
import gamedata.GameStateInfo;
import gamedata.GameStatusEnums;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class FriendlyMover {

    private static FriendlyMover instance = new FriendlyMover();
    private EnemyManager enemyManager = EnemyManager.getInstance();
    private List<FriendlyObject> activeFriendlyObjects = new ArrayList<FriendlyObject>();
    private Portal finishedLevelPortal;
    private GameStateInfo gameState = GameStateInfo.getInstance();

    private FriendlyMover () {
        initPortal();
    }

    public static FriendlyMover getInstance () {
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
        for (int i = 0; i < activeFriendlyObjects.size(); i++) {
            if (activeFriendlyObjects.get(i).getAnimation().isVisible()) {
                activeFriendlyObjects.get(i).move();
                activeFriendlyObjects.get(i).updateCurrentBoardBlock();
            }
        }
    }

    private void cycleActiveFriendlyObjects () {
        for (int i = 0; i < activeFriendlyObjects.size(); i++) {
            if (!activeFriendlyObjects.get(i).getAnimation().isVisible()) {
                activeFriendlyObjects.remove(i);
                activeFriendlyObjects.get(i).setVisible(false);
            }
        }

        // Yuck, this below here is gross and should be changed before adding upon but too busy
        // with something
        for (FriendlyObject object : activeFriendlyObjects) {
            if (object instanceof GuardianDrone) {
                ((GuardianDrone) object).activateGuardianDrone();
            }
        }
    }

    // Checks collision between friendly objects and enemies
    // Intentionally broken, fix manually
    private void checkFriendlyObjectCollision () {
        if (enemyManager == null) {
            enemyManager = EnemyManager.getInstance();
        }

        for (FriendlyObject friendlyObject : activeFriendlyObjects) {
            if (friendlyObject.isVisible()) {
                for (Enemy enemy : enemyManager.getEnemies()) {
                    if (isNearby(enemy, friendlyObject)) {
                        if (BoundsCalculator.getGameObjectBounds(enemy).intersects(BoundsCalculator.getGameObjectBounds(friendlyObject))) {

                        }
                    }
                }
            }
        }

        // Checks collision between the finished level portal and player
        if (gameState.getGameState() == GameStatusEnums.Song_Finished) {
            if (finishedLevelPortal.isVisible()) {
                if (isNearby(PlayerManager.getInstance().getSpaceship(), finishedLevelPortal)) {
                    if (BoundsCalculator.getGameObjectBounds(PlayerManager.getInstance().getSpaceship())
                            .intersects(BoundsCalculator.getGameObjectBounds(finishedLevelPortal))
                    ) {
                        gameState.setGameState(GameStatusEnums.Level_Completed);
                        finishedLevelPortal.setTransparancyAlpha(true, 1.0f, -0.02f);
                        finishedLevelPortal.setSpawned(false);
                    }
                }
            }
        }
    }

    private boolean isWithinBoardBlockThreshold (Sprite sprite1, Sprite sprite2) {
        int blockDifference = Math.abs(sprite1.getCurrentBoardBlock() - sprite2.getCurrentBoardBlock());
        return blockDifference <= 3;
    }

    private boolean isNearby (Sprite sprite1, Sprite sprite2) {
        if (!isWithinBoardBlockThreshold(sprite1, sprite2)) {
            return false;
        }

        double distance = Math.hypot(sprite1.getXCoordinate() - sprite2.getXCoordinate(),
                sprite1.getYCoordinate() - sprite2.getYCoordinate());
        return distance < 300;
    }


    public void createMissileGuardianBot (FriendlyEnums droneType, float scale) {
        createAndAddDrone(scale, droneType);
        reOrganizeOrbitingDroneFormation();
    }

    private void createAndAddDrone (float scale, FriendlyEnums droneType) {
        double meanX = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
        double meanY = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();
        double nextAngle = 0;
        if (!activeFriendlyObjects.isEmpty()) {
            List<Double> angles = new ArrayList<>();
            for (FriendlyObject drone : activeFriendlyObjects) {
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
        FriendlyObject friendlyObject = new GuardianDrone(x, y, null, Direction.RIGHT, droneType, scale, pathFinder,
                50);
        activeFriendlyObjects.add((GuardianDrone) friendlyObject);
        SpriteAnimation animation = new SpriteAnimation(x, y, ImageEnums.Guardian_Bot, true, scale);
        friendlyObject.setAnimation(animation);
        addActiveFriendlyObject(friendlyObject);
    }

    //Add the logic of this method to a seperate class, which takes a rotating target and the list of objects as a parameter
    private void reOrganizeOrbitingDroneFormation () {
        // The center around which the GuardianDrones will orbit
        double meanX = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
        double meanY = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();

        // Counting only the GuardianDrones
        int numberOfDrones = (int) activeFriendlyObjects.stream().filter(obj -> obj instanceof GuardianDrone).count();
        double angleIncrement = 2 * Math.PI / numberOfDrones;

        int totalFrames = 300; // You may replace this with an appropriate value
        int radius = 75; // Example radius

        int iterator = 0;
        for (FriendlyObject friendlyObject : activeFriendlyObjects) {
            if (friendlyObject instanceof GuardianDrone) {
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
    }

    public void addActiveFriendlyObject (FriendlyObject friendlyObject) {
        this.activeFriendlyObjects.add(friendlyObject);
    }

    public List<FriendlyObject> getActiveFriendlyObjects () {
        return this.activeFriendlyObjects;
    }

    public void resetManager () {
        activeFriendlyObjects = new ArrayList<FriendlyObject>();
        initPortal();
    }

    public void resetManagerForNextLevel () {
        activeFriendlyObjects = new ArrayList<FriendlyObject>();
    }

    private void initPortal () {
        int portalXCoordinate = (int) Math.floor(DataClass.getInstance().getWindowWidth() * 0.55);
        int portalYCoordinate = (DataClass.getInstance().getWindowHeight() / 2);

        finishedLevelPortal = new Portal(portalXCoordinate, portalYCoordinate, ImageEnums.Portal5, true, 1);
        finishedLevelPortal.setCenterCoordinates(portalXCoordinate, portalYCoordinate);
    }
}