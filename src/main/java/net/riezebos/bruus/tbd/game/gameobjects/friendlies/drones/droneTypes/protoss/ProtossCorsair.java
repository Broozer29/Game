package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.Explosion;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.neutral.ExplosionManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.effects.EffectActivationTypes;
import net.riezebos.bruus.tbd.game.items.effects.effectimplementations.SpawnScrapMetalOnDeath;
import net.riezebos.bruus.tbd.game.items.items.carrier.AimAssist;
import net.riezebos.bruus.tbd.game.items.items.carrier.EmergencyRepairs;
import net.riezebos.bruus.tbd.game.items.items.carrier.VengeanceProtocol;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;

public class ProtossCorsair extends Drone {
    private GameObject target;
    private int detectionRange = 275;
    private float defaultMoveSpeed = 3f;
    private boolean isMovingSlow = false;
    private boolean isMovingAroundCarrierDrone = false;
    private float explosionSize = 0.4f;
    public static float explosionDamageFactor = 3;
    private boolean isCharging = false;

    public ProtossCorsair(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
        this.maxHitPoints = PlayerStats.getInstance().getProtossShipBaseHealth();
        this.currentHitpoints = maxHitPoints;
        this.baseArmor = PlayerStats.getInstance().getProtossShipBaseArmor();
        super.isProtoss = true;
        super.initProtossDeathExplosion();
        super.droneType = DroneTypes.ProtossCorsair;
        super.deathSound = AudioEnums.ProtossShipDeath;
        super.appliesOnHitEffects = true;
        this.ownerOrCreator = PlayerManager.getInstance().getSpaceship();

        SpawnScrapMetalOnDeath spawnScrapMetalOnDeath = new SpawnScrapMetalOnDeath(0.25f);
        this.effects.add(spawnScrapMetalOnDeath);

        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.AimAssist) != null){
            AimAssist aimAssist = (AimAssist) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.AimAssist);
            this.detectionRange *= (1+ aimAssist.getAttackRangeBonus());
        }
    }

    public void activateObject() {
        if (this.getCurrentLocation().equals(this.movementConfiguration.getDestination())) {
            this.movementConfiguration.resetMovementPath();
            this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
            this.setAllowedVisualsToRotate(true);
            this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint());
            this.isMovingAroundCarrierDrone = ProtossUtils.carrierDroneIsPresent();
        }

        if (!ProtossUtils.carrierDroneIsPresent() && this.isMovingAroundCarrierDrone) {
            immediatlyReturnToCarrier();
        }
        fireAction();
    }

    private void immediatlyReturnToCarrier() {
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
        this.setAllowedVisualsToRotate(true);
        this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint());
        this.isMovingAroundCarrierDrone = ProtossUtils.carrierDroneIsPresent();
    }


    public void fireAction() {
        if (target == null) {
            target = EnemyManager.getInstance().getClosestEnemyTargetWithinDistance(this.getCenterXCoordinate(), this.getCenterYCoordinate(), detectionRange);
        }

        if (target != null && !isCharging) {
            chargeToEnemy();
            this.setAllowedVisualsToRotate(true);
            this.rotateGameObjectTowards(target.getCenterXCoordinate(), target.getCenterYCoordinate(), true);
            this.setAllowedVisualsToRotate(false);
        }

        if (target != null && isCharging) {
            if (isTooFarAway() || isTargetDeadOrInvisible()) {
                target = null;
                isCharging = false;
                this.setAllowedVisualsToRotate(true); //Allow for rotation towards destination again
                this.rotateObjectTowardsDestination(true);
                this.setAllowedVisualsToRotate(false);
                immediatlyReturnToCarrier();
            }
        }
    }

    private void chargeToEnemy() {
        isCharging = true;
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
        this.movementConfiguration.setDestination(new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate()));
        this.isMovingAroundCarrierDrone = false;
    }

    @Override
    public void dealDamageToGameObject(GameObject gameObject) {
        SpriteConfiguration spriteConfiguration1 = new SpriteConfiguration();
        spriteConfiguration1.setxCoordinate(this.xCoordinate);
        spriteConfiguration1.setyCoordinate(this.yCoordinate);
        spriteConfiguration1.setScale(explosionSize);

        float damage = PlayerStats.getInstance().getNormalAttackDamage() * explosionDamageFactor;

        SpriteAnimationConfiguration spriteAnimationConfiguration = new SpriteAnimationConfiguration(spriteConfiguration1, 2, false);
        spriteAnimationConfiguration.getSpriteConfiguration().setImageType(ImageEnums.ProtossCorsairExplosion);

        ExplosionConfiguration explosionConfiguration = new ExplosionConfiguration(this.isFriendly(), damage, true, true);
        Explosion explosion = new Explosion(spriteAnimationConfiguration, explosionConfiguration);
        explosion.setOwnerOrCreator(this.ownerOrCreator);
        explosion.setScale(explosionSize);
        explosion.setCenterCoordinates(this.animation.getCenterXCoordinate(), this.animation.getCenterYCoordinate());
        ExplosionManager.getInstance().addExplosion(explosion);
        this.takeDamage(this.getMaxHitPoints() * 100); //kill itself to trigger ondeath effects
    }

    @Override
    protected void activateOnDeathEffects() {
        activateEffects(EffectActivationTypes.OnObjectDeath);
        cleanseAllEffects();
    }

    private boolean isTargetDeadOrInvisible() {
        return target != null && (!target.isVisible() || target.getCurrentHitpoints() <= 0);
    }

    private void updateMoveSpeed(boolean slow) {
        if (slow != isMovingSlow) { // Only update if there is a state change
            isMovingSlow = slow;
            float newSpeed = slow ? (defaultMoveSpeed * 0.7f) : defaultMoveSpeed;
            this.getMovementConfiguration().setXMovementSpeed(newSpeed); // Only call when needed
        }
    }

    private boolean isTooFarAway() {
        Rectangle targetBounds = target.getBounds(); // Get target's bounding box

        GameObject currentHoveringOwner = this.ownerOrCreator;
        if(ProtossUtils.carrierDroneIsPresent()){
            currentHoveringOwner = FriendlyManager.getInstance().getDronesByDroneType(DroneTypes.CarrierDrone).get(0);
        }
        double distance = ProtossUtils.getDistanceToRectangle(currentHoveringOwner.getCenterXCoordinate(), currentHoveringOwner.getCenterYCoordinate(), targetBounds);

        return isCharging ? distance > detectionRange * 1.25 : distance > detectionRange; // If charging, grants 25% bonus range, should prevent odd loss of target locks
    }

    @Override
    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();
        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairs) != null) {
            EmergencyRepairs emergencyRepairs = (EmergencyRepairs) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairs);
            emergencyRepairs.applyEffectToObject(PlayerManager.getInstance().getSpaceship());
        }

        if (PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.VengeanceProtocol) != null) {
            VengeanceProtocol vengeanceProtocol = (VengeanceProtocol) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.VengeanceProtocol);
            vengeanceProtocol.applyEffectToObject(this);
        }
    }
}
