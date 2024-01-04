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

    public static FriendlyObject createDrone (SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyObjectConfiguration, GameObject objectToOrbit) {
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        friendlyObjectConfiguration.setPathFinder(createOrbitingDronePathFinder(objectToOrbit));
        return new Drone(spriteAnimationConfiguration, friendlyObjectConfiguration);
    }


    private static PathFinder createOrbitingDronePathFinder(GameObject targetToOrbit){
        double meanX = targetToOrbit.getCenterXCoordinate();
        double meanY = targetToOrbit.getCenterYCoordinate();
        double nextAngle = 0;
        if (!targetToOrbit.getObjectOrbitingThis().isEmpty()) {
            List<Double> angles = new ArrayList<>();
            for (GameObject drone : targetToOrbit.getObjectOrbitingThis()) {
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

        return new OrbitPathFinder(PlayerManager.getInstance().getSpaceship(), radius, 300, nextAngle);
    }

}
