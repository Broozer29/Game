package game.movement;

public enum Direction {
	LEFT, RIGHT, UP, DOWN, RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN, NONE;
	
	public double toAngle() {
        switch (this) {
            case LEFT:
                return 0; // correct
            case RIGHT:
                return 180.0; // correct
            case UP:
                return 90.0; // correct
            case DOWN:
                return 270.0; //correct
            case RIGHT_UP:
                return 135.0; // corrected
            case RIGHT_DOWN:
                return 225.0; // corrected
            case LEFT_UP:
                return 45.0; // corrected
            case LEFT_DOWN:
                return 315.0; // corrected
            case NONE:
            default:
                return 0.0;
        }
    }
	
	public double toAngleOld() {
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