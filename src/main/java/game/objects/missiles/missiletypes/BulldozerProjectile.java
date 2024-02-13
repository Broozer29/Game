package game.objects.missiles.missiletypes;

import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class BulldozerProjectile extends Missile {

	public BulldozerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
//		setAnimation();
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimation(missileConfiguration.getMovementDirection());

	}

	public void missileAction() {

	}

}