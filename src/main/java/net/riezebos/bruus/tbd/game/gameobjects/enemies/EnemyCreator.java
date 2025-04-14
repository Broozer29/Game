package net.riezebos.bruus.tbd.game.gameobjects.enemies;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.carrier.CarrierBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.redboss.RedBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.spacestation.SpaceStationBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.CashCarrier;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossBeacon;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossPulsingDrone;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossScout;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossShuttle;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships.*;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg.*;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;

public class EnemyCreator {

    public static Enemy createEnemy (EnemyEnums type, int xCoordinate, int yCoordinate, Direction movementDirection, float scale,
                                     float xMovementSpeed, float yMovementSpeed, MovementPatternSize movementPatternSize, boolean boxCollision) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration(
                xCoordinate, yCoordinate, scale, type.getImageEnum(),
                0, 0, 1.0f, false, 0
        );

        PathFinder pathFinder = getPathFinderByEnemy(type);

        EnemyConfiguration enemyConfiguration = createEnemyConfiguration(type, boxCollision);
        MovementConfiguration movementConfiguration = createMovementConfiguration(xCoordinate, yCoordinate, movementDirection, xMovementSpeed, yMovementSpeed, movementPatternSize, pathFinder);
        adjustMovementConfigurationPerEnemy(type, movementConfiguration);
        return createSpecificEnemy(spriteConfiguration, enemyConfiguration, movementConfiguration);
    }

    private static PathFinder getPathFinderByEnemy (EnemyEnums enemyType) {
        switch (enemyType) {
            case Seeker, Energizer, Tazer, Scout, RedBoss, CarrierBoss, ZergDevourer, ZergGuardian, ZergQueen -> {
                return new HoverPathFinder();
            }
            case FourDirectionalDrone, PulsingDrone, SpaceStationBoss, CarrierPulsingDrone, EnemyCarrierBeacon -> {
                return new DestinationPathFinder();
            }
            case Shuriken -> {
                return new BouncingPathFinder();
            }
            default -> {
                return new RegularPathFinder();
            }
        }
    }

    private static void adjustMovementConfigurationPerEnemy (EnemyEnums enemyType, MovementConfiguration movementConfiguration) {
        switch (enemyType) {
            case ZergDevourer -> movementConfiguration.setBoardBlockToHoverIn(5);
            case ZergGuardian -> movementConfiguration.setBoardBlockToHoverIn(6);
            case ZergQueen -> movementConfiguration.setBoardBlockToHoverIn(7);
            case Scout -> movementConfiguration.setBoardBlockToHoverIn(7);
            case Seeker -> {
                movementConfiguration.setBoardBlockToHoverIn(6);
            }
            case Energizer -> {
                movementConfiguration.setBoardBlockToHoverIn(5);
            }
            case Tazer -> {
                movementConfiguration.setBoardBlockToHoverIn(7);
            }
            case RedBoss, CarrierBoss -> {
                movementConfiguration.setBoardBlockToHoverIn(4);
            }
            case SpaceStationBoss -> movementConfiguration.setDestination(calculateSpaceStationBossDestination());
        }
    }

    public static Point calculateSpaceStationBossDestination () {
        int windowHalfWidth = DataClass.getInstance().getWindowWidth() / 2;
        int windowHalfHeight = DataClass.getInstance().getPlayableWindowMaxHeight() / 2;

        int enemyHalfWidth = EnemyEnums.SpaceStationBoss.getBaseWidth() / 2;
        int enemyHalfHeight = EnemyEnums.SpaceStationBoss.getBaseHeight() / 2;

        return new Point(windowHalfWidth - enemyHalfWidth, windowHalfHeight - enemyHalfHeight);
    }

    private static MovementConfiguration createMovementConfiguration (int xCoordinate, int yCoordinate, Direction movementDirection, float xMovementSpeed, float yMovementSpeed,
                                                                      MovementPatternSize patternSize, PathFinder pathFinder) {
        MovementConfiguration moveConfig = new MovementConfiguration();
        moveConfig.setCurrentLocation(new Point(xCoordinate, yCoordinate));
        moveConfig.setXMovementSpeed(xMovementSpeed);
        moveConfig.setYMovementSpeed(yMovementSpeed);
        moveConfig.setPathFinder(pathFinder);
        moveConfig.setRotation(movementDirection);
        moveConfig.setPatternSize(patternSize);

        moveConfig.initDefaultSettingsForSpecializedPathFinders();
        //Specialized specific settings here if non-default settings need to be overwritten
        return moveConfig;
    }


    private static EnemyConfiguration createEnemyConfiguration (EnemyEnums enemyType, boolean boxCollision) {
        int maxHitpoints = enemyType.getBaseHitPoints();
        int maxShields = enemyType.getBaseShieldPoints();
        boolean hasAttack = enemyType.hasAttack();
        boolean showHealthBar = false;
        AudioEnums deathSound = enemyType.getDeathSound();
        boolean allowedToDealDamage = true;
        String objectType = enemyType.getObjectType();
        float baseArmor = enemyType.getBaseArmor();
        float xpOnDeath = enemyType.getXpOnDeath();
        float cashMoneyWorth = enemyType.getCashMoneyWorth();


        return new EnemyConfiguration(enemyType, maxHitpoints, maxShields
                , hasAttack, showHealthBar, deathSound, allowedToDealDamage,
                objectType, boxCollision, baseArmor, cashMoneyWorth, xpOnDeath);
    }

    private static Enemy createSpecificEnemy (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        switch (enemyConfiguration.getEnemyType()) {
            case PulsingDrone -> {
                return new PulsingDrone(upgradeConfig(spriteConfiguration, 10, true), enemyConfiguration, movementConfiguration);
            }
            case Shuriken -> {
                return new Shuriken(upgradeConfig(spriteConfiguration, 0, true), enemyConfiguration, movementConfiguration);
            }
            case SpaceStationBoss -> {
                return new SpaceStationBoss(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case RedBoss -> {
                return new RedBoss(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
            }
            case CarrierBoss -> {
                return new CarrierBoss(upgradeConfig(spriteConfiguration, 3 , true), enemyConfiguration, movementConfiguration);
            }
            case FourDirectionalDrone -> {
                return new FourDirectionalDrone(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
            case EnemyProtossScout -> {
                return new EnemyProtossScout(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case EnemyProtossShuttle -> {
                return new EnemyProtossShuttle(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case CarrierPulsingDrone -> {
                return new EnemyProtossPulsingDrone(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case EnemyCarrierBeacon -> {
                return new EnemyProtossBeacon(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case Alien_Bomb -> {
                return new AlienBomb(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
            case Seeker -> {
                return new Seeker(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
            }
            case Tazer -> {
                return new Tazer(upgradeConfig(spriteConfiguration, 4, true), enemyConfiguration, movementConfiguration);
            }
            case Energizer -> {
                return new Energizer(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
            }
            case Bulldozer -> {
                return new Bulldozer(upgradeConfig(spriteConfiguration, 3, true), enemyConfiguration, movementConfiguration);
            }
            case Flamer -> {
                return new Flamer(upgradeConfig(spriteConfiguration, 3, true), enemyConfiguration, movementConfiguration);
            }
            case Bomba -> {
                return new Bomba(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case Needler -> {
                return new Needler(upgradeConfig(spriteConfiguration, 4, true), enemyConfiguration, movementConfiguration);
            }
            case Scout -> {
                return new Scout(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
            }
            case CashCarrier -> {
                return new CashCarrier(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case ZergDevourer -> {
                return new Devourer(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case ZergGuardian -> {
                return new Guardian(upgradeConfig(spriteConfiguration, 4, true), enemyConfiguration, movementConfiguration);
            }
            case ZergScourge -> {
                return new Scourge(upgradeConfig(spriteConfiguration, 4, true), enemyConfiguration, movementConfiguration);
            }
            case DevourerCocoon, MutaGuardianCocoon -> {
                return new ZergCocoon(upgradeConfig(spriteConfiguration, 4, true), enemyConfiguration, movementConfiguration);
            }
            case ZergMutalisk -> {
                return new Mutalisk(upgradeConfig(spriteConfiguration, 4, true), enemyConfiguration, movementConfiguration);
            }
            case ZergQueen -> {
                return new Queen(upgradeConfig(spriteConfiguration, 4, true), enemyConfiguration, movementConfiguration);
            }

        }
        return new Seeker(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
    }

    private static SpriteAnimationConfiguration upgradeConfig (SpriteConfiguration spriteConfiguration, int frameDelay, boolean infiniteLoop) {
        return new SpriteAnimationConfiguration(spriteConfiguration, frameDelay, infiniteLoop);
    }

    public static ArrayList<LaserOriginDrone> createLaserOriginDrones (int startingXCoordinate, int startingYCoordinate, int laserBodySegments) {
        ArrayList<LaserOriginDrone> droneList = new ArrayList<>();
        EnemyConfiguration droneEnemyConfig = createEnemyConfiguration(EnemyEnums.LaserOriginDrone, false);


        SpriteConfiguration firstDroneConfig = createLaserDroneSpriteConfig();
        MovementConfiguration firstDroneMoveConfig = createMovementConfiguration(startingXCoordinate, startingYCoordinate,
                Direction.LEFT, 1f, 1f, MovementPatternSize.SMALL, new DestinationPathFinder());
        firstDroneMoveConfig.setDestination(new Point(600, 400));

        LaserOriginDrone firstDrone = new LaserOriginDrone(firstDroneConfig, droneEnemyConfig, firstDroneMoveConfig);
        firstDrone.setCenterCoordinates(startingXCoordinate, startingYCoordinate);
        firstDrone.initLaserbeam(laserBodySegments, Direction.LEFT);
        firstDrone.setAllowedVisualsToRotate(false);
        firstDrone.setAllowedToMove(false);
//        firstDrone.getLaserbeam().update();

//
//        SpriteAnimation finalBodyPart = firstDrone.getLaserbeam().getLaserBodies().get(firstDrone.getLaserbeam().getLaserBodies().size() - 1);
//        int secondDroneXCoordinate = finalBodyPart.getXCoordinate() + finalBodyPart.getWidth();
//        int secondDroneYCoordinate = firstDrone.getYCoordinate();
//
//
//        SpriteConfiguration secondDroneConfig = createLaserDroneSpriteConfig();
//        MovementConfiguration secondDroneMoveConfig = createMovementConfiguration(secondDroneXCoordinate, secondDroneYCoordinate,
//                Direction.LEFT, 1f,1f, MovementPatternSize.SMALL, new RegularPathFinder());
//        LaserOriginDrone secondDrone = new LaserOriginDrone(secondDroneConfig, droneEnemyConfig, secondDroneMoveConfig);
//        secondDrone.setXCoordinate(secondDroneXCoordinate);
//        secondDrone.setYCoordinate(secondDroneYCoordinate);
//
//        firstDrone.setConnectedDrone(secondDrone);

        droneList.add(firstDrone);
//        droneList.add(secondDrone);

        return droneList;
    }

    private static SpriteConfiguration createLaserDroneSpriteConfig () {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setScale(EnemyEnums.LaserOriginDrone.getDefaultScale());
        spriteConfiguration.setImageType(EnemyEnums.LaserOriginDrone.getImageType());

        return spriteConfiguration;
    }
}
