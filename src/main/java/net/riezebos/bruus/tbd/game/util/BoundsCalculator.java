package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;

import java.awt.*;

public class BoundsCalculator {

    public static Rectangle getGameObjectBounds(GameObject gameObject){
        if(gameObject.getAnimation() != null){
            return gameObject.getAnimation().getBounds();
        } else return gameObject.getBounds();
    }
}
