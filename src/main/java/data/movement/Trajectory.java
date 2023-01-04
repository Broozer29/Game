package data.movement;

import java.util.ArrayList;
import java.util.List;

import game.managers.FriendlyManager;

public class Trajectory {

	private List<Path> pathList = new ArrayList<Path>();
	private Path currentPath;
	private PathFactory pathFactory = PathFactory.getInstance();
	private FriendlyManager friendlyManager = FriendlyManager.getInstance();

	// Needed for both homing and regular trajectories
	private String trajectoryDirection;
	private String trajectoryType;
	private int stepSize;
	private boolean infiniteMovement;
	private int angleModuloSize;

	// Needed for regular trajectories
	private int totalDistance;


	// Needed for homing trajectories
	private boolean friendly;
	private int startingXCoordinate;
	private int startingYCoordinate;
	private int currentXCoordinate;
	private int currentYCoordinate;

	public Trajectory() {

	}

	public void createRegularTrajectory(String trajectoryDirection, int totalDistance, int stepSize,
			int angleModuloSize, boolean infiniteMovement, boolean friendly) {
		this.trajectoryDirection = trajectoryDirection;
		this.totalDistance = totalDistance;
		this.stepSize = stepSize;
		this.angleModuloSize = angleModuloSize;
		this.infiniteMovement = infiniteMovement;
		this.trajectoryType = "Regular";
		createPath();
	}

	public void createHomingTrajectory(int currentXCoordinate, int currentYCoordinate, int stepSize,
			boolean friendly, String fallbackDirection, int angleModulo) {
		this.currentXCoordinate = currentXCoordinate;
		this.currentYCoordinate = currentYCoordinate;
		this.startingXCoordinate = currentXCoordinate;
		this.startingYCoordinate = currentYCoordinate;
		this.angleModuloSize = angleModulo;
		this.friendly = friendly;
		this.stepSize = stepSize;
		this.trajectoryType = "Homing";
		this.infiniteMovement = true;
		this.trajectoryDirection = fallbackDirection;
		createPath();
	}

	// Creates a path based on the trajectory type
	private void createPath() {
		if (trajectoryType.equals("Regular")) {
			createRegularPath();
		}

		if (trajectoryType.equals("Homing")) {
			createHomingPath();
		}
	}

	// Called when needed to create a homing trajectory
	private void createHomingPath() {

		// Er zou niet steeds opnieuw een pad gemaakt moeten worden. Een homing missile
		// zou een eindeloos pad moeten zijn dat een fallback heeft na X stappen
		// of als het te dichtbij de speler komt
		List<Integer> destinationCoordinatesList = friendlyManager.getNearestFriendlyHomingCoordinates();
		int xCoordinateDestination = destinationCoordinatesList.get(0);
		int yCoordinateDestination = destinationCoordinatesList.get(1);
		
		Path newPath = pathFactory.getHomingPath(currentXCoordinate, currentYCoordinate, stepSize, friendly,
				trajectoryDirection, angleModuloSize, xCoordinateDestination, yCoordinateDestination);
		addPath(newPath);
		setCurrentPath();
	}

	// Called when needed to create a straight or angled line trajectory
	private void createRegularPath() {
		Path newPath = null;
		if (trajectoryDirection.equals("Left") || trajectoryDirection.equals("Right")
				|| trajectoryDirection.equals("Up") || trajectoryDirection.equals("Down")) {
			newPath = pathFactory.getStraightLine(trajectoryDirection, totalDistance, stepSize);

		} else if (trajectoryDirection.equals("LeftUp") || trajectoryDirection.equals("RightUp")
				|| trajectoryDirection.equals("LeftDown") || trajectoryDirection.equals("RightDown")) {
			newPath = pathFactory.getAngledLine(trajectoryDirection, totalDistance, stepSize, angleModuloSize);
		}
		if (newPath != null) {
			addPath(newPath);
			setCurrentPath();
		} else
			System.out.println("I couldn't create a path for a missile or enemy!");
	}

	// Adds the path to the list of paths
	private void addPath(Path path) {
		this.pathList.add(path);
	}

	// Removes paths that have finished playing out and resets the current path that
	// needs to be followed.
	private void removeFinishedPaths() {
		for (int i = 0; i < pathList.size(); i++) {
			if (pathList.get(i).isPathWalked() && pathList.get(i).getPathType().equals("Regular")) {
				pathList.remove(i);
				setCurrentPath();
			} else if (pathList.get(i).getPathType().equals("Homing")) {
				List<Integer> coords = FriendlyManager.getInstance().getNearestFriendlyHomingCoordinates();
				pathList.get(i).updateHomingDestination(coords.get(0), coords.get(1), currentXCoordinate, currentYCoordinate);
			}
		}
	}

	// Sets the current path to the next item in the pathList.
	private void setCurrentPath() {
		if (pathList.size() > 0) {
			currentPath = pathList.get(0);
		}

	}

	// Called every move() loop, returns the new X & Y coordinates for the object
	// retrieved from Path, based on Path's direction
	public List<Integer> getPathCoordinates(int xCoordinate, int yCoordinate) {
		List<Integer> coordinatesList = new ArrayList<Integer>();
		coordinatesList = currentPath.getNewCoordinates(xCoordinate, yCoordinate);

		if (trajectoryType.equals("Homing")) {
			updateHomingCoordinates(coordinatesList);
		}
		removeFinishedPaths();
		return coordinatesList;

	}

	// Needed for homing trajectories only
	private void updateHomingCoordinates(List<Integer> coordinatesList) {
		this.currentXCoordinate = coordinatesList.get(0);
		this.currentYCoordinate = coordinatesList.get(1);
	}

	// Updates the movement speed of enemies when they change board blocks
	public void updateMovementSpeed(int movementSpeed) {
		currentPath.setMovementSpeed(movementSpeed);
	}

}
