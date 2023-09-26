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

public class MissileGuardianBot extends PowerUpEffect {

	public MissileGuardianBot(PowerUpEnums powerUpType) {
		super(powerUpType);
	}

	@Override
	public void activatePower() {
		float scale = (float) 0.5;
		FriendlyEnums friendlyType = FriendlyEnums.Missile_Guardian_Bot;

		FriendlyManager.getInstance().createMissileGuardianBot(friendlyType, scale);
	}

	@Override
	public void deactivatePower() {

	}
}
