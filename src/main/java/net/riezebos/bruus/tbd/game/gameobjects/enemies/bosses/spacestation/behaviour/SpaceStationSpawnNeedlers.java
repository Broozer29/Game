package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.spacestation.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyCreator;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.LinkedList;
import java.util.List;

public class SpaceStationSpawnNeedlers implements BossActionable {

    private Point centerPoint;
    private int priority = 2;
    private double attackCooldown = 20f;
    private double lastAttackedTime = 0;
    private List<SpriteAnimation> chargingAnimations = new LinkedList<>();
    private boolean startSpawningEnemies = false;
    private boolean actuallySpawnEnemies = false;
    private int distanceFromCenterToSpawnAt = 300;
    private int angleIncrementStepSize = 15;


    public SpaceStationSpawnNeedlers () {
        centerPoint = EnemyCreator.calculateSpaceStationBossDestination();
        createAnimations();
    }

    private void createAnimations () {
        for (int i = 0; i <= 360; i += angleIncrementStepSize) {
            Point coordinates = this.calculatePositionBasedOnAngle(i, distanceFromCenterToSpawnAt, 0, 0);
            SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
            spriteConfiguration.setScale(1);
            spriteConfiguration.setxCoordinate(coordinates.getX());
            spriteConfiguration.setyCoordinate(coordinates.getY());
            spriteConfiguration.setImageType(ImageEnums.WarpIn);

            SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 4, false);
            SpriteAnimation spriteAnimation = new SpriteAnimation(spriteAnimationConfiguration);
            chargingAnimations.add(spriteAnimation);
        }
    }

    private void updateChargingAnimationCoordinates (Enemy enemy) {
        int i = 0;
        for (SpriteAnimation spriteAnimation : chargingAnimations) {
            Point coordinates = this.calculatePositionBasedOnAngle(i, distanceFromCenterToSpawnAt, enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
            spriteAnimation.setCenterCoordinates(coordinates.getX(), coordinates.getY());
            i += angleIncrementStepSize;
        }
    }


    @Override
    public boolean activateBehaviour (Enemy enemy) {
        //Some info for use, you don't have to put everything in this method
        double currentTime = GameStateInfo.getInstance().getGameSeconds();

        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateChargingAnimationCoordinates(enemy);
            if (!chargingAnimations.get(0).isPlaying()) {
                for (SpriteAnimation spriteanimation : chargingAnimations) {
                    spriteanimation.refreshAnimation();
                    AnimationManager.getInstance().addUpperAnimation(spriteanimation);
                }
                enemy.setAttacking(true);
            }
            startSpawningEnemies = true;
        }

        if (startSpawningEnemies) {
            if (chargingAnimations.get(0).getCurrentFrame() >= chargingAnimations.get(0).getTotalFrames() - 1) {
                actuallySpawnEnemies = true;
            }

            if (actuallySpawnEnemies) {
                int i = 0;
                for (SpriteAnimation spriteAnimation : chargingAnimations) {
                    Enemy needler = createEnemy(spriteAnimation, enemy, i);
                    EnemyManager.getInstance().addEnemy(needler);
                    i += angleIncrementStepSize;
                }

                startSpawningEnemies = false;
                actuallySpawnEnemies = false;
                lastAttackedTime = currentTime;
                return true;
            }
            return false; //If we are waiting for the warp in animation, return false
        }
        return true; //We finished spawning

    }


    private Enemy createEnemy (SpriteAnimation animation, Enemy enemy, int angleDegrees) {
        EnemyEnums enemyType = EnemyEnums.Needler;
        int centerXCoordinate = animation.getCenterXCoordinate();
        int centerYCoordinate = animation.getCenterYCoordinate();
        Enemy needler = EnemyCreator.createEnemy(enemyType, centerXCoordinate, centerYCoordinate, Direction.LEFT,
                enemyType.getDefaultScale(), enemyType.getMovementSpeed(), enemyType.getMovementSpeed(), MovementPatternSize.SMALL, false);
        needler.getMovementConfiguration().setPathFinder(new StraightLinePathFinder());

        needler.setCenterCoordinates(centerXCoordinate, centerYCoordinate);

        Point destination = calculatePositionBasedOnAngle(angleDegrees, 500, enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        needler.getMovementConfiguration().setDestination(destination);
        needler.setOwnerOrCreator(enemy);
        return needler;
    }

    private Point calculatePositionBasedOnAngle (double angleDegrees, int distance, int centerX, int centerY) {
        // Adjust the angle to match the game's coordinate system.
        // Assuming the game has flipped Y-coordinates or similar issues:
        double adjustedAngleDegrees = 360 - (angleDegrees + 90) % 360; // Rotate by an additional 90 degrees to adjust

        // Convert to radians
        double angleRadians = Math.toRadians(adjustedAngleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }

    @Override
    public int getPriority () {
        return priority;
    }

    @Override
    public boolean isAvailable (Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameStateInfo.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)
                && enemy.getXCoordinate() == centerPoint.getX()
                && enemy.getYCoordinate() == centerPoint.getY();
    }
}
