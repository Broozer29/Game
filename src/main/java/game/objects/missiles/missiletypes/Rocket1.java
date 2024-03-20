package game.objects.missiles.missiletypes;

import game.movement.MovementConfiguration;
import game.objects.neutral.ExplosionManager;
import game.objects.missiles.MissileConfiguration;
import game.objects.neutral.Explosion;
import game.objects.missiles.Missile;
import game.objects.neutral.ExplosionConfiguration;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteAnimation;

public class Rocket1 extends Missile{

	float explosionDamage;
	
	public Rocket1(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.rotateAnimation(movementConfiguration.getRotation());
		this.damage = 0;
		this.explosionDamage = missileConfiguration.getDamage();
	}
	
	
	public void missileAction() {
		ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(isFriendly(), explosionDamage, true, true);

		SpriteAnimationConfiguration rocketExplosionConfig = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
		rocketExplosionConfig.getSpriteConfiguration().setImageType(ImageEnums.Destroyed_Explosion);
		SpriteAnimation explosionAnimation = new SpriteAnimation(rocketExplosionConfig);

		int explosionScale = 2;
		Explosion explosion = new Explosion(rocketExplosionConfig, explosionConfiguration);
		explosion.setScale(explosionScale);
		explosion.updateBoardBlock();
		explosion.setOwnerOrCreator(this.ownerOrCreator);
		explosionAnimation.setCenterCoordinates(this.getCenterXCoordinate() + (this.getWidth() / 2), this.getCenterYCoordinate() - (this.height / 2));
		ExplosionManager.getInstance().addExplosion(explosion);
	}
	

}
