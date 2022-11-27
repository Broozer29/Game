package data.movement;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private String pathDirection;
	private int stepsToTake;
	private int stepsTaken;
	private int stepSize;

	public Path(String pathDirection, int stepsToTake, int stepSize) {
		this.pathDirection = pathDirection;
		this.stepsToTake = stepsToTake;
		this.stepSize = stepSize;
	}

	// Returns new X & Y coordinates based on the direction
	public List<Integer> getNewCoordinates(int xCoordinate, int yCoordinate) {
		List<Integer> newCoordsList = new ArrayList<Integer>();
		int newXCoordinate = xCoordinate;
		int newYCoordinate = yCoordinate;
		if (isPathWalked() == false) {
			switch (pathDirection) {
			case ("Left"):
				newXCoordinate = getNewXCoordinate("Left", newXCoordinate);
				break;
			case ("LeftUp"):
				newXCoordinate = getNewXCoordinate("Left", newXCoordinate);
				newYCoordinate = getNewYCoordinate("Up", newYCoordinate);
				break;
			case ("LeftDown"):
				newXCoordinate = getNewXCoordinate("Left", newXCoordinate);
				newYCoordinate = getNewYCoordinate("Down", newYCoordinate);
				break;

			case ("Right"):
				newXCoordinate = getNewXCoordinate("Right", newXCoordinate);
				break;
			case ("RightUp"):
				newXCoordinate = getNewXCoordinate("Right", newXCoordinate);
				newYCoordinate = getNewYCoordinate("Up", newYCoordinate);
				break;
			case ("RightDown"):
				newXCoordinate = getNewXCoordinate("Right", newXCoordinate);
				newYCoordinate = getNewYCoordinate("Down", newYCoordinate);
				break;

			case ("Up"):
				newYCoordinate = getNewYCoordinate("Up", newYCoordinate);
				break;
			case ("Down"):
				newYCoordinate = getNewYCoordinate("Down", newYCoordinate);
				break;
			}
			increaseStepTaken();
		}

		newCoordsList.add(newXCoordinate);
		newCoordsList.add(newYCoordinate);
		return newCoordsList;

	}

	// Possible directions:
	/*
	 * Left = x - 1 Left + Up = x - 1 & y - 1 Left + Down = x - 1 & y + 1 Right = x
	 * + 1 Right + Up = x + 1 & y - 1 Right + Down = x + 1 & y + 1
	 */

	// Returns new X coordinates based on the direction & stepsize
	private int getNewXCoordinate(String direction, int currentXCoordinate) {
		int newXCoordinate = currentXCoordinate;
		switch (direction) {
		case ("Left"):
			newXCoordinate -= stepSize;
			break;
		case ("Right"):
			newXCoordinate += stepSize;
			break;
		}
		return newXCoordinate;
	}

	// Returns new Y coordinates based on the direction & stepsize
	private int getNewYCoordinate(String direction, int currentYCoordinate) {
		int newYCoordinate = currentYCoordinate;
		switch (direction) {
		case ("Up"):
			newYCoordinate -= stepSize;
			break;
		case ("Down"):
			newYCoordinate += stepSize;
			break;
		}
		return newYCoordinate;
	}

	// Called when coordinates are returned
	private void increaseStepTaken() {
		this.stepsTaken++;
	}

	// Returns wether the path has been completed
	public boolean isPathWalked() {
		return stepsTaken >= stepsToTake;
	}

	// Called when enemy needs to change speed, for the board block system
	public void setMovementSpeed(int movementSpeed) {
		this.stepSize = movementSpeed;
	}

}
