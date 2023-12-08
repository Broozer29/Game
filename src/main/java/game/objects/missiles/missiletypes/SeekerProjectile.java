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
		this.damage = damage;
//		setAnimation();
		this.animation.setFrameDelay(3);
		rotateAccordingToSpeed();

	}

	private void rotateAccordingToSpeed() {
		switch (movementConfiguration.getRotation()) {
		case DOWN:
			this.animation.rotateAnimetion(movementConfiguration.getRotation());
			break;
		case LEFT:
			this.animation.rotateAnimetion(movementConfiguration.getRotation());
			break;
		case LEFT_DOWN:
			if (movementConfiguration.getYMovementSpeed() > movementConfiguration.getXMovementSpeed()) {
				this.animation.rotateAnimetion(movementConfiguration.getRotation());
			} else {
				this.animation.rotateAnimetion(Direction.LEFT);
			}
			break;
		case LEFT_UP:
			if (movementConfiguration.getYMovementSpeed() > movementConfiguration.getXMovementSpeed()) {
				this.animation.rotateAnimetion(movementConfiguration.getRotation());
			} else {
				this.animation.rotateAnimetion(Direction.LEFT);
			}
			break;
		case RIGHT:
			this.animation.rotateAnimetion(movementConfiguration.getRotation());
			break;
		case RIGHT_DOWN:
			if (movementConfiguration.getYMovementSpeed() > movementConfiguration.getXMovementSpeed()) {
				this.animation.rotateAnimetion(movementConfiguration.getRotation());
			} else {
				this.animation.rotateAnimetion(Direction.RIGHT);
			}
			break;
		case RIGHT_UP:
			if (movementConfiguration.getYMovementSpeed() > movementConfiguration.getXMovementSpeed()) {
				this.animation.rotateAnimetion(movementConfiguration.getRotation());
			} else {
				this.animation.rotateAnimetion(Direction.RIGHT);
			}
			break;
		case UP:
			this.animation.rotateAnimetion(movementConfiguration.getRotation());
			break;
		default:
			break;

		}
	}

	public void missileAction() {
		if (movementConfiguration.getStepsTaken() == 20) {
			movementConfiguration.setXMovementSpeed(movementConfiguration.getXMovementSpeed() + 1);
			movementConfiguration.setYMovementSpeed(movementConfiguration.getYMovementSpeed() + 1);
		} else if (movementConfiguration.getStepsTaken() == 30) {
			movementConfiguration.setXMovementSpeed(movementConfiguration.getXMovementSpeed() + 1);
			movementConfiguration.setYMovementSpeed(movementConfiguration.getYMovementSpeed() + 1);
		} else if (movementConfiguration.getStepsTaken() == 45) {
			movementConfiguration.setXMovementSpeed(movementConfiguration.getXMovementSpeed() + 1);
			movementConfiguration.setYMovementSpeed(movementConfiguration.getYMovementSpeed() + 1);
		}

	}

}
