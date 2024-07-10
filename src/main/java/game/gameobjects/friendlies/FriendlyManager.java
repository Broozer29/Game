package game.gameobjects.friendlies;

import java.util.ArrayList;
import java.util.List;

import game.managers.AnimationManager;
import game.util.CollisionDetector;
import game.gameobjects.player.PlayerManager;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyManager;
import VisualAndAudioData.DataClass;
import game.gamestate.GameStateInfo;
import game.gamestate.GameStatusEnums;
import VisualAndAudioData.image.ImageEnums;
import game.util.OrbitingObjectsFormatter;
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

    public void addDrone (){
        FriendlyObject Drone = FriendlyCreator.createDrone();
        PlayerManager.getInstance().getSpaceship().getObjectOrbitingThis().add(Drone);
        this.friendlyObjects.add(Drone);

        OrbitingObjectsFormatter.reformatOrbitingObjects(PlayerManager.getInstance().getSpaceship(), 85);
    }

    public static FriendlyManager getInstance () {
        return instance;
    }


    public void updateGameTick () {
        activateFriendlyObjects();
        checkFriendlyObjectCollision();
        moveFriendlyObjects();
        removeInvisibleObjects();
        spawnFinishedLevelPortal();
    }


    public void resetManager () {
        for (FriendlyObject friendlyObject : friendlyObjects) {
            if (!friendlyObject.isPermanentFriendlyObject()) {
                friendlyObject.setVisible(false);
            }
        }

        removeInvisibleObjects();
        friendlyObjects = new ArrayList<FriendlyObject>();

        initPortal();
    }

    //Unused but can be used to remove permanent objects that resetManager does not clear.
    public void clearPermanentAndOtherObjects(){
        for (FriendlyObject friendlyObject : friendlyObjects) {
                friendlyObject.setVisible(false);
        }

        removeInvisibleObjects();
        friendlyObjects = new ArrayList<FriendlyObject>();
    }

    private void spawnFinishedLevelPortal () {
        if (gameState.getGameState() == GameStatusEnums.Level_Finished && !finishedLevelPortal.getSpawned()) {
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
        for (FriendlyObject drone : friendlyObjects) {
            if (drone.getAnimation().isVisible()) {
                drone.move();
                drone.updateGameObjectEffects();
            }
        }
    }

    private void activateFriendlyObjects () {
        for (FriendlyObject object : friendlyObjects) {
            object.activateObjectAction();
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
                    if (CollisionDetector.getInstance().detectCollision(drone, enemy)) { //old way of handling collision, check other managers
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
}