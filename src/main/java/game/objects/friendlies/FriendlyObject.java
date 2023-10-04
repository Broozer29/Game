package game.objects.friendlies;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Path;
import game.movement.Point;
import game.movement.SpriteMover;
import game.movement.pathfinderconfigs.HomingPathFinderConfig;
import game.movement.pathfinderconfigs.OrbitPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.movement.pathfinders.RegularPathFinder;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class FriendlyObject extends Sprite {

	// FriendlyObject combat stats
	protected float hitPoints;
	protected float maxHitPoints;
	protected float attackSpeedFrameCount;
	protected float currentAttackSpeedFrameCount = 0;
	protected boolean hasAttack;

	// FriendlyObject movement:
	private MovementConfiguration moveConfig;

	// FriendlyObject miscellanious attributes
	protected FriendlyEnums friendlyType;
	protected AudioEnums deathSound;
	protected boolean showHealthBar;
	protected SpriteAnimation exhaustAnimation = null;
	protected SpriteAnimation deathAnimation = null;
	protected SpriteAnimation animation = null;

	public FriendlyObject(int x, int y, Point destination, Direction rotation, FriendlyEnums friendlyType, float scale,
			PathFinder pathFinder) {
		super(x, y, scale);
		this.friendlyType = friendlyType;
		this.loadImage(ImageEnums.Test_Image);

		initMovementConfiguration(new Point(x, y), destination, pathFinder, rotation);
		this.currentLocation = new Point(x, y);
		this.setIsFriendly(true);

	}

	private void initMovementConfiguration(Point currentLocation, Point destination, PathFinder pathFinder,
			Direction rotation) {
		moveConfig = new MovementConfiguration();
		moveConfig.setPathFinder(pathFinder);
		moveConfig.setCurrentLocation(currentLocation);
		moveConfig.setDestination(destination);
		moveConfig.setRotation(rotation);

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

		if (this.exhaustAnimation != null) {
			this.exhaustAnimation.setX(this.getCenterXCoordinate() + (this.getWidth() / 2));
			this.exhaustAnimation.setY(this.getCenterYCoordinate() - (exhaustAnimation.getHeight() / 2));
		}

		bounds.setBounds(xCoordinate + xOffset, yCoordinate + yOffset, width, height);

		if (animation != null) {
			animation.setAnimationBounds(xCoordinate, yCoordinate);
		}
	}

	public SpriteAnimation getAnimation() {
		return animation;
	}

	public void setAnimation(SpriteAnimation animation) {
		this.animation = animation;
	}

	public void setPathFinder(PathFinder newPathFinder) {
		if (this.moveConfig != null) {
			this.moveConfig.setPathFinder(newPathFinder);
		} else {
			System.out.println("Tried to adjust a PathFinder without having an existing MovementConfiguration");
		}
	}

}