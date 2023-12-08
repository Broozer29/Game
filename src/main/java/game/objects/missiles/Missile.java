package game.objects.missiles;

import game.managers.AnimationManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import gamedata.image.ImageEnums;
import visual.objects.SpriteAnimation;

public class Missile extends GameObject {

	protected ImageEnums missileType;

	public Missile(int x, int y, Point destination, ImageEnums missileType, ImageEnums destructionType,
			Direction rotation, float scale, PathFinder pathFinder, boolean isFriendly, int xMovementSpeed,
			int yMovementSpeed) {
		super(x, y, scale);
		this.currentLocation = new Point(x, y);
		this.missileType = missileType;
		this.friendly = isFriendly;
		setMissileAnimation(missileType, destructionType, scale);
		initMovementConfiguration(currentLocation, destination, pathFinder, rotation, xMovementSpeed, yMovementSpeed);
		this.currentLocation = new Point(x, y);
	}
	
	private void initMovementConfiguration(Point currentLocation, Point destination, PathFinder pathFinder,
			Direction rotation, int xMovementSpeed, int yMovementSpeed) {
		movementConfiguration = new MovementConfiguration();
		movementConfiguration.setPathFinder(pathFinder);
		movementConfiguration.setCurrentLocation(currentLocation);
		movementConfiguration.setDestination(destination);
		movementConfiguration.setRotation(rotation);
		movementConfiguration.setXMovementSpeed(xMovementSpeed);
		movementConfiguration.setYMovementSpeed(yMovementSpeed);
		movementConfiguration.setStepsTaken(0);
		movementConfiguration.setHasLock(true);
	}

	public ImageEnums getMissileType() {
		return this.missileType;
	}

	private void setMissileAnimation (ImageEnums missileType, ImageEnums destructionType, float scale) {
		if (!missileType.equals(ImageEnums.Alien_Laserbeam) && !missileType.equals(ImageEnums.Player_Laserbeam)) {
			if (missileType != null) {
				this.animation = new SpriteAnimation(xCoordinate, yCoordinate, missileType, true, scale);
			}
		}
		if (destructionType != null) {
			this.destructionAnimation = new SpriteAnimation(xCoordinate, yCoordinate, destructionType, false, scale);
		}
	}

	public SpriteAnimation getAnimation() {
		if (this.animation != null) {
			return this.animation;
		}
		return null;
	}

	public void missileAction() {
		// Exists to be overriden
	}

	public void destroyMissile() {
		if (this.destructionAnimation != null) {
			AnimationManager.getInstance().addDestroyedExplosion(destructionAnimation);
		}
		this.setVisible(false);
	}

}