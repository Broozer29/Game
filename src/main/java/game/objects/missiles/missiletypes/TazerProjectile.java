package game.objects.missiles.missiletypes;

import game.movement.MovementConfiguration;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class TazerProjectile extends Missile {

	public TazerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.rotateAnimation(movementConfiguration.getRotation());
//		setAnimation();
		this.animation.setFrameDelay(3);
	}

	public void missileAction() {

	}

}