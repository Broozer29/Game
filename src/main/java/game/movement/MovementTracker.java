package game.movement;

import java.util.HashMap;
import java.util.Map;


public class MovementTracker {
    private Map<Integer, Point> positions = new HashMap<>();
    private int tickCount = 0;

    public void recordPosition (int xCoordinate, int yCoordinate) {
        positions.put(tickCount, new Point(xCoordinate, yCoordinate));
        tickCount++;
    }

    public boolean hasMovedInLastTicks (int numberOfTicks) {
        if (tickCount < numberOfTicks) return false;

        Point initialPosition = positions.get(tickCount - numberOfTicks);
        Point currentPosition = positions.get(tickCount - 1);

        return !initialPosition.equals(currentPosition);
    }

}

