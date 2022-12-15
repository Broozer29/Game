package game.objects.missiles;

import data.DataClass;

public class DefaultPlayerLaserbeam extends Missile {

	public DefaultPlayerLaserbeam(int x, int y, String missileType, String missileDirection, int angleModuloDivider, String rotation) {
		super(x, y, missileType, missileDirection, angleModuloDivider, rotation);
		this.missileDamage = (float) 27.5;
		loadImage("Player Laserbeam");
		initMissile();
	}

	private void initMissile() {
		this.missileMovementSpeed = 5;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}

}
