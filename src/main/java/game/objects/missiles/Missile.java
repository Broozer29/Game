package game.objects.missiles;

import java.util.Locale.IsoCountryCode;

import game.managers.AnimationManager;
import game.managers.OnScreenTextManager;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Path;
import game.movement.Point;
import game.movement.SpriteMover;
import game.movement.SpriteRemover;
import game.movement.pathfinderconfigs.HomingPathFinderConfig;
import game.movement.pathfinderconfigs.OrbitPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import game.objects.friendlies.FriendlyEnums;
import game.objects.friendlies.powerups.OnScreenText;
import game.objects.friendlies.powerups.PowerUpEnums;
import gamedata.DataClass;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class Missile extends Sprite {

	
	protected MovementConfiguration moveConfig;
	protected float missileDamage;
	protected SpriteAnimation animation;
	protected SpriteAnimation explosionAnimation;
	protected ImageEnums missileType;
	protected ImageEnums explosionType;
	protected boolean isFriendly;


	public Missile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, boolean isFriendly, int xMovementSpeed,
			int yMovementSpeed) {
		super(x, y, scale);
		this.currentLocation = new Point(x, y);
		this.explosionType = explosionType;
		this.missileType = missileType;
		this.isFriendly = isFriendly;
		
		initMovementConfiguration(currentLocation, destination, pathFinder, rotation, xMovementSpeed, yMovementSpeed);
		this.currentLocation = new Point(x, y);
		this.setIsFriendly(isFriendly);
	}
	
	private void initMovementConfiguration(Point currentLocation, Point destination, PathFinder pathFinder,
			Direction rotation, int xMovementSpeed, int yMovementSpeed) {
		moveConfig = new MovementConfiguration();
		moveConfig.setPathFinder(pathFinder);
		moveConfig.setCurrentLocation(currentLocation);
		moveConfig.setDestination(destination);
		moveConfig.setRotation(rotation);
		moveConfig.setXMovementSpeed(xMovementSpeed);
		moveConfig.setYMovementSpeed(yMovementSpeed);
		moveConfig.setStepsTaken(0);
		moveConfig.setHasLock(true);
	}



	public void move() {
		SpriteMover.getInstance().moveSprite(this, moveConfig);
		if (this.animation != null) {
			if (moveConfig.getCurrentPath().getWaypoints().size() > 0) {
				animation.setX(moveConfig.getCurrentPath().getWaypoints().get(0).getX());
				animation.setY(moveConfig.getCurrentPath().getWaypoints().get(0).getY());
				animation.updateCurrentBoardBlock();
			}
		}
		
		bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);
		updateCurrentBoardBlock();
		updateAnimationCoordinates();
		updateVisibility();
	}


	//Remove the missile if it's out of bounds, and remove it's animation if it has one
	private void updateVisibility() {
		if(SpriteRemover.getInstance().shouldRemoveVisibility(this, moveConfig)) {
			this.visible = false;
		}
		
		if (!this.isVisible() && this.animation != null) {
			this.animation.setVisible(false);
		}
		
	}

	// Sets the animations (the graphics of missile) to align with the missiles
	// coordinates
	private void updateAnimationCoordinates() {
		if (animation != null) {
			animation.setX(xCoordinate);
			animation.setY(yCoordinate);
			animation.setAnimationBounds(animation.getXCoordinate(), animation.getYCoordinate());
			animation.updateCurrentBoardBlock();
		}

		if (explosionAnimation != null) {
			explosionAnimation.setX(xCoordinate);
			explosionAnimation.setY(yCoordinate);
			explosionAnimation.setAnimationBounds(explosionAnimation.getXCoordinate(),
					explosionAnimation.getYCoordinate());
			explosionAnimation.updateCurrentBoardBlock();
		}
	}

	public float getMissileDamage() {
		return this.missileDamage;
	}

	public ImageEnums getMissileType() {
		return this.missileType;
	}

	protected void setAnimation() {
		if (!missileType.equals(ImageEnums.Alien_Laserbeam) && !missileType.equals(ImageEnums.Player_Laserbeam)) {
			if (missileType != null) {
				this.animation = new SpriteAnimation(xCoordinate, yCoordinate, missileType, true, scale);
			}
		}
		if (explosionType != null) {
			this.explosionAnimation = new SpriteAnimation(xCoordinate, yCoordinate, explosionType, false, scale);
		}
	}

	public SpriteAnimation getExplosionAnimation() {
		if (this.explosionAnimation != null) {
			return this.explosionAnimation;
		}
		return null;
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

	public boolean isFriendly() {
		return this.isFriendly;
	}

	public Sprite getTarget() {
		return moveConfig.getTarget();
	}

	public void setTarget(Sprite target) {
		moveConfig.setTarget(target);
	}

	public void rotateMissileAnimation(Direction direction) {
		this.animation.rotateAnimetion(direction);
	}

	public void destroyMissile() {
		if (explosionAnimation != null) {
			AnimationManager.getInstance().addDestroyedExplosion(explosionAnimation);
		}
		this.setVisible(false);
	}

}