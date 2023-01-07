package data.movement;



public class PathFactory {

	private static PathFactory instance = new PathFactory();

	private PathFactory() {

	}

	public static PathFactory getInstance() {
		return instance;
	}

	public Path getStraightLine(String pathDirection, int totalDistance, int stepSize) {
		int stepsToTake = totalDistance / stepSize;
		Path newPath = new RegularPath(pathDirection, stepSize, 0, stepsToTake);
		return newPath;
	}

	// Total distance is the X distance to walk
	public Path getAngledLine(String pathDirection, int totalDistance, int stepSize, int moduloDivider) {
		int stepsToTake = totalDistance / stepSize;
		Path newPath = new RegularPath(pathDirection, stepSize, moduloDivider, stepsToTake);
		return newPath;
	}

	public Path getHomingPath(int currentXCoordinate, int currentYCoordinate, int stepSize, boolean friendly,
			String fallbackDirection, int moduloDivider, int xCoordinateDestination, int yCoordinateDestination) {
		return new HomingPath(fallbackDirection, stepSize, moduloDivider, currentXCoordinate, currentYCoordinate,
				xCoordinateDestination, yCoordinateDestination);
	}
}
