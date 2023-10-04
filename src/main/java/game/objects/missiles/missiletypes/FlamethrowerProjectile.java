package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;

public class FlamethrowerProjectile extends Missile {

	public FlamethrowerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, float damage, int xMovementSpeed,
			int yMovementSpeed, boolean friendly) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed,
				yMovementSpeed);
		setAnimation();
		this.missileDamage = damage;
	}

	// Remove the flamethrower
	public void missileAction() {
		int xPointDifference = Math.abs(this.xCoordinate - moveConfig.getDestination().getX());
		int yPointDifference = Math.abs(this.yCoordinate - moveConfig.getDestination().getY());

		if (moveConfig.getRotation() == Direction.RIGHT || moveConfig.getRotation() == Direction.RIGHT_DOWN
				|| moveConfig.getRotation() == Direction.RIGHT_UP) {
			if (xPointDifference < 10) {
				this.setVisible(false);
			}
		}

		if (moveConfig.getRotation() == Direction.LEFT || moveConfig.getRotation() == Direction.LEFT_DOWN
				|| moveConfig.getRotation() == Direction.LEFT_UP) {
			if (yPointDifference < 10) {
				this.setVisible(false);
			}
		}

	}

}