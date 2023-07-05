package game.objects.missiles;

import data.image.ImageEnums;
import game.managers.ExplosionManager;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.Explosion;
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
	}
	
	
	public void missileAction() {
		int explosionScale = 2;
		SpriteAnimation explosionAnimation = new SpriteAnimation(xCoordinate, yCoordinate, ImageEnums.Destroyed_Explosion, false, explosionScale);
		int centeredExplosionX = xCoordinate - (explosionAnimation.getWidth() / 2);
		int centeredExplosionY = yCoordinate - (explosionAnimation.getHeight() / 2);
		
		Explosion explosion = new Explosion(centeredExplosionX, centeredExplosionY, 
				explosionScale, explosionAnimation, explosionDamage, true);
		explosion.updateCurrentBoardBlock();
		explosionAnimation.updateCurrentBoardBlock();
		ExplosionManager.getInstance().addExistingExplosion(explosion);
	}
	

}
