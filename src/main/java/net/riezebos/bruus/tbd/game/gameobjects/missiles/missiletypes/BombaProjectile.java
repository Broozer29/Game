
package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class BombaProjectile extends Missile {

	private int amountOfAnimationCyclesBeforeExplosion = 0;
	private static float explosionSize = 2.5f;

	public BombaProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.setFrameDelay(5);
		this.isDamageable = false;
		this.isDestructable = true;

		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 1, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);
			this.destructionAnimation.setAnimationScale(0.75f);
		}
	}

	public void missileAction() {
		if(this.animation.getFrameDelay() == 0){
			//Prepare the missile for explosion
			if(this.animation.getCurrentFrame() == this.animation.getTotalFrames()){
				amountOfAnimationCyclesBeforeExplosion++;
				this.movementConfiguration.setXMovementSpeed(this.movementConfiguration.getXMovementSpeed() * 0.9f);
				this.movementConfiguration.setYMovementSpeed(this.movementConfiguration.getYMovementSpeed() * 0.9f);
			}

			//Explode the missile
			if(amountOfAnimationCyclesBeforeExplosion > 4) {
				this.detonateMissile();
			}
		}


		//Speed up the animation after travelling a distance
		if(movementConfiguration.getStepsTaken() % 25 == 0){
			int newFrameDelay = Math.max(0, this.animation.getFrameDelay() - 1);
			//Slow the missile down if the animation isn't at max speed yet
			if(newFrameDelay > 0){
				this.movementConfiguration.setXMovementSpeed(this.movementConfiguration.getXMovementSpeed() * 0.8f);
				this.movementConfiguration.setYMovementSpeed(this.movementConfiguration.getYMovementSpeed() * 0.8f);
			}
			this.animation.setFrameDelay(newFrameDelay);
		}

	}

	public void detonateMissile(){
		createExplosion();
	}

	private void createExplosion(){

		SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
		spriteConfiguration1.setxCoordinate(this.xCoordinate);
		spriteConfiguration1.setyCoordinate(this.yCoordinate);
		spriteConfiguration1.setScale(explosionSize);

		SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 1, false);
		spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bomba_Missile_Explosion);

		ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(this.isFriendly(), damage, true, false);
		Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
		explosion.setOwnerOrCreator(this.ownerOrCreator);
		explosion.setScale(explosionSize);
		explosion.setCenterCoordinates(this.animation.getCenterXCoordinate(), this.animation.getCenterYCoordinate());
		ExplosionManager.getInstance().addExplosion(explosion);

		this.setVisible(false);
	}
	
}