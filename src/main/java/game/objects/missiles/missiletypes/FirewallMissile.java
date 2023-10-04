package game.objects.missiles.missiletypes;

import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;

public class FirewallMissile extends Missile {

	public FirewallMissile(int xCoordinate, int yCoordinate, Point destination, ImageEnums missileType,
			ImageEnums explosionType, Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed,
			int yMovementSpeed, float specialAttackDamage, boolean friendly) {
		super(xCoordinate, yCoordinate, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed,
				yMovementSpeed);
		this.setAnimation();
		this.animation.setFrameDelay(10);
		this.missileDamage = specialAttackDamage;	
		this.animation.rotateAnimetion(rotation);		
	}

	public void missileAction() {
		//Pulse with a firenova or something??
	}

}