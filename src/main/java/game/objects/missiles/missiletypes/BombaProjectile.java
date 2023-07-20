
package game.objects.missiles.missiletypes;

import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.Explosion;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;
import visual.objects.SpriteAnimation;

public class BombaProjectile extends Missile {

	public BombaProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed, boolean isFriendly, float damage) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, isFriendly, xMovementSpeed, yMovementSpeed);
		this.missileDamage = damage;
		setAnimation();
		this.animation.setFrameDelay(3);
		this.animation.rotateAnimetion(rotation);
	
	}

	public void missileAction() {
		if (missileStepsTaken >= 75) {
			SpriteAnimation animation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Bomba_Missile_Explosion, false, scale);
			animation.setX(xCoordinate - (animation.getWidth() / 2));
			animation.setY(yCoordinate - (animation.getHeight() / 2));
			animation.setFrameDelay(3);
			Explosion explosion = new Explosion(xCoordinate, yCoordinate, scale, animation, 20, false);
			explosion.setX(xCoordinate - (animation.getWidth() / 2));
			explosion.setY(yCoordinate - (animation.getHeight() / 2));
			
			ExplosionManager.getInstance().addExistingExplosion(explosion);
			this.setVisible(false);
		}
	}

}