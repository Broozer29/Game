package game.objects.enemies;

import game.movement.Direction;
import game.movement.pathfinders.PathFinder;
import game.objects.enemies.enemytypes.*;
import gamedata.audio.AudioEnums;
import visual.objects.CreationConfigurations.SpriteConfiguration;

public class EnemyCreator {

    public static Enemy createEnemy (EnemyEnums type, int xCoordinate, int yCoordinate, Direction movementDirection, float scale,
                                     int xMovementSpeed, int yMovementSpeed, PathFinder pathFinder) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration(
                xCoordinate,
                yCoordinate,
                scale,
                type.getImageEnum(),
                0, 0,
                (float) 1.0, false, 0
        );

        EnemyConfiguration enemyConfiguration = createEnemyConfiguration(type, xMovementSpeed, yMovementSpeed, movementDirection, pathFinder);
        return createSpecificEnemy(spriteConfiguration, enemyConfiguration);
    }


    private static EnemyConfiguration createEnemyConfiguration (EnemyEnums enemyType, int xMovementSpeed, int yMovementSpeed, Direction movementDirection, PathFinder pathFinder) {
        int maxHitpoints = enemyType.getBaseHitPoints();
        ;
        int maxShields = enemyType.getBaseShieldPoints();
        ;
        boolean hasAttack = enemyType.isHasAttack();
        boolean showHealthBar = enemyType.isShowHealthBar();
        AudioEnums deathSound = enemyType.getDeathSound();
        boolean allowedToDealDamage = true;
        String objectType = enemyType.getObjectType();
        int attackSpeed = enemyType.getAttackSpeed();


        return new EnemyConfiguration(enemyType, maxHitpoints, maxShields
                , hasAttack, showHealthBar, deathSound, movementDirection, pathFinder, xMovementSpeed, yMovementSpeed, allowedToDealDamage,
                objectType, attackSpeed);
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
