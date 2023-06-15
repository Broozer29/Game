package game.movement;

public interface PathFinder {
	Path findPath(Point start, Point end, int stepSize, Direction fallbackDirection);
	Direction getNextStep(Point currentLocation, Path path, int stepSize, Direction fallbackDirection);
    boolean shouldRecalculatePath(Path path);
    Point calculateInitialEndpoint(Point start, Direction rotation);
}
