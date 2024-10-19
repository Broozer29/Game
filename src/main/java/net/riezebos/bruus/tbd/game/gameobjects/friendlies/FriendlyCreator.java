package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.Drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

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
                0.5f, pathFinder,
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
