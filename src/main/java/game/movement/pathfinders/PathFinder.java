package game.movement.pathfinders;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.PathFinderConfig;

public interface PathFinder {
	Path findPath(PathFinderConfig pathFinderConfig);
	Direction getNextStep(Point currentLocation, Path path, Direction fallbackDirection);
    boolean shouldRecalculatePath(Path path);
    Point calculateInitialEndpoint(Point start, Direction rotation, boolean friendly);
    Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementspeed, int yMovementspeed);
}