package data.movement;

import java.util.ArrayList;
import java.util.List;

import data.DataClass;
import game.objects.enemies.Enemy;
import game.objects.missiles.AlienLaserbeam;
import game.objects.missiles.DefaultPlayerLaserbeam;
import game.objects.missiles.Missile;

public class Trajectory {

	private List<Path> pathList = new ArrayList<Path>();
	private Path currentPath;
	private PathFactory pathFactory = PathFactory.getInstance();
	private DataClass dataClass = DataClass.getInstance();

	public Trajectory() {
	}

	// Creates a trajectory & correspondent path based on enemy type
	public void setEnemyTrajectoryType(Enemy enemy) {
		Path newPath = null;

		switch (enemy.getEnemyType()) {
		case ("Default Alien Spaceship"):
			newPath = pathFactory.getStraightLine(enemy.getEnemyDirection(),
					dataClass.getWindowWidth() + getAdditionalXSteps(enemy), enemy.getMovementSpeed());
			addPath(newPath);
		case ("Alien Bomb"):
			newPath = pathFactory.getStraightLine(enemy.getEnemyDirection(),
					dataClass.getWindowHeight() + getAdditionalYSteps(enemy), enemy.getMovementSpeed());
			addPath(newPath);
		}
		setCurrentPath();
	}

	public void setMissileTrajectoryType(Missile missile) {
		Path newPath = null;

		if (missile instanceof AlienLaserbeam) {
			newPath = pathFactory.getStraightLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed());
			addPath(newPath);
		} else if (missile instanceof DefaultPlayerLaserbeam) {
			newPath = pathFactory.getStraightLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed());
			addPath(newPath);
		}

		setCurrentPath();
	}

	// Gets additional path length to ensure the enemy doesn't stop dead in its
	// tracks halfway the screen.
	private int getAdditionalXSteps(Enemy enemy) {
		switch (enemy.getEnemyType()) {
		case ("Default Alien Spaceship"):
			return Math.abs(dataClass.getWindowWidth() - enemy.getXCoordinate());
		}
		return 0;
	}

	// Gets additional path length to ensure the enemy doesn't stop dead in its
	// tracks halfway the screen.
	private int getAdditionalYSteps(Enemy enemy) {
		switch (enemy.getEnemyType()) {
		case ("Alien Bomb"):
			switch (enemy.getEnemyDirection()) {
			case ("Up"):
				return Math.abs(dataClass.getWindowHeight() - enemy.getYCoordinate());
			case ("Down"):
				return Math.abs(dataClass.getWindowHeight() + Math.abs(enemy.getYCoordinate()));
			}
		}
		return 0;
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
	public void setCurrentPath() {
		if (pathList.size() > 0) {
			currentPath = pathList.get(0);
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
