package game.gameobjects.friendlies;

import VisualAndAudioData.image.ImageEnums;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.gameobjects.friendlies.Drones.Drone;
import game.gameobjects.player.PlayerManager;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class FriendlyCreator {

    public static FriendlyObject createDrone () {
        float scale = (float) 0.5;
        FriendlyObjectEnums friendlyType = FriendlyObjectEnums.Missile_Drone;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(PlayerManager.getInstance().getSpaceship().getXCoordinate());
        spriteConfiguration.setyCoordinate(PlayerManager.getInstance().getSpaceship().getYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.Drone);
        spriteConfiguration.setScale(scale);

        PathFinder pathFinder = new OrbitPathFinder(PlayerManager.getInstance().getSpaceship(), 85, 300, 0);
        FriendlyObjectConfiguration friendlyObjectConfiguration = new FriendlyObjectConfiguration(friendlyType,
                1.5f, pathFinder,
                Direction.RIGHT, 1, 1, MovementPatternSize.SMALL, friendlyType.isBoxCollision(), friendlyType.isPermanentObject());
        FriendlyObject object = FriendlyCreator.createDrone(spriteConfiguration, friendlyObjectConfiguration);
        object.getMovementConfiguration().setLastKnownTargetX(PlayerManager.getInstance().getSpaceship().getCenterXCoordinate());
        object.getMovementConfiguration().setLastKnownTargetY(PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
        object.getMovementConfiguration().setOrbitRadius(85);
        object.setAllowedVisualsToRotate(false);
//        object.setScale(scale);
//        object.getAnimation().setAnimationScale(scale);

        object.setAllowedVisualsToRotate(false);
        return object;
    }


    private static FriendlyObject createDrone (SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyObjectConfiguration) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(new OrbitPathFinder(
                PlayerManager.getInstance().getSpaceship(), 85, 300, 0));
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        movementConfiguration.setXMovementSpeed(1);
        movementConfiguration.setYMovementSpeed(1);
        movementConfiguration.setLastUsedXMovementSpeed(1);
        movementConfiguration.setLastUsedYMovementSpeed(1);
        movementConfiguration.setRotation(Direction.RIGHT);
        movementConfiguration.setOrbitRadius(50);

        return new Drone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
    }

}
