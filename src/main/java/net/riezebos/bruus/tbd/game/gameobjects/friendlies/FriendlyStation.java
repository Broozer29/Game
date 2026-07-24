package net.riezebos.bruus.tbd.game.gameobjects.friendlies;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerStats;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.awt.*;

public class FriendlyStation extends GameObject {
    protected double lastAttackTime = 0.0;
    protected FriendlyObjectEnums friendlyObjectType;
    private GameObject target;
    private int attackRange = 250;

    protected FriendlyStation(SpriteConfiguration spriteConfiguration, FriendlyObjectConfiguration droneConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration);

        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(ImageEnums.WarpIn);
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(0.25f);
        this.maxHitPoints = 100;
        this.currentHitpoints = maxHitPoints;

        this.friendlyObjectType = droneConfiguration.getFriendlyType();
        this.attackSpeed = droneConfiguration.getAttackSpeedCooldown();
        this.setFriendly(true);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
        this.setAllowedToMove(false);
    }

    private double lastGameSecondsCheckedForTarget = 0;
    private double checkingTargetCooldown = (double) (GameState.getInstance().getDELAY() * 3) / 1000; //every 3 gameticks
    public void activateObject() {
        this.currentHitpoints-= 0.3f;
        this.setAllowedToMove(false);
        if(target == null && GameState.getInstance().getGameSeconds() >= lastGameSecondsCheckedForTarget + checkingTargetCooldown){
            target = EnemyManager.getInstance().getClosestEnemyTargetWithinDistance(this.getCenterXCoordinate(), this.getCenterYCoordinate(), attackRange);
            lastGameSecondsCheckedForTarget = GameState.getInstance().getGameSeconds();
        }

        if(this.currentHitpoints <= this.maxHitPoints){
            showHealthBar = true;
        }

        if(target != null){
            if(isTooFarAway() || isTargetDeadOrInvisible()){
                target = null;
                this.setAllowedVisualsToRotate(true); //Allow for rotation towards destination again
                this.rotateObjectTowardsDestination(true);
                this.setAllowedVisualsToRotate(false);
            } else {
                this.setAllowedVisualsToRotate(true);
                double currentTime = GameState.getInstance().getGameSeconds();
                this.rotateGameObjectTowards(target.getCenterXCoordinate(), target.getCenterYCoordinate(), true);
                this.setAllowedVisualsToRotate(false);
                if (currentTime >= lastAttackTime + this.getAttackSpeed()) {
                    fireAction();
                    lastAttackTime = currentTime;
                }
            }
        }
    }

    private boolean isTargetDeadOrInvisible(){
        return target != null && (!target.isVisible() || target.getCurrentHitpoints() <= 0);
    }


    private boolean isTooFarAway() {
        Rectangle targetBounds = target.getBounds(); // Get target's bounding box
        double distance = ProtossUtils.getDistanceToRectangle(this.getCenterXCoordinate(), this.getCenterYCoordinate(), targetBounds);

        return distance > attackRange;
    }

    private void fireAction() {
        MissileEnums missileType = MissileEnums.PlayerLaserbeam;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(missileType.getImageType());
        missileSpriteConfiguration.setScale(0.8f);

        float movementSpeed = 12f;

        float damage = (PlayerStats.getInstance().getBaseDroneDamage() * (PlayerManager.getInstance().getRandomSpaceShip().getDroneDamageModifier() * 1.5f)) * 0.15f; //small increase in drone damage modifier to enable drone scaling because of the massive damage reduction
        Direction rotation = Direction.RIGHT;
        PathFinder pathFinder = new StraightLinePathFinder();

        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                movementSpeed, pathFinder, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = true;

        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType
                , damage, ImageEnums.Impact_Explosion_One, isFriendly,
                false, false, true);

        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);

        missile.setOwnerOrCreator(this);
        missile.setObjectType("SpaceStation Missile");
        missile.resetMovementPath();
        Point point = new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        point.setX(point.getX() - missile.getWidth() / 2);
        point.setY(point.getY() - missile.getHeight() / 2);
        movementConfiguration.setDestination(point);
        missile.setCenterCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());

        missile.rotateObjectTowardsDestination(true);
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover

        missile.setOwnerOrCreator(this);

        MissileManager.getInstance().addExistingMissile(missile);
    }

    public double getLastAttackTime() {
        return lastAttackTime;
    }

    public void triggerOnDeathActions() {
        super.triggerOnDeathActions();
    }
}