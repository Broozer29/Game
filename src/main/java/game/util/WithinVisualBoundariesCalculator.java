package game.util;

import VisualAndAudioData.DataClass;
import game.gameobjects.GameObject;

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
