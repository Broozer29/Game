package game.movement;

import java.util.ArrayList;
import java.util.List;

import data.DataClass;
import game.objects.friendlies.powerups.PowerUp;

public class PowerUpDirectionBouncer {

	private final int windowWidth;
	private final int windowHeight;
	private static PowerUpDirectionBouncer instance = new PowerUpDirectionBouncer();

	private PowerUpDirectionBouncer() {
		this.windowWidth = DataClass.getInstance().getWindowWidth();
		this.windowHeight = DataClass.getInstance().getWindowHeight();
	}
	
	public static PowerUpDirectionBouncer getInstance() {
		return instance;
	}

	public Path calculateNewPath(PowerUp powerUp) {
		Direction newDirection = getNewDirection(powerUp);
		List<Integer> endCoordinates = getNewEndpointCoordinates(powerUp, newDirection);

		Point newEndpoint = new Point(endCoordinates.get(0), endCoordinates.get(1));
		RegularPathFinder regPathFinder = new RegularPathFinder();
		boolean isFriendly = true;
		Path newPath = regPathFinder.findPath(powerUp.getPoint(), newEndpoint, 1, 1, newDirection, isFriendly);

		return newPath;
	}

	public Direction getNewDirection(PowerUp powerUp) {
		int x = powerUp.getXCoordinate();
		int y = powerUp.getYCoordinate();
		int width = powerUp.getWidth();
		int height = powerUp.getHeight();
		Direction currentDirection = powerUp.getDirection();

		boolean changeX = (isLeft(currentDirection) && x <= 0)
				|| (isRight(currentDirection) && (x + width) >= windowWidth);
		boolean changeY = (isUp(currentDirection) && y <= 0)
				|| (isDown(currentDirection) && (y + height) >= windowHeight);

		if (changeX && changeY)
			return flipDirection(currentDirection);
		else if (changeX)
			return flipXDirection(currentDirection);
		else if (changeY)
			return flipYDirection(currentDirection);

		return currentDirection;
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

	private List<Integer> getNewEndpointCoordinates(PowerUp powerUp, Direction newDirection) {
		int x = powerUp.getXCoordinate();
		int y = powerUp.getYCoordinate();
		int width = powerUp.getWidth();
		int height = powerUp.getHeight();

		List<Integer> newCoords = new ArrayList<Integer>();

		switch (newDirection) {
		case LEFT:
			x = 0;
			break;
		case RIGHT:
			x = windowWidth;
			break;
		case UP:
			y = 0;
			break;
		case DOWN:
			y = windowHeight - (height);
			break;
		case RIGHT_UP:
			x = windowWidth;
			y = 0;
			break;
		case RIGHT_DOWN:
			x = windowWidth;
			y = windowHeight - (height);
			break;
		case LEFT_UP:
			x = 0;
			y = 0;
			break;
		case LEFT_DOWN:
			x = 0;
			y = windowHeight - (height);
			break;
		default:
			break;
		}

		newCoords.add(x);
		newCoords.add(y);
		return newCoords;
	}
}