package net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.carrier.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
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

public class FireTrackingLaserbeam implements BossActionable {
    //Vuurt een tracking laserbeam vanaf de neus die de speler volgt
    private int priority = 15;
    private int cooldown = 20;
    private double lastFiredTime = 0;
    private boolean isFiringLaserbeams;
    private double startedFiringTime = 0;
    private double duration = 3;

    private SpriteAnimation chargingAnimation;
    private TrackingLaserBeam trackingLaserbeam;

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();
        if (chargingAnimation == null) {
            initSpawnAnimations(enemy);
        }


        if (enemy.isAllowedToFire() && currentTime >= lastFiredTime + cooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            setLaserbeamOriginAnimation(enemy);
            if (!chargingAnimation.isPlaying() && !isFiringLaserbeams) {
                chargingAnimation.refreshAnimation();
                enemy.setAttacking(true);
                AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
                AudioManager.getInstance().addAudio(AudioEnums.ChargingLaserbeam);
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
            }

        }

        if (isFiringLaserbeams) {
            updateLaserbeamOriginPoints();
            updateLaserbeamVisibility();
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

    private void createLaserbeams (Enemy enemy) {
        //Create upper laserbeam
        float damage = enemy.getDamage() * 0.2f;
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(true, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(20);
        upperLaserbeamConfiguration.setTargetToAimAt(PlayerManager.getInstance().getSpaceship());
        upperLaserbeamConfiguration.setOriginPoint(new Point(
                chargingAnimation.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4,
                chargingAnimation.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12
        ));
        upperLaserbeamConfiguration.setBlocksMovement(false);

        if (trackingLaserbeam != null) {
            trackingLaserbeam.setVisible(false);
        }

        trackingLaserbeam = new TrackingLaserBeam(upperLaserbeamConfiguration);
        updateLaserbeamOriginPoints();
        trackingLaserbeam.setOwner(enemy);
        trackingLaserbeam.setMaxRotationPerUpdate(0.2f);
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
        spriteConfiguration.setImageType(ImageEnums.PinkLaserbeamCharging);

        return new SpriteAnimationConfiguration(spriteConfiguration, 1, false);
    }

    private void setLaserbeamOriginAnimation(Enemy enemy) {
        chargingAnimation.setCenterCoordinates(
                enemy.getXCoordinate(),
                enemy.getCenterYCoordinate() - (enemy.getHeight() * 0.1f)
        );
    }

    private void updateLaserbeamOriginPoints() {
        if (trackingLaserbeam != null) {
            trackingLaserbeam.setOriginPoint(new Point(
                    chargingAnimation.getCenterXCoordinate() + Laserbeam.bodyWidth / 2,
                    chargingAnimation.getCenterYCoordinate() - (Laserbeam.bodyWidth / 2 + 12)
            ));
        }
    }


    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isAvailable(Enemy enemy) {
        return enemy.isAllowedToFire()
                && GameState.getInstance().getGameSeconds() >= lastFiredTime + cooldown
                && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy);
    }

}
