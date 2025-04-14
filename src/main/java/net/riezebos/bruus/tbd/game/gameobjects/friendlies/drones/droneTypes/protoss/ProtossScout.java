package net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.FriendlyObjectConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.Drone;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.DroneTypes;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.items.items.carrier.SynergeticLink;
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

public class ProtossScout extends Drone {
    private GameObject target;
    private int attackRange = 200;
    private float defaultMoveSpeed = 3.25f;
    private boolean isMovingSlow = false;
    public static float scoutDamageFactor = 0.5f;
    private boolean isMovingAroundCarrierDrone = false;
    private float baseDamage = 0;

    public ProtossScout(SpriteAnimationConfiguration spriteAnimationConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, droneConfiguration, movementConfiguration);
        this.maxHitPoints = PlayerStats.getInstance().getProtossShipBaseHealth();
        this.currentHitpoints = maxHitPoints;
        this.baseArmor = PlayerStats.getInstance().getProtossShipBaseArmor();
        super.isProtoss = true;
        super.initProtossDeathExplosion();
        super.droneType = DroneTypes.ProtossScout;
        super.deathSound = AudioEnums.ProtossShipDeath;
        super.appliesOnHitEffects = true;
        this.baseDamage = damage;
    }

    public void activateObject () {
        if(this.getCurrentLocation().equals(this.movementConfiguration.getDestination())){
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
            this.damage = baseDamage * (1 + synergeticLink.getCurrentScoutBonusDamage());
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



    public void fireAction () {
        if(target == null){
            target = EnemyManager.getInstance().getClosestEnemyWithinDistance(this.getCenterXCoordinate(), this.getCenterYCoordinate(), attackRange);
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
        MissileEnums missileType = MissileEnums.PlayerLaserbeam;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(missileType.getImageType());
        missileSpriteConfiguration.setScale(1f);

        float xMovementSpeed = 10f;
        float yMovementSpeed = 10f;
        float damage = PlayerStats.getInstance().getNormalAttackDamage() * scoutDamageFactor;
        Direction rotation = Direction.RIGHT;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        PathFinder pathFinder = new StraightLinePathFinder();

        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementSpeed, yMovementSpeed, pathFinder, movementPatternSize, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = true;
        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType
                , 100, 100, null, damage, ImageEnums.Impact_Explosion_One, isFriendly, allowedToDealDamage, objectType,
                missileType.isUsesBoxCollision(), false, false, false);

        missileConfiguration.setPiercesMissiles(PlayerStats.getInstance().getPiercingMissilesAmount() > 0);
        missileConfiguration.setAmountOfPierces(PlayerStats.getInstance().getPiercingMissilesAmount());

        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
        missile.setObjectType("Protoss Scout Missile");

        missile.resetMovementPath();



        Point point = new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        point.setX(point.getX() - missile.getWidth() / 2);
        point.setY(point.getY() - missile.getHeight() / 2);
        movementConfiguration.setDestination(point);
        missile.setCenterCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());
        missile.rotateObjectTowardsDestination(true);
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover
        missile.setOwnerOrCreator(this);
        MissileManager.getInstance().addExistingMissile(missile);
    }
}
