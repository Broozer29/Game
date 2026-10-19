package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamIndicator;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.AudioManager;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.Random;

public class FinalBossPhaseOneLaserbeamAttack implements BossActionable {
    //Fires two laserbeams from the nose around the player, aimed by tracking indicators. Once fired, both sweep the same way to drag the trap along
    private int priority = 15;
    private float cooldown = 7.5f;
    private double lastFiredTime = GameState.getInstance().getGameSeconds();
    private boolean isFiringLaserbeams;
    private double startedFiringTime = 0;
    private double duration = 3;
    private boolean isLiningUp = false;

    private SpriteAnimation chargingAnimation;
    private AngledLaserBeam upperTrackingLaserbeam;
    private LaserbeamIndicator upperLaserbeamIndicator;
    private AngledLaserBeam lowerTrackingLaserbeam;
    private LaserbeamIndicator lowerLaserbeamIndicator;

    private GameObject target;
    //Degrees both laserbeams rotate per update once fired. setAngleDegrees rounds to 0.2, so this effectively moves 0.2 degrees per update
    private float angleDegreeIncrement = 0.14f;
    //1 sweeps clockwise, -1 counterclockwise. Picked randomly every time the laserbeams fire
    private int rotationDirection = 1;
    private Random random = new Random();
    //Upper laserbeam aims this many degrees off the target, the lower one the same amount the other way. Indicators use the same offset
    private float laserbeamAngleOffset = 10;


    @Override
    public boolean activateBehaviour(Enemy enemy) {

        double currentTime = GameState.getInstance().getGameSeconds();
        if (chargingAnimation == null) {
            initLaserbeamChargeAnimations(enemy);
        } else {
            updateLaserbeamChargeAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastFiredTime + cooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            setLaserbeamOriginAnimation(enemy);
            if (!chargingAnimation.isPlaying() && !isFiringLaserbeams) {
                lockOnTarget(enemy);
                chargingAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
                AudioManager.getInstance().addAudio(AudioEnums.ChargingLaserbeam);
                isLiningUp = true;
            }

            if (chargingAnimation.isPlaying() &&
                    chargingAnimation.getCurrentFrame() == chargingAnimation.getTotalFrames() - 1 &&
                    !isFiringLaserbeams) {
                createLaserbeams(enemy);

                upperTrackingLaserbeam.update(); //Place the segments before adding them to missilemanager
                lowerTrackingLaserbeam.update();
                chargingAnimation.setVisible(false);
                MissileManager.getInstance().addLaserBeam(upperTrackingLaserbeam);
                MissileManager.getInstance().addLaserBeam(lowerTrackingLaserbeam);
                startedFiringTime = currentTime;
                isFiringLaserbeams = true;
                isLiningUp = false;
            }

        }

        if (this.isLiningUp && this.upperLaserbeamIndicator == null) {
            upperLaserbeamIndicator = new LaserbeamIndicator(chargingAnimation.getCenterXCoordinate(), chargingAnimation.getCenterYCoordinate(), target.getCenterXCoordinate(), target.getCenterYCoordinate(), 1, enemy);
            upperLaserbeamIndicator.setLength(20 * Laserbeam.bodyWidth);
            upperLaserbeamIndicator.setAngleOffset(laserbeamAngleOffset);
            MissileManager.getInstance().addLaserbeamIndicator(upperLaserbeamIndicator);

            lowerLaserbeamIndicator = new LaserbeamIndicator(chargingAnimation.getCenterXCoordinate(), chargingAnimation.getCenterYCoordinate(), target.getCenterXCoordinate(), target.getCenterYCoordinate(), 1, enemy);
            lowerLaserbeamIndicator.setLength(20 * Laserbeam.bodyWidth);
            lowerLaserbeamIndicator.setAngleOffset(-laserbeamAngleOffset);
            MissileManager.getInstance().addLaserbeamIndicator(lowerLaserbeamIndicator);
        }

        if (this.isLiningUp && this.upperLaserbeamIndicator != null) {
            upperLaserbeamIndicator.setStartingXCoordinate(chargingAnimation.getCenterXCoordinate());
            upperLaserbeamIndicator.setStartingYCoordinate(chargingAnimation.getCenterYCoordinate());
            lowerLaserbeamIndicator.setStartingXCoordinate(chargingAnimation.getCenterXCoordinate());
            lowerLaserbeamIndicator.setStartingYCoordinate(chargingAnimation.getCenterYCoordinate());
            this.upperLaserbeamIndicator.targetTowardsCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
            this.lowerLaserbeamIndicator.targetTowardsCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        }

        if (isFiringLaserbeams) {
            updateLaserbeamOriginPoints(enemy);
            rotateLaserbeams();
            updateLaserbeamVisibility();
            if (upperLaserbeamIndicator != null && upperLaserbeamIndicator.isActive()) {
                upperLaserbeamIndicator.setActive(false);
                lowerLaserbeamIndicator.setActive(false);
                upperLaserbeamIndicator = null;
                lowerLaserbeamIndicator = null;
            }
            if (!upperTrackingLaserbeam.isVisible()) {
                upperTrackingLaserbeam = null;
                lowerTrackingLaserbeam = null;
                enemy.setAttacking(false);
                isFiringLaserbeams = false;
                lastFiredTime = currentTime;
                return true;
            }
            return false;
        }

        return isFiringLaserbeams; //Laserbeams should removed and this attack is finished
    }

    private void updateLaserbeamVisibility() {
        if (upperTrackingLaserbeam != null && startedFiringTime + duration < GameState.getInstance().getGameSeconds()) {
            upperTrackingLaserbeam.setVisible(false);
            lowerTrackingLaserbeam.setVisible(false);
        }
    }

    private void rotateLaserbeams() {
        if (upperTrackingLaserbeam != null && lowerTrackingLaserbeam != null) {
            upperTrackingLaserbeam.setAngleDegrees(upperTrackingLaserbeam.getAngleDegrees() + angleDegreeIncrement * rotationDirection);
            lowerTrackingLaserbeam.setAngleDegrees(lowerTrackingLaserbeam.getAngleDegrees() + angleDegreeIncrement * rotationDirection);
        }
    }

    //The angle the indicators tracked towards, so the laserbeams fire exactly where the telegraph pointed
    private double getAimedAngleDegrees() {
        if (upperLaserbeamIndicator != null) {
            return upperLaserbeamIndicator.getCurrentAngleDegrees();
        }
        return Math.toDegrees(Math.atan2(
                target.getCenterYCoordinate() - chargingAnimation.getCenterYCoordinate(),
                target.getCenterXCoordinate() - chargingAnimation.getCenterXCoordinate()
        ));
    }

    private void lockOnTarget(Enemy enemy) {
        target = PlayerManager.getInstance().getFurthestSpaceShip(enemy);
    }

    private void createLaserbeams(Enemy enemy) {
        //Create upper laserbeam
        float damage = enemy.getDamage() * 0.2f;
        double aimedAngleDegrees = getAimedAngleDegrees();
        rotationDirection = random.nextBoolean() ? 1 : -1;

        LaserbeamConfiguration laserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        laserbeamConfiguration.setAmountOfLaserbeamSegments(20);
        laserbeamConfiguration.setOriginPoint(new Point(
                chargingAnimation.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4,
                chargingAnimation.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12
        ));
        laserbeamConfiguration.setBlocksMovement(true);

        if (upperTrackingLaserbeam != null) {
            upperTrackingLaserbeam.setVisible(false);
        }

        laserbeamConfiguration.setAngleDegrees(aimedAngleDegrees + laserbeamAngleOffset);
        upperTrackingLaserbeam = new AngledLaserBeam(laserbeamConfiguration);
        updateLaserbeamOriginPoints(enemy);
        upperTrackingLaserbeam.setOwner(enemy);

        if (lowerTrackingLaserbeam != null) {
            lowerTrackingLaserbeam.setVisible(false);
        }
        laserbeamConfiguration.setAngleDegrees(aimedAngleDegrees - laserbeamAngleOffset);
        lowerTrackingLaserbeam = new AngledLaserBeam(laserbeamConfiguration);
        updateLaserbeamOriginPoints(enemy);
        lowerTrackingLaserbeam.setOwner(enemy);
    }

    private void updateLaserbeamChargeAnimation(Enemy enemy) {
        chargingAnimation.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
    }

    private void initLaserbeamChargeAnimations(Enemy enemy) {
        chargingAnimation = new SpriteAnimation(createChargingAnimationConfig(enemy));
        chargingAnimation.setAnimationScale(2f);
        chargingAnimation.setFrameDelay(10);
        setLaserbeamOriginAnimation(enemy);
    }

    private SpriteAnimationConfiguration createChargingAnimationConfig(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(enemy.getXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.LaserbeamCharging);

        return new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
    }

    private void setLaserbeamOriginAnimation(Enemy enemy) {
        chargingAnimation.setCenterCoordinates(
                enemy.getChargingUpAttackAnimation().getCenterXCoordinate(),
                enemy.getChargingUpAttackAnimation().getCenterYCoordinate()
        );
    }

    private void updateLaserbeamOriginPoints(Enemy enemy) {
        if (upperTrackingLaserbeam != null) {
            upperTrackingLaserbeam.setOriginPoint(new Point(
                    enemy.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4,
                    enemy.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12
            ));
        }
        if (lowerTrackingLaserbeam != null) {
            lowerTrackingLaserbeam.setOriginPoint(new Point(
                    enemy.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4,
                    enemy.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12
            ));
        }
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
        if (enemy instanceof FinalBoss finalBoss) {
            return finalBoss.isAllowedToFire()
                    && finalBoss.getBossPhase() == FinalBoss.BOSSPHASE_1
                    && GameState.getInstance().getGameSeconds() >= lastFiredTime + cooldown
                    && WithinVisualBoundariesCalculator.isWithinBoundaries(finalBoss);
        }
        return false;
    }

}
