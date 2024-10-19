package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.visuals.audiodata.DataClass;

public class WithinVisualBoundariesCalculator {

    public static boolean isWithinBoundaries(GameObject object){
        if(object.getXCoordinate() >= 0 && object.getXCoordinate() <= DataClass.getInstance().getWindowWidth()){
            if(object.getYCoordinate() >= DataClass.getInstance().getPlayableWindowMinHeight() && object.getYCoordinate() <= DataClass.getInstance().getPlayableWindowMaxHeight()){
                return true;
            }
        }
        return false;

    }
}
