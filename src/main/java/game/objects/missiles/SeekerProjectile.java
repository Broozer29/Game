package game.objects.missiles;

import data.movement.Trajectory;

public class SeekerProjectile extends Missile {

	public SeekerProjectile(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider, String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.movementSpeed = 2;
		this.trajectory = new Trajectory();
		this.trajectory.createHomingTrajectory(xCoordinate, yCoordinate, 2, false, missileDirection, 3);
//		this.trajectory.createRegularTrajectory(missileDirection, totalDistance(), movementSpeed, angleModuloDivider, true, false);	
		}

	
	public void missileAction() {
		
	}

}
 