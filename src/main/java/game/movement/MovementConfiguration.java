package game.movement;

import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;

public class MovementConfiguration {

	private Direction rotation;
	private Point currentLocation;
	private Point destination;
	private PathFinder pathFinder;
	private Path currentPath;
	private int stepsTaken;
	private int XMovementSpeed;
	private int YMovementSpeed;
	private int lastUsedXMovementSpeed;
	private int lastUsedYMovementSpeed;
	private int currentBoardBlock;
	private int lastKnownTargetX;
	private int lastKnownTargetY;
	
	protected boolean hasLock;
	private Point nextPoint;
	private GameObject target;
	
	
	public MovementConfiguration() {
	}


	public Point getCurrentLocation() {
		return currentLocation;
	}


	public void setCurrentLocation(Point currentLocation) {
		this.currentLocation = currentLocation;
	}


	public Point getDestination() {
		return destination;
	}


	public void setDestination(Point destination) {
		this.destination = destination;
	}


	public PathFinder getPathFinder() {
		return pathFinder;
	}


	public void setPathFinder(PathFinder pathFinder) {
		this.pathFinder = pathFinder;
	}

	public Path getCurrentPath() {
		return currentPath;
	}


	public void setCurrentPath(Path currentPath) {
		this.currentPath = currentPath;
	}


	public int getXMovementSpeed() {
		return XMovementSpeed;
	}


	public void setXMovementSpeed(int xMovementSpeed) {
		XMovementSpeed = xMovementSpeed;
	}


	public int getYMovementSpeed() {
		return YMovementSpeed;
	}


	public void setYMovementSpeed(int yMovementSpeed) {
		YMovementSpeed = yMovementSpeed;
	}


	public int getLastUsedXMovementSpeed() {
		return lastUsedXMovementSpeed;
	}


	public void setLastUsedXMovementSpeed(int lastUsedXMovementSpeed) {
		this.lastUsedXMovementSpeed = lastUsedXMovementSpeed;
	}


	public int getLastUsedYMovementSpeed() {
		return lastUsedYMovementSpeed;
	}


	public void setLastUsedYMovementSpeed(int lastUsedYMovementSpeed) {
		this.lastUsedYMovementSpeed = lastUsedYMovementSpeed;
	}


	public int getCurrentBoardBlock() {
		return currentBoardBlock;
	}


	public void setCurrentBoardBlock(int currentBoardBlock) {
		this.currentBoardBlock = currentBoardBlock;
	}


	public int getLastKnownTargetX() {
		return lastKnownTargetX;
	}


	public void setLastKnownTargetX(int lastKnownTargetX) {
		this.lastKnownTargetX = lastKnownTargetX;
	}


	public int getLastKnownTargetY() {
		return lastKnownTargetY;
	}


	public void setLastKnownTargetY(int lastKnownTargetY) {
		this.lastKnownTargetY = lastKnownTargetY;
	}


	public Direction getRotation() {
		return rotation;
	}


	public void setRotation(Direction rotation) {
		this.rotation = rotation;
	}
	
	
	
	public void setStepsTaken(int stepAmount) {
		this.stepsTaken = stepAmount;
	}
	
	public int getStepsTaken() {
		return this.stepsTaken;
	}


	public boolean hasLock() {
		return hasLock;
	}


	public void setHasLock(boolean hasLock) {
		this.hasLock = hasLock;
	}


	public Point getNextPoint() {
		return nextPoint;
	}


	public void setNextPoint(Point nextPoint) {
		this.nextPoint = nextPoint;
	}


	public GameObject getTarget() {
		return target;
	}


	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	
}
