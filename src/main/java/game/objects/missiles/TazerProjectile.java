package game.objects.missiles;

import data.movement.RegularTrajectory;
import data.movement.Trajectory;

public class TazerProjectile extends Missile {

	public TazerProjectile(int x, int y, String missileType, String explosionType, String missileDirection,
			int angleModuloDivider, String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);

		this.movementSpeed = 5;
		this.trajectory = new RegularTrajectory(direction, movementSpeed, true, angleModuloDivider, getTotalTravelDistance());
	}

	public void missileAction() {

	}

}
