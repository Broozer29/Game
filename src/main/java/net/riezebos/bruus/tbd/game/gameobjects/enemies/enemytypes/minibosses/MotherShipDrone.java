package net.riezebos.bruus.tbd.game.gameobjects.enemies.enemytypes.minibosses;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.Enemy;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyConfiguration;
import net.riezebos.bruus.tbd.game.gameobjects.friendlies.drones.droneTypes.protoss.ProtossUtils;
import net.riezebos.bruus.tbd.game.gameobjects.missiles.*;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameState;
import net.riezebos.bruus.tbd.game.movement.Direction;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.MovementPatternSize;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.DestinationPathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.PathFinder;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.game.util.WithinVisualBoundariesCalculator;
import net.riezebos.bruus.tbd.visualsandaudio.data.image.ImageEnums;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

public class MotherShipDrone extends Enemy {

    private GameObject target;
    private boolean justInitialized;

    public MotherShipDrone(SpriteAnimationConfiguration spriteAnimationConfigurationion, EnemyConfiguration enemyConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfigurationion, enemyConfiguration, movementConfiguration);
        SpriteAnimationConfiguration destroyedExplosionfiguration = new SpriteAnimationConfiguration(spriteAnimationConfigurationion.getSpriteConfiguration(), 0, false);
        destroyedExplosionfiguration.getSpriteConfiguration().setImageType(this.enemyType.getDestructionType());
        this.destructionAnimation = new SpriteAnimation(destroyedExplosionfiguration);
        this.destructionAnimation.setAnimationScale(1f);
        this.detonateOnCollision = false;
        this.knockbackStrength = 8;
        justInitialized = true;
    }


    public void fireAction () {
        //idk of deze gedrag moet hebben

        if (justInitialized) {
            this.movementConfiguration.setPathFinder(new DestinationPathFinder());
            updateMovementPath();
            this.damage = this.ownerOrCreator.getDamage() / 2;
            this.attackSpeed = 1f;
            justInitialized = false;
            target = PlayerManager.getInstance().getSpaceship();
        }

        if(this.ownerOrCreator != null && this.ownerOrCreator.getCurrentHitpoints() <= 0){
            this.takeDamage(99999);
        }

        if (this.getCurrentLocation().equals(this.movementConfiguration.getDestination())) {
            updateMovementPath();
        }

        double currentTime = GameState.getInstance().getGameSeconds();
        if (currentTime >= lastAttackTime + this.getAttackSpeed() && WithinVisualBoundariesCalculator.isWithinBoundaries(this)
                && allowedToFire) {
            updateChargingAttackAnimationCoordination();
            if (!chargingUpAttackAnimation.isPlaying()) {
                this.isAttacking = true;
                chargingUpAttackAnimation.refreshAnimation();
                AnimationManager.getInstance().addUpperAnimation(chargingUpAttackAnimation);
            }

            if (chargingUpAttackAnimation.getCurrentFrame() >= chargingUpAttackAnimation.getTotalFrames() - 1) {
                shootMissile();
                this.isAttacking = false;
                lastAttackTime = currentTime; // Update the last attack time after firing
            }
        }

        this.setAllowedVisualsToRotate(true);
        this.rotateGameObjectTowards(target.getCenterXCoordinate(), target.getCenterYCoordinate(), true);
        this.setAllowedVisualsToRotate(false);
    }


    private void updateMovementPath() {
        this.movementConfiguration.resetMovementPath();
        this.movementConfiguration.setCurrentLocation(new Point(this.getXCoordinate(), this.getYCoordinate()));

        GameObject target = this.ownerOrCreator;
        int minDistance = 80;
        int maxDistance = 180;

        this.movementConfiguration.setDestination(ProtossUtils.getRandomPoint(target, minDistance, maxDistance));
    }

    private void shootMissile() {
        MissileEnums missileType = MissileEnums.DefaultAnimatedBullet;
        SpriteConfiguration missileSpriteConfiguration = new SpriteConfiguration();
        missileSpriteConfiguration.setxCoordinate(this.getCenterXCoordinate());
        missileSpriteConfiguration.setyCoordinate(this.getCenterYCoordinate());
        missileSpriteConfiguration.setImageType(ImageEnums.MotherShipDroneMissile);
        missileSpriteConfiguration.setScale(0.2f);

        float xMovementSpeed = 1.75f;
        float yMovementSpeed = 1.75f;
        Direction rotation = Direction.RIGHT;
        MovementPatternSize movementPatternSize = MovementPatternSize.SMALL;
        PathFinder pathFinder = new StraightLinePathFinder();

        MovementConfiguration movementConfiguration = MissileCreator.getInstance().createMissileMovementConfig(
                xMovementSpeed, yMovementSpeed, pathFinder, movementPatternSize, rotation
        );
        movementConfiguration.initDefaultSettingsForSpecializedPathFinders();

        boolean isFriendly = false;
        MissileConfiguration missileConfiguration = MissileCreator.getInstance().createMissileConfiguration(missileType
                , 100, 100, null, damage, ImageEnums.Impact_Explosion_One, isFriendly, allowedToDealDamage, objectType,
                missileType.isUsesBoxCollision(), false, false, false);

        Missile missile = MissileCreator.getInstance().createMissile(missileSpriteConfiguration, missileConfiguration, movementConfiguration);
        missile.setOwnerOrCreator(this);
        missile.setObjectType("Enemy Mothership Drone Missile");
        missile.resetMovementPath();
        missile.getAnimation().setFrameDelay(0);

        Point point = new Point(target.getCenterXCoordinate(), target.getCenterYCoordinate());
        point.setX(point.getX() - missile.getWidth() / 2);
        point.setY(point.getY() - missile.getHeight() / 2);
        movementConfiguration.setDestination(point);
        missile.setCenterCoordinates(this.getChargingUpAttackAnimation().getCenterXCoordinate(), this.getChargingUpAttackAnimation().getCenterYCoordinate());
//        missile.rotateObjectTowardsDestination(true);
        missile.setAllowedVisualsToRotate(false); //Prevent it from being rotated again by the SpriteMover
        missile.setOwnerOrCreator(this);
        MissileManager.getInstance().addExistingMissile(missile);
    }
}
