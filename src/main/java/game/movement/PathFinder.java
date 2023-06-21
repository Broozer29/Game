package game.movement;

public interface PathFinder {
	Path findPath(Point start, Point end, int XstepSize, int yStepSize, Direction fallbackDirection, boolean isFriendly);
	Direction getNextStep(Point currentLocation, Path path, Direction fallbackDirection);
    boolean shouldRecalculatePath(Path path);
    Point calculateInitialEndpoint(Point start, Direction rotation);
}
