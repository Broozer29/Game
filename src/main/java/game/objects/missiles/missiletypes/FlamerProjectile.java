package game.objects.missiles.missiletypes;

import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FlamerProjectile extends Missile {

	public FlamerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
		this.animation.setFrameDelay(3);
	}

	public void missileAction() {
		if (movementConfiguration.getStepsTaken() % 5 == 0 && animation.getScale() < 2.00) {
			this.animation.setAnimationScale((float) (animation.getScale() + 0.05));
			;
		}
	}

}