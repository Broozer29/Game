package game.objects.missiles.missiletypes;

import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class GenericMissile extends Missile {

	public GenericMissile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
	}

	public GenericMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration){
		super(spriteConfiguration, missileConfiguration);
	}

	public void missileAction() {
		//Shouldn't do anything cause generic missile
	}

}