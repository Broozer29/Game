package data.movement;


public interface PathFinder {
	Path findPath(Point start, Point end, int stepSize);
	Direction getNextStep(Point currentLocation, Path path, int stepSize);
}
