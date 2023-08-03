package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.Point;

public class HomingPathFinderConfig implements PathFinderConfig {

	private Point start;
	private Direction falbackDirection;
	private boolean isHoming = true;
	private boolean isFriendly;

	public HomingPathFinderConfig(Point start, Direction falbackDirection, boolean isHoming, boolean isFriendly) {
		super();
		this.start = start;
		this.falbackDirection = falbackDirection;
		this.isHoming = isHoming;
		this.isFriendly = isFriendly;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public boolean isHoming() {
		return isHoming;
	}

	public void setHoming(boolean isHoming) {
		this.isHoming = isHoming;
	}

	public boolean isFriendly() {
		return isFriendly;
	}

	public void setFriendly(boolean isFriendly) {
		this.isFriendly = isFriendly;
	}

	@Override
	public Direction getFallbackDirection() {
		return this.falbackDirection;
	}

	@Override
	public void setFallbackDirection(Direction fallbackDirection) {
		this.falbackDirection = fallbackDirection;

	}

}
