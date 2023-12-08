package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.FriendlyEnums;
import game.objects.friendlies.FriendlyMover;
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

		FriendlyMover.getInstance().createMissileGuardianBot(friendlyType, scale);
	}

	@Override
	public void deactivatePower() {

	}
}
