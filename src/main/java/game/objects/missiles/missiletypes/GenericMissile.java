package game.objects.missiles.missiletypes;

import VisualAndAudioData.image.ImageEnums;
import game.movement.MovementConfiguration;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class GenericMissile extends Missile {

	public GenericMissile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.rotateImage(movementConfiguration.getRotation(), true);
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

			if(this.destructionAnimation.getImageType().equals(ImageEnums.LaserBulletDestruction)){
				this.destructionAnimation.rotateAnimation(movementConfiguration.getRotation(), false);
				this.destructionAnimation.setFrameDelay(1);
			}
		}
	}


	public void missileAction() {
		//Shouldn't do anything cause generic missile
	}

}