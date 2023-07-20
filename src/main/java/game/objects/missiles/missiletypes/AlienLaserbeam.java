package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;

public class AlienLaserbeam extends Missile {

	public AlienLaserbeam(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed, boolean isFriendly, float damage) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, isFriendly, xMovementSpeed, yMovementSpeed);
		loadImage(missileType);
		this.missileDamage = damage;
	}

	public void missileAction() {

	}

}