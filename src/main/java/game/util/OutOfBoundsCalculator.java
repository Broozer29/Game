package game.util;

import VisualAndAudioData.DataClass;
import game.movement.Direction;
import game.movement.pathfinders.OrbitPathFinder;
import game.gameobjects.GameObject;

public class OutOfBoundsCalculator {
    public static boolean isOutOfBounds (GameObject gameObject) {
        if (gameObject.getMovementConfiguration().getRotation() == null) {
            System.out.println("Iets is null: " + gameObject.getClass());
        }

        if(gameObject.getMovementConfiguration() != null){
            if(gameObject.getMovementConfiguration().getPathFinder() != null){
                if(gameObject.getMovementConfiguration().getPathFinder() instanceof OrbitPathFinder){
                    return false; //These should be deleted when their owner is deleted, not by themselves for going out of bounds
                }
            }
        }

        return calculateOutOfBounds(gameObject);
    }

    public static boolean isOutOfBounds (int xCoordinate, int yCoordinate, Direction rotation) {
        return calculateOutOfBounds(xCoordinate, yCoordinate, rotation);
    }

    private static boolean calculateOutOfBounds (GameObject gameObject) {
        int xCoordinate = 0;
        int yCoordinate = 0;
        int width = 0;
        int height = 0;


        if (gameObject.getAnimation() != null) {
            xCoordinate = gameObject.getAnimation().getXCoordinate();
            yCoordinate = gameObject.getAnimation().getYCoordinate();
            width = gameObject.getAnimation().getWidth();
            height = gameObject.getAnimation().getHeight();
        } else {
            xCoordinate = gameObject.getXCoordinate();
            yCoordinate = gameObject.getYCoordinate();
            width = gameObject.getWidth();
            height = gameObject.getHeight();
        }
//        if(gameObject instanceof SeekerProjectile){
//            if(xCoordinate < 0 || xCoordinate > 1400 || yCoordinate < 0 || yCoordinate > 800){
//                System.out.println("break");
//            }
//        }

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
            case NONE:
                return true;
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
        }

        return false;
    }


    private static boolean calculateOutOfBounds (int xCoordinate, int yCoordinate, Direction direction) {
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
