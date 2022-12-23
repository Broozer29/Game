package data.movement;

import java.util.ArrayList;
import java.util.List;

import data.DataClass;
import game.objects.enemies.Alien;
import game.objects.enemies.Enemy;
import game.objects.missiles.AlienLaserbeam;
import game.objects.missiles.DefaultPlayerLaserbeam;
import game.objects.missiles.Missile;
import game.objects.missiles.SeekerProjectile;

public class Trajectory {

	private List<Path> pathList = new ArrayList<Path>();
	private Path currentPath;
	private PathFactory pathFactory = PathFactory.getInstance();

	// enemyDirection, enemyTotalDistance, enemyMovementSpeed
	// missileDirection, missileLength, missileMovementSpeed, missileAngleSize
	private String trajectoryDirection;
	private int totalDistance;
	private int movementSpeed;
	private int angleModuloSize;
	private boolean infiniteMovement;


	public Trajectory(String trajectoryDirection, int totalDistance, int movementSpeed, int angleModuloSize, boolean infiniteMovement) {
		this.trajectoryDirection = trajectoryDirection;
		this.totalDistance = totalDistance;
		this.movementSpeed = movementSpeed;
		this.angleModuloSize = angleModuloSize;
		
		createPath();
	}

	//Creates a path based on the given parameters in the constructor.
	public void createPath() {
		Path newPath = null;
		if (trajectoryDirection.equals("Left") || trajectoryDirection.equals("Right")
				|| trajectoryDirection.equals("Up") || trajectoryDirection.equals("Down")) {
			newPath = pathFactory.getStraightLine(trajectoryDirection, totalDistance, movementSpeed);
			
		} else if (trajectoryDirection.equals("LeftUp") || trajectoryDirection.equals("RightUp")
				|| trajectoryDirection.equals("LeftDown") || trajectoryDirection.equals("RightDown")) {
			newPath = pathFactory.getAngledLine(trajectoryDirection, totalDistance, movementSpeed, angleModuloSize);
		}
		if(newPath != null) {
			addPath(newPath);
			setCurrentPath();
		} else System.out.println("I couldn't create a path for a missile or enemy!");

	}


	// Adds the path to the list of paths
	private void addPath(Path path) {
		this.pathList.add(path);
	}

	// Removes paths that have finished playing out and resets the current path that
	// needs to be followed.
	private void removeFinishedPaths() {
		for (int i = 0; i < pathList.size(); i++) {
			if (pathList.get(i).isPathWalked()) {
				pathList.remove(i);
				setCurrentPath();
			}
		}
	}

	// Sets the current path to the next item in the pathList.
	private void setCurrentPath() {
		if (pathList.size() > 0) {
			currentPath = pathList.get(0);
		}
		// Creates a brand new path based on the type if the sprite becomes stationary
		else if (pathList.size() == 0) {
			if(infiniteMovement) {
				setCurrentPath();
			}
		}
	}

	// Called every move() loop, returns the new X & Y coordinates for the object
	// retrieved from Path, based on Path's direction
	public List<Integer> getPathCoordinates(int xCoordinate, int yCoordinate) {
		List<Integer> coordinatesList = new ArrayList<Integer>();
		coordinatesList = currentPath.getNewCoordinates(xCoordinate, yCoordinate);
		removeFinishedPaths();
		return coordinatesList;

	}

	// Updates the movement speed of enemies when they change board blocks
	public void updateMovementSpeed(int movementSpeed) {
		currentPath.setMovementSpeed(movementSpeed);
	}

}
