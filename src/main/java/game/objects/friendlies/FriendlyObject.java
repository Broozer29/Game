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
import game.objects.GameObject;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;
import visual.objects.Sprite;
import visual.objects.SpriteAnimation;

public class FriendlyObject extends GameObject {
	//Whats the purpose of friendly objects like this?
	protected FriendlyEnums friendlyType;

	public FriendlyObject(int x, int y, Point destination, Direction rotation, FriendlyEnums friendlyType, float scale,
			PathFinder pathFinder) {
		super(x, y, scale);
		this.friendlyType = friendlyType;
		loadImage(ImageEnums.Test_Image);
		initMovementConfiguration(new Point(x, y), destination, pathFinder, rotation);
		this.currentLocation = new Point(x, y);
		this.setFriendly(true);
	}

	private void initMovementConfiguration (Point currentLocation, Point destination, PathFinder pathFinder,
											  Direction rotation) {
		movementConfiguration = new MovementConfiguration();
		movementConfiguration.setPathFinder(pathFinder);
		movementConfiguration.setCurrentLocation(currentLocation);
		movementConfiguration.setDestination(destination);
		movementConfiguration.setRotation(rotation);
	}
}