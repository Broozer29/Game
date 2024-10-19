package net.riezebos.bruus.tbd.game.movement.pathfinders;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.Path;
import net.riezebos.bruus.tbd.game.movement.Point;

public interface PathFinder {
	Path findPath(GameObject gameObject);
	Direction getNextStep(GameObject gameObject, Direction fallbackDirection);
    boolean shouldRecalculatePath(GameObject gameObject);
    Point calculateInitialEndpoint(Point start, Direction rotation, boolean friendly);
    Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed);
}