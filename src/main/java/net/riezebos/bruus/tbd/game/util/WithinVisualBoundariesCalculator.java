package net.riezebos.bruus.tbd.game.util;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.visuals.data.DataClass;

public class WithinVisualBoundariesCalculator {

    public static boolean isWithinBoundaries(GameObject object) {
        int windowWidth = DataClass.getInstance().getWindowWidth();
        int minHeight = DataClass.getInstance().getPlayableWindowMinHeight();
        int maxHeight = DataClass.getInstance().getPlayableWindowMaxHeight();

        int objectLeft = object.getXCoordinate();
        int objectRight = object.getXCoordinate() + object.getWidth();
        int objectTop = object.getYCoordinate();
        int objectBottom = object.getYCoordinate() + object.getHeight();

        // Check if the object is partially or fully within the visual boundaries
        boolean isWithinHorizontalBoundaries = (objectRight >= 0 && objectLeft <= windowWidth);
        boolean isWithinVerticalBoundaries = (objectBottom >= minHeight && objectTop <= maxHeight);

        return isWithinHorizontalBoundaries && isWithinVerticalBoundaries;
    }
}
