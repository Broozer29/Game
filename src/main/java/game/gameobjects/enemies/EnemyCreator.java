package game.gameobjects.enemies;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.deprecatedpathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.*;
import game.gameobjects.enemies.enemytypes.*;
import VisualAndAudioData.audio.enums.AudioEnums;
import game.gameobjects.enemies.enums.EnemyEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class EnemyCreator {

    public static Enemy createEnemy (EnemyEnums type, int xCoordinate, int yCoordinate, Direction movementDirection, float scale,
                                     float xMovementSpeed, float yMovementSpeed, MovementPatternSize movementPatternSize, boolean boxCollision) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration(
                xCoordinate,
//                800,
                yCoordinate,
                scale,
                type.getImageEnum(),
                0, 0,
                (float) 1.0, false, 0
        );



        PathFinder pathFinder = getPathFinderByEnemy(type);
        
        EnemyConfiguration enemyConfiguration = createEnemyConfiguration(type,boxCollision);
        MovementConfiguration movementConfiguration = createMovementConfiguration(xCoordinate, yCoordinate, movementDirection, xMovementSpeed, yMovementSpeed, movementPatternSize, pathFinder);
        adjustMovementConfigurationPerEnemy(type,movementConfiguration);
        return createSpecificEnemy(spriteConfiguration, enemyConfiguration, movementConfiguration);
    }

    private static PathFinder getPathFinderByEnemy(EnemyEnums enemyType){
        switch (enemyType){
            case Seeker, Energizer, Tazer, Scout -> {return new HoverPathFinder();}
            default -> {return new RegularPathFinder();}
        }
    }

    private static void adjustMovementConfigurationPerEnemy(EnemyEnums enemyType, MovementConfiguration movementConfiguration){
        switch (enemyType){
            case Scout -> movementConfiguration.setBoardBlockToHoverIn(7);
            case Seeker -> {movementConfiguration.setBoardBlockToHoverIn(6);}
            case Energizer -> {movementConfiguration.setBoardBlockToHoverIn(5);}
            case Tazer -> {movementConfiguration.setBoardBlockToHoverIn(7);}
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


    private static EnemyConfiguration createEnemyConfiguration (EnemyEnums enemyType,boolean boxCollision) {
        int maxHitpoints = enemyType.getBaseHitPoints();
        int maxShields = enemyType.getBaseShieldPoints();
        boolean hasAttack = enemyType.isHasAttack();
        boolean showHealthBar = enemyType.isShowHealthBar();
        AudioEnums deathSound = enemyType.getDeathSound();
        boolean allowedToDealDamage = true;
        String objectType = enemyType.getObjectType();
        float attackSpeed = enemyType.getAttackSpeed();
        float baseArmor = enemyType.getBaseArmor();
        float xpOnDeath = enemyType.getXpOnDeath();
        float cashMoneyWorth = enemyType.getCashMoneyWorth();


        return new EnemyConfiguration(enemyType, maxHitpoints, maxShields
                , hasAttack, showHealthBar, deathSound, allowedToDealDamage,
                objectType, attackSpeed, boxCollision, baseArmor, cashMoneyWorth, xpOnDeath);
    }

    private static Enemy createSpecificEnemy (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        switch (enemyConfiguration.getEnemyType()) {

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
}
