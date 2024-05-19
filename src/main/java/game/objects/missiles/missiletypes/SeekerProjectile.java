package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class SeekerProjectile extends Missile {

	public SeekerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.setFrameDelay(3);

		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);
		}
	}

	public void missileAction() {
	}

}
