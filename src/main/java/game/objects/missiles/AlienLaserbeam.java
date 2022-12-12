package game.objects.missiles;

import data.DataClass;

public class AlienLaserbeam extends Missile{

	public AlienLaserbeam(int x, int y, String missileType, int angleModuloDivider) {
		super(x, y);
		this.missileType = missileType;
		this.angleModuloDivider = angleModuloDivider;
		loadImage("Alien Laserbeam");
		this.missileDamage = (float) 2.5;
		initMissile();
	}
	
	private void initMissile() {
		this.missileDirection = "Left";
		this.missileMovementSpeed = 3;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}

}
