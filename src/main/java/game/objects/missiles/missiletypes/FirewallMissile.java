package game.objects.missiles.missiletypes;

import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class FirewallMissile extends Missile {

	public FirewallMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
		this.animation.setFrameDelay(10);

	}

	public void missileAction() {
		//Pulse with a firenova or something??
	}

}