package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.bosses.finalboss.behavior.fasetwo;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.MissileManager;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.AngledLaserBeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.Laserbeam;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.laserbeams.LaserbeamConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class FinalBossLaserbeamClone extends Enemy {
    private AngledLaserBeam angledLaserbeam = null;
    private SpriteAnimation chargingAnimation;
    private boolean isFiringLaserbeams = false;

    private float laserbeamAngle;
    private static int laserbeamBodyLength = 6;
    private boolean shouldBeDead = false;

    public FinalBossLaserbeamClone(SpriteAnimationConfiguration spriteConfiguration, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {

        super(spriteConfiguration, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration.getSpriteConfiguration(), 1, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.SmokeExplosion);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1.15f);
        this.deathSound = null;
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
                createLaserbeams();
                angledLaserbeam.update(); //Prevents the laserbeams from "jumping" to the right position by doing it before adding them to missilemanager
                chargingAnimation.setVisible(false);
                MissileManager.getInstance().addLaserBeam(angledLaserbeam);
                isFiringLaserbeams = true;
            }
        }

        //Keep firing and don't stop, the StrikerBoss sends the signal to stop/detonate
        if (isFiringLaserbeams) {
            updateLaserbeamOriginPoints();
            if (!angledLaserbeam.isVisible()) {
                angledLaserbeam = null;
                this.setAttacking(false);
                isFiringLaserbeams = false;
            }
        }

        //Theoretically, this should never happen, but there are reports that clones are not correctly deleted so this might be a safeguard
        if (shouldBeDead) {
            if (this.destructionAnimation != null) {
                this.destructionAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
            }
            this.takeDamage(this.getMaxHitPoints() * 100000);
            this.setVisible(false);
        }
    }

    public void detonateClone() {
        this.destructionAnimation.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        this.takeDamage(this.getMaxHitPoints() * 100000);
        shouldBeDead = true;
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
        if (angledLaserbeam != null) {
            angledLaserbeam.setOriginPoint(new Point(
                    this.getCenterXCoordinate() - Laserbeam.bodyWidth / 2 + 4,
                    this.getCenterYCoordinate() - Laserbeam.bodyWidth / 2 + 12
            ));
        }
    }

    private void createLaserbeams() {
        float damage = this.getDamage() * FinalBossLaserbeamCloneAttack.damageRatio;
        LaserbeamConfiguration upperLaserbeamConfiguration = new LaserbeamConfiguration(false, damage);
        upperLaserbeamConfiguration.setAmountOfLaserbeamSegments(laserbeamBodyLength);
        upperLaserbeamConfiguration.setAngleDegrees(this.laserbeamAngle);
        upperLaserbeamConfiguration.setOriginPoint(new Point(
                this.getCenterXCoordinate(),
                this.getCenterYCoordinate()
        ));

        angledLaserbeam = new AngledLaserBeam(upperLaserbeamConfiguration);
        updateLaserbeamOriginPoints();
        angledLaserbeam.setOwner(this);


        //dirty hack that should help recenter the angledLaserbeam before its added to the game
        for(int i = 0; i< 10; i++){
            angledLaserbeam.update();
        }
    }

    public void setLaserbeamAngle(float laserbeamAngle) {
        this.laserbeamAngle = laserbeamAngle;
    }
}