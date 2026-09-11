package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.faseone;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.FinalBoss;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamIndicator;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.TrackingLaserBeam;
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

public class FinalBossPhaseOneLaserbeamAttack implements BossActionable {
    //Vuurt een tracking laserbeam vanaf de neus die de speler volgt
    private int priority = 15;
    private float cooldown = 7.5f;
    private double lastFiredTime = GameState.getInstance().getGameSeconds(); //Immediatly set it to prevent carrier from immediatly sniping the player
    private boolean isFiringLaserbeams;
    private double startedFiringTime = 0;
    private double duration = 3;
    private boolean isLiningUp = false;

    private SpriteAnimation chargingAnimation;
    private TrackingLaserBeam trackingLaserbeam;
    private LaserbeamIndicator laserbeamIndicator;
    private GameObject target;
    private float angleDegreeIncrement = 0.13f;


    @Override
    public boolean activateBehaviour(Enemy enemy) {

        double currentTime = GameState.getInstance().getGameSeconds();
        if (chargingAnimation == null) {
            initSpawnAnimations(enemy);
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
                trackingLaserbeam.update(); //Prevents the laserbeams from "jumping" to the right position by doing it before adding them to missilemanager
                chargingAnimation.setVisible(false);
                MissileManager.getInstance().addLaserBeam(trackingLaserbeam);
                startedFiringTime = currentTime;
                isFiringLaserbeams = true;
                isLiningUp = false;
            }

        }

        if (this.isLiningUp && this.laserbeamIndicator == null) {
            laserbeamIndicator = new LaserbeamIndicator(chargingAnimation.getCenterXCoordinate(), chargingAnimation.getCenterYCoordinate(), target.getCenterXCoordinate(), target.getCenterYCoordinate(), 1, enemy);
            laserbeamIndicator.setLength(20 * Laserbeam.bodyWidth);
            MissileManager.getInstance().addLaserbeamIndicator(laserbeamIndicator);
        }

        if (this.isLiningUp && this.laserbeamIndicator != null) {
            laserbeamIndicator.setStartingXCoordinate(chargingAnimation.getCenterXCoordinate());
            laserbeamIndicator.setStartingYCoordinate(chargingAnimation.getCenterYCoordinate());
            this.laserbeamIndicator.targetTowardsCoordinates(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        }

        if (isFiringLaserbeams) {
            updateLaserbeamOriginPoints(enemy);
            updateLaserbeamVisibility();
            if (laserbeamIndicator != null && laserbeamIndicator.isActive()) {
                laserbeamIndicator.setActive(false);
                laserbeamIndicator = null;
            }
            if (!trackingLaserbeam.isVisible()) {
                trackingLaserbeam = null;
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
        if (trackingLaserbeam != null && startedFiringTime + duration < GameState.getInstance().getGameSeconds()) {
            trackingLaserbeam.setVisible(false);
        }
    }

    private void lockOnTarget(Enemy enemy){
        target = PlayerManager.getInstance().getFurthestSpaceShip(enemy);
    }

    private void createLaserbeams(Enemy enemy) {
        //Create upper laserbeam
        float damage = enemy.getDamage() * 0.2f;
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(20);
        upperLaserbeamConfiguration.setTargetToAimAt(target);
        upperLaserbeamConfiguration.setOriginPoint(new Point(
                chargingAnimation.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4,
                chargingAnimation.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12
        ));
        upperLaserbeamConfiguration.setBlocksMovement(true);

        if (trackingLaserbeam != null) {
            trackingLaserbeam.setVisible(false);
        }

        trackingLaserbeam = new TrackingLaserBeam(upperLaserbeamConfiguration);
        updateLaserbeamOriginPoints(enemy);
        trackingLaserbeam.setOwner(enemy);
        trackingLaserbeam.setMaxRotationPerUpdate(angleDegreeIncrement);
    }

    private void initSpawnAnimations(Enemy enemy) {
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
        if (trackingLaserbeam != null) {
            trackingLaserbeam.setOriginPoint(new Point(
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
