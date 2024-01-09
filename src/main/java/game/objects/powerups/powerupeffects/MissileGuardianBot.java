package game.objects.powerups.powerupeffects;

import game.movement.Direction;
import game.objects.friendlies.*;
import game.objects.powerups.creation.PowerUpEffect;
import game.objects.powerups.PowerUpEnums;
import game.objects.player.PlayerManager;
import game.util.OrbitingObjectsFormatter;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class MissileGuardianBot extends PowerUpEffect {

	public MissileGuardianBot(PowerUpEnums powerUpType) {
		super(powerUpType);
	}

	@Override
	public void activatePower() {
		float scale = (float) 0.5;
		FriendlyObjectEnums friendlyType = FriendlyObjectEnums.Missile_Drone;


		SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
		spriteConfiguration.setImageType(ImageEnums.Drone);
		spriteConfiguration.setScale(scale);


		FriendlyObjectConfiguration friendlyObjectConfiguration = new FriendlyObjectConfiguration();
		friendlyObjectConfiguration.setAttackSpeedCooldown(100);
		friendlyObjectConfiguration.setMovementDirection(Direction.RIGHT);
		friendlyObjectConfiguration.setFriendlyType(FriendlyObjectEnums.Missile_Drone);

		FriendlyObject object = FriendlyCreator.createDrone(spriteConfiguration, friendlyObjectConfiguration, PlayerManager.getInstance().getSpaceship());

		PlayerManager.getInstance().getSpaceship().getObjectOrbitingThis().add(object);
		FriendlyManager.getInstance().addFriendlyObject(object);
		OrbitingObjectsFormatter.reformatOrbitingObjects(PlayerManager.getInstance().getSpaceship());

	}

	@Override
	public void deactivatePower() {

	}
}
