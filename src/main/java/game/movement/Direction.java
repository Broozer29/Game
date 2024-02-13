package game.movement;

public enum Direction {
	LEFT, RIGHT, UP, DOWN, RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN, NONE;
	
	
	
	public double toAngle() {
	    switch (this) {
	        case LEFT:
	            return 0;   // Facing left
	        case RIGHT:
	            return 180; // Facing right, flip horizontally
	        case UP:
	            return 90;  // Facing up, flip vertically
	        case DOWN:
	            return 270; // Facing down
	        case RIGHT_UP:
	            return 225; // Facing right and up, rotate 225 degrees
	        case RIGHT_DOWN:
	            return 135; // Facing right and down, rotate 135 degrees
	        case LEFT_UP:
	            return 45;  // Facing left and up, rotate 45 degrees
	        case LEFT_DOWN:
	            return 315; // Facing left and down, rotate 315 degrees
	        case NONE:
	        default:
	            return 0.0;
	    }
	}
}