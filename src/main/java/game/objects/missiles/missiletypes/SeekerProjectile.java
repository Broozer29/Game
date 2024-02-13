package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.Point;
import game.objects.missiles.Missile;
import game.objects.missiles.MissileConfiguration;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;

public class SeekerProjectile extends Missile {

	public SeekerProjectile(SpriteAnimationConfiguration spriteConfiguration, MissileConfiguration missileConfiguration) {
		super(spriteConfiguration, missileConfiguration);
		this.animation.setFrameDelay(3);
//		rotateAccordingToSpeed();
	}

	private void rotateAccordingToSpeed() {
		switch (movementConfiguration.getRotation()) {
		case DOWN:
			this.animation.rotateAnimation(movementConfiguration.getRotation());
			break;
		case LEFT:
			this.animation.rotateAnimation(movementConfiguration.getRotation());
			break;
		case LEFT_DOWN:
			if (movementConfiguration.getYMovementSpeed() > movementConfiguration.getXMovementSpeed()) {
				this.animation.rotateAnimation(movementConfiguration.getRotation());
			} else {
				this.animation.rotateAnimation(Direction.LEFT);
			}
			break;
		case LEFT_UP:
			if (movementConfiguration.getYMovementSpeed() > movementConfiguration.getXMovementSpeed()) {
				this.animation.rotateAnimation(movementConfiguration.getRotation());
			} else {
				this.animation.rotateAnimation(Direction.LEFT);
			}
			break;
		case RIGHT:
			this.animation.rotateAnimation(movementConfiguration.getRotation());
			break;
		case RIGHT_DOWN:
			if (movementConfiguration.getYMovementSpeed() > movementConfiguration.getXMovementSpeed()) {
				this.animation.rotateAnimation(movementConfiguration.getRotation());
			} else {
				this.animation.rotateAnimation(Direction.RIGHT);
			}
			break;
		case RIGHT_UP:
			if (movementConfiguration.getYMovementSpeed() > movementConfiguration.getXMovementSpeed()) {
				this.animation.rotateAnimation(movementConfiguration.getRotation());
			} else {
				this.animation.rotateAnimation(Direction.RIGHT);
			}
			break;
		case UP:
			this.animation.rotateAnimation(movementConfiguration.getRotation());
			break;
		default:
			break;

		}
	}

	public void missileAction() {
//		if (movementConfiguration.getStepsTaken() == 20) {
//			movementConfiguration.setXMovementSpeed(movementConfiguration.getXMovementSpeed() + 1);
//			movementConfiguration.setYMovementSpeed(movementConfiguration.getYMovementSpeed() + 1);
//			movementConfiguration.setCurrentLocation(new Point(this.xCoordinate, this.yCoordinate));
//			movementConfiguration.setDestination(movementConfiguration.getCurrentPath().getWaypoints().get(movementConfiguration.getCurrentPath().getWaypoints().size() - 1));
//		} else if (movementConfiguration.getStepsTaken() == 30) {
//			movementConfiguration.setXMovementSpeed(movementConfiguration.getXMovementSpeed() + 1);
//			movementConfiguration.setYMovementSpeed(movementConfiguration.getYMovementSpeed() + 1);
//			movementConfiguration.setCurrentLocation(new Point(this.xCoordinate, this.yCoordinate));
//			movementConfiguration.setDestination(movementConfiguration.getCurrentPath().getWaypoints().get(movementConfiguration.getCurrentPath().getWaypoints().size() - 1));
//		} else if (movementConfiguration.getStepsTaken() == 45) {
//			movementConfiguration.setXMovementSpeed(movementConfiguration.getXMovementSpeed() + 1);
//			movementConfiguration.setYMovementSpeed(movementConfiguration.getYMovementSpeed() + 1);
//			movementConfiguration.setCurrentLocation(new Point(this.xCoordinate, this.yCoordinate));
//			movementConfiguration.setDestination(movementConfiguration.getCurrentPath().getWaypoints().get(movementConfiguration.getCurrentPath().getWaypoints().size() - 1));
//		}
	}

}
