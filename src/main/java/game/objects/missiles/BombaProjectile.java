package game.objects.missiles;

import data.DataClass;

public class BombaProjectile extends Missile {

	public BombaProjectile(int x, int y, String missileType, int angleModuloDivider, String missileDirection) {
		super(x, y);
		this.missileType = missileType;
		this.missileDirection = missileDirection;
		this.angleModuloDivider = angleModuloDivider;
		this.missileDamage = (float) 7.5;
		initMissile();
		setAnimation();
	}

	private void initMissile() {
		this.missileMovementSpeed = 5;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}


}
