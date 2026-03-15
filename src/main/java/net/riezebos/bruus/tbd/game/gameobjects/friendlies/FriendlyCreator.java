package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.MissileDrone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.SpecialAttackDrone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.spaceship.SpaceShip;
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

    public static Drone createDrone(GameObject owner) {
        SpaceShip spaceShip = (SpaceShip) owner;

        float scale = (float) 0.5;
        FriendlyObjectEnums friendlyType = FriendlyObjectEnums.Drone;

        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(owner.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(owner.getCenterYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.Drone);
        spriteConfiguration.setScale(scale);

        FriendlyObjectConfiguration friendlyObjectConfiguration = new FriendlyObjectConfiguration(friendlyType,
                getDroneAttackSpeed(spaceShip.getDroneType()), false);
        Drone object = FriendlyCreator.createDrone(spriteConfiguration, friendlyObjectConfiguration, spaceShip);
        object.getMovementConfiguration().setLastKnownTargetX(spaceShip.getCenterXCoordinate());
        object.getMovementConfiguration().setLastKnownTargetY(spaceShip.getCenterYCoordinate());
        object.getMovementConfiguration().setOrbitRadius(85);
        object.setAllowedVisualsToRotate(false);
        object.setOwnerOrCreator(spaceShip);
        return object;
    }


    private static Drone createDrone(SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration friendlyObjectConfiguration, GameObject owner) {
        SpaceShip spaceShip = (SpaceShip) owner;
        DroneTypes droneTypes = spaceShip.getDroneType();
        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, true);
        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setPathFinder(new OrbitPathFinder(spaceShip));
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        movementConfiguration.setXMovementSpeed(droneTypes.getMovementSpeed());
        movementConfiguration.setYMovementSpeed(droneTypes.getMovementSpeed());
        movementConfiguration.setLastUsedXMovementSpeed(droneTypes.getMovementSpeed());
        movementConfiguration.setLastUsedYMovementSpeed(droneTypes.getMovementSpeed());
        movementConfiguration.setDirection(Direction.RIGHT);
        movementConfiguration.setOrbitRadius(50);

        Drone drone = null;
        switch (droneTypes) {
            case Missile -> {
                drone = new MissileDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
            }
            case ElectroShred -> {
                drone = new SpecialAttackDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration, droneTypes);
            }
            case FireBall -> {
                drone = new SpecialAttackDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration, droneTypes);
            }
            default -> {
                drone = new MissileDrone(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
            }
        }
        drone.setOwnerOrCreator(owner);
        return drone;
    }

    public static Drone createProtossShip(DroneTypes droneType, GameObject owner) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(owner.getXCoordinate());
        spriteConfiguration.setyCoordinate(owner.getYCoordinate());
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
        movementConfiguration.setDestination(ProtossUtils.getRandomPoint(owner));
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
        drone.setOwnerOrCreator(owner);
        return drone;
    }

    public static Drone getCarrierBeacon(SpaceShip owner) {
        List<Drone> carrierDrones = FriendlyManager.getInstance().getDrones().stream()
                .filter(drone -> drone instanceof CarrierBeacon && drone.getOwnerOrCreator().equals(owner))
                .toList();

        if (!carrierDrones.isEmpty()) {
            return carrierDrones.get(0);
        }
        return null;
    }

    public static Drone createCarrierBeacon(GameObject owner) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(owner.getXCoordinate());
        spriteConfiguration.setyCoordinate(owner.getYCoordinate());
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

        CarrierBeacon carrierBeacon = new CarrierBeacon(spriteAnimationConfiguration, friendlyObjectConfiguration, movementConfiguration);
        carrierBeacon.setOwnerOrCreator(owner);
        return carrierBeacon;
    }

    private static float getDroneAttackSpeed(DroneTypes droneType) {
        switch (droneType) {
            case ProtossScout -> {
                return 0.35f;
            }
            case ProtossShuttle -> {
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
