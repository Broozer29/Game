
package game.objects.missiles.missiletypes;

import game.managers.ExplosionManager;
import game.objects.missiles.MissileConfiguration;
import game.objects.neutral.Explosion;
import game.objects.missiles.Missile;
import game.objects.neutral.ExplosionConfiguration;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class BombaProjectile extends Missile {

	public BombaProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
	
	}

	public void missileAction() {
		if (movementConfiguration.getStepsTaken() >= 75) {


			SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
			spriteConfiguration1.setxCoordinate(this.xCoordinate);
			spriteConfiguration1.setyCoordinate(this.yCoordinate);
			spriteConfiguration1.setScale(this.scale);

			SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 2, false);
			spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Missile_Explosion);

			ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(isFriendly(), damage * 5, false);
			Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);

			ExplosionManager.getInstance().addExistingExplosion(explosion);
			this.setVisible(false);
		}
	}
	
}