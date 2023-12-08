package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;

public class FlamethrowerProjectile extends Missile {

	public FlamethrowerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, float damage, int xMovementSpeed,
			int yMovementSpeed, boolean friendly) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed,
				yMovementSpeed);
//		setAnimation();
		this.damage = damage;
	}

	// Remove the flamethrower
	public void missileAction() {
		int xPointDifference = Math.abs(this.xCoordinate - movementConfiguration.getDestination().getX());
		int yPointDifference = Math.abs(this.yCoordinate - movementConfiguration.getDestination().getY());

		if (movementConfiguration.getRotation() == Direction.RIGHT || movementConfiguration.getRotation() == Direction.RIGHT_DOWN
				|| movementConfiguration.getRotation() == Direction.RIGHT_UP) {
			if (xPointDifference < 10) {
				this.setVisible(false);
			}
		}

		if (movementConfiguration.getRotation() == Direction.LEFT || movementConfiguration.getRotation() == Direction.LEFT_DOWN
				|| movementConfiguration.getRotation() == Direction.LEFT_UP) {
			if (yPointDifference < 10) {
				this.setVisible(false);
			}
		}

	}

}