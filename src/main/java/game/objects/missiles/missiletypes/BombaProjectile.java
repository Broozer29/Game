
package game.objects.missiles.missiletypes;

import game.movement.MovementConfiguration;
import game.objects.neutral.ExplosionManager;
import game.objects.missiles.MissileConfiguration;
import game.objects.neutral.Explosion;
import game.objects.missiles.Missile;
import game.objects.neutral.ExplosionConfiguration;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class BombaProjectile extends Missile {

	public BombaProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
	}

	public void missileAction() {
		if (movementConfiguration.getStepsTaken() >= 75) {
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
	
}