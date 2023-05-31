package game.objects.missiles;

import data.movement.HomingTrajectory;
import data.movement.RegularTrajectory;

public class SeekerProjectile extends Missile {

	public SeekerProjectile(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider, String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.movementSpeed = 2;
//		this.trajectory = new RegularTrajectory(direction, movementSpeed, true, angleModuloDivider, getTotalTravelDistance());
		this.trajectory = new HomingTrajectory(direction, movementSpeed, true, angleModuloDivider);

		}

	
	public void missileAction() {
		
	}

}
 	