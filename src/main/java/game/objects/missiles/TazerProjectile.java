package game.objects.missiles;

import data.DataClass;

public class TazerProjectile extends Missile{

	public TazerProjectile(int x, int y, int scale) {
		super(x, y, scale);
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
