package game.objects.missiles.missiletypes;

import game.movement.Direction;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.missiles.Missile;
import gamedata.image.ImageEnums;

public class OrbitingMissile extends Missile {

	public OrbitingMissile(int x, int y, Point destination, ImageEnums missileType, ImageEnums explosionType,
			Direction rotation, float scale, PathFinder pathFinder, boolean isFriendly, int xMovementSpeed,
			int yMovementSpeed) {
		super(x, y, destination, missileType, explosionType, rotation, scale, pathFinder, isFriendly, xMovementSpeed,
				yMovementSpeed);
		// TODO Auto-generated constructor stub
		
		
		
	}

}
