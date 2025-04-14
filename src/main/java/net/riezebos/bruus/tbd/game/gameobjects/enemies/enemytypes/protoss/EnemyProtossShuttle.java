package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.protoss;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.enums.EnemyEnums;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;

public class EnemyProtossShuttle extends Enemy {
    private GameObject target;
    private int attackRange = 500;
    private float defaultMoveSpeed = 1.75f;
    private boolean isMovingSlow = false;
    private boolean justInitialized;
    private boolean wasFiringAtTarget = false;

    public EnemyProtossShuttle(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        justInitialized = true;
    }

    @Override
    public void fireAction() {
        //Handle construction here, because the "owner" is NOT given in the constructor, meaning it has to be done after initialization
        //Too lazy to refactor this for all enemies if this also solves it.
        if (justInitialized) {
            this.movementConfiguration.setPathFinder(new DestinationPathFinder());
            updateMovementPath();
            this.damage = this.ownerOrCreator.getDamage();
            this.attackSpeed = 3f;
            justInitialized = false;
            target = PlayerManager.getInstance().getSpaceship();
        }

        if(this.ownerOrCreator != null && this.ownerOrCreator.getCurrentHitpoints() <= 0){
            this.takeDamage(99999);
        }

        if (this.getCurrentLocation().equals(this.movementConfiguration.getDestination())) {
            updateMovementPath();
        }

        if (isTooFarAway()) {
            if (wasFiringAtTarget) {
                rotateObjectTowardsDestination();
                wasFiringAtTarget = false;
            }
            if (isMovingSlow) {
                updateMoveSpeed(false);
            }

        } else {
            if (!wasFiringAtTarget) {
                wasFiringAtTarget = true;
            }
            if (!isMovingSlow) {
                updateMoveSpeed(true);
            }
            engageTarget();
        }

    }

    private void updateMovementPath() {
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));
        this.setAllowedVisualsToRotate(true);

        GameObject target = this.ownerOrCreator;
        int minDistance = 80;
        int maxDistance = 350;

        if(!EnemyManager.getInstance().getEnemiesByType(EnemyEnums.EnemyCarrierBeacon).isEmpty()){
            target = EnemyManager.getInstance().getEnemiesByType(EnemyEnums.EnemyCarrierBeacon).get(0);
            minDistance = 20;
            maxDistance = 150;
        }

        this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint(target, minDistance, maxDistance));
    }

    private void rotateObjectTowardsDestination() {
        this.setAllowedVisualsToRotate(true);
        this.rotateObjectTowardsDestination(true);
        this.setAllowedVisualsToRotate(false);
    }

    private void engageTarget() {
        this.setAllowedVisualsToRotate(true);
        this.rotateGameObjectTowards(target.getCenterXCoordinate(), target.getCenterYCoordinate(), true);
        this.setAllowedVisualsToRotate(false);

        // Shoot if the attack cooldown has passed
        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed()) {
            shootMissile();
            lastAttackTime = currentTime;
        }
    }


    private void updateMoveSpeed(boolean slow) {
        if (slow != isMovingSlow) { // Only update if there is a state change
            isMovingSlow = slow;
            float newSpeed = slow ? (defaultMoveSpeed * 0.7f) : defaultMoveSpeed;
            this.getMovementConfiguration().setXMovementSpeed(newSpeed); // Only call when needed
        }
    }

    private boolean isTooFarAway() {
        int attackRangeToCheck = attackRange;
        if (wasFiringAtTarget) {
            attackRangeToCheck += 25; //voorkomt het constant roteren van locking/losing lock
        }

        Rectangle targetBounds = target.getBounds();
        double distance = ProtossUtils.getDistanceToRectangle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetBounds);
        return distance > attackRangeToCheck;
    }


    private void shootMissile() {
        MissileEnums missileType = MissileEnums.ProtossShuttleMissile;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(missileType.getImageType());
        missileSpriteConfiguration.setScale(0.225f);

        float xMovementSpeed = 2;
        float yMovementSpeed = 2;
        Direction rotation = Direction.RIGHT;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        PathFinder pathFinder = new StraightLinePathFinder();

        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementSpeed, yMovementSpeed, pathFinder, movementPatternSize, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = false;
        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType
                , 100, 100, null, damage, missileType.getDeathOrExplosionImageEnum(), isFriendly, allowedToDealDamage, objectType,
                missileType.isUsesBoxCollision(), true, false, false);

        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
        missile.setObjectType("Enemy Protoss Shuttle Missile");

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