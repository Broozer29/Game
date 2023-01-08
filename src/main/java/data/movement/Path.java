package data.movement;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Path {
	// Required for both types
	private String pathType;
	private String pathDirection;
	private int stepSize;
	private int moduloDivider;

	// Required for angled & straight lined directions
	private int stepsToTake;
	private int stepsTaken;

	// Required for homing missiles
	private String fallbackDirection;
	private String previousDirection;
	private List<String> allowedFallbackDirections = new ArrayList<String>();
	private boolean hasTargetLock;

	public Path(String pathType, String pathDirection, int stepSize, int moduloDivider) {
		this.pathType = pathType;
		this.pathDirection = pathDirection;
		this.stepSize = stepSize;
		this.moduloDivider = moduloDivider;
	}

	protected void initRegularPath(int stepsToTake) {
		this.stepsToTake = stepsToTake;
		this.stepsTaken = 0;
	}

	protected void initHomingPath(String fallbackstring) {
		this.fallbackDirection = fallbackstring;
		initAllowedFallbackDirections(fallbackstring);
		this.hasTargetLock = true;
	}

	private void initAllowedFallbackDirections(String fallbackDirection) {
		if (fallbackDirection.equals("Left") || fallbackDirection.equals("LeftUp")
				|| fallbackDirection.equals("LeftDown")) {
			allowedFallbackDirections.add("Left");
			allowedFallbackDirections.add("LeftUp");
			allowedFallbackDirections.add("LeftDown");
		}

		else if (fallbackDirection.equals("Right") || fallbackDirection.equals("RightUp")
				|| fallbackDirection.equals("RightDown")) {
			allowedFallbackDirections.add("Right");
			allowedFallbackDirections.add("RightUp");
			allowedFallbackDirections.add("RightDown");
		}

		else if (fallbackDirection.equals("Up")) {
			allowedFallbackDirections.add("Up");
			allowedFallbackDirections.add("RightUp");
			allowedFallbackDirections.add("LeftUp");
		}

		else if (fallbackDirection.equals("Down")) {
			allowedFallbackDirections.add("Down");
			allowedFallbackDirections.add("LeftDown");
			allowedFallbackDirections.add("RightDown");
		}
	}

	// Gets the rectangles of the spaceship homing square and the missile
	// Checks if rectangles intersect, if so, lose target lock, if not, update
	// direction
	public void setHomingPathDirection(Rectangle homingRectangle, Rectangle missileRectangle) {
		int homingRectangleXCoordinate = (int) Math.round(homingRectangle.getCenterX());
		int homingRectangleYCoordinate = (int) Math.round(homingRectangle.getCenterY());

		int missileRectangleXCoordinate = (int) Math.round(missileRectangle.getCenterX());
		int missileRectangleYCoordinate = (int) Math.round(missileRectangle.getCenterY());
		if (!homingRectangle.intersects(missileRectangle) && hasTargetLock) {
			String leftOrRight = "";
			String upOrDown = "";

			if (homingRectangleXCoordinate < missileRectangleXCoordinate) {
				leftOrRight = "Left";
			} else if (homingRectangleXCoordinate > missileRectangleXCoordinate) {
				leftOrRight = "Right";
			}

			if (homingRectangleYCoordinate < missileRectangleYCoordinate) {
				upOrDown = "Up";
			} else if (homingRectangleYCoordinate > missileRectangleYCoordinate) {
				leftOrRight = "Down";
			}

			if (allowedFallbackDirections.contains(pathDirection)) {
				previousDirection = pathDirection;
				pathDirection = leftOrRight + upOrDown;
				fallbackDirection = pathDirection;
			} else
				pathDirection = previousDirection;

			// Check wether or not the missile has already passed the player. Meaning if the
			// homing rectangle is already passed by the missilerectangle
		} else if (homingRectangle.intersects(missileRectangle) && hasTargetLock) {
			pathDirection = fallbackDirection;
			hasTargetLock = false;
		} else if (!hasTargetLock) {
			pathDirection = fallbackDirection;
		}
	}

	public boolean withinHomingRectangle(Rectangle homingRectangle, Rectangle missileRectangle) {
		return (homingRectangle.intersects(missileRectangle));
	}

	// Returns new X & Y coordinates based on the direction
	// Used for "Regular" type directions, where the direction is set once and never
	// changes.
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
		if (pathType.equals("Homing")) {
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
