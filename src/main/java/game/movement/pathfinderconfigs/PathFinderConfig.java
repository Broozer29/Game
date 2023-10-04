package game.movement.pathfinderconfigs;

import game.movement.Direction;
import game.movement.Point;
import game.objects.friendlies.powerups.PowerUp;

public interface PathFinderConfig {
	public void setStart(Point start);
	public Point getStart();
	public Direction getFallbackDirection();
	public void setFallbackDirection(Direction fallbackDirection);
	public boolean isFriendly();
	public void setFriendly(boolean isFriendly);
	

	
}
