package game.objects.friendlies;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.OrbitPathFinder;
import game.movement.Path;
import game.movement.PathFinder;
import game.movement.Point;
import gamedata.DataClass;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class FriendlyObject extends Sprite {

	// Enemy combat stats
	protected float hitPoints;
	protected float maxHitPoints;
	protected float attackSpeedFrameCount;
	protected float currentAttackSpeedFrameCount = 0;
	protected boolean hasAttack;

	// Enemy new movement:
	private Point currentLocation;
	private Point destination;
	private PathFinder pathFinder;
	private Path currentPath;
	protected int XMovementSpeed;
	protected int YMovementSpeed;
	private int lastUsedXMovementSpeed;
	private int lastUsedYMovementSpeed;
	protected int currentBoardBlock;
	private int lastKnownTargetX;
	private int lastKnownTargetY;

	// Enemy miscellanious attributes
	protected Direction rotation;
	protected FriendlyEnums friendlyType;
	protected AudioEnums deathSound;
	protected boolean showHealthBar;
	protected SpriteAnimation exhaustAnimation = null;
	protected SpriteAnimation deathAnimation = null;
	protected SpriteAnimation animation = null;
	private boolean isFriendly = true;

	public FriendlyObject(int x, int y, Point destination, Direction rotation, FriendlyEnums friendlyType, float scale,
			PathFinder pathFinder) {
		super(x, y, scale);
		this.friendlyType = friendlyType;
		this.loadImage(ImageEnums.Test_Image);
		this.currentLocation = new Point(x, y);
		this.destination = destination;
		this.rotation = rotation;
		this.pathFinder = pathFinder;
	}

	public void move() {
		if (currentPath == null || currentPath.getWaypoints().isEmpty() || XMovementSpeed != lastUsedXMovementSpeed
				|| YMovementSpeed != lastUsedYMovementSpeed || pathFinder.shouldRecalculatePath(currentPath)) {
			// calculate a new path if necessary
			currentPath = pathFinder.findPath(currentLocation, destination, XMovementSpeed, YMovementSpeed, rotation,
					isFriendly);
			lastUsedXMovementSpeed = XMovementSpeed;
			lastUsedYMovementSpeed = YMovementSpeed;
		}

		currentPath.updateCurrentLocation(currentLocation);
		// get the next point from the path

		// move towards the next point
		currentLocation = currentPath.getWaypoints().get(0);
		this.xCoordinate = currentPath.getWaypoints().get(0).getX();
		this.yCoordinate = currentPath.getWaypoints().get(0).getY();
		if (this.animation != null) {
			animation.setX(currentPath.getWaypoints().get(0).getX());
			animation.setY(currentPath.getWaypoints().get(0).getY());
			animation.updateCurrentBoardBlock();
		}
		updateCurrentBoardBlock();

		// if reached the next point, remove it from the path
		if (currentLocation.equals(currentPath.getWaypoints().get(0))) {
			currentPath.getWaypoints().remove(0);
		}

		if (pathFinder instanceof OrbitPathFinder) {
			// Check if the target's position has changed
			Sprite target = ((OrbitPathFinder) pathFinder).getTarget();
			if (target.getCenterXCoordinate() != lastKnownTargetX
					|| target.getCenterYCoordinate() != lastKnownTargetY) {

				int deltaX = 0;
				int deltaY = 0;

				if (lastKnownTargetX == 0 || lastKnownTargetY == 0) {
					deltaX = 0;
					deltaY = 0;
				} else {
					deltaX = target.getCenterXCoordinate() - lastKnownTargetX;
					deltaY = target.getCenterYCoordinate() - lastKnownTargetY;
				}

				// Update the last known position
				lastKnownTargetX = target.getCenterXCoordinate();
				lastKnownTargetY = target.getCenterYCoordinate();

				// Adjust the orbit path based on the deltas
				((OrbitPathFinder) pathFinder).adjustPathForTargetMovement(currentPath, deltaX, deltaY);
			}
		}

		if (this.exhaustAnimation != null) {
			this.exhaustAnimation.setX(this.getCenterXCoordinate() + (this.getWidth() / 2));
			this.exhaustAnimation.setY(this.getCenterYCoordinate() - (exhaustAnimation.getHeight() / 2));
		}

		bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
		
		if (animation != null) {
			animation.setAnimationBounds(xCoordinate, yCoordinate);
		}
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

	public SpriteAnimation getAnimation() {
		return animation;
	}

	public void setAnimation(SpriteAnimation animation) {
		this.animation = animation;
	}

}