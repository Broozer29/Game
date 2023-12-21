package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class FlamerProjectile extends Missile {

	public FlamerProjectile(SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.rotateAnimetion(missileConfiguration.getMovementDirection());
		this.animation.setFrameDelay(3);
	}

	public void missileAction() {
		if (movementConfiguration.getStepsTaken() % 5 == 0 && animation.getScale() < 2.00) {
			this.animation.setAnimationScale((float) (animation.getScale() + 0.05));
			;
		}
	}

}