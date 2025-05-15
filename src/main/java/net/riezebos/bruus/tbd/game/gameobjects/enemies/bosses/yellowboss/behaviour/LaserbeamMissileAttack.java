package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.yellowboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class LaserbeamMissileAttack implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 20;
    private int priority = 3;

    public static int lowerLaserbeamLowestAngle = 135;
    public static int lowerLaserbeamHighestAngle = 225;
    public static int upperLaserbeamLowestAngle = 135;
    public static int upperLaserbeamHighestAngle = 225;
    public static float angleStepSize = 0.35f;

    private Point upperLaserbeamOriginPoint;
    private Point lowerLaserbeamOriginPoint;
    private SpriteAnimation upperChargingUpAnimation;
    private SpriteAnimation lowerChargingUpAnimation;
    private Laserbeam upperLaserbeam;
    private Laserbeam lowerLaserbeam;
    private boolean isFiringLaserbeams;


    private SpriteAnimation missileChargingAnimation;
    private double lastFireMissileTie = 0;
    private double fireMissileCooldown = 0.65f;
    private boolean isFiringMissiles = false;

    private List<Integer> missileAnglesList = new ArrayList<>();

    public LaserbeamMissileAttack() {
        setAngles();
    }

    private void setAngles() {
        lowerLaserbeamHighestAngle = 205;
        upperLaserbeamLowestAngle = 155;

        missileAnglesList.clear();
        for (int i = 130; i <= 200; i += 10) {
            missileAnglesList.add(i);
        }
    }

    @Override
    //Return if the behaviour is completed.
    // if returns true; this behaviour is removed and another is selected
    // if false, this behaviour remains and this method keeps getting called
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (upperChargingUpAnimation == null || lowerChargingUpAnimation == null) {
            initSpawnAnimations(enemy);
            initMissileChargingAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateAnimationLocations(enemy);

            if (!lowerChargingUpAnimation.isPlaying() && !isFiringLaserbeams) {
                upperChargingUpAnimation.refreshAnimation();
                lowerChargingUpAnimation.refreshAnimation();
                missileChargingAnimation.refreshAnimation();

                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(upperChargingUpAnimation);
                AnimationManager.getInstance().addUpperAnimation(lowerChargingUpAnimation);
                AudioManager.getInstance().addAudio(AudioEnums.ChargingLaserbeam);
            }

            if (lowerChargingUpAnimation.isPlaying() &&
                    lowerChargingUpAnimation.getCurrentFrame() == lowerChargingUpAnimation.getTotalFrames() - 1 &&
                    !isFiringLaserbeams) {
                updateLaserbeamOriginPoints();
                createLaserbeams(enemy);
                lowerChargingUpAnimation.setVisible(false);
                upperChargingUpAnimation.setVisible(false);
                upperLaserbeam.update();
                lowerLaserbeam.update();
                MissileManager.getInstance().addLaserBeam(upperLaserbeam);
                MissileManager.getInstance().addLaserBeam(lowerLaserbeam);
                isFiringLaserbeams = true;
            }
        }


        if (isFiringLaserbeams) {
            updateMissileChargingAnimation(enemy);
            if (missileChargingAnimation.getCurrentFrame() == 0 && !isFiringMissiles) {
                AnimationManager.getInstance().addUpperAnimation(missileChargingAnimation);
            }

            if (missileChargingAnimation.isPlaying() && missileChargingAnimation.getCurrentFrame() == missileChargingAnimation.getTotalFrames() - 1 && !isFiringMissiles) {
                isFiringMissiles = true;
            }

            if (isFiringMissiles) {
                fireMissiles(enemy);
            }

            updateLaserbeamOriginPoints();
            changeLaserbeamAngle();
            if (!lowerLaserbeam.isVisible() && !upperLaserbeam.isVisible()) {
                lowerLaserbeam = null;
                upperLaserbeam = null;
                enemy.setAttacking(false);
                isFiringLaserbeams = false;
                lastAttackedTime = currentTime;
                return true;
            }
            return false;
        }

        return isFiringLaserbeams; //Laserbeams should removed and this attack is finished
    }


    private void initMissileChargingAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setImageType(ImageEnums.Charging);
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getYCoordinate());
        spriteConfiguration.setScale(1);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        this.missileChargingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
    }

    private void changeLaserbeamAngle() {
        upperLaserbeam.setAngleDegrees(upperLaserbeam.getAngleDegrees() - angleStepSize);
        if (upperLaserbeam.getAngleDegrees() <= upperLaserbeamLowestAngle) {
            upperLaserbeam.setVisible(false);
        }

        lowerLaserbeam.setAngleDegrees(lowerLaserbeam.getAngleDegrees() + angleStepSize);
        if (lowerLaserbeam.getAngleDegrees() >= lowerLaserbeamHighestAngle) {
            lowerLaserbeam.setVisible(false);
        }
    }


    private void createLaserbeams(Enemy enemy) {
        //Create upper laserbeam

        float damage = enemy.getDamage() / 2;
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(20);
        upperLaserbeamConfiguration.setOriginPoint(upperLaserbeamOriginPoint);

        LaserbeamConfiguration lowerLaserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        lowerLaserbeamConfiguration.setAmountOfLaserbeamSegments(20);
        lowerLaserbeamConfiguration.setOriginPoint(lowerLaserbeamOriginPoint);

        upperLaserbeamConfiguration.setAngleDegrees(upperLaserbeamHighestAngle);
        lowerLaserbeamConfiguration.setAngleDegrees(lowerLaserbeamLowestAngle);

        if (upperLaserbeam != null) {
            upperLaserbeam.setVisible(false);
        }

        if (lowerLaserbeam != null) {
            lowerLaserbeam.setVisible(false);
        }

        upperLaserbeam = new AngledLaserBeam(upperLaserbeamConfiguration);
        lowerLaserbeam = new AngledLaserBeam(lowerLaserbeamConfiguration);
        upperLaserbeam.setOwner(enemy);
        lowerLaserbeam.setOwner(enemy);
        upperLaserbeam.setKnockbackStrength(13);
        lowerLaserbeam.setKnockbackStrength(13);
    }

    private void updateLaserbeamOriginPoints() {
        if (upperLaserbeamOriginPoint == null) {
            upperLaserbeamOriginPoint = new Point(upperChargingUpAnimation.getCenterXCoordinate(), upperChargingUpAnimation.getCenterYCoordinate());
        } else {
            upperLaserbeamOriginPoint.setX(upperChargingUpAnimation.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4);
            upperLaserbeamOriginPoint.setY(upperChargingUpAnimation.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12);
        }

        if (lowerLaserbeamOriginPoint == null) {
            lowerLaserbeamOriginPoint = new Point(lowerChargingUpAnimation.getCenterXCoordinate(), lowerChargingUpAnimation.getCenterYCoordinate());
        } else {
            lowerLaserbeamOriginPoint.setX(lowerChargingUpAnimation.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4);
            lowerLaserbeamOriginPoint.setY(lowerChargingUpAnimation.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12);
        }
    }

    private void deleteLaserbeams() {
        if (upperLaserbeam != null) {
            upperLaserbeam.setVisible(false);
        }

        if (lowerLaserbeam != null) {
            lowerLaserbeam.setVisible(false);
        }
    }


    private void initSpawnAnimations(Enemy enemy) {
        upperChargingUpAnimation = new SpriteAnimation(createChargingAnimationConfig(enemy));
        upperChargingUpAnimation.setAnimationScale(2f);
        upperChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate() + Math.round(enemy.getWidth() * 0.24f),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.3125f)
        );
        upperChargingUpAnimation.setFrameDelay(10);

        lowerChargingUpAnimation = new SpriteAnimation(createChargingAnimationConfig(enemy));
        lowerChargingUpAnimation.setAnimationScale(2f);
        lowerChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate() + Math.round(enemy.getWidth() * 0.24f),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.6875f)
        );

        lowerChargingUpAnimation.setFrameDelay(10);
    }

    private SpriteAnimationConfiguration createChargingAnimationConfig(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.LaserbeamCharging);

        return new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
    }

    private void updateAnimationLocations(Enemy enemy) {
        upperChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate() + Math.round(enemy.getWidth() * 0.00238f),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.1225f)
        );

        lowerChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate() + Math.round(enemy.getWidth() * 0.00238f),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.87875f)
        );
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

    private void updateMissileChargingAnimation(Enemy enemy) {
        if (missileChargingAnimation == null) {
            initMissileChargingAnimation(enemy);
        }

        missileChargingAnimation.setCenterCoordinates(enemy.getXCoordinate() + Math.round(enemy.getWidth() * 0.0379),
                enemy.getCenterYCoordinate());
    }

    private void fireMissiles(Enemy enemy) {
        if (GameState.getInstance().getGameSeconds() >= lastFireMissileTie + fireMissileCooldown) {
            lastFireMissileTie = GameState.getInstance().getGameSeconds();
            for (int angle : missileAnglesList) {
                Point destination = calculateBulletDestination(angle, 500, upperChargingUpAnimation.getCenterXCoordinate(), upperChargingUpAnimation.getCenterYCoordinate());
                MissileManager.getInstance().addExistingMissile(createMissile(enemy, destination));
            }
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
}

