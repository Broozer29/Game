package net.riezebos.bruus.tbd.game.movement;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum Direction {
	LEFT,
	RIGHT,
	UP, DOWN, RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN, NONE;
	public static Direction getRandomDiagonalDirection(){
		List<Direction> filteredDirections = Arrays.stream(Direction.values())
				.filter(direction -> !direction.equals(Direction.NONE) && !direction.equals(Direction.LEFT)
						&& !direction.equals(Direction.RIGHT) && !direction.equals(Direction.UP) && !direction.equals(Direction.DOWN))
				.collect(Collectors.toList());
		if (filteredDirections.isEmpty()) {
			return Direction.LEFT_DOWN;
		}
		return filteredDirections.get(new Random().nextInt(filteredDirections.size()));
	}

	public double toAngle() {
		switch (this) {
			case LEFT:
				return 180;
			case RIGHT:
				return 0;
			case UP:
				return 270;
			case DOWN:
				return 90;
			case RIGHT_UP:
				return 315;
			case RIGHT_DOWN:
				return 45;
			case LEFT_UP:
				return 225;
			case LEFT_DOWN:
				return 135;
			case NONE:
			default:
				return 0.0;
		}
	}
}
