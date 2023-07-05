package game.objects.missiles;

import data.image.ImageEnums;
import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;

public class FirewallMissile extends Missile {

	public FirewallMissile(int xCoordinate, int yCoordinate, Point destination, ImageEnums missileType,
			ImageEnums explosionType, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed,
			int yMovementSpeed, float specialAttackDamage, boolean friendly) {
		super(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed,
				yMovementSpeed);
		this.setAnimation();
		this.animation.setFrameDelay(10);
		this.missileDamage = specialAttackDamage;	}

	public void missileAction() {
		//Pulse with a firenova or something??
	}

}