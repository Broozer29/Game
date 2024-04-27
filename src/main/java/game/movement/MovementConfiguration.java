package game.movement;

import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

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

	//For the spiral pathfinder
	private int curveDistance;
	private double angleStep;
	private double radius;
	private double radiusIncrement;

	//

	//For the Triangle pathfinder:
	private int primaryDirectionStepAmount;
	private int firstDiagonalDirectionStepAmount;
	private int secondDiagonalDirectionStepAmount;

	private List<GameObject> untrackableObjects = new ArrayList<GameObject>();

	//For the hover pathfinder
	private int boardBlockToHoverIn;

	private MovementPatternSize patternSize;


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
			setDiamondWidth(patternSize.getDiamondWidth());
			setDiamondHeight(patternSize.getDiamondHeight());
			setStepsBeforeBounceInOtherDirection(patternSize.getStepsBeforeBounceInOtherDirection());

			setAngleStep(0.1);
			setCurveDistance(1);
			setRadius(5);
			setRadiusIncrement(patternSize.getRadiusIncrement());

			setPrimaryDirectionStepAmount(patternSize.getPrimaryDirectionStepAmount());
			setFirstDiagonalDirectionStepAmount(patternSize.getSecondaryDirectionStepAmount());
			setSecondDiagonalDirectionStepAmount(patternSize.getSecondaryDirectionStepAmount());

			setBoardBlockToHoverIn(6);
			setHasLock(true);
		}
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

	public double getRadius () {
		return radius;
	}

	public void setRadius (double radius) {
		this.radius = radius;
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
