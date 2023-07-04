package game.movement;

public enum Direction {
	LEFT, RIGHT, UP, DOWN, RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN, NONE;
	
	public double toAngle() {
        switch (this) {
            case LEFT:
                return 0;
            case RIGHT:
                return 90.0;
            case UP:
                return 0.0;
            case DOWN:
                return 180.0;
            case RIGHT_UP:
                return 45.0;
            case RIGHT_DOWN:
                return 135.0;
            case LEFT_UP:
                return 315.0;
            case LEFT_DOWN:
                return 225.0;
            case NONE:
            default:
                return 0.0;
        }
    }
	
	public double toAngleReal() {
        switch (this) {
            case LEFT:
                return 0;
            case RIGHT:
                return 180.0;
            case UP:
                return 0.0;
            case DOWN:
                return 180.0;
            case RIGHT_UP:
                return 45.0;
            case RIGHT_DOWN:
                return 135.0;
            case LEFT_UP:
                return 315.0;
            case LEFT_DOWN:
                return 225.0;
            case NONE:
            default:
                return 0.0;
        }
    }
}