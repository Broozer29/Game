package game.objects.missiles;

import data.movement.Direction;
import data.movement.PathFinder;
import data.movement.Point;

public class SeekerProjectile extends Missile {

	public SeekerProjectile(int x, int y, Point destination, String missileType, String explosionType, Direction rotation,
			float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.movementSpeed = 2;
		}

	
	public void missileAction() {
		
	}

}
 	