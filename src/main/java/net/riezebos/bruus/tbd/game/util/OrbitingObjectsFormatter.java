package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;

public class OrbitingObjectsFormatter {

    private OrbitingObjectsFormatter(){

    }

    public static void reformatOrbitingObjects(GameObject gameObject, int radius) {
        double meanX = gameObject.getCenterXCoordinate();
        double meanY = gameObject.getCenterYCoordinate();

        // Counting only the GuardianDrones
        int numberOfDrones = gameObject.getObjectOrbitingThis().size();
        if (numberOfDrones == 0) {
            return; // No objects to reformat
        }

        double angleIncrement = 2 * Math.PI / numberOfDrones;

        int iterator = 0;

        for (GameObject object : gameObject.getObjectOrbitingThis()) {
            double nextAngle = angleIncrement * iterator;

            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            object.setCenterCoordinates(x, y);

            // Create a new OrbitPathFinder with the correct offset angle
            OrbitPathFinder newOrbit = new OrbitPathFinder(gameObject, radius, 300, 0);
            // Update the GuardianDrone's path finder
            object.resetMovementPath();
            object.setPathFinder(newOrbit);


            iterator++;
        }

    }
}

