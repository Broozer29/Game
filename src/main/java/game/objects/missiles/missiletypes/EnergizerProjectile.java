package game.objects.missiles.missiletypes;

import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class EnergizerProjectile extends Missile {

	public EnergizerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
	}

	public void missileAction() {

	}

}