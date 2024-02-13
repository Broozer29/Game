package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FlamethrowerProjectile extends Missile {

	public FlamethrowerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.rotateAnimation(missileConfiguration.getMovementDirection());
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