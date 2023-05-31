package game.objects.missiles;

import data.movement.Direction;
import data.movement.PathFinder;
import data.movement.Point;

public class FlamerProjectile extends Missile {

	public FlamerProjectile(int x, int y, Point destination, String missileType, String explosionType,
			Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);

		this.movementSpeed = 5;
	}

	public void missileAction() {
		if (missileStepsTaken % 5 == 0 && animation.getScale() < 2.00) {
			float newScale = (float) (animation.getScale() + 0.05);
			this.animation.setAnimationScale(newScale);
			;
		}
	}

}
