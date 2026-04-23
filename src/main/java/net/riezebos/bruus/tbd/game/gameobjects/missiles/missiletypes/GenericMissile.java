package net.riezebos.bruus.tbd.game.gameobjects.missiles.missiletypes;

import net.riezebos.bruus.tbd.game.gameobjects.missiles.Missile;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class GenericMissile extends Missile {

	public GenericMissile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.rotateGameObjectTowards(movementConfiguration.getRotation(), true);
		initDestructionAnimation(missileConfiguration, movementConfiguration);
		this.isDamageable = false;
		this.isDestructable = true;
//		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
	}

	public GenericMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration){
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
		initDestructionAnimation(missileConfiguration, movementConfiguration);
		this.isDamageable = false;
		this.isDestructable = true;
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

	public void missileAction() {
		if(this.speedsUp){
			stepsTaken++;

			if(stepsTaken % stepsBetweenSpeedUpIncrease == 0){
				this.movementConfiguration.setMovementSpeed(this.getMovementConfiguration().getMovementSpeed() * speedUpIncreaseAmount);
                stepsBetweenSpeedUpIncrease = Math.round(stepsBetweenSpeedUpIncrease * 1.5f);
			}
		}
	}
}