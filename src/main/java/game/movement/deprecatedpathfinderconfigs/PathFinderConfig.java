package game.movement.deprecatedpathfinderconfigs;

import game.movement.Direction;
import game.movement.Point;

public interface PathFinderConfig {
	public void setStart(Point start);
	public Point getStart();
	public Direction getMovementDirection ();
	public void setMovementDirection (Direction movementDirection);
	public boolean isFriendly();
	public void setFriendly(boolean isFriendly);
	

	
}
