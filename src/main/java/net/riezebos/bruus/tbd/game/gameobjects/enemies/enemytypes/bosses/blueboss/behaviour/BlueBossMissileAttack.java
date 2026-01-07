package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.behaviour;

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

public class BlueBossMissileAttack implements BossActionable {

    private int priority = 2;
    private double lastAttackedTime = GameState.getInstance().getGameSeconds() + 6; //give it time to spawn the factories
    private boolean allowedToFireBurst = false;
    private double lastSingleShotAttackTime = 0;
    private double intermittenAttackCooldown = 0.75f;
    private int angleIncrement = 4;
    private int burstShotsFired = 0; // Track the number of shots fired in the burst
    private int amountOfBursts = 8;
    private double attackCooldown = 5;

    private List<Integer> angleList = new ArrayList<>();
    private SpriteAnimation chargingAttackAnimation = null;

    public BlueBossMissileAttack(){
        for(int i = 0; i < 360; i += 10){
            angleList.add(i);
        }
    }

    @Override
    public boolean activateBehaviour (Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        angleIncrement = 4;
        intermittenAttackCooldown = 0.75f;

        if (chargingAttackAnimation == null) {
            initChargingAttackAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateChargingAttackAnimationLocation(enemy);

            if (!chargingAttackAnimation.isPlaying() && !allowedToFireBurst) {
                chargingAttackAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(chargingAttackAnimation);
                burstShotsFired = 0; // Reset the burst shot counter, so that it is ready for the actual firing
            }

            if (chargingAttackAnimation.getCurrentFrame() >= chargingAttackAnimation.getTotalFrames() - 1) {
                allowedToFireBurst = true;
            }
        }

        if (allowedToFireBurst) {
            boolean canFireAgain = currentTime >= lastSingleShotAttackTime + intermittenAttackCooldown;
            if (burstShotsFired < amountOfBursts && canFireAgain) {
                for (int i = 0; i < angleList.size(); i++) {
                    int angle = angleList.get(i) + angleIncrement;
                    if (angle > 360) {
                        angle -= 360;
                    }
                    angleList.set(i, angle);
                    fireMissile(enemy, angle);
                }
                burstShotsFired++;
                lastSingleShotAttackTime = currentTime;

                if (burstShotsFired >= amountOfBursts) {
                    lastAttackedTime = currentTime;
                    allowedToFireBurst = false;
                    enemy.setAttacking(false);
                    return true; //We finished
                }
            }
            return false; //We still running this behaviour
        }
        return true; //We dont have anything to do at this point
    }

    private void initChargingAttackAnimation (Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(ImageEnums.Charging);
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getYCoordinate());
        spriteConfiguration.setScale(1);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        chargingAttackAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        chargingAttackAnimation.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());

    }

    private void updateChargingAttackAnimationLocation (Enemy enemy) {
        chargingAttackAnimation.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
    }



    private void fireMissile (Enemy enemy, Integer angleDegrees) {
        Missile missile = createMissile(enemy, angleDegrees);
        MissileManager.getInstance().addExistingMissile(missile);
    }

    private Missile createMissile (Enemy enemy, Integer angleDegrees) {
        MissileEnums missileType = MissileEnums.DefaultLaserBullet;
        SpriteConfiguration spriteConfiguration = MissileCreator.getInstance().createMissileSpriteConfig(
                enemy.getXCoordinate(), enemy.getCenterYCoordinate(),
                missileType.getImageType(), 0.6f);


        float movementSpeed = 2.5f;

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


        Missile missile = MissileCreator.getInstance().createMissile(spriteConfiguration, missileConfiguration, movementConfiguration);

        Point rotationCoordinates = calculatePositionBasedOnAngle(angleDegrees, 500, enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        missile.resetMovementPath();
        missile.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        missile.getMovementConfiguration().setDestination(rotationCoordinates); // again because reset removes it
        missile.rotateObjectTowardsDestination(true);
        missile.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(enemy);

        return missile;
    }

    private Point calculatePositionBasedOnAngle(double angleDegrees, int distance, int centerX, int centerY) {
        // Assuming the game has flipped Y-coordinates or similar issues:
        double adjustedAngleDegrees = 360 - (angleDegrees + 90) % 360; // Rotate by an additional 90 degrees to adjust

        double angleRadians = Math.toRadians(adjustedAngleDegrees);

        int targetX = centerX + (int) (Math.cos(angleRadians) * distance);
        int targetY = centerY + (int) (Math.sin(angleRadians) * distance);

        return new Point(targetX, targetY);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }
}
