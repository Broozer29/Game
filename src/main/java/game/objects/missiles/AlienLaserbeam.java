package game.objects.missiles;

import data.image.enums.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class AlienLaserbeam extends Missile {

	public AlienLaserbeam(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder);
		loadImage(missileType);
		this.missileDamage = (float) 2.5;
		this.movementSpeed = 3;
	}

	public void missileAction() {

	}

}
