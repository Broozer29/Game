package game.objects.missiles.missiletypes;

import data.image.ImageEnums;
import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.Missile;

public class SeekerProjectile extends Missile {

	public SeekerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType, Direction rotation,
			float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed, boolean friendly, float damage) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed, yMovementSpeed);
		this.missileDamage = damage;
		setAnimation();
		this.animation.setFrameDelay(3);
		rotateAccordingToSpeed();
		}
	
	private void rotateAccordingToSpeed() {
		switch(this.rotation) {
		case DOWN:
			this.animation.rotateAnimetion(rotation);
			break;
		case LEFT:
			this.animation.rotateAnimetion(rotation);
			break;
		case LEFT_DOWN:
			if(yMovementSpeed > xMovementSpeed) {
				this.animation.rotateAnimetion(rotation);
			} else {
				this.animation.rotateAnimetion(Direction.LEFT);
			}
			break;
		case LEFT_UP:
			if(yMovementSpeed > xMovementSpeed) {
				this.animation.rotateAnimetion(rotation);
			} else {
				this.animation.rotateAnimetion(Direction.LEFT);
			}
			break;
		case RIGHT:
			this.animation.rotateAnimetion(rotation);
			break;
		case RIGHT_DOWN:
			if(yMovementSpeed > xMovementSpeed) {
				this.animation.rotateAnimetion(rotation);
			} else {
				this.animation.rotateAnimetion(Direction.RIGHT);
			}
			break;
		case RIGHT_UP:
			if(yMovementSpeed > xMovementSpeed) {
				this.animation.rotateAnimetion(rotation);
			} else {
				this.animation.rotateAnimetion(Direction.RIGHT);
			}
			break;
		case UP:
			this.animation.rotateAnimetion(rotation);
			break;
		default:
			break;
		
		}
	}

	
	public void missileAction() {
		
	}

}
 	