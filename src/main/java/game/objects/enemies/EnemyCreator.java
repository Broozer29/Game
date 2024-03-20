package game.objects.enemies;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.*;
import game.objects.enemies.enemytypes.*;
import VisualAndAudioData.audio.enums.AudioEnums;
import game.objects.enemies.enums.EnemyEnums;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

public class EnemyCreator {

    public static Enemy createEnemy (EnemyEnums type, int xCoordinate, int yCoordinate, Direction movementDirection, float scale,
                                     int xMovementSpeed, int yMovementSpeed, MovementPatternSize movementPatternSize, boolean boxCollision) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration(
                xCoordinate,
//                800,
                yCoordinate,
                scale,
                type.getImageEnum(),
                0, 0,
                (float) 1.0, false, 0
        );

        PathFinder pathFinder = new RegularPathFinder();
        
        EnemyConfiguration enemyConfiguration = createEnemyConfiguration(type,boxCollision);
        MovementConfiguration movementConfiguration = createMovementConfiguration(xCoordinate, yCoordinate, movementDirection, xMovementSpeed, yMovementSpeed, movementPatternSize, pathFinder);
        return createSpecificEnemy(spriteConfiguration, enemyConfiguration, movementConfiguration);
    }

    private static MovementConfiguration createMovementConfiguration (int xCoordinate, int yCoordinate, Direction movementDirection, int xMovementSpeed, int yMovementSpeed,
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
        int attackSpeed = enemyType.getAttackSpeed();
        float baseArmor = enemyType.getBaseArmor();
        float xpOnDeath = enemyType.getXpOnDeath();
        float cashMoneyWorth = enemyType.getCashMoneyWorth();


        return new EnemyConfiguration(enemyType, maxHitpoints, maxShields
                , hasAttack, true, deathSound, allowedToDealDamage,
                objectType, attackSpeed, boxCollision, baseArmor, cashMoneyWorth, xpOnDeath);
    }

    private static Enemy createSpecificEnemy (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        switch (enemyConfiguration.getEnemyType()) {

            case Alien_Bomb -> {
                return new AlienBomb(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
            case Seeker -> {
                return new Seeker(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
            case Tazer -> {
                return new Tazer(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
            case Energizer -> {
                return new Energizer(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
            case Bulldozer -> {
                return new Bulldozer(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
            case Flamer -> {
                return new Flamer(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
            case Bomba -> {
                return new Bomba(spriteConfiguration, enemyConfiguration, movementConfiguration);
            }
        }
        return new Seeker(spriteConfiguration, enemyConfiguration, movementConfiguration);
    }
}
