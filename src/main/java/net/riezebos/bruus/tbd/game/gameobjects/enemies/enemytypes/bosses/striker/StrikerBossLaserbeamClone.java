package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.striker.behaviour.StrikerBossCloneLaserbeamAttack;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.TrackingLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class StrikerBossLaserbeamClone extends Enemy {
    private TrackingLaserBeam trackingLaserbeam = null;
    private SpriteAnimation chargingAnimation;
    private boolean isFiringLaserbeams = false;

    public StrikerBossLaserbeamClone(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.SmokeExplosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1.15f);
        this.deathSound = null;
        this.damage = 15;
        this.attackSpeed = 5;
        this.knockbackStrength = 9;
        this.allowedToFire = false;

        createChargingAnimationConfig();
        setLaserbeamOriginAnimation();
    }


    public boolean isShowHealthBar() {
        return false;
    }

    @Override
    public void fireAction() {
        updateChargingAttackAnimationCoordination();

        if (WithinVisualBoundariesCalculator.isWithinBoundaries(this) && allowedToFire && !isFiringLaserbeams) {
            GameObject closestPlayer = PlayerManager.getInstance().getClosestSpaceShip(this);
            setLaserbeamOriginAnimation();
            this.rotateGameObjectTowards(closestPlayer.getCenterXCoordinate(), closestPlayer.getCenterYCoordinate(), false);
            if (!chargingAnimation.isPlaying() && !isFiringLaserbeams) {
                this.isAttacking = true;
                chargingAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingAnimation);
                //DO NOT PLAY AUDIO charging, the StrikerBoss should play the audio once, not these lads
            }

            if (chargingAnimation.isPlaying() &&
                    chargingAnimation.getCurrentFrame() == chargingAnimation.getTotalFrames() - 1 &&
                    !isFiringLaserbeams) {
                createLaserbeams(closestPlayer);
                trackingLaserbeam.update(); //Prevents the laserbeams from "jumping" to the right position by doing it before adding them to missilemanager
                chargingAnimation.setVisible(false);
                MissileManager.getInstance().addLaserBeam(trackingLaserbeam);
                isFiringLaserbeams = true;
            }
        }

        //Keep firing and don't stop, the StrikerBoss sends the signal to stop/detonate
        if (isFiringLaserbeams) {
            updateLaserbeamOriginPoints();
            if (!trackingLaserbeam.isVisible()) {
                trackingLaserbeam = null;
                this.setAttacking(false);
                isFiringLaserbeams = false;
            }
        }
    }

    public void detonateClone() {
        this.destructionAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        this.takeDamage(this.getMaxHitPoints() * 10);
    }

    private void createChargingAnimationConfig() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(this.getChargingUpAttackAnimation().getCenterXCoordinate());
        spriteConfiguration.setyCoordinate(this.getChargingUpAttackAnimation().getCenterYCoordinate());
        spriteConfiguration.setScale(1);
        spriteConfiguration.setImageType(ImageEnums.PinkLaserbeamCharging);

        chargingAnimation = new SpriteAnimation(new SpriteAnimationConfiguration(spriteConfiguration, 1, false));
        chargingAnimation.setAnimationScale(2f);
        chargingAnimation.setFrameDelay(10);
    }

    private void setLaserbeamOriginAnimation() {
        chargingAnimation.setCenterCoordinates(
                this.getChargingUpAttackAnimation().getCenterXCoordinate(),
                this.getChargingUpAttackAnimation().getCenterYCoordinate()
        );
    }
    private void updateLaserbeamOriginPoints() {
        if (trackingLaserbeam != null) {
            trackingLaserbeam.setOriginPoint(new Point(
                    this.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4,
                    this.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12
            ));
        }
    }

    private void createLaserbeams (GameObject target) {
        float damage = this.getDamage() * StrikerBossCloneLaserbeamAttack.damageRatio;
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(false, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(StrikerBossCloneLaserbeamAttack.laserbeamBodySegmentLength);
        upperLaserbeamConfiguration.setTargetToAimAt(target);
        upperLaserbeamConfiguration.setOriginPoint(new Point(
                this.getCenterXCoordinate(),
                this.getCenterYCoordinate()
        ));

        trackingLaserbeam = new TrackingLaserBeam(upperLaserbeamConfiguration);
        updateLaserbeamOriginPoints();
        trackingLaserbeam.setOwner(this);
        trackingLaserbeam.setMaxRotationPerUpdate(StrikerBossCloneLaserbeamAttack.maxLaserbeamRotationsPerUpdate);
    }
}