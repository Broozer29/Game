package game.gameobjects.missiles.missiletypes;

import game.movement.MovementConfiguration;
import game.gameobjects.missiles.Missile;
import game.gameobjects.missiles.MissileConfiguration;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class BulldozerProjectile extends Missile {

	public BulldozerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
//		setAnimation();
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);

		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);
		}
	}

	public void missileAction() {

	}

}