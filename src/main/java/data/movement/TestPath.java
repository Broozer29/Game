//package data.movement;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestPath {
//
//	private String pathType;
//	private String pathDirection;
//
//	// Required for angled & straight lined directions
//	private int moduloDivider;
//	private int stepsToTake;
//	private int stepsTaken;
//	private int stepSize;
//
//	// Required for homing missiles
//	private int startingXCoordinate;
//	private int startingYCoordinate;
//	private int xCoordinateDestination;
//	private int yCoordinateDestination;
//	private String fallbackDirection;
//
//	public TestPath(String pathDirection, int stepsToTake, int stepSize, int moduloDivider) {
//		this.pathDirection = pathDirection;
//		this.stepsToTake = stepsToTake;
//		this.stepSize = stepSize;
//		this.moduloDivider = moduloDivider;
//		this.pathType = "Regular";
//	}
//
//	public TestPath(int currentXCoordinate, int currentYCoordinate, int stepsToTake, int stepSize,
//			int xCoordinateDestination, int yCoordinateDestination, int angleModulo, String fallbackDirection) {
//		this.pathType = "Homing";
//		this.startingXCoordinate = currentXCoordinate;
//		this.startingYCoordinate = currentYCoordinate;
//		this.xCoordinateDestination = xCoordinateDestination;
//		this.yCoordinateDestination = yCoordinateDestination;
//		this.moduloDivider = angleModulo;
//		this.stepsToTake = stepsToTake;
//		this.stepSize = stepSize;
//		this.fallbackDirection = fallbackDirection;
//		this.setHomingDirection();
//	}
//
//
//}
