package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class DefaultPlayerLaserbeam extends Missile {

	public DefaultPlayerLaserbeam(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
	}

	public void missileAction() {

	}

}