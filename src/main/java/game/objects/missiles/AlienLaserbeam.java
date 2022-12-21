package game.objects.missiles;

import data.DataClass;

public class AlienLaserbeam extends Missile {

	public AlienLaserbeam(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider,
			String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		loadImage("Alien Laserbeam");
		this.missileDamage = (float) 2.5;
		initMissile();
	}

	private void initMissile() {
		this.missileMovementSpeed = 3;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}
	
	public void missileAction() {
		
	}

}
