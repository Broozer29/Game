
package game.objects.missiles.missiletypes;

import game.managers.ExplosionManager;
import game.objects.missiles.MissileConfiguration;
import game.objects.neutral.Explosion;
import game.objects.missiles.Missile;
import game.objects.neutral.ExplosionConfiguration;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteAnimation;

public class BombaProjectile extends Missile {

	public BombaProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
	
	}

	public void missileAction() {
		if (movementConfiguration.getStepsTaken() >= 75) {

			SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
			spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Missile_Explosion);
			SpriteAnimation animation  = new SpriteAnimation(spriteAnimationConfiguration);

			animation.setX(xCoordinate - (animation.getWidth() / 2));
			animation.setY(yCoordinate - (animation.getHeight() / 2));
			animation.setFrameDelay(3);


			ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(isFriendly(), damage, false);
			Explosion explosion = new Explosion(spriteAnimationConfiguration ,explosionConfiguration);
			explosion.setX(xCoordinate - (animation.getWidth() / 2));
			explosion.setY(yCoordinate - (animation.getHeight() / 2));
			
			ExplosionManager.getInstance().addExistingExplosion(explosion);
			this.setVisible(false);
		}
	}
	
	//Destroyed explosives explode
	public void destroyMissile() {
		ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(isFriendly(), damage, true);

		SpriteAnimationConfiguration rocketExplosionConfig = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
		rocketExplosionConfig.getSpriteConfiguration().setImageType(ImageEnums.Destroyed_Explosion);
		SpriteAnimation explosionAnimation = new SpriteAnimation(rocketExplosionConfig);

		explosionAnimation.setX(xCoordinate - (animation.getWidth() / 2));
		explosionAnimation.setY(yCoordinate - (animation.getHeight() / 2));
		explosionAnimation.setFrameDelay(3);


		Explosion explosion = new Explosion(rocketExplosionConfig, explosionConfiguration);
//		explosion.setX(xCoordinate - (animation.getWidth() / 2));
//		explosion.setY(yCoordinate - (animation.getHeight() / 2));
		
		ExplosionManager.getInstance().addExistingExplosion(explosion);
		this.setVisible(false);
	}

}