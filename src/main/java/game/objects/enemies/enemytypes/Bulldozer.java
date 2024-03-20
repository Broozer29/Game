package game.objects.enemies.enemytypes;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinderconfigs.MovementPatternSize;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.enemies.enums.EnemyEnums;
import game.objects.enemies.EnemyManager;
import game.objects.missiles.MissileManager;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Bulldozer extends Enemy {

    private boolean spawnedBombs;

    public Bulldozer (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        spawnedBombs = false;
//        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
//        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bulldozer_Normal_Exhaust);
//        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bulldozer_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

    }

    private void createRotatingBombs () {
        // The center around which the AlienBombs will orbit
        double meanX = this.getCenterXCoordinate();
        double meanY = this.getCenterYCoordinate();

        // Calculate the angle increment based on how many bombs you want
        double angleIncrement = 2 * Math.PI / 8; // 8 is the total number of bombs

        for (int iterator = 0; iterator < 8; iterator++) {
            // 2. Find the next angle
            double nextAngle = angleIncrement * iterator;

            // 3. Place the new drone
            int radius = 75;
            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            PathFinder pathFinder = new OrbitPathFinder(this, radius, 300, nextAngle);
            Enemy alienBomb = getEnemy(x, y, pathFinder);
            alienBomb.setOwnerOrCreator(this);
            alienBomb.setAllowedVisualsToRotate(false);
            this.objectOrbitingThis.add(alienBomb);
            EnemyManager.getInstance().addEnemy(alienBomb);
        }
    }

    private Enemy getEnemy (int x, int y, PathFinder pathFinder) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration(
                x,
                y,
                scale,
                ImageEnums.Alien_Bomb,
                0, 0,
                (float) 1.0, false, 0
        );

        EnemyConfiguration enemyConfiguration = new EnemyConfiguration(
                EnemyEnums.Alien_Bomb,
                25, 0,
                false, false, AudioEnums.Alien_Bomb_Destroyed,
                true, "Alien Bomb",
                0, EnemyEnums.Alien_Bomb.isBoxCollision(),
                EnemyEnums.Alien_Bomb.getBaseArmor(), 0, 0);

        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setCurrentLocation(new Point(xCoordinate, yCoordinate));
        movementConfiguration.setXMovementSpeed(1);
        movementConfiguration.setYMovementSpeed(1);
        movementConfiguration.setPathFinder(pathFinder);
        movementConfiguration.setRotation(Direction.LEFT);
        movementConfiguration.setPatternSize(MovementPatternSize.SMALL);

        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        Enemy alienBomb = new AlienBomb(spriteConfiguration, enemyConfiguration, movementConfiguration);
        return alienBomb;
    }

    public void onCreationEffects () {
        if (!spawnedBombs) {
            createRotatingBombs();
            spawnedBombs = true;
        }
    }

    public void onDeathEffects () {
        //exist to be overriden
    }


    public void fireAction () {
        if (missileManager == null) {
            missileManager = MissileManager.getInstance();
        }


    }

}