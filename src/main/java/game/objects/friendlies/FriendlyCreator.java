package game.objects.friendlies;

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
        return new Drone(spriteAnimationConfiguration, friendlyObjectConfiguration);
    }




}
