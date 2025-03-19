package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.MissileDrone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossShuttle;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.SpecialAttackDrone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossArbiter;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossScout;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.DamageReduction;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FriendlyCreator {

    public static Drone createDrone() {
        float scale = (float) 0.5;
        FriendlyObjectEnums friendlyType = FriendlyObjectEnums.Drone;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(PlayerManager.getInstance().getSpaceship().getXCoordinate());
        spriteConfiguration.setyCoordinate(PlayerManager.getInstance().getSpaceship().getYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.Drone);
        spriteConfiguration.setScale(scale);

        FriendlyObjectConfiguration friendlyObjectConfiguration = new FriendlyObjectConfiguration(friendlyType,
                getDroneAttackSpeed(PlayerStats.getInstance().getDroneType()), false);
        Drone object = FriendlyCreator.createDrone(spriteConfiguration, friendlyObjectConfiguration);
        object.getMovementConfiguration().setLastKnownTargetX(PlayerManager.getInstance().getSpaceship().getCenterXCoordinate());
        object.getMovementConfiguration().setLastKnownTargetY(PlayerManager.getInstance().getSpaceship().getCenterYCoordinate());
        object.getMovementConfiguration().setOrbitRadius(85);
        object.setAllowedVisualsToRotate(false);
        return object;
    }


    private static Drone createDrone(SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyObjectConfiguration) {
        DroneTypes droneTypes = PlayerStats.getInstance().getDroneType();
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(new OrbitPathFinder(
                PlayerManager.getInstance().getSpaceship(), 85, 300, 0));
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        movementConfiguration.setXMovementSpeed(droneTypes.getMovementSpeed());
        movementConfiguration.setYMovementSpeed(droneTypes.getMovementSpeed());
        movementConfiguration.setLastUsedXMovementSpeed(droneTypes.getMovementSpeed());
        movementConfiguration.setLastUsedYMovementSpeed(droneTypes.getMovementSpeed());
        movementConfiguration.setRotation(Direction.RIGHT);
        movementConfiguration.setOrbitRadius(50);


        switch (droneTypes) {
            case Missile -> {
                return new MissileDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
            }
            case ElectroShred -> {
                return new SpecialAttackDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration, droneTypes);
            }
            case FireBall -> {
                return new SpecialAttackDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration, droneTypes);
            }
            default -> {
                return new MissileDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
            }
        }
    }

    public static Drone createProtossShip(DroneTypes droneType) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(PlayerManager.getInstance().getSpaceship().getXCoordinate());
        spriteConfiguration.setyCoordinate(PlayerManager.getInstance().getSpaceship().getYCoordinate());
        spriteConfiguration.setScale(getProtossScale(droneType));
        spriteConfiguration.setImageType(droneType.getCorrespondingImageEnum());

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        FriendlyObjectConfiguration friendlyObjectConfiguration = new FriendlyObjectConfiguration(FriendlyObjectEnums.Drone,
                getDroneAttackSpeed(droneType), false);

        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(new DestinationPathFinder());
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        movementConfiguration.setXMovementSpeed(droneType.getMovementSpeed());
        movementConfiguration.setYMovementSpeed(droneType.getMovementSpeed());
        movementConfiguration.setLastUsedXMovementSpeed(droneType.getMovementSpeed());
        movementConfiguration.setLastUsedYMovementSpeed(droneType.getMovementSpeed());
        movementConfiguration.setRotation(Direction.RIGHT);
        movementConfiguration.setDestination(ProtossUtils.getRandomPoint());

        Drone drone = null;
        switch (droneType) {
            case ProtossScout -> {
                drone = new ProtossScout(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
            }
            case ProtossShuttle -> {
                drone = new ProtossShuttle(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
            }
            case ProtossArbiter -> {
                drone = new ProtossArbiter(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
            }
        }

        DamageReduction damageReduction = new DamageReduction(9999999, 0.5f, null);
        drone.addEffect(damageReduction);
        return drone;

    }

    private static float getDroneAttackSpeed(DroneTypes droneType) {
        switch (droneType) {
            case ProtossScout -> {
                return 0.35f;
            }
            default -> {
                return 0.5f;
            }
        }
    }

    private static float getProtossScale(DroneTypes droneType){
        switch (droneType) {
            case ProtossScout -> {
                return 0.2f;
            }
            case ProtossArbiter -> {
                return 0.3f;
            }
            default -> {
                return 0.2f;
            }
        }
    }

}
