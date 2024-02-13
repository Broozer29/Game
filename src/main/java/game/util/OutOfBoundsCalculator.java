package game.util;

import VisualAndAudioData.DataClass;
import game.movement.Direction;
import game.objects.GameObject;

public class OutOfBoundsCalculator {
    public static boolean isOutOfBounds (GameObject gameObject){
        if(gameObject.getMovementConfiguration().getRotation() == null){
            System.out.println("Iets is null: " + gameObject.getClass());
        }
        return calculateOutOfBounds(gameObject.getXCoordinate(), gameObject.getYCoordinate(), gameObject.getMovementConfiguration().getCurrentPath().getFallbackDirection());
    }

    public static boolean isOutOfBounds (int xCoordinate, int yCoordinate, Direction rotation){
        return calculateOutOfBounds(xCoordinate, yCoordinate, rotation);
    }


    private static boolean calculateOutOfBounds (int xCoordinate, int yCoordinate, Direction direction){
        switch (direction) {
            case UP:
                if (yCoordinate <= 0) {
                    return true;
                }
                break;
            case DOWN:
                if (yCoordinate >= DataClass.getInstance().getWindowHeight()) {
                    return true;
                }
                break;
            case LEFT:
                if (xCoordinate <= 0) {
                    return true;
                }
                break;
            case RIGHT:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth()) {
                    return true;
                }
                break;
            case LEFT_DOWN:
                if (xCoordinate <= 0 || yCoordinate >= DataClass.getInstance().getWindowHeight()) {
                    return true;
                }
                break;
            case LEFT_UP:
                if (xCoordinate <= 0 || yCoordinate <= 0) {
                    return true;
                }
                break;
            case NONE:
                return true;
            case RIGHT_DOWN:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth()
                        || yCoordinate >= DataClass.getInstance().getWindowHeight()) {
                    return true;
                }
                break;
            case RIGHT_UP:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() || yCoordinate <= 0) {
                    return true;
                }
                break;
        }
        return false;
    }
}
