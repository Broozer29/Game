package data.movement;

import data.DataClass;

public class InitialEndPointCalculator {
	public Point calculateEndPoint(int xCoordinate, int yCoordinate, Direction rotation) {
		int endXCoordinate = 0;
		int endYCoordinate = 0;
		DataClass dataClass = DataClass.getInstance();

		switch (rotation) {
		case UP:
			endYCoordinate = 0;
			endXCoordinate = xCoordinate;
			break;
		case DOWN:
			endYCoordinate = dataClass.getWindowHeight();
			endXCoordinate = xCoordinate;
			break;
		case LEFT:
			endYCoordinate = yCoordinate;
			endXCoordinate = 0;
			break;
		case RIGHT:
			endYCoordinate = yCoordinate;
			endXCoordinate = dataClass.getWindowWidth();
			break;
		case RIGHT_UP:
			endYCoordinate = 0;
			endXCoordinate = dataClass.getWindowWidth();
			break;
		case RIGHT_DOWN:
			endYCoordinate = dataClass.getWindowHeight();
			endXCoordinate = dataClass.getWindowWidth();
			break;
		case LEFT_UP:
			endYCoordinate = 0;
			endXCoordinate = 0;
			break;
		case LEFT_DOWN:
			endYCoordinate = dataClass.getWindowHeight();
			endXCoordinate = 0;
			break;
		default:
			endYCoordinate = yCoordinate;
			endXCoordinate = 0;
			break;
		}

		Point endPoint = new Point(endXCoordinate, endYCoordinate);
		return endPoint;
	}
}
