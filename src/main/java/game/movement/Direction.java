package game.movement;

public enum Direction {
	LEFT, RIGHT, UP, DOWN, RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN, NONE;

	public double toAngle() {
		switch (this) {
			case LEFT:
				return 180; // Was 0, now flipped to 180 for left
			case RIGHT:
				return 0;   // Was 180, now flipped to 0 for right
			case UP:
				return 270; // Was 90, flipped to 270 for up
			case DOWN:
				return 90;  // Was 270, flipped to 90 for down
			case RIGHT_UP:
				return 315; // Was 225, adjusted to 315 for right and up
			case RIGHT_DOWN:
				return 45;  // Was 135, adjusted to 45 for right and down
			case LEFT_UP:
				return 225; // Was 45, adjusted to 225 for left and up
			case LEFT_DOWN:
				return 135; // Was 315, adjusted to 135 for left and down
			case NONE:
			default:
				return 0.0;
		}
	}
}
