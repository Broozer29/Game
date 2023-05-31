package game.objects.missiles;

import data.movement.Direction;
import data.movement.PathFinder;
import data.movement.Point;

public class DefaultPlayerLaserbeam extends Missile {

	public DefaultPlayerLaserbeam(int x, int y, Point destination, String missileType, String explosionType, Direction rotation,
			float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder);
		this.missileDamage = (float) 27.5;
		loadImage("Player Laserbeam");
		setAnimation();

		this.movementSpeed = 5;
	}

	public void missileAction() {

	}

}
