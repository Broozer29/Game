package game.objects.missiles;

import data.DataClass;
import data.movement.Trajectory;

public class AlienLaserbeam extends Missile {

	public AlienLaserbeam(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider,
			String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		loadImage("Alien Laserbeam");
		this.missileDamage = (float) 2.5;
		this.movementSpeed = 3;
		this.trajectory = new Trajectory();
		this.trajectory.createRegularTrajectory(missileDirection, totalDistance(), movementSpeed, angleModuloDivider, true, false);
	}

	public void missileAction() {
		
	}

}
