package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
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
		switch (moveConfig.getRotation()) {
		case DOWN:
			this.animation.rotateAnimetion(moveConfig.getRotation());
			break;
		case LEFT:
			this.animation.rotateAnimetion(moveConfig.getRotation());
			break;
		case LEFT_DOWN:
			if (moveConfig.getYMovementSpeed() > moveConfig.getXMovementSpeed()) {
				this.animation.rotateAnimetion(moveConfig.getRotation());
			} else {
				this.animation.rotateAnimetion(Direction.LEFT);
			}
			break;
		case LEFT_UP:
			if (moveConfig.getYMovementSpeed() > moveConfig.getXMovementSpeed()) {
				this.animation.rotateAnimetion(moveConfig.getRotation());
			} else {
				this.animation.rotateAnimetion(Direction.LEFT);
			}
			break;
		case RIGHT:
			this.animation.rotateAnimetion(moveConfig.getRotation());
			break;
		case RIGHT_DOWN:
			if (moveConfig.getYMovementSpeed() > moveConfig.getXMovementSpeed()) {
				this.animation.rotateAnimetion(moveConfig.getRotation());
			} else {
				this.animation.rotateAnimetion(Direction.RIGHT);
			}
			break;
		case RIGHT_UP:
			if (moveConfig.getYMovementSpeed() > moveConfig.getXMovementSpeed()) {
				this.animation.rotateAnimetion(moveConfig.getRotation());
			} else {
				this.animation.rotateAnimetion(Direction.RIGHT);
			}
			break;
		case UP:
			this.animation.rotateAnimetion(moveConfig.getRotation());
			break;
		default:
			break;

		}
	}

	public void missileAction() {
		if (moveConfig.getStepsTaken() == 20) {
			moveConfig.setXMovementSpeed(moveConfig.getXMovementSpeed() + 1);
			moveConfig.setYMovementSpeed(moveConfig.getYMovementSpeed() + 1);
		} else if (moveConfig.getStepsTaken() == 30) {
			moveConfig.setXMovementSpeed(moveConfig.getXMovementSpeed() + 1);
			moveConfig.setYMovementSpeed(moveConfig.getYMovementSpeed() + 1);
		} else if (moveConfig.getStepsTaken() == 45) {
			moveConfig.setXMovementSpeed(moveConfig.getXMovementSpeed() + 1);
			moveConfig.setYMovementSpeed(moveConfig.getYMovementSpeed() + 1);
		}

	}

}
