package game.gameobjects.enemies;

import game.gameobjects.enemies.boss.RedBoss;
import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.MovementPatternSize;
import game.movement.pathfinders.*;
import game.gameobjects.enemies.enemytypes.*;
import VisualAndAudioData.audio.enums.AudioEnums;
import game.gameobjects.enemies.enums.EnemyEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;

public class EnemyCreator {

    public static Enemy createEnemy (EnemyEnums type, int xCoordinate, int yCoordinate, Direction movementDirection, float scale,
                                     float xMovementSpeed, float yMovementSpeed, MovementPatternSize movementPatternSize, boolean boxCollision) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration(
                xCoordinate, yCoordinate, scale, type.getImageEnum(),
                0, 0, 1.0f, false, 0
        );

        PathFinder pathFinder = getPathFinderByEnemy(type);
        
        EnemyConfiguration enemyConfiguration = createEnemyConfiguration(type,boxCollision);
        MovementConfiguration movementConfiguration = createMovementConfiguration(xCoordinate, yCoordinate, movementDirection, xMovementSpeed, yMovementSpeed, movementPatternSize, pathFinder);
        adjustMovementConfigurationPerEnemy(type,movementConfiguration);
        return createSpecificEnemy(spriteConfiguration, enemyConfiguration, movementConfiguration);
    }

    private static PathFinder getPathFinderByEnemy(EnemyEnums enemyType){
        switch (enemyType){
            case Seeker, Energizer, Tazer, Scout, RedBoss -> {return new HoverPathFinder();}
            case FourDirectionalDrone, PulsingDrone -> {return new DestinationPathFinder();}
            default -> {return new RegularPathFinder();}
        }
    }

    private static void adjustMovementConfigurationPerEnemy(EnemyEnums enemyType, MovementConfiguration movementConfiguration){
        switch (enemyType){
            case Scout -> movementConfiguration.setBoardBlockToHoverIn(7);
            case Seeker -> {movementConfiguration.setBoardBlockToHoverIn(6);}
            case Energizer -> {movementConfiguration.setBoardBlockToHoverIn(5);}
            case Tazer -> {movementConfiguration.setBoardBlockToHoverIn(7);}
            case RedBoss -> {movementConfiguration.setBoardBlockToHoverIn(4);}
        }
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
        boolean hasAttack = enemyType.isHasAttack();
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
                return new PulsingDrone(upgradeConfig(spriteConfiguration, 5, true), enemyConfiguration, movementConfiguration);
            }
            case Shuriken -> {
                return new Shuriken(upgradeConfig(spriteConfiguration, 0, true), enemyConfiguration, movementConfiguration);
            }
            case RedBoss -> {
                return new RedBoss(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
            }
            case FourDirectionalDrone -> {
                return new FourDirectionalDrone(spriteConfiguration, enemyConfiguration, movementConfiguration);
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
                return new CashCarrier(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
        }
        return new Seeker(upgradeConfig(spriteConfiguration, 1, true), enemyConfiguration, movementConfiguration);
    }

    private static SpriteAnimationConfiguration upgradeConfig (SpriteConfiguration spriteConfiguration, int frameDelay, boolean infiniteLoop) {
        return new SpriteAnimationConfiguration(spriteConfiguration, frameDelay, infiniteLoop);
    }

    public static ArrayList<LaserOriginDrone> createLaserOriginDrones(int startingXCoordinate, int startingYCoordinate, int laserBodySegments){
        ArrayList<LaserOriginDrone> droneList = new ArrayList<>();
        EnemyConfiguration droneEnemyConfig = createEnemyConfiguration(EnemyEnums.LaserOriginDrone, false);


        SpriteConfiguration firstDroneConfig = createLaserDroneSpriteConfig();
        MovementConfiguration firstDroneMoveConfig = createMovementConfiguration(startingXCoordinate, startingYCoordinate,
                Direction.LEFT, 1f,1f, MovementPatternSize.SMALL, new DestinationPathFinder());
        firstDroneMoveConfig.setDestination(new Point(600,400));

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

    private static SpriteConfiguration createLaserDroneSpriteConfig (){
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setScale(EnemyEnums.LaserOriginDrone.getDefaultScale());
        spriteConfiguration.setImageType(EnemyEnums.LaserOriginDrone.getImageType());

        return spriteConfiguration;
    }
}
