package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.redboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gamestate.GameStateInfo;
import net.riezebos.bruus.tbd.visuals.objects.AnimationManager;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visuals.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visuals.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visuals.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visuals.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visuals.objects.SpriteConfigurations.SpriteConfiguration;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class CrossingLaserbeamsAttack implements BossActionable {
    private double lastAttackedTime = 0;
    private double attackCooldown = 20;
    private int priority = 3;

    private int lowerLaserbeamLowestAngle = 135;
    private int lowerLaserbeamHighestAngle = 225;
    private int upperLaserbeamLowestAngle = 135;
    private int upperLaserbeamHighestAngle = 225;

    private Point upperLaserbeamOriginPoint;
    private Point lowerLaserbeamOriginPoint;
    private SpriteAnimation upperChargingUpAnimation;
    private SpriteAnimation lowerChargingUpAnimation;
    private Laserbeam upperLaserbeam;
    private Laserbeam lowerLaserbeam;
    private boolean isFiringLaserbeams;
    private boolean inwards;
    private float angleStepSize = 0.4f;

    public CrossingLaserbeamsAttack (boolean inwards) {
        //Determines wether the laserbeams go from outside to the center if true, if false the laserbeams should go inwards to outwards
        this.inwards = inwards;
        setAngles();
    }

    private void setAngles () {
        if (inwards) {
            lowerLaserbeamHighestAngle = 185;
            upperLaserbeamLowestAngle = 175;
        }

        else {
            lowerLaserbeamLowestAngle = 160;  // Start closer to the center
            lowerLaserbeamHighestAngle = 190; // Diverging outward

            upperLaserbeamLowestAngle = 170;  // Start closer to the center
            upperLaserbeamHighestAngle = 200; // Diverging outward
            angleStepSize = 0.25f;
        }
    }

    @Override
    //Return if the behaviour is completed.
    // if returns true; this behaviour is removed and another is selected
    // if false, this behaviour remains and this method keeps getting called
    public boolean activateBehaviour (Enemy enemy) {
        double currentTime = GameStateInfo.getInstance().getGameSeconds();
        if (upperChargingUpAnimation == null || lowerChargingUpAnimation == null) {
            initSpawnAnimations(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateAnimationLocations(enemy);

            if (!lowerChargingUpAnimation.isPlaying() && !isFiringLaserbeams) {
                upperChargingUpAnimation.refreshAnimation();
                lowerChargingUpAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(upperChargingUpAnimation);
                AnimationManager.getInstance().addUpperAnimation(lowerChargingUpAnimation);
                try {
                    AudioManager.getInstance().addAudio(AudioEnums.ChargingLaserbeam);
                } catch (UnsupportedAudioFileException | IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (lowerChargingUpAnimation.isPlaying() &&
                    lowerChargingUpAnimation.getCurrentFrame() == lowerChargingUpAnimation.getTotalFrames() - 1 &&
                    !isFiringLaserbeams) {
                updateLaserbeamOriginPoints();
                createLaserbeams(enemy);
                lowerChargingUpAnimation.setVisible(false);
                upperChargingUpAnimation.setVisible(false);
                MissileManager.getInstance().addLaserBeam(upperLaserbeam);
                MissileManager.getInstance().addLaserBeam(lowerLaserbeam);
                isFiringLaserbeams = true;
            }
        }


        if (isFiringLaserbeams) {
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

    private void changeLaserbeamAngle () {
        if (inwards) {
            upperLaserbeam.setAngleDegrees(upperLaserbeam.getAngleDegrees() - angleStepSize);
            if (upperLaserbeam.getAngleDegrees() <= upperLaserbeamLowestAngle) {
                upperLaserbeam.setVisible(false);
            }

            lowerLaserbeam.setAngleDegrees(lowerLaserbeam.getAngleDegrees() + angleStepSize);
            if (lowerLaserbeam.getAngleDegrees() >= lowerLaserbeamHighestAngle) {
                lowerLaserbeam.setVisible(false);
            }
        } else {
            upperLaserbeam.setAngleDegrees(upperLaserbeam.getAngleDegrees() + angleStepSize);
            if (upperLaserbeam.getAngleDegrees() >= upperLaserbeamHighestAngle) {
                upperLaserbeam.setVisible(false);
            }

            lowerLaserbeam.setAngleDegrees(lowerLaserbeam.getAngleDegrees() - angleStepSize);
            if (lowerLaserbeam.getAngleDegrees() <= lowerLaserbeamLowestAngle) {
                lowerLaserbeam.setVisible(false);
            }
        }
    }


    private void createLaserbeams (Enemy enemy) {
        //Create upper laserbeam

        float damage = enemy.getDamage() / 2;
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(20);
        upperLaserbeamConfiguration.setOriginPoint(upperLaserbeamOriginPoint);

        LaserbeamConfiguration lowerLaserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        lowerLaserbeamConfiguration.setAmountOfLaserbeamSegments(20);
        lowerLaserbeamConfiguration.setOriginPoint(lowerLaserbeamOriginPoint);

        if (inwards) {
            upperLaserbeamConfiguration.setAngleDegrees(upperLaserbeamHighestAngle);
            lowerLaserbeamConfiguration.setAngleDegrees(lowerLaserbeamLowestAngle);
        } else {
            upperLaserbeamConfiguration.setAngleDegrees(upperLaserbeamLowestAngle);
            lowerLaserbeamConfiguration.setAngleDegrees(lowerLaserbeamHighestAngle);
        }

        if (upperLaserbeam != null) {
            upperLaserbeam.setVisible(false);
            upperLaserbeam.setOwner(enemy);
        }

        if (lowerLaserbeam != null) {
            lowerLaserbeam.setVisible(false);
            lowerLaserbeam.setOwner(enemy);
        }

        upperLaserbeam = new AngledLaserBeam(upperLaserbeamConfiguration);
        lowerLaserbeam = new AngledLaserBeam(lowerLaserbeamConfiguration);
        upperLaserbeam.setKnockbackStrength(13);
        lowerLaserbeam.setKnockbackStrength(13);
    }

    private void updateLaserbeamOriginPoints () {
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

    private void deleteLaserbeams () {
        if (upperLaserbeam != null) {
            upperLaserbeam.setVisible(false);
        }

        if (lowerLaserbeam != null) {
            lowerLaserbeam.setVisible(false);
        }
    }


    private void initSpawnAnimations (Enemy enemy) {
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

    private SpriteAnimationConfiguration createChargingAnimationConfig (Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.LaserbeamCharging);

        return new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
    }

    private void updateAnimationLocations (Enemy enemy) {
        upperChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate() + Math.round(enemy.getWidth() * 0.24f),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.3125f)
        );

        lowerChargingUpAnimation.setCenterCoordinates(
                enemy.getXCoordinate() + Math.round(enemy.getWidth() * 0.24f),
                enemy.getYCoordinate() + Math.round(enemy.getHeight() * 0.6875f)
        );

    }

    @Override
    public int getPriority () {
        return priority;
    }

    @Override
    public boolean isAvailable (Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameStateInfo.getInstance().getGameSeconds() >= lastAttackedTime + attackCooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }
}
