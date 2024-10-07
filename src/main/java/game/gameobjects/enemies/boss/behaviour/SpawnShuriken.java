package game.gameobjects.enemies.boss.behaviour;

import VisualAndAudioData.image.ImageEnums;
import game.gameobjects.enemies.Enemy;
import game.gameobjects.enemies.EnemyCreator;
import game.gameobjects.enemies.EnemyManager;
import game.gameobjects.enemies.enums.EnemyEnums;
import game.gamestate.GameStateInfo;
import game.managers.AnimationManager;
import game.movement.Direction;
import game.movement.MovementPatternSize;
import game.movement.Point;
import game.movement.pathfinders.BouncingPathFinder;
import game.util.BoardBlockUpdater;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class SpawnShuriken implements BossActionable{
    private double lastSpawnedTime = 0;
    private double spawnCooldown = 14;
    private Random random;
    private int priority = 2;

    private SpriteAnimation spawnAnimation;

    @Override
    public boolean activateBehaviour(Enemy enemy){
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if(spawnAnimation == null) {
            initSpawnAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastSpawnedTime + spawnCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateSpawnAnimationLocation(enemy);

            if (!spawnAnimation.isPlaying()) {
                spawnAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(spawnAnimation);
            }


            if(spawnAnimation.isPlaying() && spawnAnimation.getCurrentFrame() == 4) {
                Enemy fourDirectionalDrone = createShuriken(enemy);
                fourDirectionalDrone.setCenterCoordinates(spawnAnimation.getCenterXCoordinate(), spawnAnimation.getCenterYCoordinate());
                EnemyManager.getInstance().addEnemy(fourDirectionalDrone);
                lastSpawnedTime = currentTime;
                enemy.setAttacking(false);
                return true; //We finished
            }
            return false; //We still running this behaviour
        }
        return true; //We still running this behaviour

    }



    private void initSpawnAnimation (Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        spawnAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spawnAnimation.setAnimationScale(0.5f);
        spawnAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
        spawnAnimation.addXOffset(Math.round(spawnAnimation.getWidth() * 0.1f));
    }

    private void updateSpawnAnimationLocation (Enemy enemy) {
        spawnAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
    }

    private Enemy createShuriken (Enemy enemy){
        EnemyEnums enemyType = EnemyEnums.Shuriken;
        Direction direction = getRandomDirection();
        Enemy shuriken = EnemyCreator.createEnemy(enemyType, enemy.getXCoordinate(), enemy.getYCoordinate(), direction,
                enemyType.getDefaultScale(), enemyType.getMovementSpeed(),enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);

        BouncingPathFinder pathFinder = new BouncingPathFinder();
        pathFinder.setMaxBounces(15);
        shuriken.getMovementConfiguration().setPathFinder(new BouncingPathFinder());
        shuriken.setOwnerOrCreator(enemy);
        return shuriken;
    }

    private Direction getRandomDirection(){
        if(random == null){
            random = new Random();
        }

        int number = random.nextInt(0, 2);
        return switch (number) {
            case 0 -> Direction.LEFT_UP;
            case 1 -> Direction.LEFT_DOWN;
            default -> Direction.LEFT;
        };
    }

    @Override
    public int getPriority () {
        return priority;
    }

    public void setPriority (int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isAvailable (Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameStateInfo.getInstance().getGameSeconds() >= lastSpawnedTime + spawnCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }
}
