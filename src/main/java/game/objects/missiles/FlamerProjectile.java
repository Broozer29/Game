package game.objects.missiles;

import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class FlamerProjectile extends Missile {

	public FlamerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, false, xMovementSpeed, yMovementSpeed);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
	}

	public void missileAction() {
		if (missileStepsTaken % 5 == 0 && animation.getScale() < 2.00) {
			this.animation.setAnimationScale((float) (animation.getScale() + 0.05));
			;
		}
	}

}