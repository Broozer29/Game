package data.movement;

import java.util.ArrayList;
import java.util.List;

import data.DataClass;
import game.managers.PathFactory;
import game.objects.Enemy;

public class Trajectory {

	private List<Path> pathList = new ArrayList<Path>();
	private Path currentPath;
	private PathFactory pathFactory = PathFactory.getInstance();
	private DataClass dataClass = DataClass.getInstance();

	public Trajectory() {
	}

	/*
	 * To-do lijst: - Pre-generated paths maken. Voor rechts naar links, boven naar
	 * beneden en onder naar boven. (Gebruik window width/height) - Het juiste
	 * trajectory (combinatie van paden) kiezen op basis van enemy type -
	 * Enemy.move() aansluiten zodat het de nieuwe coordinaten van een pad
	 * terugkrijgt. (getPathCoordinates)
	 * 
	 */

	public void setTrajectoryType(Enemy enemy) {
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

	// Gets additional path length to ensure the enemy doesn't stop dead in its
	// tracks.
	private int getAdditionalXSteps(Enemy enemy) {
		switch (enemy.getEnemyType()) {
		case ("Default Alien Spaceship"):
			return Math.abs(dataClass.getWindowWidth() - enemy.getXCoordinate());
		}
		return 0;
	}

	// Gets additional path length to ensure the enemy doesn't stop dead in its
	// tracks.
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

	// Should this be called every frame? As it stands, needs to be called on every
	// enemyManager.updateGameTick();
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

	public void updateMovementSpeed(int movementSpeed) {
		currentPath.setMovementSpeed(movementSpeed);
	}

}
