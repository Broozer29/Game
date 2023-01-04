package data.movement;

import java.util.ArrayList;
import java.util.List;

public class Path {

	private String pathType;
	private String pathDirection;

	// Required for angled & straight lined directions
	private int moduloDivider;
	private int stepsToTake;
	private int stepsTaken;
	private int stepSize;

	// Required for homing missiles
	private int startingXCoordinate;
	private int startingYCoordinate;
	private int xCoordinateDestination;
	private int yCoordinateDestination;
	private String fallbackDirection;

	public Path(String pathDirection, int stepsToTake, int stepSize, int moduloDivider) {
		this.pathDirection = pathDirection;
		this.stepsToTake = stepsToTake;
		this.stepSize = stepSize;
		this.moduloDivider = moduloDivider;
		this.pathType = "Regular";
	}

	public Path(int currentXCoordinate, int currentYCoordinate, int stepsToTake, int stepSize,
			int xCoordinateDestination, int yCoordinateDestination, int angleModulo, String fallbackDirection) {
		this.pathType = "Homing";
		this.startingXCoordinate = currentXCoordinate;
		this.startingYCoordinate = currentYCoordinate;
		this.xCoordinateDestination = xCoordinateDestination;
		this.yCoordinateDestination = yCoordinateDestination;
		this.moduloDivider = angleModulo;
		this.stepsToTake = stepsToTake;
		this.stepSize = stepSize;
		this.fallbackDirection = fallbackDirection;
		this.setHomingDirection();
	}

	private void setHomingDirection() {
		boolean canConcat = false;
		if (startingXCoordinate >= xCoordinateDestination && fallbackDirection.contains("Left")) {
			pathDirection = "Left";
			canConcat = true;
		} else if (startingXCoordinate < xCoordinateDestination && fallbackDirection.contains("Right")) {
			pathDirection = "Right";
			canConcat = true;
		}

		if (startingYCoordinate >= yCoordinateDestination && canConcat) {
			pathDirection = pathDirection.concat("Up");
		} else if (startingYCoordinate < yCoordinateDestination && canConcat) {
			pathDirection = pathDirection.concat("Down");
		}
		
		if(!canConcat){
			pathDirection = fallbackDirection;
		}
	}

	public void updateHomingDestination(int xCoordinate, int yCoordinate, int currentXCoordinate,
			int currentYCoordinate) {
		this.xCoordinateDestination = xCoordinate;
		this.yCoordinateDestination = yCoordinate;
		boolean passedPlayer = false;

		int xDistance = Math.abs((xCoordinateDestination - currentXCoordinate));
		int yDistance = Math.abs((yCoordinateDestination - currentYCoordinate));
		
		//Hier checken of de missile al voorbij de speler is
		if(xCoordinateDestination - currentXCoordinate > 0 && fallbackDirection.contains("Left")) {
			this.pathDirection = fallbackDirection;
			passedPlayer = true;
		}
		
		if(xCoordinateDestination - currentXCoordinate < 0 && fallbackDirection.contains("Right")) {
			this.pathDirection = fallbackDirection;
			passedPlayer = true;
		}
		
		if(passedPlayer) {
			return;
		}
		
		if (xDistance < 150 && yDistance < 150) {
			this.pathDirection = fallbackDirection;
		} else setHomingDirection();
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
		if (isYStepAllowed()) {
			switch (direction) {
			case ("Up"):
				newYCoordinate -= stepSize;
				break;
			case ("Down"):
				newYCoordinate += stepSize;
				break;
			}
		}
		return newYCoordinate;
	}

	// Called when coordinates are returned
	private void increaseStepTaken() {
		this.stepsTaken++;
	}

	// Returns wether the path has been completed
	public boolean isPathWalked() {
		if(pathType.equals("Homing")) {
			return false;
		}
		return stepsTaken >= stepsToTake;
	}

	// Called when enemy needs to change speed, for the board block system
	public void setMovementSpeed(int movementSpeed) {
		this.stepSize = movementSpeed;
	}

	public String getPathType() {
		return this.pathType;
	}

	// Use to determine what amount of StepsTaken needs to be taken before another
	// step can be added to the Y coordinate
	// Use modulo
	private boolean isYStepAllowed() {
		if (moduloDivider == 0) {
			return true;
		}
		if (stepsTaken % moduloDivider == 0) {
			return true;
		}
		return false;
	}

}
