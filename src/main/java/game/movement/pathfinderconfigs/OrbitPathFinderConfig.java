package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.Point;

public class OrbitPathFinderConfig implements PathFinderConfig {
	private Point start;
	private Direction fallbackDirection;
	private boolean friendly;

	public OrbitPathFinderConfig(Point start, Direction fallbackDirection, boolean friendly) {
		super();
		this.start = start;
		this.fallbackDirection = fallbackDirection;
		this.friendly = friendly;
	}

	@Override
	public void setStart(Point start) {
		this.start = start;
	}

	@Override
	public Point getStart() {
		return start;
	}

	@Override
	public Direction getFallbackDirection() {
		return fallbackDirection;
	}

	@Override
	public void setFallbackDirection(Direction fallbackDirection) {
		this.fallbackDirection = fallbackDirection;

	}

	@Override
	public boolean isFriendly() {
		return friendly;
	}

	@Override
	public void setFriendly(boolean isFriendly) {
		this.friendly = isFriendly;

	}

}