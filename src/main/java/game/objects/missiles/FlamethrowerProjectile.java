package game.objects.missiles;

import data.image.ImageEnums;
import game.managers.OnScreenTextManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.friendlies.powerups.PowerUpAcquiredText;
import game.objects.friendlies.powerups.PowerUpEnums;

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
		int xPointDifference = Math.abs(this.xCoordinate - this.destination.getX());
		int yPointDifference = Math.abs(this.yCoordinate - this.destination.getY());

		if (this.rotation == Direction.RIGHT || this.rotation == Direction.RIGHT_DOWN
				|| this.rotation == Direction.RIGHT_UP) {
			if (xPointDifference < 10) {
				this.setVisible(false);
//				PowerUpAcquiredText text = new PowerUpAcquiredText(this.xCoordinate, this.yCoordinate,
//						PowerUps.DUMMY_DO_NOT_USE);
//				OnScreenTextManager.getInstance().addPowerUpText(text);
			}
		}

		if (this.rotation == Direction.LEFT || this.rotation == Direction.LEFT_DOWN
				|| this.rotation == Direction.LEFT_UP) {
			if (yPointDifference < 10) {
				this.setVisible(false);
//				PowerUpAcquiredText text = new PowerUpAcquiredText(this.xCoordinate, this.yCoordinate,
//						PowerUps.DUMMY_DO_NOT_USE);
//				OnScreenTextManager.getInstance().addPowerUpText(text);
			}
		}

	}

}