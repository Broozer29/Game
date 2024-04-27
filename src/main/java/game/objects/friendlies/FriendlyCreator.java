package game.objects.friendlies;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import game.objects.friendlies.Drones.Drone;
import game.objects.player.PlayerManager;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendlyCreator {

    public static FriendlyObject createFriendlyObject (SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyObjectConfiguration) {
        return null;
    }

    public static FriendlyObject createDrone (SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyObjectConfiguration) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(new OrbitPathFinder(
                PlayerManager.getInstance().getSpaceship(),
                50,300, 2
        ));
        movementConfiguration.setXMovementSpeed(1);
        movementConfiguration.setYMovementSpeed(1);
        movementConfiguration.setLastUsedXMovementSpeed(1);
        movementConfiguration.setLastUsedYMovementSpeed(1);
        movementConfiguration.setRotation(Direction.RIGHT);
        movementConfiguration.setHasLock(true);
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        return new Drone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
    }

}
