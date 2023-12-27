package game.objects.missiles.missiletypes;

import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.MissileConfiguration;
import game.objects.neutral.Explosion;
import game.objects.missiles.Missile;
import game.objects.neutral.ExplosionConfiguration;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class Rocket1 extends Missile{

	float explosionDamage;
	
	public Rocket1(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
		this.damage = 0;
		this.explosionDamage = missileConfiguration.getDamage();
	}
	
	
	public void missileAction() {
		ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(isFriendly(), explosionDamage, true);

		SpriteAnimationConfiguration rocketExplosionConfig = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
		rocketExplosionConfig.getSpriteConfiguration().setImageType(ImageEnums.Destroyed_Explosion);
		SpriteAnimation explosionAnimation = new SpriteAnimation(rocketExplosionConfig);

		int explosionScale = 2;
		Explosion explosion = new Explosion(rocketExplosionConfig, explosionConfiguration);
		explosion.setScale(explosionScale);
		explosion.updateBoardBlock();
		explosionAnimation.setCenterCoordinates(this.getCenterXCoordinate() + (this.getWidth() / 2), this.getCenterYCoordinate() - (this.height / 2));
		ExplosionManager.getInstance().addExistingExplosion(explosion);
	}
	

}
