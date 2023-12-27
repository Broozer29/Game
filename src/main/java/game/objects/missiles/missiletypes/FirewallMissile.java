package game.objects.missiles.missiletypes;

import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class FirewallMissile extends Missile {

	public FirewallMissile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
		this.animation.setFrameDelay(10);

	}

	public void missileAction() {
		//Pulse with a firenova or something??
	}

}