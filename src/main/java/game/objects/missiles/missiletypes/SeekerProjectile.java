package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;

public class SeekerProjectile extends Missile {

	public SeekerProjectile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, int xMovementSpeed, int yMovementSpeed,
			boolean friendly, float damage) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, friendly, xMovementSpeed,
				yMovementSpeed);
		this.missileDamage = damage;
		setAnimation();
		this.animation.setFrameDelay(3);
		rotateAccordingToSpeed();

	}

	private void rotateAccordingToSpeed() {
		switch (this.rotation) {
		case DOWN:
			this.animation.rotateAnimetion(rotation);
			break;
		case LEFT:
			this.animation.rotateAnimetion(rotation);
			break;
		case LEFT_DOWN:
			if (yMovementSpeed > xMovementSpeed) {
				this.animation.rotateAnimetion(rotation);
			} else {
				this.animation.rotateAnimetion(Direction.LEFT);
			}
			break;
		case LEFT_UP:
			if (yMovementSpeed > xMovementSpeed) {
				this.animation.rotateAnimetion(rotation);
			} else {
				this.animation.rotateAnimetion(Direction.LEFT);
			}
			break;
		case RIGHT:
			this.animation.rotateAnimetion(rotation);
			break;
		case RIGHT_DOWN:
			if (yMovementSpeed > xMovementSpeed) {
				this.animation.rotateAnimetion(rotation);
			} else {
				this.animation.rotateAnimetion(Direction.RIGHT);
			}
			break;
		case RIGHT_UP:
			if (yMovementSpeed > xMovementSpeed) {
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
		if (this.missileStepsTaken == 20) {
			this.xMovementSpeed += 1;
			this.yMovementSpeed += 1;
		} else if (this.missileStepsTaken == 30) {
			this.xMovementSpeed += 1;
			this.yMovementSpeed += 1;
		} else if (this.missileStepsTaken == 45) {
			this.xMovementSpeed += 1;
			this.yMovementSpeed += 1;
		}

	}

}
