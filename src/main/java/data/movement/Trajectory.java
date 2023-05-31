package data.movement;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import game.managers.FriendlyManager;
import game.objects.enemies.Enemy;
import game.objects.missiles.Missile;

public class Trajectory {

	protected List<Path> pathList = new ArrayList<Path>();
	protected Path currentPath;

	protected String trajectoryDirection;
	protected String trajectoryType;
	protected int stepSize;
	protected boolean infiniteMovement;
	protected int angleModuloSize;
	protected boolean friendly;
	protected int startingXCoordinate;
	protected int startingYCoordinate;
	protected int currentXCoordinate;
	protected int currentYCoordinate;
	protected int totalDistance;
	protected boolean lostTargetLock;

	public Trajectory(String trajectoryDirection, String trajectoryType, int stepSize, boolean infiniteMovement,
			int angleModuloSize) {
		this.trajectoryDirection = trajectoryDirection;
		this.trajectoryType = trajectoryType;
		this.stepSize = stepSize;
		this.infiniteMovement = infiniteMovement;
		this.angleModuloSize = angleModuloSize;
	}

	// Adds the path to the list of paths
	protected void addPath(Path path) {
		this.pathList.add(path);
	}

	// Sets the current path to the next item in the pathList.
	protected void setPath() {
		if (pathList.size() > 0) {
			currentPath = pathList.get(0);
		}

	}

	// Called every move() loop, returns the new X & Y coordinates for the object
	// retrieved from Path, based on Path's direction
	public List<Integer> getPathCoordinates(int xCoordinate, int yCoordinate) {
		List<Integer> coordinatesList = currentPath.getNewCoordinates(xCoordinate, yCoordinate);

		if (trajectoryType.equals("Homing")) {
			updateHomingCoordinates(coordinatesList);
		}
		return coordinatesList;

	}

	// Needed for homing trajectories only
	private void updateHomingCoordinates(List<Integer> coordinatesList) {
		this.currentXCoordinate = coordinatesList.get(0);
		this.currentYCoordinate = coordinatesList.get(1);
	}

	public void updateRegularPath() {
		for (int i = 0; i < pathList.size(); i++) {
			if (pathList.get(i).isPathWalked()) {
				pathList.remove(i);
				setPath();
			}
		}
	}

	public void updateEnemyHomingPaths(Enemy enemy) {
		Rectangle friendlyBounds = FriendlyManager.getInstance().getSpaceship().getHomingRangeBounds();
		Rectangle enemyBounds = enemy.getBounds();
		pathList.get(0).setHomingPathDirection(friendlyBounds, enemyBounds);
	}

	public void updateMissileHomingPaths(Missile missile) {
		Rectangle friendlyBounds = FriendlyManager.getInstance().getSpaceship().getHomingRangeBounds();
		Rectangle missileBounds = missile.getBounds();
		
		if(missile.getAnimation()!= null) {
			missileBounds = missile.getAnimation().getBounds();
		}
		
		if (!lostTargetLock) {
			lostTargetLock = pathList.get(0).withinHomingRectangle(friendlyBounds, missileBounds);
		}
		
		pathList.get(0).setHomingPathDirection(friendlyBounds, missileBounds);
	}

	public void updateMovementSpeed(int stepSize) {
		this.stepSize = stepSize;
	}

	public String getTrajectoryType() {
		return this.trajectoryType;
	}

}
