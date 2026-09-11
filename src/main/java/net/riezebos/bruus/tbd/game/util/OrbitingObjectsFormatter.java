package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;

public class OrbitingObjectsFormatter {

    private OrbitingObjectsFormatter(){

    }

    public static void reformatOrbitingObjects(GameObject gameObject, int layerIndex, float radius, boolean reverse) {
        double meanX = gameObject.getCenterXCoordinate();
        double meanY = gameObject.getCenterYCoordinate();

        // Get objects for the specific layer
        var orbitingObjects = gameObject.getOrbitingObjectsAtLayer(layerIndex);
        int numberOfObjects = orbitingObjects.size();
        if (numberOfObjects == 0) {
            return; // No objects to reformat
        }

        double angleIncrement = 2 * Math.PI / numberOfObjects;

        int iterator = 0;

        for (GameObject object : orbitingObjects) {
            double nextAngle = angleIncrement * iterator;

            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            object.setCenterCoordinates(x, y);

            // Create a new OrbitPathFinder with the correct offset angle
            OrbitPathFinder newOrbit = new OrbitPathFinder(gameObject);
            newOrbit.setReverse(reverse);
            // Update the object's path finder

            object.resetMovementPath();
            object.getMovementConfiguration().setOrbitRadius(radius);
            object.setPathFinder(newOrbit);


            iterator++;
        }

    }

    // Convenience method for backward compatibility - formats all layers
    public static void reformatOrbitingObjects(GameObject gameObject, float radius) {
        for (Integer layerIndex : gameObject.getObjectOrbitingThisByLayers().keySet()) {
            reformatOrbitingObjects(gameObject, layerIndex, radius, false);
        }
    }

    public static void reformatOrbitingObjects(GameObject gameObject, float radius, int layerIndex, boolean reverse) {
        reformatOrbitingObjects(gameObject, layerIndex, radius, reverse);
    }
}

