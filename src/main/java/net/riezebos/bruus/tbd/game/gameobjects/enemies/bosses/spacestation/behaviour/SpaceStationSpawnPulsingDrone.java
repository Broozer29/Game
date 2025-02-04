package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.spacestation.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.game.movement.BoardBlockUpdater;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class SpaceStationSpawnPulsingDrone implements BossActionable {
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
                Enemy pulsingDrone = createPulsingDrone(enemy);
                pulsingDrone.setCenterCoordinates(spawnAnimation.getCenterXCoordinate(), spawnAnimation.getCenterYCoordinate());
                EnemyManager.getInstance().addEnemy(pulsingDrone);
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
        spawnAnimation.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
    }

    private int getRandomBoardBlock () {
        if (random == null) {
            random = new Random();
        }

        return random.nextInt(1, 7);
    }

    private Enemy createPulsingDrone (Enemy enemy){
        EnemyEnums enemyType = EnemyEnums.PulsingDrone;
        Enemy pulsingDrone = EnemyCreator.createEnemy(enemyType, enemy.getXCoordinate(), enemy.getYCoordinate(), Direction.LEFT,
                enemyType.getDefaultScale(), enemyType.getMovementSpeed(),enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);

        Point point = BoardBlockUpdater.getRandomCoordinateInBlock(getRandomBoardBlock(), pulsingDrone.getWidth(), pulsingDrone.getHeight());
        pulsingDrone.getMovementConfiguration().setDestination(point);
        pulsingDrone.setOwnerOrCreator(enemy);
        pulsingDrone.setAllowedVisualsToRotate(false);
        return pulsingDrone;
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
