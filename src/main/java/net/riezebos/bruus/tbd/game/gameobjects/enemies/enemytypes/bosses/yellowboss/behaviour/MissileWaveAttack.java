package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.yellowboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class MissileWaveAttack implements BossActionable {

    private double lastAttackTime = 0;
    private double lastSingleShotAttackTime = 0;
    private boolean allowedToFireBurst = false;
    private double intermittenAttackCooldown = 0.25f;
    private int burstShotsFired = 0; // Track the number of shots fired in the burst
    private int amountOfShotsPerAttack = 6;
    private double attackCooldown = 5;

    private int priority = 1;
    private SpriteAnimation missileChargingAnimation = null;
    private List<Integer> missileAnglesList = new ArrayList<>();


    public MissileWaveAttack() {
        missileAnglesList.clear();
        for (int i = 120; i <= 240; i += 10) {
            missileAnglesList.add(i);
        }
    }

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();

        if (missileChargingAnimation == null) {
            initChargingAttackAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastAttackTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateChargingAttackAnimationLocation(enemy);

            if (!missileChargingAnimation.isPlaying() && !allowedToFireBurst) {
                missileChargingAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(missileChargingAnimation);
                burstShotsFired = 0; // Reset the burst shot counter, so that it is ready for the actual firing
            }

            if (missileChargingAnimation.getCurrentFrame() >= missileChargingAnimation.getTotalFrames() - 1) {
                // The "charging up" animation has finished, missiles are now allowed to be fired
                allowedToFireBurst = true;
            }
        }

        if (allowedToFireBurst) {
            boolean canFireAgain = currentTime >= lastSingleShotAttackTime + intermittenAttackCooldown;
            if (burstShotsFired < amountOfShotsPerAttack && canFireAgain) {
                // Fire a missile and update lastAttackTime
                fireMissile(enemy);
                burstShotsFired++;
                lastSingleShotAttackTime = currentTime;

                if (burstShotsFired >= amountOfShotsPerAttack) {
                    // After the 4th shot, the burst is finished
                    lastAttackTime = currentTime;
                    allowedToFireBurst = false;
                    enemy.setAttacking(false);
                    return true; //We finished
                }
            }
            return false; //We still running this behaviour
        }
        return true; //We dont have anything to do at this point
    }

    private void initChargingAttackAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(ImageEnums.Charging);
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getYCoordinate());
        spriteConfiguration.setScale(1);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 5, false);
        this.missileChargingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    private void updateChargingAttackAnimationLocation(Enemy enemy) {
        missileChargingAnimation.setCenterCoordinates(enemy.getXCoordinate() + Math.round(enemy.getWidth() * 0.0379),
                enemy.getCenterYCoordinate());
    }

    private void fireMissile(Enemy enemy) {
        for (int angle : missileAnglesList) {
            Point destination = calculateBulletDestination(angle, 500, missileChargingAnimation.getCenterXCoordinate(), missileChargingAnimation.getCenterYCoordinate());
            MissileManager.getInstance().addExistingMissile(createMissile(enemy, destination));
        }
    }

    private Missile createMissile(Enemy enemy, Point destination) {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(
                enemy.getXCoordinate(), enemy.getCenterYCoordinate(),
                missileType.getImageType(), enemy.getScale());


        int movementSpeed = 5;
        //Create missile movement attributes and create a movement configuration
        PathFinder missilePathFinder = new StraightLinePathFinder();
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, movementSpeed, missilePathFinder, movementPatternSize, enemy.getMovementConfiguration().getRotation()
        );


        boolean isFriendly = false;
        int maxHitPoints = 100;
        int maxShields = 100;
        AudioEnums deathSound = null;
        boolean allowedToDealDamage = true;
        String objectType = "Boss Burst Missile";

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType, maxHitPoints, maxShields,
                deathSound, enemy.getDamage(), missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                false, false, true, false);


        //Create the missile and finalize the creation process, then add it to the manager and consequently the game
        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);
        missile.resetMovementPath();
        missile.setCenterCoordinates(missileChargingAnimation.getCenterXCoordinate(), missileChargingAnimation.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(destination); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(missileChargingAnimation.getCenterXCoordinate(), missileChargingAnimation.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(enemy);

        return missile;
    }

    private Point calculateBulletDestination(double angleDegrees, int distance, int centerX, int centerY) {
        // Convert the angle from degrees to radians because Math functions use radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Calculate the X and Y coordinates
        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        // Return the calculated coordinates as a Point object
        return new Point(targetX, targetY);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastAttackTime + attackCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }
}
