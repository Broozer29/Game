package data.movement;

public class PathFactory {

	private static PathFactory instance = new PathFactory();

	private PathFactory() {

	}

	public static PathFactory getInstance() {
		return instance;
	}

	public Path getStraightLine(String direction, int totalDistance, int movementSpeed) {
		int stepsToTake = totalDistance / movementSpeed;
		Path newPath = new Path(direction, stepsToTake, movementSpeed, 0);
		return newPath;
	}
	
	//Total distance is the X distance to walk
	public Path getAngledLine(String direction, int totalDistance, int movementSpeed, int moduloDivider) {
		int stepsToTake = totalDistance / movementSpeed;
		Path newPath = new Path(direction, stepsToTake, movementSpeed, moduloDivider);
		return newPath;
	}
	
}
