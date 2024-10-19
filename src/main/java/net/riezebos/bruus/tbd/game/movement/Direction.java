package net.riezebos.bruus.tbd.game.movement;

public enum Direction {
	LEFT, RIGHT, UP, DOWN, RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN, NONE;

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
