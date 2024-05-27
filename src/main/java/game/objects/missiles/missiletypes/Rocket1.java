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
		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
		this.damage = 0;
		this.explosionDamage = missileConfiguration.getDamage();

		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);
		}
	}
	
	
	public void missileAction() {

	}

	@Override
	public void detonateMissile(){
		ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(isFriendly(), explosionDamage, true, true);
		SpriteAnimationConfiguration rocketExplosionConfig = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
		rocketExplosionConfig.getSpriteConfiguration().setImageType(ImageEnums.Destroyed_Explosion);

		float explosionScale = 1.5f;
		Explosion explosion = new Explosion(rocketExplosionConfig, explosionConfiguration);
		explosion.setScale(explosionScale);
		explosion.setOwnerOrCreator(this.ownerOrCreator);
		explosion.setCenterCoordinates(this.getCenterXCoordinate() + explosion.getWidth() / 2, this.getCenterYCoordinate());
		ExplosionManager.getInstance().addExplosion(explosion);
	}
	

}
