package game.objects.friendlies.powerups.powerupeffects;

import game.objects.friendlies.Drones.DroneEnums;
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
		DroneEnums friendlyType = DroneEnums.Missile_Guardian_Bot;

		FriendlyManager.getInstance().createMissileGuardianBot(friendlyType, scale);
	}

	@Override
	public void deactivatePower() {

	}
}
