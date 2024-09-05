package game.gameobjects.enemies.enemytypes;

import game.movement.Direction;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.MovementPatternSize;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.gameobjects.enemies.EnemyConfiguration;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.gameobjects.enemies.EnemyManager;
import game.gameobjects.missiles.MissileManager;
import VisualAndAudioData.audio.enums.AudioEnums;
import VisualAndAudioData.image.ImageEnums;
import game.util.OrbitingObjectsFormatter;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

public class Bulldozer extends Enemy {

    private boolean spawnedBombs;

    public Bulldozer (SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);

        spawnedBombs = false;

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 3, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bulldozer_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

    }

    private void createRotatingBombs () {
        // The center around which the AlienBombs will orbit
        double meanX = this.getCenterXCoordinate();
        double meanY = this.getCenterYCoordinate();

        // Calculate the angle increment based on how many bombs you want
        int amountOfBombs = 14;

        double angleIncrement = 2 * Math.PI / amountOfBombs;

        int radius = 85;
        for (int iterator = 0; iterator < amountOfBombs; iterator++) {
            // 2. Find the next angle
            double nextAngle = angleIncrement * iterator;

            // 3. Place the new drone
            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            PathFinder pathFinder = new OrbitPathFinder(this, 85, 300, 0);
            Enemy alienBomb = getEnemy(x, y, pathFinder);
            alienBomb.setOwnerOrCreator(this);
            alienBomb.getMovementConfiguration().setLastKnownTargetX(this.getCenterXCoordinate());
            alienBomb.getMovementConfiguration().setLastKnownTargetY(this.getCenterYCoordinate());
            alienBomb.getMovementConfiguration().setOrbitRadius(85);
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
                (float) 1.0, false, 0
        );

        EnemyConfiguration enemyConfiguration = new EnemyConfiguration(
                EnemyEnums.Alien_Bomb,
                25, 0,
                false, false, AudioEnums.Alien_Bomb_Destroyed,
                true, "Alien Bomb", false,
                EnemyEnums.Alien_Bomb.getBaseArmor(), 0, 0);

        MovementConfiguration movementConfiguration = new MovementConfiguration();
        movementConfiguration.setCurrentLocation(new Point(xCoordinate, yCoordinate));
        movementConfiguration.setXMovementSpeed(1);
        movementConfiguration.setYMovementSpeed(1);
        movementConfiguration.setPathFinder(pathFinder);

        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        movementConfiguration.setRotation(Direction.LEFT);
        movementConfiguration.setPatternSize(MovementPatternSize.SMALL);

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