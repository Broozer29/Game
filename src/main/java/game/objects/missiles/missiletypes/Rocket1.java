package game.objects.missiles.missiletypes;

import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.Explosion;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;
import visual.objects.SpriteAnimation;

public class Rocket1 extends Missile{

	float explosionDamage;
	
	public Rocket1(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed,
			int yMovementSpeed, float explosionDamage, boolean friendly) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed,
				yMovementSpeed);
		this.explosionType = ImageEnums.Rocket_1_Explosion;
		setAnimation();
		this.missileDamage = 0;
		this.explosionDamage = explosionDamage;
		this.animation.rotateAnimetion(rotation);
	}
	
	
	public void missileAction() {
		int explosionScale = 3;
		SpriteAnimation explosionAnimation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Destroyed_Explosion, false, explosionScale);
		Explosion explosion = new Explosion(this.xCoordinate, this.yCoordinate, 
				explosionScale, explosionAnimation, explosionDamage, true);
		explosion.updateCurrentBoardBlock();
		explosionAnimation.updateCurrentBoardBlock();
		explosionAnimation.setCenterCoordinates(this.getCenterXCoordinate() + 50, this.getCenterYCoordinate());
		ExplosionManager.getInstance().addExistingExplosion(explosion);
	}
	

}
