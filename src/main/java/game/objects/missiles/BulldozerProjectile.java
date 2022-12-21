package game.objects.missiles;

import data.DataClass;

public class BulldozerProjectile extends Missile{

	public BulldozerProjectile(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider, String rotationAngle, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotationAngle, scale);
		this.missileDamage = (float) 7.5;
		initMissile();
		setAnimation();
		this.animation.setFrameDelay(3);
	}

	private void initMissile() {
		this.missileMovementSpeed = 5;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}
	
	public void missileAction() {
		
	}


}
