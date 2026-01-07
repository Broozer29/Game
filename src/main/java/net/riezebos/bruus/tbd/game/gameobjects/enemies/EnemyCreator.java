package net.riezebos.bruus.tbd.game.gameobjects.enemies;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.CashCarrier;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.BlueBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.BlueBossFactory;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.BlueBossFactoryDefender;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.carrier.CarrierBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.redboss.RedBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.spacestation.SpaceStationBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker.StrikerBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker.StrikerBossLaserbeamClone;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker.StrikerCornerDrone;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.twinboss.TwinBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss.YellowBoss;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.minibosses.*;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.CarrierPulsinDrone;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossBeacon;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossScout;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss.EnemyProtossShuttle;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.spaceships.*;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.zerg.*;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameMode;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.*;
import net.riezebos.bruus.tbd.visualsandaudio.data.DataClass;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class EnemyCreator {

    public static Enemy createEnemy(EnemyEnums type, int xCoordinate, int yCoordinate, Direction movementDirection, float scale,
                                    float movementSpeed) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration(
                xCoordinate, yCoordinate, scale, type.getImageEnum(),
                0, 0, 1.0f, false, 0
        );

        PathFinder pathFinder = getPathFinderByEnemy(type);

        EnemyConfiguration enemyConfiguration = createEnemyConfiguration(type);
        MovementConfiguration movementConfiguration = createMovementConfiguration(xCoordinate, yCoordinate, movementDirection, movementSpeed, pathFinder);
        adjustMovementConfigurationPerEnemy(type, movementConfiguration);
        return createSpecificEnemy(spriteConfiguration, enemyConfiguration, movementConfiguration);
    }

    private static PathFinder getPathFinderByEnemy(EnemyEnums enemyType) {
        boolean isFormatted = GameState.getInstance().getGameMode().equals(GameMode.Formatted);

        switch (enemyType) {
            case Seeker, Energizer, Tazer, Scout, RedBoss, CarrierBoss, ZergDevourer, ZergGuardian, ZergQueen,
                 YellowBoss, MotherShipMiniBoss, StrikerBoss, TwinBoss -> {
                if (isFormatted && (
                        enemyType.equals(EnemyEnums.Scout) || enemyType.equals(EnemyEnums.Energizer) || enemyType.equals(EnemyEnums.Seeker)
                                || enemyType.equals(EnemyEnums.Tazer) || enemyType.equals(EnemyEnums.ZergDevourer) || enemyType.equals(EnemyEnums.ZergGuardian))) {
                    return new StraightLinePathFinder();
                }
                return new HoverPathFinder();
            }
            case FourDirectionalDrone, PulsingDrone, SpaceStationBoss, CarrierPulsingDrone, EnemyCarrierBeacon,
                 DefenderMiniBoss, LaserbeamMiniBoss, StrikerBossLaserbeamClone, StrikerBossCornerDrone -> {
                return new DestinationPathFinder();
            }
            case Shuriken, ShurikenMiniBoss, MirageMiniBoss -> {
                return new BouncingPathFinder();
            }
            case BlueBoss, BlueBossFactory -> {
                return new FloatingPathFinder();
            }
            default -> {
                return new RegularPathFinder();
            }
        }
    }

    private static void adjustMovementConfigurationPerEnemy(EnemyEnums enemyType, MovementConfiguration movementConfiguration) {
        switch (enemyType) {
            case ZergDevourer, Energizer -> setBoardBlockToHoverIn(movementConfiguration, 5);
            case ZergGuardian -> setBoardBlockToHoverIn(movementConfiguration, 6);
            case Seeker, YellowBoss -> {
                setBoardBlockToHoverIn(movementConfiguration, 6);
            }
            case Tazer, MotherShipMiniBoss, Scout, ZergQueen, StrikerBoss, TwinBoss -> {
                setBoardBlockToHoverIn(movementConfiguration, 7);
            }
            case RedBoss, CarrierBoss -> {
                setBoardBlockToHoverIn(movementConfiguration, 4);
            }
            case SpaceStationBoss, DefenderMiniBoss, LaserbeamMiniBoss ->
                    movementConfiguration.setDestination(calculateSpaceStationBossDestination(enemyType));
        }
    }

    private static void setBoardBlockToHoverIn(MovementConfiguration movementConfiguration, int boardBlockToHoverIn) {
        if (movementConfiguration.getRotation().equals(Direction.RIGHT)) {
            movementConfiguration.setBoardBlockToHoverIn(Math.max(7 - boardBlockToHoverIn, 1));
        } else {
            movementConfiguration.setBoardBlockToHoverIn(boardBlockToHoverIn);
        }
    }

    public static Point calculateSpaceStationBossDestination(EnemyEnums enemyEnums) {
        int windowHalfWidth = DataClass.getInstance().getWindowWidth() / 2;
        int windowHalfHeight = DataClass.getInstance().getPlayableWindowMaxHeight() / 2;

        int enemyHalfWidth = Math.round((enemyEnums.getDefaultScale() * enemyEnums.getBaseWidth()) / 2);
        int enemyHalfHeight = Math.round((enemyEnums.getDefaultScale() * enemyEnums.getBaseHeight()) / 2);

        return new Point(windowHalfWidth - enemyHalfWidth, windowHalfHeight - enemyHalfHeight);
    }

    public static MovementConfiguration createMovementConfiguration(int xCoordinate, int yCoordinate, Direction movementDirection, float movementSpeed, PathFinder pathFinder) {
        MovementConfiguration moveConfig = new MovementConfiguration();
        moveConfig.setCurrentLocation(new Point(xCoordinate, yCoordinate));
        moveConfig.setXMovementSpeed(movementSpeed);
        moveConfig.setYMovementSpeed(movementSpeed);
        moveConfig.setPathFinder(pathFinder);
        moveConfig.setDirection(movementDirection);
        moveConfig.setPatternSize(MovementPatternSize.SMALL);

        moveConfig.initDefaultSettingsForSpecializedPathFinders();
        //Specialized specific settings here if non-default settings need to be overwritten
        return moveConfig;
    }


    private static EnemyConfiguration createEnemyConfiguration(EnemyEnums enemyType) {
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
                objectType, false, baseArmor, cashMoneyWorth, xpOnDeath);
    }

    private static Enemy createSpecificEnemy(SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        switch (enemyConfiguration.getEnemyType()) {
            case PulsingDrone -> {
                return new PulsingDrone(upgradeConfig(spriteConfiguration, 10, true), enemyConfiguration, movementConfiguration);
            }
            case Shuriken -> {
                return new Shuriken(upgradeConfig(spriteConfiguration, 0, true), enemyConfiguration, movementConfiguration);
            }
            case ShurikenMiniBoss -> {
                return new ShurikenMiniBoss(upgradeConfig(spriteConfiguration, 0, true), enemyConfiguration, movementConfiguration);
            }
            case MotherShipMiniBoss -> {
                return new MotherShipMiniBoss(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
            }
            case SpaceStationBoss -> {
                return new SpaceStationBoss(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case RedBoss -> {
                return new RedBoss(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
            }
            case CarrierBoss -> {
                return new CarrierBoss(upgradeConfig(spriteConfiguration, 3, true), enemyConfiguration, movementConfiguration);
            }
            case YellowBoss -> {
                return new YellowBoss(upgradeConfig(spriteConfiguration, 3, true), enemyConfiguration, movementConfiguration);
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
                return new CarrierPulsinDrone(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
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
            case MotherShipDrone -> {
                return new MotherShipDrone(upgradeConfig(spriteConfiguration, 3, true), enemyConfiguration, movementConfiguration);
            }
            case MirageMiniBoss -> {
                return new MirageMiniBoss(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case DefenderMiniBoss -> {
                return new DefenderMiniBoss(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case LaserbeamMiniBoss -> {
                return new LaserbeamMiniBoss(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case StrikerBoss -> {
                return new StrikerBoss(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case StrikerBossLaserbeamClone -> {
                return new StrikerBossLaserbeamClone(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case StrikerBossCornerDrone -> {
                return new StrikerCornerDrone(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case BlueBoss -> {
                return new BlueBoss(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case BlueBossFactory -> {
                return new BlueBossFactory(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case BlueBossFactoryDefender -> {
                return new BlueBossFactoryDefender(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }
            case TwinBoss -> {
                return new TwinBoss(upgradeConfig(spriteConfiguration, 2, true), enemyConfiguration, movementConfiguration);
            }

        }
        return new Seeker(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
    }

    private static SpriteAnimationConfiguration upgradeConfig(SpriteConfiguration spriteConfiguration, int frameDelay, boolean infiniteLoop) {
        return new SpriteAnimationConfiguration(spriteConfiguration, frameDelay, infiniteLoop);
    }
}
