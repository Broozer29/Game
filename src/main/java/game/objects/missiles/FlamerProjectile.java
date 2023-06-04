package game.objects.missiles;

import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class FlamerProjectile extends Missile {

	public FlamerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
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
