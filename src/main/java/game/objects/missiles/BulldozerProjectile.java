package game.objects.missiles;

import data.movement.Trajectory;

public class BulldozerProjectile extends Missile{

	public BulldozerProjectile(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider, String rotationAngle, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotationAngle, scale);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		
		this.missileMovementSpeed = 5;
		this.trajectory = new Trajectory(missileDirection, totalDistance(), missileMovementSpeed, angleModuloDivider, true);
	}

	public void missileAction() {
		
	}


}
