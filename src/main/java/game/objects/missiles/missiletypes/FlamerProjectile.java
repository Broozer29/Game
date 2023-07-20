package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;

public class FlamerProjectile extends Missile {

	public FlamerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed, boolean friendly, float damage) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed, yMovementSpeed);
		this.missileDamage = damage;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimetion(rotation);
	}

	public void missileAction() {
		if (missileStepsTaken % 5 == 0 && animation.getScale() < 2.00) {
			this.animation.setAnimationScale((float) (animation.getScale() + 0.05));
			;
		}
	}

}