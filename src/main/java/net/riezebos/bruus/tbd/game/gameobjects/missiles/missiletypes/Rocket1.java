package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visuals.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;

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
