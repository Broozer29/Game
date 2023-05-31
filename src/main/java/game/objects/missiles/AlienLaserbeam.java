package game.objects.missiles;

import data.movement.Direction;
import data.movement.PathFinder;
import data.movement.Point;

public class AlienLaserbeam extends Missile {

	public AlienLaserbeam(int x, int y, Point destination, String missileType, String explosionType, Direction rotation,
			float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder);
		loadImage("Alien Laserbeam");
		this.missileDamage = (float) 2.5;
		this.movementSpeed = 3;
	}

	public void missileAction() {

	}

}
