package game.objects.missiles;

import data.DataClass;

public class EnergizerProjectile extends Missile {

	public EnergizerProjectile(int x, int y) {
		super(x, y);
		this.missileDamage = (float) 7.5;
		initMissile();
		setAnimation();
	}

	private void initMissile() {
		this.missileDirection = "LeftUp";
		this.missileMovementSpeed = 5;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}

}
