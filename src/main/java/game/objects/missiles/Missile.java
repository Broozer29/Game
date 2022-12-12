package game.objects.missiles;

import java.util.List;

import data.DataClass;
import data.ImageDatabase;
import data.movement.Trajectory;
import image.objects.Animation;
import image.objects.Sprite;

public class Missile extends Sprite {

	protected float missileDamage;
	protected Trajectory trajectory = new Trajectory();
	protected String missileDirection;
	protected int angleModuloDivider;
	protected int missileMovementSpeed;
	protected int maxMissileLength;
	protected Animation animation;
	protected String missileType;

	public Missile(int x, int y) {
		super(x, y);
	}

	public void updateGameTick() {
		move();
		if (animation != null) {
			updateAnimationCoordinates();
		}

	}

	private void move() {
		List<Integer> newCoordsList = trajectory.getPathCoordinates(xCoordinate, yCoordinate);
		xCoordinate = newCoordsList.get(0);
		yCoordinate = newCoordsList.get(1);

		if (xCoordinate > DataClass.getInstance().getWindowWidth()) {
			visible = false;
		}
		if (xCoordinate < 0) {
			visible = false;
		}
	}

	// Sets the animations (the graphics of missile) to align with the missiles
	// coordinates
	private void updateAnimationCoordinates() {
		animation.setX(xCoordinate);
		animation.setY(yCoordinate);
	}

	public float getMissileDamage() {
		return this.missileDamage;
	}

	public String getMissileDirection() {
		return this.missileDirection;
	}

	public int getMissileMovementSpeed() {
		return this.missileMovementSpeed;
	}

	public int getMaxMissileLength() {
		return this.maxMissileLength;
	}
	
	public int getAngleSize() {
		return this.angleModuloDivider;
	}

	protected void setAnimation() {
		if (!missileType.equals("Alien Laserbeam") && !missileType.equals("Player Laserbeam")) {
			if (missileType != null) {
				this.animation = new Animation(xCoordinate, yCoordinate, missileType, true);
			}
		}
	}

	public Animation getAnimation() {
		if (this.animation != null) {
			return this.animation;
		}
		return null;
	}

}