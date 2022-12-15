
package game.objects.missiles;

import data.DataClass;

public class BombaProjectile extends Missile {

	public BombaProjectile(int x, int y, String missileType, String missileDirection, int angleModuloDivider,
			String rotationAngle) {
		super(x, y, missileType, missileDirection, angleModuloDivider, rotationAngle);
		this.missileDamage = (float) 7.5;
		initMissile();
		setAnimation();
		this.animation.resizeAnimation(1);
	}

	private void initMissile() {
		this.missileMovementSpeed = 5;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);

	}

}
