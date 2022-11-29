package game.objects.missiles;

import data.DataClass;

public class DefaultPlayerLaserbeam extends Missile {

	public DefaultPlayerLaserbeam(int x, int y, String missileType) {
		super(x, y, missileType);
		this.missileDamage = (float) 27.5;
		loadImage("Player Laserbeam");
		initMissile();
	}

	private void initMissile() {
		this.missileDirection = "Right";
		this.missileMovementSpeed = 5;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}

}
