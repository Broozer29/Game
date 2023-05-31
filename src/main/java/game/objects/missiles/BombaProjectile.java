
package game.objects.missiles;

import data.movement.Direction;
import data.movement.PathFinder;
import data.movement.Point;
import game.managers.ExplosionManager;

public class BombaProjectile extends Missile {

	public BombaProjectile(int x, int y, Point destination, String missileType, String explosionType,
			Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.movementSpeed = 5;
	
	}

	public void missileAction() {
		if (missileStepsTaken >= 75) {
			ExplosionManager.getInstance().addExplosion(getCenterXCoordinate(), getCenterYCoordinate(),
					"Bomba Projectile Explosion", scale, 20);
			this.setVisible(false);
		}
	}

}
