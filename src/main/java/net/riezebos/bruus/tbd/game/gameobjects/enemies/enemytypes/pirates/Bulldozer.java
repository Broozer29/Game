package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.pirates;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.OrbitPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.util.OrbitingObjectsFormatter;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class Bulldozer extends Enemy {

    private boolean spawnedBombs;

    public Bulldozer (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        spawnedBombs = false;

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.detonateOnCollision = false;
        this.movementConfiguration.setMovementSpeed(this.movementConfiguration.getOriginalMovementSpeed() + EnemyManager.getInstance().getEnemyDifficultyModifier() * 0.25f);
        this.knockbackStrength = 8;
        this.hasAttack = false;
    }

    private void createRotatingBombs () {
        // The center around which the AlienBombs will orbit
        double meanX = this.getCenterXCoordinate();
        double meanY = this.getCenterYCoordinate();

        // Calculate the angle increment based on how many bombs you want
        int amountOfBombs = 14;

        double angleIncrement = 2 * Math.PI / amountOfBombs;

        int radius = 85 + (EnemyManager.getInstance().getEnemyDifficultyModifier() * 5);
        for (int iterator = 0; iterator < amountOfBombs; iterator++) {
            // 2. Find the next angle
            double nextAngle = angleIncrement * iterator;

            // 3. Place the new drone
            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            PathFinder pathFinder = new OrbitPathFinder(this);
            Enemy alienBomb = getEnemy(x, y, pathFinder);
            alienBomb.setOwnerOrCreator(this);
            alienBomb.getMovementConfiguration().setLastKnownTargetX(this.getCenterXCoordinate());
            alienBomb.getMovementConfiguration().setLastKnownTargetY(this.getCenterYCoordinate());
            alienBomb.getMovementConfiguration().setOrbitRadius(radius);
            //Bomb rotation is done in GameObject, not initialization because it's dependent on the owner (bulldozer) so it rotates with bulldozer
            this.objectOrbitingThis.add(alienBomb);
            EnemyManager.getInstance().addEnemy(alienBomb);
        }

        OrbitingObjectsFormatter.reformatOrbitingObjects(this, radius);
    }

    private Enemy getEnemy (int x, int y, PathFinder pathFinder) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration(
                x,
                y,
                scale,
                ImageEnums.Alien_Bomb,
                0, 0,
                1.0f, false, 0
        );

        EnemyConfiguration enemyConfiguration = new EnemyConfiguration(
                EnemyEnums.Alien_Bomb,
                EnemyEnums.Alien_Bomb.getBaseHitPoints(),
                EnemyEnums.Alien_Bomb.getBaseDamage(),
                EnemyEnums.Alien_Bomb.getDeathSound(),
                EnemyEnums.Alien_Bomb.getBaseArmor(), EnemyEnums.Alien_Bomb.getXpOnDeath(), EnemyEnums.Alien_Bomb.getCashMoneyWorth());

        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setCurrentLocation(new Point(xCoordinate, yCoordinate));
        movementConfiguration.setMovementSpeed(1);
        movementConfiguration.setPathFinder(pathFinder);

        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        movementConfiguration.setDirection(Direction.LEFT);

        Enemy alienBomb = new AlienBomb(spriteConfiguration, enemyConfiguration, movementConfiguration);
        return alienBomb;
    }

    public void onCreationEffects () {
        if (!spawnedBombs) {
            createRotatingBombs();
            spawnedBombs = true;
        }
    }

    public void fireAction () {
        if (missileManager == null) {
            missileManager = MissileManager.getInstance();
        }


    }

}