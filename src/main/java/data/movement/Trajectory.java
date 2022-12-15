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
	private DataClass dataClass = DataClass.getInstance();
	private Enemy enemy = null;
	private Missile missile = null;

	public Trajectory() {
	}

	// Creates a trajectory & correspondent path based on enemy type
	public void setEnemyTrajectoryType(Enemy enemy) {
		Path newPath = null;
		this.enemy = enemy;
		switch (enemy.getEnemyDirection()) {
		case ("Left"):
			newPath = pathFactory.getStraightLine(enemy.getEnemyDirection(),
					dataClass.getWindowWidth() + enemy.getAdditionalXSteps(), enemy.getMovementSpeed());
			addPath(newPath);
			break;
		case ("Right"):
			newPath = pathFactory.getStraightLine(enemy.getEnemyDirection(),
					dataClass.getWindowWidth() + enemy.getAdditionalXSteps(), enemy.getMovementSpeed());
			addPath(newPath);
			break;
		case ("Up"):
			newPath = pathFactory.getStraightLine(enemy.getEnemyDirection(),
					dataClass.getWindowHeight() + enemy.getAdditionalYSteps(), enemy.getMovementSpeed());
			addPath(newPath);
			break;
		case ("Down"):
			newPath = pathFactory.getStraightLine(enemy.getEnemyDirection(),
					dataClass.getWindowHeight() + enemy.getAdditionalYSteps(), enemy.getMovementSpeed());
			addPath(newPath);
			break;
		case ("LeftUp"):
			newPath = pathFactory.getAngledLine(enemy.getEnemyDirection(),
					dataClass.getWindowWidth() + enemy.getAdditionalXSteps(), enemy.getMovementSpeed(), enemy.getAngleModuloDivider());
			addPath(newPath);
			break;
		case ("LeftDown"):
			newPath = pathFactory.getAngledLine(enemy.getEnemyDirection(),
					dataClass.getWindowWidth() + enemy.getAdditionalXSteps(), enemy.getMovementSpeed(), enemy.getAngleModuloDivider());
			addPath(newPath);
			break;
		case ("RightUp"):
			newPath = pathFactory.getAngledLine(enemy.getEnemyDirection(),
					dataClass.getWindowWidth() + enemy.getAdditionalXSteps(), enemy.getMovementSpeed(), enemy.getAngleModuloDivider());
			addPath(newPath);
			break;
		case ("RightDown"):
			newPath = pathFactory.getAngledLine(enemy.getEnemyDirection(),
					dataClass.getWindowWidth() + enemy.getAdditionalXSteps(), enemy.getMovementSpeed(), enemy.getAngleModuloDivider());
			addPath(newPath);
			break;
		}
		setCurrentPath();
	}

	public void setMissileTrajectoryType(Missile missile) {
		Path newPath = null;
		this.missile = missile;
		switch (missile.getMissileDirection()) {
		case ("Left"):
			newPath = pathFactory.getStraightLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed());
			addPath(newPath);
			break;
		case ("Right"):
			newPath = pathFactory.getStraightLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed());
			addPath(newPath);
			break;
		case ("Up"):
			newPath = pathFactory.getStraightLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed());
			addPath(newPath);
			break;
		case ("Down"):
			newPath = pathFactory.getStraightLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed());
			addPath(newPath);
			break;
		case ("LeftUp"):
			newPath = pathFactory.getAngledLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed(), missile.getAngleSize());
			addPath(newPath);
			break;
		case ("LeftDown"):
			newPath = pathFactory.getAngledLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed(), missile.getAngleSize());
			addPath(newPath);
			break;
		case ("RightUp"):
			newPath = pathFactory.getAngledLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed(), missile.getAngleSize());
			addPath(newPath);
			break;
		case ("RightDown"):
			newPath = pathFactory.getAngledLine(missile.getMissileDirection(), missile.getMaxMissileLength(),
					missile.getMissileMovementSpeed(), missile.getAngleSize());
			addPath(newPath);
			break;
		}
		setCurrentPath();
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
			if (missile != null) {
				setMissileTrajectoryType(missile);
			} else if (enemy != null) {
				setEnemyTrajectoryType(enemy);
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
