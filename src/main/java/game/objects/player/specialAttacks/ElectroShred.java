package game.objects.player.specialAttacks;

import game.objects.missiles.MissileConfiguration;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;

public class ElectroShred extends SpecialAttack{

	public ElectroShred(SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteAnimationConfiguration, missileConfiguration);
		this.setObjectType("ElectroShred");
	}

	
}