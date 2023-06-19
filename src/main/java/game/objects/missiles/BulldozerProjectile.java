package game.objects.missiles;

import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class BulldozerProjectile extends Missile {

	public BulldozerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, false);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);

		this.xMovementSpeed = 5;
		this.yMovementSpeed = 2;
	}

	public void missileAction() {

	}

}
