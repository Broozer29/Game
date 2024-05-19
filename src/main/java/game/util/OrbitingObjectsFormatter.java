package game.util;

import game.movement.pathfinders.OrbitPathFinder;
import game.objects.GameObject;
import game.objects.friendlies.Drones.Drone;
import game.objects.friendlies.FriendlyObject;
import game.objects.player.PlayerManager;

public class OrbitingObjectsFormatter {
    public static void reformatOrbitingObjects (GameObject gameObject, int radius) {
        double meanX = gameObject.getCenterXCoordinate();
        double meanY = gameObject.getCenterYCoordinate();

        // Counting only the GuardianDrones
        int numberOfDrones = gameObject.getObjectOrbitingThis().size();
        double angleIncrement = 2 * Math.PI / numberOfDrones;

        int totalFrames = 300; // You may replace this with an appropriate value

        int iterator = 0;
        for (GameObject object : gameObject.getObjectOrbitingThis()) {
            double nextAngle = angleIncrement * iterator;

            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            object.setX(x);
            object.setY(y);

            // Create a new OrbitPathFinder with the correct offset angle
            OrbitPathFinder newOrbit = new OrbitPathFinder(gameObject, radius,
                    totalFrames, nextAngle);
            // Update the GuardianDrone's path finder
            object.setPathFinder(newOrbit);

            iterator++;
        }
    }
}
