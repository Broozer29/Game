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
import game.util.BoardBlockUpdater;
import game.util.WithinVisualBoundariesCalculator;
import visualobjects.SpriteAnimation;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class SpawnFourDirectionalDrone implements BossActionable {


    private double lastSpawnedTime = 0;
    private double spawnCooldown = 15;
    private Random random;
    private int priority = 3;

    private SpriteAnimation spawnAnimation;

    @Override
    public boolean activateBehaviour (Enemy enemy) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if(spawnAnimation == null) {
            initSpawnAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastSpawnedTime + spawnCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateSpawnAnimationLocation(enemy);

            if (!spawnAnimation.isPlaying()) {
                enemy.setAttacking(true);
                spawnAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(spawnAnimation);
            }


            if(spawnAnimation.isPlaying() && spawnAnimation.getCurrentFrame() == 4) {
                Enemy fourDirectionalDrone = createFourDirectionalDrone(enemy);
                fourDirectionalDrone.setCenterCoordinates(spawnAnimation.getCenterXCoordinate(), spawnAnimation.getCenterYCoordinate());
                EnemyManager.getInstance().addEnemy(fourDirectionalDrone);
                lastSpawnedTime = currentTime;
                enemy.setAttacking(false);
                return true; //We finished
            }

            return false; //we not finished yet
        }
        return true; //We dont have anything to do at this point
    }

    private void initSpawnAnimation (Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.WarpIn);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
        spawnAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        spawnAnimation.setAnimationScale(0.4f);
        spawnAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
        spawnAnimation.addXOffset(-10);
    }

    private void updateSpawnAnimationLocation (Enemy enemy) {
        spawnAnimation.setCenterCoordinates(enemy.getXCoordinate(), enemy.getCenterYCoordinate());
    }

    private int getRandomBoardBlock () {
        if (random == null) {
            random = new Random();
        }

        return random.nextInt(2, 5);
    }

    private Enemy createFourDirectionalDrone(Enemy enemy){
        Enemy fourDirectionalDrone = EnemyCreator.createEnemy(EnemyEnums.FourDirectionalDrone, enemy.getXCoordinate(), enemy.getYCoordinate(), Direction.LEFT,
                1, 2,2, MovementPatternSize.SMALL, false);

        fourDirectionalDrone.setScale(0.4f);
        Point point = BoardBlockUpdater.getRandomCoordinateInBlock(getRandomBoardBlock(), fourDirectionalDrone.getWidth(), fourDirectionalDrone.getHeight());
        fourDirectionalDrone.getMovementConfiguration().setDestination(point);
        fourDirectionalDrone.setOwnerOrCreator(enemy);
        return fourDirectionalDrone;
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
