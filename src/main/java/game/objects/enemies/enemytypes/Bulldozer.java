package game.objects.enemies.enemytypes;

import game.movement.Direction;
import game.movement.pathfinders.OrbitPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.enemies.EnemyConfiguration;
import game.objects.enemies.Enemy;
import game.objects.enemies.EnemyEnums;
import game.objects.enemies.EnemyManager;
import game.objects.missiles.MissileManager;
import gamedata.audio.AudioEnums;
import gamedata.image.ImageEnums;
import visual.objects.CreationConfigurations.SpriteAnimationConfiguration;
import visual.objects.CreationConfigurations.SpriteConfiguration;
import visual.objects.SpriteAnimation;

public class Bulldozer extends Enemy {

    public Bulldozer (SpriteConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration) {
        super(spriteConfiguration, enemyConfiguration);

        SpriteAnimationConfiguration exhaustConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 0, true);
        exhaustConfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bulldozer_Normal_Exhaust);
        this.exhaustAnimation = new SpriteAnimation(exhaustConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.Bulldozer_Destroyed_Explosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);

        createRotatingBombs();
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
            int radius = 75; // Example radius
            int x = (int) (meanX + Math.cos(nextAngle) * radius);
            int y = (int) (meanY + Math.sin(nextAngle) * radius);

            PathFinder pathFinder = new OrbitPathFinder(this, radius, 300, nextAngle);

            Enemy alienBomb = getEnemy(x, y, pathFinder);
            this.followingEnemies.add(alienBomb);
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
                Direction.LEFT, pathFinder, 1, 1, true, "Alien Bomb",
                0
        );

        Enemy alienBomb = new AlienBomb(spriteConfiguration, enemyConfiguration);
        return alienBomb;
    }


    public void fireAction () {
        if (missileManager == null) {
            missileManager = MissileManager.getInstance();
        }
    }

}