package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visuals.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

public class GenericMissile extends Missile {

	public GenericMissile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.rotateImage(movementConfiguration.getRotation());
		initDestructionAnimation(missileConfiguration, movementConfiguration);

//		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
	}

	public GenericMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration){
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
		initDestructionAnimation(missileConfiguration, movementConfiguration);
	}

	private void initDestructionAnimation(MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration){
		if(missileConfiguration.getDestructionType() != null){
			SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(this.spriteConfiguration, 2, false);
			destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
			this.destructionAnimation = new SpriteAnimation(destructionAnimation);

			if(this.destructionAnimation.getImageEnum().equals(ImageEnums.LaserBulletDestruction)){
				this.destructionAnimation.rotateAnimation(movementConfiguration.getRotation(), false);
				this.destructionAnimation.setFrameDelay(1);
			}
		}
	}




	private int stepsTaken = 0;
	private int moduloDivider = 90;
	public void missileAction() {
		if(this.speedsUp){
			stepsTaken++;

			if(stepsTaken % moduloDivider == 0){
				this.movementConfiguration.setXMovementSpeed(this.getMovementConfiguration().getXMovementSpeed() * 1.2f);
				this.movementConfiguration.setYMovementSpeed(this.getMovementConfiguration().getYMovementSpeed() * 1.2f);
				moduloDivider = Math.round(moduloDivider * 1.5f);
			}
		}
	}

}