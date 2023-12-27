package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class BulldozerProjectile extends Missile {

	public BulldozerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
//		setAnimation();
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());

	}

	public void missileAction() {

	}

}