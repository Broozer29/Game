package game.movement;

import gamedata.DataClass;

public class BoardBlockUpdater {

    public static int getBoardBlock(int xCoordinate){
        if (xCoordinate >= 0 && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 1)) {
            return 0;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 1)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 2)) {
            return 1;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 2)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 3)) {
            return 2;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 3)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth())) {
            return 3;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 4)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 5)) {
            return 4;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 5)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 6)) {
            return 5;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 6)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 7)) {
            return 6;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 7)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 8)) {
            return 7;
        } else if (xCoordinate > DataClass.getInstance().getBoardBlockWidth() * 8) {
            return 8;
        }
        return 0;
    }
}
