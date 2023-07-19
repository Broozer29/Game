package game.objects.missiles.missiletypes;

import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.Missile;

public class DefaultPlayerLaserbeam extends Missile {

	public DefaultPlayerLaserbeam(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder, float damage, int xMovementSpeed, int yMovementSpeed, boolean isFriendly) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, isFriendly, xMovementSpeed, yMovementSpeed);
		loadImage(missileType);
		setAnimation();
		this.missileDamage = damage;
	}

	public void missileAction() {

	}

}