package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FlamethrowerProjectile extends Missile {

	public FlamethrowerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);

		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);
		}
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