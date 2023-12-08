package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;

public class TazerProjectile extends Missile {

	public TazerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed,
			boolean friendly, float damage) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed,
				yMovementSpeed);
		this.damage = damage;
//		setAnimation();
		this.animation.setFrameDelay(3);
		this.rotateGameObject(rotation);
	}

	public void missileAction() {

	}

}