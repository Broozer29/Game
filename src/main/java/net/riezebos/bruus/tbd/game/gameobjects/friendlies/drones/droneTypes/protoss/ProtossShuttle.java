package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.EmergencyRepairs;
import net.riezebos.bruus.tbd.game.items.items.carrier.SynergeticLink;
import net.riezebos.bruus.tbd.game.items.items.carrier.VengeanceProtocol;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.audio.enums.AudioEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;

public class ProtossShuttle extends Drone {
    private GameObject target;
    private int attackRange = 300;
    private float defaultMoveSpeed = 2.55f;
    private boolean isMovingAroundCarrierDrone = false;
    private boolean isMovingSlow = false;
    public static float shuttleDamageRatio = 5f;
    private float baseAttackSpeed = 1.25f; //duplicate from friendlycreator, code smell because its hardcoded seperately

    public ProtossShuttle(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
        super.isProtoss = true;
        this.maxHitPoints = PlayerStats.getInstance().getProtossShipBaseHealth();
        this.currentHitpoints = maxHitPoints;
        this.baseArmor = PlayerStats.getInstance().getProtossShipBaseArmor();
        super.initProtossDeathExplosion();
        super.droneType = DroneTypes.ProtossShuttle;
        super.deathSound = AudioEnums.ProtossShipDeath;
        super.appliesOnHitEffects = true;
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

        SynergeticLink synergeticLink = (SynergeticLink) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.SynergeticLink);
        if(synergeticLink != null){
            this.attackSpeed = baseAttackSpeed * (1 - synergeticLink.getCurrentShuttleAttackSpeedBonus());
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
            target = EnemyManager.getInstance().getClosestEnemyTargetWithinDistance(this.getCenterXCoordinate(), this.getCenterYCoordinate(), attackRange);
        }

        if(target != null){
            if(isTooFarAway() || isTargetDeadOrInvisible()){
                updateMoveSpeed(false);
                target = null;
                this.setAllowedVisualsToRotate(true); //Allow for rotation towards destination again
                this.rotateObjectTowardsDestination(true);
                this.setAllowedVisualsToRotate(false);
            } else {
                updateMoveSpeed(true);
                this.setAllowedVisualsToRotate(true);
                double currentTime = GameState.getInstance().getGameSeconds();
                this.rotateGameObjectTowards(target.getCenterXCoordinate(), target.getCenterYCoordinate(), true);
                this.setAllowedVisualsToRotate(false);
                if (currentTime >= lastAttackTime + this.getAttackSpeed()) {
                    shootMissile();
                    lastAttackTime = currentTime;
                }
            }
        }
    }

    private boolean isTargetDeadOrInvisible(){
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
        double distance = ProtossUtils.getDistanceToRectangle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetBounds);

        return distance > attackRange; // Directly compare Euclidean distance
    }



    private void shootMissile(){
        MissileEnums missileType = MissileEnums.ProtossShuttleMissile;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(missileType.getImageType());
        missileSpriteConfiguration.setScale(0.15f);

        float xMovementSpeed = 3f;
        float yMovementSpeed = 3f;
        float damage = PlayerStats.getInstance().getNormalAttackDamage() * shuttleDamageRatio;
        Direction rotation = Direction.RIGHT;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        PathFinder pathFinder = new StraightLinePathFinder();

        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementSpeed, yMovementSpeed, pathFinder, movementPatternSize, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = true;
        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType
                , 100, 100, null, damage, missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                missileType.isUsesBoxCollision(), true, false, false);

        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
        missile.setObjectType("Protoss Shuttle Missile");

        missile.resetMovementPath();

        Point point = new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        point.setX(point.getX() - missile.getWidth() / 2);
        point.setY(point.getY() - missile.getHeight() / 2);
        movementConfiguration.setDestination(point);
        missile.setCenterCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover
        missile.setOwnerOrCreator(this);
        MissileManager.getInstance().addExistingMissile(missile);
    }

    @Override
    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairs) != null){
            EmergencyRepairs emergencyRepairs = (EmergencyRepairs) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.EmergencyRepairs);
            emergencyRepairs.applyEffectToObject(PlayerManager.getInstance().getSpaceship());
        }
        if(PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.VengeanceProtocol) != null){
            VengeanceProtocol vengeanceProtocol = (VengeanceProtocol) PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.VengeanceProtocol);
            vengeanceProtocol.applyEffectToObject(this);
        }
    }

}