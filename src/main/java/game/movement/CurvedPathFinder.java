package game.movement;

public class CurvedPathFinder implements PathFinder {
	
    @Override
    public boolean shouldRecalculatePath(Path path) {
        return path == null; // only recalculate if we don't have a path yet
    }

	@Override
	public Path findPath(Point start, Point end, int stepSize, Direction fallbackDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Direction getNextStep(Point currentLocation, Path path, int stepSize, Direction fallbackDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point calculateInitialEndpoint(Point start, Direction rotation) {
		// TODO Auto-generated method stub
		return null;
	}
}