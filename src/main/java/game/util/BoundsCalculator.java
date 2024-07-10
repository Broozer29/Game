package game.util;

import game.gameobjects.GameObject;

import java.awt.*;

public class BoundsCalculator {

    public static Rectangle getGameObjectBounds(GameObject gameObject){
        if(gameObject.getAnimation() != null){
            return gameObject.getAnimation().getBounds();
        } else return gameObject.getBounds();
    }
}
