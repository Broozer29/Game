
package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visuals.audiodata.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

public class BombaProjectile extends Missile {

	public BombaProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);

		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);
		}
	}

	public void missileAction() {
		if (movementConfiguration.getStepsTaken() >= 75) {
			createExplosion();
		}
	}

	public void detonateMissile(){
		createExplosion();
	}

	private void createExplosion(){
		SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
		spriteConfiguration1.setxCoordinate(this.xCoordinate);
		spriteConfiguration1.setyCoordinate(this.yCoordinate);
		spriteConfiguration1.setScale(this.scale);

		SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 2, false);
		spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Missile_Explosion);

		ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(this.isFriendly(), damage, true, false);
		Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
		explosion.setOwnerOrCreator(this.ownerOrCreator);
		explosion.setScale(1.4f);
		explosion.getAnimation().setAnimationScale(1.4f);

		explosion.getAnimation().setOriginCoordinates(this.animation.getCenterXCoordinate(), this.animation.getCenterYCoordinate());
		explosion.setCenterCoordinates(this.animation.getCenterXCoordinate(), this.animation.getCenterYCoordinate());
		explosion.getAnimation().setCenterCoordinates(this.animation.getCenterXCoordinate(), this.animation.getCenterYCoordinate());
		ExplosionManager.getInstance().addExplosion(explosion);

		this.setVisible(false);
	}
	
}