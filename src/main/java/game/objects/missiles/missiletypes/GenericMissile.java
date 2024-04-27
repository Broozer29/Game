package game.objects.missiles.missiletypes;

import game.movement.MovementConfiguration;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class GenericMissile extends Missile {

	public GenericMissile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.rotateImage(movementConfiguration.getRotation(), true);
//		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
	}

	public GenericMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration){
		super(spriteConfiguration, missileConfiguration, movementConfiguration);
		this.animation.rotateAnimation(movementConfiguration.getRotation(), true);
	}

	public void missileAction() {
		//Shouldn't do anything cause generic missile
	}

}