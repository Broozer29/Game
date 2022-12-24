package game.objects.missiles;

import data.DataClass;
import data.movement.Trajectory;

public class DefaultPlayerLaserbeam extends Missile {

	public DefaultPlayerLaserbeam(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider, String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		this.missileDamage = (float) 27.5;
		loadImage("Player Laserbeam");
		setAnimation();
		
		this.movementSpeed = 5;
		this.trajectory = new Trajectory();
		this.trajectory.createRegularTrajectory(missileDirection, totalDistance(), movementSpeed, angleModuloDivider, true, false);	
	}

	public void missileAction() {
		
	}

}
