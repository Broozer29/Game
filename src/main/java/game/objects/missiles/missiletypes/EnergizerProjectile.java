package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class EnergizerProjectile extends Missile {

	public EnergizerProjectile(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
	}

	public void missileAction() {

	}

}