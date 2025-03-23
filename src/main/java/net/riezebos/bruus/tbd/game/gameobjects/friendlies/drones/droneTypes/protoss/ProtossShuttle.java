package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class ProtossShuttle extends Drone {
    private GameObject target;
    private float detectionRange = 100;
    private float explosionScale = 1;
    private float shuttleDamage = PlayerStats.getInstance().getNormalAttackDamage() * 300;
    private boolean hasAccelerated = false;
    private static float shuttleSpeed = 2;
    private boolean isMovingAroundCarrierDrone = false;

    public ProtossShuttle(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
        super.isProtoss = true;
        this.maxHitPoints = PlayerStats.getInstance().getProtossShipBaseHealth();
        this.currentHitpoints = maxHitPoints;
        this.baseArmor = PlayerStats.getInstance().getProtossShipBaseArmor();
        super.initProtossDeathExplosion();
        super.droneType = DroneTypes.ProtossShuttle;
        super.deathSound = AudioEnums.ProtossShipDeath;
    }


    public void activateObject() {
        if (this.getCurrentLocation().equals(this.movementConfiguration.getDestination())) {
            this.movementConfiguration.resetMovementPath();
            this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
            this.setAllowedVisualsToRotate(true);
            this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint());
            this.isMovingAroundCarrierDrone = ProtossUtils.carrierDroneIsPresent();
        }

        if(!ProtossUtils.carrierDroneIsPresent() && this.isMovingAroundCarrierDrone){
            immediatlyReturnToCarrier();
        }

        fireAction();
    }

    private void immediatlyReturnToCarrier(){
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
        this.setAllowedVisualsToRotate(true);
        this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint());
        this.isMovingAroundCarrierDrone = ProtossUtils.carrierDroneIsPresent();
    }


    public void fireAction() {
        if (target == null) {
            target = EnemyManager.getInstance().getClosestEnemyWithinDistance(this.getCenterXCoordinate(), this.getCenterYCoordinate(), detectionRange);
        }

        if (target != null && target.isVisible() && target.getCurrentHitpoints() >= 0) {
            this.movementConfiguration.resetMovementPath();
            if (!this.hasAccelerated) {
                this.movementConfiguration.setXMovementSpeed(shuttleSpeed * 2f);
                this.movementConfiguration.setYMovementSpeed(shuttleSpeed * 2f);
                this.hasAccelerated = true;
            }
            this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
            this.setAllowedVisualsToRotate(true);
            this.movementConfiguration.setDestination(new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate()));
        } else {
            target = null;
            if (this.hasAccelerated) {
                this.movementConfiguration.setXMovementSpeed(shuttleSpeed);
                this.movementConfiguration.setYMovementSpeed(shuttleSpeed);
                this.hasAccelerated = false;
            }
        }
    }


    public void detonate() {
        SpriteConfiguration spriteConfiguration = new SpriteConfiguration();
        spriteConfiguration.setxCoordinate(this.xCoordinate);
        spriteConfiguration.setyCoordinate(this.yCoordinate);
        spriteConfiguration.setScale(explosionScale);
        spriteConfiguration.setImageType(ImageEnums.Explosion2); //placeholder

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);

        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(true, shuttleDamage, true, true);
        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());

        ExplosionManager.getInstance().addExplosion(explosion);
        this.setVisible(false);
    }

}