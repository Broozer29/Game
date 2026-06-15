package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.blueboss.behaviour;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.BossActionable;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamIndicator;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlueBossSpinningLaserbeamAttack implements BossActionable {

    private int attackCooldown = 14;
    private double lastAttackedTime = GameState.getInstance().getGameSeconds() + 15;
    private double startedFiringTime = 0;
    private int attackDuration = 6;
    private boolean isFiringLaserbeams = false;
    private int priority = 10;
    private List<Laserbeam> laserbeamList = new LinkedList<>();
    private List<LaserbeamIndicator> laserbeamIndicators = new LinkedList<>();
    private SpriteAnimation chargingAnimation = null;
    private List<Integer> angleDegrees = new ArrayList<>();

    public BlueBossSpinningLaserbeamAttack() {
        for(int i = 0; i <= 360; i += 60){
            angleDegrees.add(i);
        }
    }

    @Override
    public boolean activateBehaviour(Enemy enemy) {
        double currentTime = GameState.getInstance().getGameSeconds();

        if (chargingAnimation == null) {
            createChargingAnimation(enemy);
        }

        if (enemy.isAllowedToFire() && currentTime >= lastAttackedTime + attackCooldown && WithinVisualBoundariesCalculator.isWithinBoundaries(enemy)) {
            updateChargingAnimationLocation(enemy);

            if (!chargingAnimation.isPlaying() && !isFiringLaserbeams) {
                chargingAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
                enemy.setAttacking(true);
                AudioManager.getInstance().addAudio(AudioEnums.ChargingLaserbeam);
                createLaserBeamIndicators(enemy);

                for(LaserbeamIndicator laserbeamIndicator : laserbeamIndicators){
                    MissileManager.getInstance().addLaserbeamIndicator(laserbeamIndicator);
                }
            }

            updateLaserbeamIndicators(chargingAnimation);

            if (chargingAnimation.isPlaying() && !isFiringLaserbeams &&
                    chargingAnimation.getCurrentFrame() == chargingAnimation.getTotalFrames() - 1) {
                createLaserbeams(enemy);
                updateLaserbeamCoordinates(enemy);
                isFiringLaserbeams = true;
                startedFiringTime = currentTime;
                for (Laserbeam laserbeam : laserbeamList) {
                    MissileManager.getInstance().addLaserBeam(laserbeam);
                }
                for(LaserbeamIndicator laserbeamIndicator : laserbeamIndicators){
                    laserbeamIndicator.setActive(false);
                }
            }

        }

        //Attacking phase
        if (isFiringLaserbeams) {
            updateLaserbeamCoordinates(enemy);
            if (attackIsFinished(currentTime)) {
                for (Laserbeam laserbeam : laserbeamList) {
                    laserbeam.setVisible(false);
                }
                laserbeamList.clear();
                enemy.setAttacking(false);
                isFiringLaserbeams = false;
                lastAttackedTime = currentTime;
                return true;
            }
            return false;
        }

        return isFiringLaserbeams;
    }

    private boolean attackIsFinished(double currenttime) {
        return currenttime >= startedFiringTime + attackDuration;
    }

    private void createLaserBeamIndicators(GameObject owner){
        for (Integer integer : angleDegrees) {
            int distance = (laserbeamBodyAmount + 2) * Laserbeam.bodyWidth;
            laserbeamIndicators.add(new LaserbeamIndicator(owner.getCenterXCoordinate(), owner.getCenterYCoordinate(), integer, distance, owner));
        }
    }

    private void updateLaserbeamIndicators(SpriteAnimation chargingAnimation) {
        for(LaserbeamIndicator laserbeamIndicator : laserbeamIndicators){
            laserbeamIndicator.setStartingXCoordinate(chargingAnimation.getCenterXCoordinate());
            laserbeamIndicator.setStartingYCoordinate(chargingAnimation.getCenterYCoordinate());
        }
    }


    private int laserbeamBodyAmount = 25;
    private void createLaserbeams(Enemy enemy) {

        for (Integer integer : angleDegrees) {
            LaserbeamConfiguration laserbeamConfiguration = new LaserbeamConfiguration(true, enemy.getDamage() / 2);
            laserbeamConfiguration.setAmountOfLaserbeamSegments(laserbeamBodyAmount);
            laserbeamConfiguration.setBlocksMovement(false);
            laserbeamConfiguration.setOriginPoint(enemy.getCurrentCenterLocation());
            laserbeamConfiguration.setAngleDegrees(integer);
            Laserbeam laserbeam = new AngledLaserBeam(laserbeamConfiguration);
            laserbeamList.add(laserbeam);
            laserbeam.setOwner(enemy);
            MissileManager.getInstance().addLaserBeam(laserbeam);
        }
    }

    private void updateLaserbeamCoordinates(Enemy enemy) {
        for (Laserbeam laserbeam : laserbeamList) {
            laserbeam.setAngleDegrees(laserbeam.getAngleDegrees() + 0.85f);
            laserbeam.setOriginPoint(
                    new Point(enemy.getCenterXCoordinate() - Laserbeam.bodyWidth / 2,
                            enemy.getCenterYCoordinate() - Laserbeam.bodyWidth));
            if (laserbeam.getAngleDegrees() >= 360) {
                laserbeam.setAngleDegrees(laserbeam.getAngleDegrees() - 360);
            }
        }
    }

    private int yOffset = -25;
    private void createChargingAnimation(Enemy enemy) {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setScale(2);
        spriteConfiguration.setxCoordinate(enemy.getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(enemy.getCenterYCoordinate());
        spriteConfiguration.setImageType(ImageEnums.LaserbeamCharging);

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 10, false);
        this.chargingAnimation = new SpriteAnimation(spriteAnimationConfiguration);
        this.chargingAnimation.addYOffset(yOffset);

    }

    private void updateChargingAnimationLocation(Enemy enemy) {
        this.chargingAnimation.setCenterCoordinates(enemy.getCenterXCoordinate(), enemy.getCenterYCoordinate());
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
