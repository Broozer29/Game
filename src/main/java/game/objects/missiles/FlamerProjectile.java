package game.objects.missiles;

import data.DataClass;

public class FlamerProjectile extends Missile{

	public FlamerProjectile(int x, int y, String missileType, String explosionType, String missileDirection, int angleModuloDivider, String rotation, float scale) {
		super(x, y, missileType, explosionType, missileDirection, angleModuloDivider, rotation, scale);
		this.missileDamage = (float) 7.5;
		initMissile();
		setAnimation();
		this.animation.setFrameDelay(3);
	}

	private void initMissile() {
		this.missileMovementSpeed = 5;
		this.maxMissileLength = (int) (DataClass.getInstance().getWindowWidth() * 1.5);
		this.trajectory.setMissileTrajectoryType(this);
	}
	
	public void missileAction() {
		if (missileStepsTaken % 5 == 0 && animation.getScale() < 2.00) {
			float newScale = (float) (animation.getScale() + 0.05);
			this.animation.setAnimationScale(newScale);;
		}
	}


}
