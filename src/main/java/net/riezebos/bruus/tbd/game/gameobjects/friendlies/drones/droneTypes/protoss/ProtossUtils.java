package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.ProtossThorns;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.ThornsDamageDealer;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class ProtossUtils {


    //todo protossutils is niet multiplayer compatible:
    // - Static attributen die gedeeld gebruikt worden (building timer, fast/slow movement, ships luisteren niet naar hun eigen beacon maar naar iedereens beacon)
    // - Max aantal protoss ships logica gaat er nog vanuit dat er maar 1 speler is
    private static final Random random = new Random();
    private static ProtossUtils instance = new ProtossUtils();
    public static int maxHoverDistance = 200;


    private ProtossUtils() {

    }

    public static ProtossUtils getInstance() {
        return instance;
    }

    public static boolean carrierDroneIsPresent(GameObject player) {
        return !FriendlyManager.getInstance().getDronesByDroneType(DroneTypes.CarrierDrone, player).isEmpty();
    }

    public static Point getRandomPoint(GameObject selectedObject) {
        GameObject finalSelectedObject = selectedObject;
        List<Drone> carrierDrones = FriendlyManager.getInstance().getDrones().stream()
                .filter(drone -> drone instanceof CarrierBeacon && drone.getOwnerOrCreator().equals(finalSelectedObject))
                .toList();

        if (!carrierDrones.isEmpty()) {
            selectedObject = carrierDrones.get(0);
        }

        int xCoordinate = selectedObject.getCenterXCoordinate();
        int yCoordinate = selectedObject.getCenterYCoordinate();

        int minDistance = 20; //To be adjusted

        double angle = random.nextDouble() * 2 * Math.PI; // Random angle in radians
        int distance = minDistance + random.nextInt(maxHoverDistance - minDistance + 1); // Random distance within range

        int newX = xCoordinate + (int) (Math.cos(angle) * distance);
        int newY = yCoordinate + (int) (Math.sin(angle) * distance);

        return new Point(newX, newY);
    }

    //This one is to be used by the enemy boss
    public static Point getRandomPoint(GameObject selectedObject, int minDistance, int maxDistance) {
        int xCoordinate = selectedObject.getCenterXCoordinate();
        int yCoordinate = selectedObject.getCenterYCoordinate();

        double angle = random.nextDouble() * 2 * Math.PI; // Random angle in radians
        int distance = minDistance + random.nextInt(maxDistance - minDistance + 1); // Random distance within range

        int newX = xCoordinate + (int) (Math.cos(angle) * distance);
        int newY = yCoordinate + (int) (Math.sin(angle) * distance);

        return new Point(newX, newY);
    }

    public static boolean canHostMoreProtossToRollItems() {
        int currentAmount = Math.max(PlayerInventory.getInstance().getCountOfItem(ItemEnums.ProtossArbiter), 1) + Math.max(PlayerInventory.getInstance().getCountOfItem(ItemEnums.ProtossCorsair), 1) + PlayerInventory.getInstance().getCountOfItem(ItemEnums.ProtossScout) + PlayerInventory.getInstance().getCountOfItem(ItemEnums.ProtossShuttle) + PlayerStats.getInstance().getCarrierStartingScouts();
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


    private float overBuildResidu = 0;

    public void buildProtossShips(SpaceShip spaceShip) {
        if (!spaceShip.isAllowedToBuildProtoss) {
            return;
        }

        if (spaceShip.protossShipBuilderTimer < spaceShip.protossShipBuildTime) {
            spaceShip.protossShipBuilderTimer += (0.015f * spaceShip.getProtossConstructionSpeedModifier());
        }

        if (spaceShip.protossShipBuilderTimer >= spaceShip.protossShipBuildTime) {
            spaceShip.overBuildResidu = spaceShip.protossShipBuilderTimer - spaceShip.protossShipBuildTime;
            spaceShip.protossShipBuilderTimer = spaceShip.protossShipBuildTime;

            if (spaceShip.overBuildResidu < 0.2) { //shouldnt be possible but safeguards against negative values
                spaceShip.overBuildResidu = 0;
            }
        }

        if (spaceShip.protossShipBuilderTimer >= spaceShip.protossShipBuildTime) {
            boolean hasBuildShips = false;

            if (canFitMoreShips(DroneTypes.ProtossScout, spaceShip)) {
                buildProtossScout(spaceShip);
                hasBuildShips = true;
            }
            if (canFitMoreShips(DroneTypes.ProtossShuttle, spaceShip)) {
                buildProtossShuttle(spaceShip);
                hasBuildShips = true;
            }
            if (canFitMoreShips(DroneTypes.ProtossArbiter, spaceShip)) {
                buildProtossArbiter(spaceShip);
                hasBuildShips = true;
            }
            if (canFitMoreShips(DroneTypes.ProtossCorsair, spaceShip)) {
                buildProtossCorsair(spaceShip);
                hasBuildShips = true;
            }

            if (hasBuildShips) {
                spaceShip.protossShipBuilderTimer = 0.0f;
                if (spaceShip.overBuildResidu >= 0.01) {
                    spaceShip.protossShipBuilderTimer += spaceShip.overBuildResidu;
                    spaceShip.overBuildResidu = 0;
                }
            }
        }
    }

    private boolean canFitMoreShips(DroneTypes droneType, SpaceShip player) {
        int maxShipCount = 0;

        switch (droneType) {
            case ProtossArbiter -> {
                maxShipCount = player.getArbiterCount();
            }
            case ProtossShuttle -> {
                maxShipCount = player.getShuttleCount();
            }
            case ProtossScout -> {
                maxShipCount = player.getScoutCount();
            }
            case ProtossCorsair -> {
                maxShipCount = player.getCorsairCount();
            }
            default -> maxShipCount = 0; //always returns 0
        }

        int test = FriendlyManager.getInstance().getDronesByDroneType(droneType, player).size();
        return test < maxShipCount;
    }

    public void handleProtossThorns(GameObject enemy) {
        if (enemy == null) {
            return;
        }

        ProtossThorns protossThorns = (ProtossThorns) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.ProtossThorns);
        if (protossThorns != null) {
            ThornsDamageDealer.getInstance().dealThornsDamageTo(enemy, PlayerStats.getInstance().getBaseDamage() * PlayerStats.getInstance().getProtossShipThornsDamageRatio()); //todo baseDamage zou thorns damage moeten zijn maar dit is deprecated
        }
    }

    private void buildProtossScout(GameObject player) {
        FriendlyManager.getInstance().addProtossShip(DroneTypes.ProtossScout, player);
    }

    private void buildProtossShuttle(GameObject player) {
        FriendlyManager.getInstance().addProtossShip(DroneTypes.ProtossShuttle, player);
    }

    private void buildProtossArbiter(GameObject player) {
        FriendlyManager.getInstance().addProtossShip(DroneTypes.ProtossArbiter, player);
    }

    private void buildProtossCorsair(GameObject player) {
        FriendlyManager.getInstance().addProtossShip(DroneTypes.ProtossCorsair, player);
    }

    public static void handleScrapMetalPickUp(float repairAmountMultiplier, SpaceShip spaceShip) {
        spaceShip.protossShipBuilderTimer += (repairAmountMultiplier * spaceShip.protossShipBuildTime);
    }
}
