package game.objects.missiles;

import java.util.List;

import data.DataClass;
import data.image.ImageDatabase;
import data.movement.Trajectory;
import image.objects.Animation;
import image.objects.Sprite;

public class Missile extends Sprite {

	protected float missileDamage;
	protected Trajectory trajectory;
	protected String direction;
	protected int angleModuloDivider;
	protected int movementSpeed;
	protected int maxMissileLength;
	protected Animation animation;
	protected Animation explosionAnimation;
	protected String missileType;
	protected String explosionType;
	protected String rotationAngle;
	protected int missileStepsTaken;

	public Missile(int x, int y, String missileType, String explosionType, String missileDirection,
			int angleModuloDivider, String rotationAngle, float scale) {
		super(x, y, scale);
		this.explosionType = explosionType;
		this.angleModuloDivider = angleModuloDivider;
		this.rotationAngle = rotationAngle;
		this.missileType = missileType;
		this.direction = missileDirection;
	}

	public void move() {
		List<Integer> newCoordsList = trajectory.getPathCoordinates(xCoordinate, yCoordinate);
		xCoordinate = newCoordsList.get(0);
		yCoordinate = newCoordsList.get(1);
		missileStepsTaken++;
		if (xCoordinate > DataClass.getInstance().getWindowWidth()) {
			visible = false;
		}
		if (xCoordinate < 0) {
			visible = false;
		}

		if (animation != null) {
			updateAnimationCoordinates();
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

	protected int getTotalTravelDistance() {
		if (direction.equals("Up") || direction.equals("Down")) {
			return DataClass.getInstance().getWindowHeight() + this.getWidth();
		} else
			return DataClass.getInstance().getWindowWidth() + this.getHeight();
	}

	public float getMissileDamage() {
		return this.missileDamage;
	}

	public String getMissileDirection() {
		return this.direction;
	}

	public int getMissileMovementSpeed() {
		return this.movementSpeed;
	}

	public int getMaxMissileLength() {
		return this.maxMissileLength;
	}

	public int getAngleModuloDivider() {
		return this.angleModuloDivider;
	}

	public String getMissileType() {
		return this.missileType;
	}
	
	public void updateTrajectory() {
		switch(trajectory.getTrajectoryType()) {
		case("Regular"):
			trajectory.updateRegularPath();
			break;
		case("Homing"):
			trajectory.updateMissileHomingPaths(this);
			break;
		}
	}

	protected void setAnimation() {
		if (!missileType.equals("Alien Laserbeam") && !missileType.equals("Player Laserbeam")) {
			if (missileType != null) {
				this.animation = new Animation(xCoordinate, yCoordinate, missileType, true, scale);
				this.explosionAnimation = new Animation(xCoordinate, yCoordinate, explosionType, false, scale);
			}
		} else {
			this.animation = new Animation(xCoordinate, yCoordinate, "Impact Explosion One", false, scale);
			this.explosionAnimation = new Animation(xCoordinate, yCoordinate, "Impact Explosion One", false, scale);
		}
		this.animation.rotateAnimetion(rotationAngle);
	}

	public Animation getExplosionAnimation() {
		if (this.explosionAnimation != null) {
			return this.explosionAnimation;
		}
		return null;
	}

	public Animation getAnimation() {
		if (this.animation != null) {
			return this.animation;
		}
		return null;
	}

}