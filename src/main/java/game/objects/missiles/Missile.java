package game.objects.missiles;

import data.DataClass;
import data.movement.Direction;
import data.movement.Path;
import data.movement.PathFinder;
import data.movement.Point;
import image.objects.Sprite;
import image.objects.SpriteAnimation;

public class Missile extends Sprite {

	protected float missileDamage;
	protected int movementSpeed;
	protected SpriteAnimation animation;
	protected SpriteAnimation explosionAnimation;
	protected String missileType;
	protected String explosionType;
	protected Direction rotation;
	protected int missileStepsTaken;

	private Point currentLocation;
	private Point destination;
	private PathFinder pathFinder;
	private Path currentPath;

	public Missile(int x, int y, Point destination, String missileType, String explosionType, Direction rotation,
			float scale, PathFinder pathFinder) {
		super(x, y, scale);
		this.currentLocation = new Point(x, y);
		this.destination = destination;
		this.explosionType = explosionType;
		this.rotation = rotation;
		this.missileType = missileType;
		this.missileStepsTaken = 0;
		this.pathFinder = pathFinder;
	}

	public void move() {
		if (currentPath == null || currentPath.getWaypoints().isEmpty()) {
			// calculate a new path if necessary
			currentPath = pathFinder.findPath(currentLocation, destination, movementSpeed);
		}

		// get the next point from the path
		Point nextPoint = currentPath.getWaypoints().get(0);

		// move towards the next point
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
		return this.movementSpeed;
	}

	public String getMissileType() {
		return this.missileType;
	}

	protected void setAnimation() {
		if (!missileType.equals("Alien Laserbeam") && !missileType.equals("Player Laserbeam")) {
			if (missileType != null) {
				this.animation = new SpriteAnimation(xCoordinate, yCoordinate, missileType, true, scale);
				this.explosionAnimation = new SpriteAnimation(xCoordinate, yCoordinate, explosionType, false, scale);
			}
		} else {
			this.animation = new SpriteAnimation(xCoordinate, yCoordinate, "Impact Explosion One", false, scale);
			this.explosionAnimation = new SpriteAnimation(xCoordinate, yCoordinate, "Impact Explosion One", false,
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