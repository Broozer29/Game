package game.util;

import VisualAndAudioData.DataClass;
import com.badlogic.gdx.Game;
import game.movement.Direction;
import game.objects.GameObject;
import game.objects.missiles.missiletypes.SeekerProjectile;
import game.objects.player.PlayerManager;

public class OutOfBoundsCalculator {
    public static boolean isOutOfBounds (GameObject gameObject) {
        if (gameObject.getMovementConfiguration().getRotation() == null) {
            System.out.println("Iets is null: " + gameObject.getClass());
        }


        return calculateOutOfBounds(gameObject);
    }

    public static boolean isOutOfBounds (int xCoordinate, int yCoordinate, Direction rotation) {
        return calculateOutOfBounds(xCoordinate, yCoordinate, rotation);
    }

    private static boolean calculateOutOfBounds(GameObject gameObject){
        int xCoordinate = 0;
        int yCoordinate = 0;
        int width = 0;
        int height = 0;


        if(gameObject.getAnimation() != null){
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
        if(gameObject.getMovementConfiguration().getCurrentPath() != null){
            direction = gameObject.getMovementConfiguration().getCurrentPath().getFallbackDirection();
        } else {
            direction = gameObject.getMovementConfiguration().getRotation();
        }

        switch(direction){
            case UP:
                if (yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - height)) {
                    return true;
                }
                break;
            case DOWN:
                if (yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + height)) {
                    return true;
                }
                break;
            case LEFT:
                if (xCoordinate <= 0 - width) {
                    return true;
                }
                break;
            case RIGHT:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + width) {
                    return true;
                }
                break;
            case LEFT_DOWN:
                if (xCoordinate <= 0 - width || yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + height)) {
                    return true;
                }
                break;
            case LEFT_UP:
                if (xCoordinate <= 0 - width || yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - height)) {
                    return true;
                }
                break;
            case NONE:
                return true;
            case RIGHT_DOWN:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + width
                        || yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + height)) {
                    return true;
                }
                break;
            case RIGHT_UP:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + width || yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - height)) {
                    return true;
                }
                break;
        }

        return false;
    }


    private static boolean calculateOutOfBounds (int xCoordinate, int yCoordinate, Direction direction) {
        switch (direction) {
            case UP:
                if (yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - 250)) {
                    return true;
                }
                break;
            case DOWN:
                if (yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + 250)) {
                    return true;
                }
                break;
            case LEFT:
                if (xCoordinate <= -250) {
                    return true;
                }
                break;
            case RIGHT:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + 250) {
                    return true;
                }
                break;
            case LEFT_DOWN:
                if (xCoordinate <= -50 || yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + 250)) {
                    return true;
                }
                break;
            case LEFT_UP:
                if (xCoordinate <= -50 || yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - 250)) {
                    return true;
                }
                break;
            case NONE:
                return true;
            case RIGHT_DOWN:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + 250
                        || yCoordinate >= (DataClass.getInstance().getPlayableWindowMaxHeight() + 250)) {
                    return true;
                }
                break;
            case RIGHT_UP:
                if (xCoordinate >= DataClass.getInstance().getWindowWidth() + 250 || yCoordinate <= (DataClass.getInstance().getPlayableWindowMinHeight() - 250)) {
                    return true;
                }
                break;
        }
        return false;
    }
}
