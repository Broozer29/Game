package game.objects.missiles;

import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class DefaultPlayerLaserbeam extends Missile {

	public DefaultPlayerLaserbeam(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, true);
		this.missileDamage = (float) 27.5;
		loadImage(missileType);
		setAnimation();

		this.xMovementSpeed = 5;
		this.yMovementSpeed = 2;
	}

	public void missileAction() {

	}

}
