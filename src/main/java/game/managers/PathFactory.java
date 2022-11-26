package game.managers;

import data.movement.Path;

public class PathFactory {

	private static PathFactory instance = new PathFactory();

	private PathFactory() {

	}

	public static PathFactory getInstance() {
		return instance;
	}

	public Path getStraightLine(String direction, int totalDistance, int movementSpeed) {
		int stepsToTake = totalDistance / movementSpeed;
		Path newPath = new Path(direction, stepsToTake, movementSpeed);
		return newPath;
	}
}
