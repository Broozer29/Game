package game.objects.missiles;

import data.image.ImageEnums;
import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class FirewallMissile extends Missile {

	public FirewallMissile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, boolean isFriendly, int xMovementSpeed,
			int yMovementSpeed, float damage) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, isFriendly, xMovementSpeed,
				yMovementSpeed);
		this.setAnimation();
		this.animation.setFrameDelay(10);
		this.missileDamage = damage;
	}
	
	public void missileAction() {
		//Pulse with a firenova or something??
	}

}