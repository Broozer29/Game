package game.objects.missiles.missiletypes;

import game.movement.MovementConfiguration;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class BulldozerProjectile extends Missile {

	public BulldozerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
//		setAnimation();
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimation(movementConfiguration.getRotation());

	}

	public void missileAction() {

	}

}