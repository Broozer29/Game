package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.movement.Point;

import java.awt.*;
import java.util.Random;

public class ProtossUtils {

    private static final Random random = new Random();
    private static ProtossUtils instance = new ProtossUtils();
    private static float protossShipBuilderTimer = 0.0f;
    private static float protossShipBuildTime = 5.0f;
    private boolean isAllowedToBuildProtoss = true;
    private ProtossUtils() {

    }

    public static ProtossUtils getInstance() {
        return instance;
    }

    public static Point getRandomPoint() {
        SpaceShip spaceShip = PlayerManager.getInstance().getSpaceship();

        int xCoordinate = spaceShip.getCenterXCoordinate();
        int yCoordinate = spaceShip.getCenterYCoordinate();

        int minDistance = 20; //To be adjusted
        int maxDistance = 200; //To be adjusted

        double angle = random.nextDouble() * 2 * Math.PI; // Random angle in radians
        int distance = minDistance + random.nextInt(maxDistance - minDistance + 1); // Random distance within range

        int newX = xCoordinate + (int) (Math.cos(angle) * distance);
        int newY = yCoordinate + (int) (Math.sin(angle) * distance);

        return new Point(newX, newY);
    }

    public static boolean canHostMoreProtoss() {
        int currentAmount = PlayerStats.getInstance().getAmountOfProtossArbiters() + PlayerStats.getInstance().getAmountOfProtossShuttles() + PlayerStats.getInstance().getAmountOfProtossScouts();
        return currentAmount < PlayerStats.getInstance().getMaxAmountOfProtoss();
    }

    public static double getDistanceToRectangle(int px, int py, Rectangle rect) {
        // If the point is inside the rectangle, the distance is 0
        if (rect.contains(px, py)) {
            return 0;
        }

        // Find the closest X and Y coordinates within the rectangle
        int closestX = Math.max(rect.x, Math.min(px, rect.x + rect.width));
        int closestY = Math.max(rect.y, Math.min(py, rect.y + rect.height));

        // Compute Euclidean distance
        double deltaX = px - closestX;
        double deltaY = py - closestY;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void buildProtossShips() {
        if(!isAllowedToBuildProtoss) {
            return;
        }

        protossShipBuilderTimer += 0.015f;
        if (protossShipBuilderTimer >= protossShipBuildTime) {
            boolean hasBuildShips = false;

            if(canFitMoreShips(DroneTypes.ProtossScout)){
                buildProtossScout();
                hasBuildShips = true;
            }
            if(canFitMoreShips(DroneTypes.ProtossShuttle)){
                buildProtossShuttle();
                hasBuildShips = true;
            }
            if(canFitMoreShips(DroneTypes.ProtossArbiter)){
                buildProtossArbiter();
                hasBuildShips = true;
            }

            if (hasBuildShips) {
                protossShipBuilderTimer = 0.0f;
            }
        }
    }

    private boolean canFitMoreShips(DroneTypes droneType) {
        int maxShipCount = 0;

        switch (droneType) {
            case ProtossArbiter -> {
                maxShipCount = PlayerStats.getInstance().getAmountOfProtossArbiters();
            }
            case ProtossShuttle -> {
                maxShipCount = PlayerStats.getInstance().getAmountOfProtossShuttles();
            }
            case ProtossScout -> {
                maxShipCount = PlayerStats.getInstance().getAmountOfProtossScouts();
//                maxShipCount = 10;
            }
            default -> maxShipCount = 0; //always returns 0
        }


        return FriendlyManager.getInstance().getDronesByDroneType(droneType).size() < maxShipCount;
    }

    private void buildProtossScout() {
        FriendlyManager.getInstance().addProtossShip(DroneTypes.ProtossScout);
    }

    private void buildProtossShuttle() {
        FriendlyManager.getInstance().addProtossShip(DroneTypes.ProtossShuttle);
    }

    private void buildProtossArbiter() {
        FriendlyManager.getInstance().addProtossShip(DroneTypes.ProtossArbiter);
    }

    public boolean isAllowedToBuildProtoss() {
        return isAllowedToBuildProtoss;
    }

    public void setAllowedToBuildProtoss(boolean allowedToBuildProtoss) {
        isAllowedToBuildProtoss = allowedToBuildProtoss;
    }
}
