package game.objects.missiles;

import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class AlienLaserbeam extends Missile {

	public AlienLaserbeam(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, false, xMovementSpeed, yMovementSpeed);
		loadImage(missileType);
		this.missileDamage = (float) 2.5;
	}

	public void missileAction() {

	}

}