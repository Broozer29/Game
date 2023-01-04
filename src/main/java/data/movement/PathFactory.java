package data.movement;

import java.util.List;

import data.DataClass;
import game.managers.FriendlyManager;

public class PathFactory {

	private static PathFactory instance = new PathFactory();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();

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

	// Total distance is the X distance to walk
	public Path getAngledLine(String direction, int totalDistance, int movementSpeed, int moduloDivider) {
		int stepsToTake = totalDistance / movementSpeed;
		Path newPath = new Path(direction, stepsToTake, movementSpeed, moduloDivider);
		return newPath;
	}

	public Path getHomingPath(int currentXCoordinate, int currentYCoordinate, int movementSpeed, boolean friendly,
			String fallbackDirection, int angleModulo, int xCoordinateDestination, int yCoordinateDestination) {
		if (friendlyManager == null) {
			friendlyManager = FriendlyManager.getInstance();
		}

		int xStepsToTake = Math.abs((xCoordinateDestination - currentXCoordinate) / movementSpeed);
		int yStepsToTake = Math.abs((yCoordinateDestination - currentYCoordinate) / movementSpeed);

		int stepsToTake = 0;
		if (xStepsToTake > yStepsToTake) {
			stepsToTake = xStepsToTake;
		} else
			stepsToTake = yStepsToTake;

		return new Path(currentXCoordinate, currentYCoordinate, stepsToTake, movementSpeed, xCoordinateDestination,
				yCoordinateDestination, angleModulo, fallbackDirection);
	}
}
