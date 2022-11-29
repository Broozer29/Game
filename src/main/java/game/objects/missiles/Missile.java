package game.objects.missiles;

import java.util.List;

import data.DataClass;
import data.movement.Trajectory;
import image.objects.Sprite;

public class Missile extends Sprite {

	protected float missileDamage;
	protected String missileType;
	protected Trajectory trajectory = new Trajectory();
	protected String missileDirection;
	protected int missileMovementSpeed;
	protected int maxMissileLength;

	public Missile(int x, int y, String missileType) {
		super(x, y);
		this.missileType = missileType;
	}

	public void updateGameTick() {
		move();
	}

	public void move() {
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

}