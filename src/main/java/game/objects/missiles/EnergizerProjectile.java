package game.objects.missiles;

import data.movement.Trajectory;

public class EnergizerProjectile extends Missile {

	public EnergizerProjectile(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider, String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.movementSpeed = 5;
		this.trajectory = new Trajectory();
		this.trajectory.createRegularTrajectory(missileDirection, totalDistance(), movementSpeed, angleModuloDivider, true, false);
	}
	
	public void missileAction() {
		
	}

}
