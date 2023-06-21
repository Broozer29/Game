
package game.objects.missiles;

import data.image.enums.ImageEnums;
import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class BombaProjectile extends Missile {

	public BombaProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, false);
		this.missileDamage = (float) 7.5;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.xMovementSpeed = 5;
		this.yMovementSpeed = 2;
	
	}

	public void missileAction() {
		if (missileStepsTaken >= 75) {
			ExplosionManager.getInstance().createAndAddExplosion(getCenterXCoordinate(), getCenterYCoordinate(),
					ImageEnums.Bomba_Missile_Explosion, scale, 20, false);
			this.setVisible(false);
		}
	}

}
