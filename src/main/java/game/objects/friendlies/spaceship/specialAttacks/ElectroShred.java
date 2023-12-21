package game.objects.friendlies.spaceship.specialAttacks;

import game.objects.missiles.MissileConfiguration;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.SpriteAnimation;

public class ElectroShred extends SpecialAttack{

	public ElectroShred(SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteAnimationConfiguration, missileConfiguration);
		this.setObjectType("ElectroShred");
	}

	
}