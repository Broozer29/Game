package net.riezebos.bruus.tbd.game.movement;

import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;

public class MovementConfiguration {

	private Direction rotation;
	private Point currentLocation;
	private Point destination;
	private PathFinder pathFinder;
	private Path currentPath;
	private int stepsTaken;
	private float movementSpeed;
    private float originalMovementSpeed;
	private float lastUsedMovementSpeed;
	private int currentBoardBlock;
	private int lastKnownTargetX;
	private int lastKnownTargetY;

	//For the diamond shape pathfinder
	private double orbitRadius;

	//For the hover pathfinder
	private int boardBlockToHoverIn;
	private float movementSpeedModifier = 1.0f;


	public MovementConfiguration() {

	}

	public void resetMovementPath(){
		this.currentPath = null;
		this.destination = null;
		this.currentLocation = null;
	}

	public void initDefaultSettingsForSpecializedPathFinders(){
		//Hover
		setBoardBlockToHoverIn(6);

		//orbit
		setOrbitRadius(85);
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


	public float getMovementSpeed() {
		return movementSpeed * movementSpeedModifier;
	}


	public void setMovementSpeed(float xMovementSpeed) {
		movementSpeed = xMovementSpeed;
	}



    public float getOriginalMovementSpeed() {
        return originalMovementSpeed;
    }

    public void setOriginalMovementSpeed(float originalMovementSpeed) {
        this.originalMovementSpeed = originalMovementSpeed;
    }


	public float getLastUsedMovementSpeed() {
		return lastUsedMovementSpeed;
	}


	public void setLastUsedMovementSpeed(float lastUsedMovementSpeed) {
		this.lastUsedMovementSpeed = lastUsedMovementSpeed;
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


	public void setDirection(Direction rotation) {
		this.rotation = rotation;
	}



	public void setStepsTaken(int stepAmount) {
		this.stepsTaken = stepAmount;
	}
	
	public int getStepsTaken() {
		return this.stepsTaken;
	}

	public void deleteConfiguration(){
		this.pathFinder = null;
		this.currentPath = null;
	}

	public double getOrbitRadius () {
		return orbitRadius;
	}

	public void setOrbitRadius (double orbitRadius) {
		this.orbitRadius = orbitRadius;
	}

	public int getBoardBlockToHoverIn () {
		return boardBlockToHoverIn;
	}

	public void setBoardBlockToHoverIn (int boardBlockToHoverIn) {
		this.boardBlockToHoverIn = boardBlockToHoverIn;
	}

	public float getMovementSpeedModifier () {
		return movementSpeedModifier;
	}

	public void modifyMovementSpeedModifier (float movementSpeedModifier) {
		this.movementSpeedModifier += movementSpeedModifier;
	}
}
