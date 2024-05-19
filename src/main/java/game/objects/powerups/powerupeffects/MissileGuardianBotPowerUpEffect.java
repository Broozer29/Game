package game.objects.powerups.powerupeffects;

import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import game.objects.friendlies.*;
import game.objects.powerups.creation.PowerUpEffect;
import game.objects.powerups.PowerUpEnums;
import game.objects.player.PlayerManager;
import game.util.OrbitingObjectsFormatter;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MissileGuardianBotPowerUpEffect extends PowerUpEffect {

    public MissileGuardianBotPowerUpEffect (PowerUpEnums powerUpType) {
        super(powerUpType);
    }

    @Override
    public void activatePower () {
        float scale = (float) 0.5;
        FriendlyObjectEnums friendlyType = FriendlyObjectEnums.Missile_Drone;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(PlayerManager.getInstance().getSpaceship().getXCoordinate());
        spriteConfiguration.setyCoordinate(PlayerManager.getInstance().getSpaceship().getYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.Drone);
        spriteConfiguration.setScale(scale);


        FriendlyObjectConfiguration friendlyObjectConfiguration = new FriendlyObjectConfiguration(friendlyType,
                1.5f, createOrbitingDronePathFinder(PlayerManager.getInstance().getSpaceship()),
                Direction.RIGHT, 1, 1, MovementPatternSize.SMALL, friendlyType.isBoxCollision(), friendlyType.isPermanentObject());
        FriendlyObject object = FriendlyCreator.createDrone(spriteConfiguration, friendlyObjectConfiguration);
        object.getMovementConfiguration().setLastKnownTargetX(PlayerManager.getInstance().getSpaceship().getCenterXCoordinate());
        object.getMovementConfiguration().setLastKnownTargetY(PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
        object.setAllowedVisualsToRotate(false);
        object.setScale(0.5f);
        object.getAnimation().setAnimationScale(0.5f);

        PlayerManager.getInstance().getSpaceship().getObjectOrbitingThis().add(object);
        OrbitingObjectsFormatter.reformatOrbitingObjects(PlayerManager.getInstance().getSpaceship(), 75);
        FriendlyManager.getInstance().addFriendlyObject(object);

    }

    private PathFinder createOrbitingDronePathFinder (GameObject targetToOrbit) {
        double meanX = targetToOrbit.getCenterXCoordinate();
        double meanY = targetToOrbit.getCenterYCoordinate();
        double nextAngle = 0;
        if (!targetToOrbit.getObjectOrbitingThis().isEmpty()) {
            List<Double> angles = new ArrayList<>();
            for (GameObject drone : targetToOrbit.getObjectOrbitingThis()) {
                double angle = Math.atan2(drone.getYCoordinate() - meanY, drone.getXCoordinate() - meanX);
                angles.add(angle);
            }
            Collections.sort(angles);
            double maxGap = 0;
            for (int i = 0; i < angles.size(); i++) {
                double gap = angles.get((i + 1) % angles.size()) - angles.get(i);
                if (gap < 0) {
                    gap += Math.PI * 2;
                }
                if (gap > maxGap) {
                    maxGap = gap;
                    nextAngle = angles.get(i) + gap / 2;
                }
            }
        }

        int radius = 75; // Example radius
        int x = (int) (meanX + Math.cos(nextAngle) * radius);
        int y = (int) (meanY + Math.sin(nextAngle) * radius);

        return new OrbitPathFinder(PlayerManager.getInstance().getSpaceship(), radius, 300, nextAngle);
    }

    @Override
    public void deactivatePower () {

    }
}
