package game.util;

import game.managers.OnScreenTextManager;
import game.movement.pathfinders.OrbitPathFinder;
import game.objects.GameObject;
import game.objects.friendlies.Drones.Drone;
import game.objects.friendlies.FriendlyObject;
import game.objects.player.PlayerManager;
import game.objects.player.spaceship.SpaceShip;

public class OrbitingObjectsFormatter {
    public static void reformatOrbitingObjects (GameObject gameObject, int radius) {
        double meanX = gameObject.getCenterXCoordinate();
        double meanY = gameObject.getCenterYCoordinate();

        if(gameObject instanceof SpaceShip){
            meanX -= 10;
            meanY -= 20;
        }


        // Counting only the GuardianDrones
        int numberOfDrones = gameObject.getObjectOrbitingThis().size();
        double angleIncrement = 2 * Math.PI / numberOfDrones;

        int totalWayPoints = 300;

        int iterator = 0;
        for (GameObject object : gameObject.getObjectOrbitingThis()) {
            double nextAngle = angleIncrement * iterator;

            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            object.setCenterCoordinates(x, y);

            // Create a new OrbitPathFinder with the correct offset angle
            OrbitPathFinder newOrbit = new OrbitPathFinder(gameObject, radius,
                    totalWayPoints, nextAngle);
            // Update the GuardianDrone's path finder
            object.resetMovementPath();
            object.setPathFinder(newOrbit);

            iterator++;
        }
    }
}
