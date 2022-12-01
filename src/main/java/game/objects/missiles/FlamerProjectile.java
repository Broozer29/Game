package game.objects.missiles;

import data.DataClass;

public class FlamerProjectile extends Missile{

	public FlamerProjectile(int x, int y, String missileType) {
		super(x, y, missileType);
		this.missileDamage = (float) 7.5;
		initMissile();
		setAnimation();
	}

	private void initMissile() {
		this.missileDirection = "Left";
		this.missileMovementSpeed = 5;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}


}
