package game.movement.pathfinders;

import game.gameobjects.GameObject;
import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;

public interface PathFinder {
	Path findPath(GameObject gameObject);
	Direction getNextStep(GameObject gameObject, Direction fallbackDirection);
    boolean shouldRecalculatePath(GameObject gameObject);
    Point calculateInitialEndpoint(Point start, Direction rotation, boolean friendly);
    Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed);
}