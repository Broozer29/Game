package game.movement.pathfinders;

import java.util.ArrayList;
import java.util.List;

import game.movement.Direction;
import game.movement.Path;
import game.movement.Point;
import game.movement.pathfinderconfigs.BouncingPathFinderConfig;
import game.movement.pathfinderconfigs.PathFinderConfig;
import game.movement.pathfinderconfigs.RegularPathFinderConfig;
import VisualAndAudioData.DataClass;
import visualobjects.Sprite;

public class BouncingPathFinder implements PathFinder {

	private final int windowWidth;
	private final int windowHeight;

	public BouncingPathFinder() {
		this.windowWidth = DataClass.getInstance().getWindowWidth();
		this.windowHeight = DataClass.getInstance().getWindowHeight();
	}

	@Override
	public Path findPath(PathFinderConfig pathFinderConfig) {
		if (!(pathFinderConfig instanceof BouncingPathFinderConfig)) {
			throw new IllegalArgumentException("Expected BouncingPathFinderConfig");
		} else {

			int xCoordinate = ((BouncingPathFinderConfig) pathFinderConfig).getXCoordinate();
			int yCoordinate = ((BouncingPathFinderConfig) pathFinderConfig).getYCoordinate();
			int spriteWidth = ((BouncingPathFinderConfig) pathFinderConfig).getSpriteWidth();
			int spriteHeight = ((BouncingPathFinderConfig) pathFinderConfig).getSpriteHeight();
			Direction spriteRotation = ((BouncingPathFinderConfig) pathFinderConfig).getSpriteDirection();

			Direction newDirection = getNewDirection(((BouncingPathFinderConfig) pathFinderConfig).getSprite(), spriteRotation);
			List<Integer> endCoordinates = getNewEndpointCoordinates(xCoordinate,yCoordinate, spriteWidth, spriteHeight, newDirection);


			Point newEndpoint = new Point(endCoordinates.get(0), endCoordinates.get(1));
			RegularPathFinder regPathFinder = new RegularPathFinder();
			boolean isFriendly = true;

			RegularPathFinderConfig config = new RegularPathFinderConfig(((BouncingPathFinderConfig) pathFinderConfig).getCurrentLocation(), newEndpoint, 2,
					2, isFriendly, newDirection);
			Path newPath = regPathFinder.findPath(config);
			return newPath;
		}
	}

	@Override
	public Direction getNextStep(Point currentLocation, Path path, Direction fallbackDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean shouldRecalculatePath(Path path) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Point calculateInitialEndpoint(Point start, Direction rotation, boolean friendly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point calculateEndPointBySteps(Point start, Direction rotation, int steps, int xMovementspeed,
			int yMovementspeed) {
		// TODO Auto-generated method stub
		return null;
	}

	public Direction getNewDirection(Sprite sprite, Direction spriteDirection) {
		int xCoordinate = sprite.getXCoordinate();
		int yCoordinate = sprite.getYCoordinate();
		int spriteWidth = sprite.getWidth();
		int spriteHeight = sprite.getHeight();
		
		
		boolean changeX = (isLeft(spriteDirection) && xCoordinate <= 0)
				|| (isRight(spriteDirection) && (xCoordinate + spriteWidth) >= windowWidth);
		boolean changeY = (isUp(spriteDirection) && yCoordinate <= 0)
				|| (isDown(spriteDirection) && (yCoordinate + spriteHeight) >= windowHeight);

		if (changeX && changeY)
			return flipDirection(spriteDirection);
		else if (changeX)
			return flipXDirection(spriteDirection);
		else if (changeY)
			return flipYDirection(spriteDirection);

		return spriteDirection;
	}

	private Direction flipDirection(Direction direction) {
		switch (direction) {
		case LEFT:
			return Direction.RIGHT;
		case RIGHT:
			return Direction.LEFT;
		case UP:
			return Direction.DOWN;
		case DOWN:
			return Direction.UP;
		case LEFT_UP:
			return Direction.RIGHT_DOWN;
		case LEFT_DOWN:
			return Direction.RIGHT_UP;
		case RIGHT_UP:
			return Direction.LEFT_DOWN;
		case RIGHT_DOWN:
			return Direction.LEFT_UP;
		default:
			return direction;
		}
	}

	private Direction flipXDirection(Direction direction) {
		switch (direction) {
		case LEFT:
			return Direction.RIGHT;
		case RIGHT:
			return Direction.LEFT;
		case LEFT_UP:
			return Direction.RIGHT_UP;
		case LEFT_DOWN:
			return Direction.RIGHT_DOWN;
		case RIGHT_UP:
			return Direction.LEFT_UP;
		case RIGHT_DOWN:
			return Direction.LEFT_DOWN;
		default:
			return direction;
		}
	}

	private Direction flipYDirection(Direction direction) {
		switch (direction) {
		case UP:
			return Direction.DOWN;
		case DOWN:
			return Direction.UP;
		case LEFT_UP:
			return Direction.LEFT_DOWN;
		case LEFT_DOWN:
			return Direction.LEFT_UP;
		case RIGHT_UP:
			return Direction.RIGHT_DOWN;
		case RIGHT_DOWN:
			return Direction.RIGHT_UP;
		default:
			return direction;
		}
	}

	private boolean isLeft(Direction direction) {
		return direction == Direction.LEFT || direction == Direction.LEFT_UP || direction == Direction.LEFT_DOWN;
	}

	private boolean isRight(Direction direction) {
		return direction == Direction.RIGHT || direction == Direction.RIGHT_UP || direction == Direction.RIGHT_DOWN;
	}

	private boolean isUp(Direction direction) {
		return direction == Direction.UP || direction == Direction.LEFT_UP || direction == Direction.RIGHT_UP;
	}

	private boolean isDown(Direction direction) {
		return direction == Direction.DOWN || direction == Direction.LEFT_DOWN || direction == Direction.RIGHT_DOWN;
	}

	private List<Integer> getNewEndpointCoordinates(int xCoordinate, int yCoordinate, int spriteWidth, int spriteHeight, Direction newDirection) {
		List<Integer> newCoords = new ArrayList<Integer>();

		switch (newDirection) {
		case LEFT:
			xCoordinate = 0;
			break;
		case RIGHT:
			xCoordinate = windowWidth;
			break;
		case UP:
			yCoordinate = 0;
			break;
		case DOWN:
			yCoordinate = windowHeight - (spriteHeight);
			break;
		case RIGHT_UP:
			xCoordinate = windowWidth;
			yCoordinate = 0;
			break;
		case RIGHT_DOWN:
			xCoordinate = windowWidth;
			yCoordinate = windowHeight - (spriteHeight);
			break;
		case LEFT_UP:
			xCoordinate = 0;
			yCoordinate = 0;
			break;
		case LEFT_DOWN:
			xCoordinate = 0;
			yCoordinate = windowHeight - (spriteHeight);
			break;
		default:
			break;
		}

		newCoords.add(xCoordinate);
		newCoords.add(yCoordinate);
		return newCoords;
	}

}