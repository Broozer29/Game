package game.movement;

import game.movement.pathfinders.PathFinder;
import game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class MovementConfiguration {

	private Direction rotation;
	private Point currentLocation;
	private Point destination;
	private PathFinder pathFinder;
	private Path currentPath;
	private int stepsTaken;
	private float XMovementSpeed;
	private float YMovementSpeed;
	private float lastUsedXMovementSpeed;
	private float lastUsedYMovementSpeed;
	private int currentBoardBlock;
	private int lastKnownTargetX;
	private int lastKnownTargetY;

	//For the diamond shape pathfinder
	private int diamondWidth;
	private int diamondHeight;
	//
	
	protected boolean hasLock;
	private Point nextPoint;
	private GameObject targetToChase;

	//For the zigzag pathfinder
	private int stepsBeforeBounceInOtherDirection;
	//

	//For the spiral / Orbit pathfinder
	private int curveDistance;
	private double angleStep;
	private double spiralRadius;
	private double radiusIncrement;

	//For orbit
	private double orbitRadius;

	//For the Triangle pathfinder:
	private int primaryDirectionStepAmount;
	private int firstDiagonalDirectionStepAmount;
	private int secondDiagonalDirectionStepAmount;

	private List<GameObject> untrackableObjects = new ArrayList<GameObject>();

	//For the hover pathfinder
	private int boardBlockToHoverIn;

	private MovementPatternSize patternSize;
	private int orbitSpeed;


	public MovementConfiguration() {

	}

	public void resetMovementPath(){
		this.currentPath = null;
		this.nextPoint = null;
		this.destination = null;
		this.currentLocation = null;
	}

	public void initDefaultSettingsForSpecializedPathFinders(){
		if(patternSize != null) {
			//Diamnond
			setDiamondWidth(patternSize.getDiamondWidth());
			setDiamondHeight(patternSize.getDiamondHeight());
			setStepsBeforeBounceInOtherDirection(patternSize.getStepsBeforeBounceInOtherDirection());

			//Spiral
			setRadiusIncrement(patternSize.getRadiusIncrement());

			//Triangle
			setPrimaryDirectionStepAmount(patternSize.getPrimaryDirectionStepAmount());
			setFirstDiagonalDirectionStepAmount(patternSize.getSecondaryDirectionStepAmount());
			setSecondDiagonalDirectionStepAmount(patternSize.getSecondaryDirectionStepAmount());


		}
		//spiral
		setAngleStep(0.1);
		setCurveDistance(1);
		setSpiralRadius(5);


		//Hover
		setBoardBlockToHoverIn(6);
		setHasLock(true);

		//orbit
		setOrbitRadius(85);
		setOrbitSpeed(300);
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


	public float getXMovementSpeed() {
		return XMovementSpeed;
	}


	public void setXMovementSpeed(float xMovementSpeed) {
		XMovementSpeed = xMovementSpeed;
	}


	public float getYMovementSpeed() {
		return YMovementSpeed;
	}


	public void setYMovementSpeed(float yMovementSpeed) {
		YMovementSpeed = yMovementSpeed;
	}


	public float getLastUsedXMovementSpeed() {
		return lastUsedXMovementSpeed;
	}


	public void setLastUsedXMovementSpeed(float lastUsedXMovementSpeed) {
		this.lastUsedXMovementSpeed = lastUsedXMovementSpeed;
	}


	public float getLastUsedYMovementSpeed() {
		return lastUsedYMovementSpeed;
	}


	public void setLastUsedYMovementSpeed(float lastUsedYMovementSpeed) {
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


	public GameObject getTargetToChase () {
		return targetToChase;
	}


	public void setTargetToChase (GameObject targetToChase) {
		this.targetToChase = targetToChase;
	}

	public int getDiamondWidth () {
		return diamondWidth;
	}

	public void setDiamondWidth (int diamondWidth) {
		this.diamondWidth = diamondWidth;
	}

	public int getDiamondHeight () {
		return diamondHeight;
	}

	public void setDiamondHeight (int diamondHeight) {
		this.diamondHeight = diamondHeight;
	}

	public boolean isHasLock () {
		return hasLock;
	}

	public int getStepsBeforeBounceInOtherDirection () {
		return stepsBeforeBounceInOtherDirection;
	}

	public void setStepsBeforeBounceInOtherDirection (int stepsBeforeBounceInOtherDirection) {
		this.stepsBeforeBounceInOtherDirection = stepsBeforeBounceInOtherDirection;
	}

	public int getCurveDistance () {
		return curveDistance;
	}

	//KEEP THIS SMALL, IT'S THE MOVEMENT SPEED
	public void setCurveDistance (int curveDistance) {
		this.curveDistance = curveDistance;
	}

	public double getAngleStep () {
		return angleStep;
	}

	public void setAngleStep (double angleStep) {
		this.angleStep = angleStep;
	}

	public double getSpiralRadius () {
		return spiralRadius;
	}

	public void setSpiralRadius (double spiralRadius) {
		this.spiralRadius = spiralRadius;
	}

	public double getRadiusIncrement () {
		return radiusIncrement;
	}

	public void setRadiusIncrement (double radiusIncrement) {
		this.radiusIncrement = radiusIncrement;
	}

	public int getPrimaryDirectionStepAmount () {
		return primaryDirectionStepAmount;
	}

	public void setPrimaryDirectionStepAmount (int primaryDirectionStepAmount) {
		this.primaryDirectionStepAmount = primaryDirectionStepAmount;
	}

	public int getFirstDiagonalDirectionStepAmount () {
		return firstDiagonalDirectionStepAmount;
	}

	public void setFirstDiagonalDirectionStepAmount (int firstDiagonalDirectionStepAmount) {
		this.firstDiagonalDirectionStepAmount = firstDiagonalDirectionStepAmount;
	}

	public int getSecondDiagonalDirectionStepAmount () {
		return secondDiagonalDirectionStepAmount;
	}

	public void setSecondDiagonalDirectionStepAmount (int secondDiagonalDirectionStepAmount) {
		this.secondDiagonalDirectionStepAmount = secondDiagonalDirectionStepAmount;
	}

	public List<GameObject> getUntrackableObjects () {
		return untrackableObjects;
	}

	public void setUntrackableObjects (List<GameObject> untrackableObjects) {
		this.untrackableObjects = untrackableObjects;
	}

	public void deleteConfiguration(){
		this.targetToChase = null;
		this.pathFinder = null;
		this.currentPath = null;
		this.nextPoint = null;
	}

	public double getOrbitRadius () {
		return orbitRadius;
	}

	public int getOrbitSpeed () {
		return orbitSpeed;
	}

	public void setOrbitSpeed (int orbitSpeed) {
		this.orbitSpeed = orbitSpeed;
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

	public MovementPatternSize getPatternSize () {
		return patternSize;
	}

	public void setPatternSize (MovementPatternSize patternSize) {
		this.patternSize = patternSize;
	}
}
