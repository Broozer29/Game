package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossScout;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossShuttle;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;

public class OutOfBoundsCalculator {

    private OutOfBoundsCalculator() {

    }

    public static boolean isOutOfBounds(GameObject gameObject) {
        if (isExempt(gameObject)){
            return false;
        }
        return calculateOutOfBounds(gameObject);
    }

    private static boolean isExempt(GameObject gameObject) {
        if (gameObject instanceof Drone || gameObject instanceof EnemyProtossScout || gameObject instanceof EnemyProtossShuttle) {
            return true;
        }


        if (gameObject.getMovementConfiguration() != null &&
                gameObject.getMovementConfiguration().getPathFinder() != null &&
                gameObject.getMovementConfiguration().getPathFinder() instanceof OrbitPathFinder) {
            return true;
        }

        return false;
    }

    public static boolean isOutOfBounds(int xCoordinate, int yCoordinate, Direction rotation) {
        return calculateOutOfBounds(xCoordinate, yCoordinate, rotation);
    }

    private static boolean calculateOutOfBounds(GameObject gameObject) {
        int xCoordinate = gameObject.getXCoordinate();
        int yCoordinate = gameObject.getYCoordinate();
        int width = gameObject.getWidth();
        int height = gameObject.getHeight();

        // Handle case for Missiles
        if (gameObject instanceof Missile) {
            int playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();
            int playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
            int windowMaxWidth = DataClass.getInstance().getWindowWidth();
            int windowMinWidth = 0;

            // Check if the missile is out of bounds in any direction
            if (yCoordinate <= (playableWindowMinHeight - height) // Out at the top
                    || yCoordinate >= (playableWindowMaxHeight + height) // Out at the bottom
                    || xCoordinate <= (windowMinWidth - width) // Out on the left
                    || xCoordinate >= (windowMaxWidth + width)) { // Out on the right
                return true;
            }
        }

        // Handle other cases (example: SpaceStationBoss)
        if (gameObject.getImageEnum().equals(ImageEnums.SpaceStationBoss)) {
            width = gameObject.getWidth() * 3;
            height = gameObject.getHeight() * 3;
        }

        Direction direction = null;
        if (gameObject.getMovementConfiguration().getCurrentPath() != null) {
            direction = gameObject.getMovementConfiguration().getCurrentPath().getFallbackDirection();
        } else {
            direction = gameObject.getMovementConfiguration().getRotation();
        }

        int playableWindowMaxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();
        int playableWindowMinHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        int windowMaxWidth = DataClass.getInstance().getWindowWidth();
        int windowMinWidth = 0;
        switch (direction) {
            case UP:
                if (yCoordinate <= (playableWindowMinHeight - height)
                        || xCoordinate <= windowMinWidth - width
                        || xCoordinate >= windowMaxWidth + width) {
                    return true;
                }
                break;
            case DOWN:
                if (yCoordinate >= (playableWindowMaxHeight + height)
                        || xCoordinate <= windowMinWidth - width
                        || xCoordinate >= windowMaxWidth + width) {
                    return true;
                }
                break;
            case LEFT:
                if (xCoordinate <= windowMinWidth - width
                        || yCoordinate <= (playableWindowMinHeight - height)
                        || yCoordinate >= (playableWindowMaxHeight + height)) {
                    return true;
                }
                break;
            case RIGHT:
                if (xCoordinate >= windowMaxWidth + width
                        || yCoordinate <= (playableWindowMinHeight - height)
                        || yCoordinate >= (playableWindowMaxHeight + height)) {
                    return true;
                }
                break;
            case LEFT_DOWN:
                if (xCoordinate <= 0 - width || yCoordinate >= (playableWindowMaxHeight + height)) {
                    return true;
                }
                break;
            case LEFT_UP:
                if (xCoordinate <= 0 - width || yCoordinate <= (playableWindowMinHeight - height)) {
                    return true;
                }
                break;
            case RIGHT_DOWN:
                if (xCoordinate >= windowMaxWidth + width
                        || yCoordinate >= (playableWindowMaxHeight + height)) {
                    return true;
                }
                break;
            case RIGHT_UP:
                if (xCoordinate >= windowMaxWidth + width || yCoordinate <= (playableWindowMinHeight - height)) {
                    return true;
                }
                break;
            case NONE:
                return false;
        }

        return false;
    }


    private static boolean calculateOutOfBounds(int xCoordinate, int yCoordinate, Direction direction) {
        switch (direction) {
            case UP:
                if (yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - 350)) {
                    return true;
                }
                break;
            case DOWN:
                if (yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + 350)) {
                    return true;
                }
                break;
            case LEFT:
                if (xCoordinate <= -350) {
                    return true;
                }
                break;
            case RIGHT:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + 350) {
                    return true;
                }
                break;
            case LEFT_DOWN:
                if (xCoordinate <= -350 || yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + 350)) {
                    return true;
                }
                break;
            case LEFT_UP:
                if (xCoordinate <= -350 || yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - 350)) {
                    return true;
                }
                break;
            case NONE:
                return true;
            case RIGHT_DOWN:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + 350
                        || yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + 350)) {
                    return true;
                }
                break;
            case RIGHT_UP:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + 350 || yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - 350)) {
                    return true;
                }
                break;
        }
        return false;
    }
}
