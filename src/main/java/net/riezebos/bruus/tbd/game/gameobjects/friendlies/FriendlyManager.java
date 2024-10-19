package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.gamestate.GameStatusEnums;
import net.riezebos.bruus.tbd.game.managers.AnimationManager;
import net.riezebos.bruus.tbd.game.util.OrbitingObjectsFormatter;
import net.riezebos.bruus.tbd.game.util.collision.CollisionDetector;
import net.riezebos.bruus.tbd.game.util.collision.CollisionInfo;
import net.riezebos.bruus.tbd.visuals.audiodata.DataClass;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

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
                    CollisionInfo collisionInfo = CollisionDetector.getInstance().detectCollision(drone, enemy);
                    if (collisionInfo != null) { //old way of handling collision, check other managers
                        // Collision logic here
                    }
                }
            }
        }

        // Checks collision between the finished level portal and player
        if (gameState.getGameState() == GameStatusEnums.Level_Finished && finishedLevelPortal.isVisible()) {
            CollisionInfo collisionInfo =CollisionDetector.getInstance().detectCollision(PlayerManager.getInstance().getSpaceship(), finishedLevelPortal);
            if (collisionInfo != null) {
                if(finishedLevelPortal.getTransparancyAlpha() >= 0.5f) {
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
}