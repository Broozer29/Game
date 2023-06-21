package game.objects.missiles;

import data.DataClass;
import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.Path;
import game.movement.PathFinder;
import game.movement.Point;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class Missile extends Sprite {

	protected float missileDamage;
	protected int xMovementSpeed;
	protected int yMovementSpeed;
	private int lastUsedXMovementSpeed;
	private int lastUsedYMovementSpeed;
	
	protected SpriteAnimation animation;
	protected SpriteAnimation explosionAnimation;
	protected ImageEnums missileType;
	protected ImageEnums explosionType;
	protected Direction rotation;
	protected int missileStepsTaken;

	private Point currentLocation;
	private Point destination;
	private PathFinder pathFinder;
	private Path currentPath;
	private boolean isFriendly;

	private boolean hasLock;

	public Missile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder, boolean isFriendly) {
		super(x, y, scale);
		this.currentLocation = new Point(x, y);
		this.destination = destination;
		this.explosionType = explosionType;
		this.rotation = rotation;
		this.missileType = missileType;
		this.missileStepsTaken = 0;
		this.pathFinder = pathFinder;
		this.hasLock = true;
		this.isFriendly = isFriendly;
	}

	public void move() {
		if (currentPath == null || currentPath.getWaypoints().isEmpty() || xMovementSpeed != lastUsedXMovementSpeed
				|| yMovementSpeed != lastUsedYMovementSpeed) {
			// calculate a new path if necessary
			currentPath = pathFinder.findPath(currentLocation, destination, xMovementSpeed, yMovementSpeed, rotation, isFriendly);
			lastUsedXMovementSpeed = xMovementSpeed;
			lastUsedYMovementSpeed = yMovementSpeed;
			currentPath.setCurrentLocation(new Point(xCoordinate, yCoordinate));
		}

		// check if the missile has lost lock
		boolean hasPassedPlayerOrNeverHadLock = false;
		
		if (pathFinder.shouldRecalculatePath(currentPath)) {
			hasPassedPlayerOrNeverHadLock = true; // if it should recalculate path, it means it lost lock or never had one
		}
		
		if(hasPassedPlayerOrNeverHadLock) {
			hasLock = false;
		}

		// declare the nextPoint variable here, before the if-else block
		Point nextPoint = null;

		// only calculate next direction and update location if missile still has lock
		if (hasLock) {
			// Get the direction of the next step
			Direction nextStep = pathFinder.getNextStep(currentLocation, currentPath, rotation);
			// Based on the direction, calculate the next point
			nextPoint = calculateNextPoint(currentLocation, nextStep, xMovementSpeed, yMovementSpeed);
			currentPath.setFallbackDirection(nextStep);
		} else {
			// if missile lost lock, it should keep moving in the last direction
			rotation = currentPath.getFallbackDirection();
			nextPoint = calculateNextPoint(currentLocation, currentPath.getFallbackDirection(), xMovementSpeed, yMovementSpeed);
		}

		// Update the current location
		currentLocation = nextPoint;
		this.xCoordinate = nextPoint.getX();
		this.yCoordinate = nextPoint.getY();

		this.missileStepsTaken += 1;
		// if reached the next point, remove it from the path
		if (currentLocation.equals(nextPoint)) {
			currentPath.getWaypoints().remove(0);
		}

		if (animation != null) {
			updateAnimationCoordinates();
		}

		updateVisibility();
	}
	
	private void updateVisibility() {
		switch (rotation) {
		case UP:
			if (yCoordinate <= 0) {
				this.setVisible(false);
			}
			break;
		case DOWN:
			if (yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
			break;
		case LEFT:
			if (xCoordinate < 0) {
				this.setVisible(false);
			}
			break;
		case RIGHT:
			if (xCoordinate > DataClass.getInstance().getWindowWidth()) {
				this.setVisible(false);
			}
			break;
		case LEFT_DOWN:
			if (xCoordinate < 0 || yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
			break;
		case LEFT_UP:
			if (xCoordinate < 0 || yCoordinate <= 0) {
				this.setVisible(false);
			}
			break;
		case NONE:
			this.setVisible(false);
			break;
		case RIGHT_DOWN:
			if (xCoordinate > DataClass.getInstance().getWindowWidth()
					|| yCoordinate >= DataClass.getInstance().getWindowHeight()) {
				this.setVisible(false);
			}
			break;
		case RIGHT_UP:
			if (xCoordinate > DataClass.getInstance().getWindowWidth() || yCoordinate <= 0) {
				this.setVisible(false);
			}
			break;
		}
	}

	//Needed for all PathFinders, so added to missiles
	private Point calculateNextPoint(Point currentLocation, Direction direction, int XStepSize, int YStepSize) {
		int x = currentLocation.getX();
		int y = currentLocation.getY();

		switch (direction) {
		case UP:
			y -= YStepSize;
			break;
		case DOWN:
			y += YStepSize;
			break;
		case LEFT:
			x -= XStepSize;
			break;
		case RIGHT:
			x += XStepSize;
			break;
		case LEFT_UP:
			x -= XStepSize;
			y -= YStepSize;
			break;
		case LEFT_DOWN:
			x -= XStepSize;
			y += YStepSize;
			break;
		case RIGHT_UP:
			x += XStepSize;
			y -= YStepSize;
			break;
		case RIGHT_DOWN:
			x += XStepSize;
			y += YStepSize;
			break;
		case NONE:
			// no movement
			break;
		}

		return new Point(x, y);
	}

	// Sets the animations (the graphics of missile) to align with the missiles
	// coordinates
	private void updateAnimationCoordinates() {
		animation.setX(xCoordinate);
		animation.setY(yCoordinate);
		explosionAnimation.setX(xCoordinate);
		explosionAnimation.setY(yCoordinate);
	}

	public float getMissileDamage() {
		return this.missileDamage;
	}

	public int getMissileMovementSpeed() {
		return this.xMovementSpeed;
	}

	public ImageEnums getMissileType() {
		return this.missileType;
	}

	protected void setAnimation() {
		if (!missileType.equals(ImageEnums.Alien_Laserbeam) && !missileType.equals(ImageEnums.Player_Laserbeam)) {
			if (missileType != null) {
				this.animation = new SpriteAnimation(xCoordinate, yCoordinate, missileType, true, scale);
				this.explosionAnimation = new SpriteAnimation(xCoordinate, yCoordinate, explosionType, false, scale);
			}
		} else {
			this.animation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Impact_Explosion_One, false, scale);
			this.explosionAnimation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Impact_Explosion_One, false,
					scale);
		}
		this.animation.rotateAnimetion(rotation);
	}

	public SpriteAnimation getExplosionAnimation() {
		if (this.explosionAnimation != null) {
			return this.explosionAnimation;
		}
		return null;
	}

	public SpriteAnimation getAnimation() {
		if (this.animation != null) {
			return this.animation;
		}
		return null;
	}



}