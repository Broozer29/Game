package data.movement;

public class HomingPath extends Path {

	public HomingPath(String pathDirection, int stepSize, int moduloDivider, int startingXCoordinate,
			int startingYCoordinate, int xCoordinateDestination, int yCoordinateDestination) {
		super("Homing", pathDirection, stepSize, moduloDivider);
		this.initHomingPath(startingXCoordinate, startingYCoordinate, xCoordinateDestination, yCoordinateDestination,
				pathDirection);
	}

}
