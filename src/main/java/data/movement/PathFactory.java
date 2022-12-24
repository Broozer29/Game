package data.movement;

import java.util.List;

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

	public Path getHomingPath(int currentXCoordinate, int currentYCoordinate, int movementSpeed, boolean friendly) {
		if(friendlyManager == null) {
			friendlyManager = FriendlyManager.getInstance();
		}
		List<Integer> destinationCoordinatesList = friendlyManager.getNearestFriendlyHomingCoordinates();
		
		int xCoordinateDestination = destinationCoordinatesList.get(0);
		int yCoordinateDestination = destinationCoordinatesList.get(1);
		Path newPath = new Path(currentXCoordinate, currentYCoordinate, movementSpeed, xCoordinateDestination,
				yCoordinateDestination);
		return newPath;
	}

}
