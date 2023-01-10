package game.objects.missiles;

import data.movement.RegularTrajectory;
import data.movement.Trajectory;

public class FlamerProjectile extends Missile{

	public FlamerProjectile(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider, String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		
		this.movementSpeed = 5;
		this.trajectory = new RegularTrajectory(direction, movementSpeed, true, angleModuloDivider, getTotalTravelDistance());
	}

	
	public void missileAction() {
		if (missileStepsTaken % 5 == 0 && animation.getScale() < 2.00) {
			float newScale = (float) (animation.getScale() + 0.05);
			this.animation.setAnimationScale(newScale);;
		}
	}


}
