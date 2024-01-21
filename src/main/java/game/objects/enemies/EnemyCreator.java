package game.objects.enemies;

import game.movement.Direction;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.*;
import game.objects.enemies.enemytypes.*;
import VisualAndAudioData.audio.AudioEnums;
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
        EnemyConfiguration enemyConfiguration = createEnemyConfiguration(type, xMovementSpeed, yMovementSpeed, movementDirection, pathFinder, movementPatternSize, boxCollision);
        return createSpecificEnemy(spriteConfiguration, enemyConfiguration);
    }


    private static EnemyConfiguration createEnemyConfiguration (EnemyEnums enemyType, int xMovementSpeed, int yMovementSpeed, Direction movementDirection, PathFinder pathFinder, MovementPatternSize movementPatternSize
    , boolean boxCollision) {
        int maxHitpoints = enemyType.getBaseHitPoints();
        int maxShields = enemyType.getBaseShieldPoints();
        boolean hasAttack = enemyType.isHasAttack();
        boolean showHealthBar = enemyType.isShowHealthBar();
        AudioEnums deathSound = enemyType.getDeathSound();
        boolean allowedToDealDamage = true;
        String objectType = enemyType.getObjectType();
        int attackSpeed = enemyType.getAttackSpeed();
        float baseArmor = enemyType.getBaseArmor();


        return new EnemyConfiguration(enemyType, maxHitpoints, maxShields
                , hasAttack, true, deathSound, movementDirection, pathFinder, xMovementSpeed, yMovementSpeed, allowedToDealDamage,
                objectType, attackSpeed, movementPatternSize, boxCollision, baseArmor);
    }

    private static Enemy createSpecificEnemy (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
        switch (enemyConfiguration.getEnemyType()) {

            case Alien_Bomb -> {
                return new AlienBomb(spriteConfiguration, enemyConfiguration);
            }
            case Seeker -> {
                return new Seeker(spriteConfiguration, enemyConfiguration);
            }
            case Tazer -> {
                return new Tazer(spriteConfiguration, enemyConfiguration);
            }
            case Energizer -> {
                return new Energizer(spriteConfiguration, enemyConfiguration);
            }
            case Bulldozer -> {
                return new Bulldozer(spriteConfiguration, enemyConfiguration);
            }
            case Flamer -> {
                return new Flamer(spriteConfiguration, enemyConfiguration);
            }
            case Bomba -> {
                return new Bomba(spriteConfiguration, enemyConfiguration);
            }
        }
        return new Seeker(spriteConfiguration, enemyConfiguration);
    }
}
