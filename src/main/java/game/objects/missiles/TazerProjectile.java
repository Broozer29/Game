package game.objects.missiles;

import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class TazerProjectile extends Missile {

	public TazerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);

		this.movementSpeed = 5;
	}

	public void missileAction() {

	}

}
