
package game.objects.missiles;

import data.DataClass;
import data.movement.Trajectory;
import game.managers.ExplosionManager;

public class BombaProjectile extends Missile {

	public BombaProjectile(int x, int y, String missileType, String explosionType, String missileDirection,
			int angleModuloDivider, String rotationAngle, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotationAngle, scale);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.movementSpeed = 5;
		this.trajectory = new Trajectory();
		this.trajectory.createRegularTrajectory(missileDirection, totalDistance(), movementSpeed, angleModuloDivider, true, false);
	
	}

	public void missileAction() {
		if (missileStepsTaken >= 75) {
			ExplosionManager.getInstance().addExplosion(getCenterXCoordinate(), getCenterYCoordinate(),
					"Bomba Projectile Explosion", scale, 20);
			this.setVisible(false);
		}
	}

}
