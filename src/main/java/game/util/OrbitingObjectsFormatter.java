package game.util;

import game.movement.pathfinders.OrbitPathFinder;
import game.objects.GameObject;
import game.objects.player.spaceship.SpaceShip;

public class OrbitingObjectsFormatter {
    public static void reformatOrbitingObjects(GameObject gameObject, int radius) {
        double meanX = gameObject.getCenterXCoordinate();
        double meanY = gameObject.getCenterYCoordinate();

        // Adjust the center if the object is a SpaceShip
        if (gameObject instanceof SpaceShip) {
            meanX -= 10;
            meanY -= 20;
        }

        // Adjust for the dimensions of the gameObject


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
            OrbitPathFinder newOrbit = new OrbitPathFinder(gameObject, 85, 300, 0);
            // Update the GuardianDrone's path finder
            object.resetMovementPath();
            object.setPathFinder(newOrbit);

//            System.out.println("Object placed at: calculated X: " + x + " Calculated Y: " + y + " object dimensions: " +
//                    object.getWidth() + " / " + object.getHeight());

            iterator++;
        }

//        System.out.println("Reformatted: " + gameObject.getObjectOrbitingThis().size() + " objects, calculated angleIncrement: " + angleIncrement
//        + " central target is at: " + gameObject.getCenterXCoordinate() + " / " + gameObject.getCenterYCoordinate());
    }
}

