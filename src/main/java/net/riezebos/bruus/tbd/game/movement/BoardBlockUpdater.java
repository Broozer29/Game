package net.riezebos.bruus.tbd.game.movement;

import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;

import java.util.Random;

public class BoardBlockUpdater {
    public static int getBoardBlock(int xCoordinate){
        if (xCoordinate >= 0 && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth())) {
            return 0;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth())
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 2)) {
            return 1;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 2)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 3)) {
            return 2;
        } else if (xCoordinate >= (DataClass.getInstance().getBoardBlockWidth() * 3)
                && xCoordinate <= (DataClass.getInstance().getBoardBlockWidth() * 4)) {
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

    public static Point getRandomCoordinateInBlock(int blockIndex, int objectWidth, int objectHeight) {
        // Validate blockIndex
        if (blockIndex < 0 || blockIndex >= 8) {
            throw new IllegalArgumentException("Block index must be between 0 and 7.");
        }

        // Calculate X coordinate range for the block
        int blockWidth = DataClass.getInstance().getBoardBlockWidth();
        int minX = blockWidth * blockIndex;
        int maxX = minX + blockWidth - objectWidth; // Adjust maxX to prevent the object from extending beyond the block

        // Ensure maxX is greater than minX after adjustment for object width
        maxX = Math.max(minX, maxX);

        // Calculate Y coordinate range for the playable area
        int minY = 0;
        int maxY = DataClass.getInstance().getPlayableWindowMaxHeight() - objectHeight; // Adjust maxY to prevent the object from extending beyond the bottom edge

        // Ensure maxY is greater than minY after adjustment for object height
        maxY = Math.max(minY, maxY);

        Random random = new Random();

        // Generate random X and Y within the block, ensuring the entire object is within bounds
        int x = minX + random.nextInt(maxX - minX + 1); // +1 to include maxX in the range
        int y = minY + random.nextInt(maxY - minY + 1); // +1 to include maxY in the range

        return new Point(x, y);
    }




}
