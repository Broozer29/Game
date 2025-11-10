package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.MissileDrone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.*;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.SpecialAttackDrone;
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

import java.util.List;

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
        movementConfiguration.setDirection(Direction.RIGHT);
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
        movementConfiguration.setDirection(Direction.RIGHT);
        movementConfiguration.setDestination(ProtossUtils.getRandomPoint());
        DamageReduction damageReduction = new DamageReduction(9999999, 0.25f, null);
        Drone drone = null;
        switch (droneType) {
            case ProtossScout -> {
                drone = new ProtossScout(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
                drone.addEffect(damageReduction);
            }
            case ProtossShuttle -> {
                drone = new ProtossShuttle(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
                drone.addEffect(damageReduction);
            }
            case ProtossArbiter -> {
                drone = new ProtossArbiter(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
                drone.addEffect(damageReduction);
            }
            case ProtossCorsair -> {
                drone = new ProtossCorsair(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
                //no damage reduction since its a suicide bomber but requires playtesting to see if it actually needs it
            }
        }
        return drone;
    }

    public static Drone getCarrierBeacon() {
        List<Drone> carrierDrones = FriendlyManager.getInstance().getDrones().stream()
                .filter(drone -> drone instanceof CarrierDrone)
                .toList();

        if (!carrierDrones.isEmpty()) {
            return carrierDrones.get(0);
        }
        return null;
    }

    public static Drone createCarrierBeacon(){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(PlayerManager.getInstance().getSpaceship().getXCoordinate());
        spriteConfiguration.setyCoordinate(PlayerManager.getInstance().getSpaceship().getYCoordinate());
        spriteConfiguration.setScale(getProtossScale(DroneTypes.CarrierDrone));
        spriteConfiguration.setImageType(DroneTypes.CarrierDrone.getCorrespondingImageEnum());

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        FriendlyObjectConfiguration friendlyObjectConfiguration = new FriendlyObjectConfiguration(FriendlyObjectEnums.Drone,
                1, false);

        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(new DestinationPathFinder());
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        movementConfiguration.setXMovementSpeed(DroneTypes.CarrierDrone.getMovementSpeed());
        movementConfiguration.setYMovementSpeed(DroneTypes.CarrierDrone.getMovementSpeed());
        movementConfiguration.setDirection(Direction.RIGHT);

        return new CarrierDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
    }

    private static float getDroneAttackSpeed(DroneTypes droneType) {
        switch (droneType) {
            case ProtossScout -> {
                return 0.35f;
            }
            case ProtossShuttle ->{
                return 1.5f;
            }

            default -> {
                return 0.5f;
            }
        }
    }

    private static float getProtossScale(DroneTypes droneType) {
        switch (droneType) {
            case ProtossScout -> {
                return 0.2f;
            }
            case ProtossArbiter -> {
                return 0.3f;
            }
            case CarrierDrone -> {
                return 0.5f;
            }
            case ProtossCorsair -> {
                return 0.3f;
            }
            default -> {
                return 0.2f;
            }
        }
    }

}
