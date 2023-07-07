package game.objects.friendlies.powerups.powerupeffects;

import game.managers.PlayerManager;
import game.movement.Direction;
import game.movement.OrbitPathFinder;
import game.movement.PathFinder;
import game.movement.Point;
import game.objects.friendlies.FriendlyEnums;
import game.objects.friendlies.FriendlyManager;
import game.objects.friendlies.powerups.PowerUpEffect;
import game.objects.friendlies.powerups.PowerUpEnums;

public class HomingMissileGuardianBot extends PowerUpEffect {

	public HomingMissileGuardianBot(PowerUpEnums powerUpType) {
		super(powerUpType);
	}

	@Override
	public void activatePower() {
		int xCoordinate = PlayerManager.getInstance().getSpaceship().getCenterXCoordinate();
		int yCoordinate = PlayerManager.getInstance().getSpaceship().getCenterYCoordinate();
		float scale = (float) 0.5;
		FriendlyEnums friendlyType = FriendlyEnums.Homing_Missile_Guardian_Bot;
		Point destination = null;
		Direction rotation = Direction.RIGHT;
		PathFinder pathFinder = new OrbitPathFinder(PlayerManager.getInstance().getSpaceship(), 75, 300);

		FriendlyManager.getInstance().createMissileGuardianBot(xCoordinate, yCoordinate, destination, rotation,
				friendlyType, scale, pathFinder);
	}

	@Override
	public void deactivatePower() {

	}
}
