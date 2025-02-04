package net.riezebos.bruus.tbd.game.gameobjects.missiles;

import net.riezebos.bruus.tbd.game.gameobjects.GameObject;
import net.riezebos.bruus.tbd.game.gameobjects.enemies.EnemyManager;
import net.riezebos.bruus.tbd.game.gameobjects.player.PlayerManager;
import net.riezebos.bruus.tbd.game.gamestate.GameStatsTracker;
import net.riezebos.bruus.tbd.game.items.Item;
import net.riezebos.bruus.tbd.game.items.ItemEnums;
import net.riezebos.bruus.tbd.game.items.PlayerInventory;
import net.riezebos.bruus.tbd.game.movement.MovementConfiguration;
import net.riezebos.bruus.tbd.game.movement.Point;
import net.riezebos.bruus.tbd.game.movement.pathfinders.StraightLinePathFinder;
import net.riezebos.bruus.tbd.visualsandaudio.objects.AnimationManager;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteAnimation;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteAnimationConfiguration;
import net.riezebos.bruus.tbd.visualsandaudio.objects.SpriteConfigurations.SpriteConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Missile extends GameObject {

    protected List<GameObject> collidedObjects;
    protected MissileEnums missileEnum;
    protected boolean isExplosive;
    protected boolean destroysMissiles;
    protected boolean piercesThroughObjects;
    protected int amountOfPiercesLeft;
    protected int maximumAmountOfPierces;
    protected boolean isDestructable;
    protected boolean speedsUp;


    public Missile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration);
        initMissile(missileConfiguration);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
    }

    public Missile (SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration);
        initMissile(missileConfiguration);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
    }

    private void initMissile (MissileConfiguration missileConfiguration) {
        this.friendly = missileConfiguration.isFriendly();
        this.maxHitPoints = missileConfiguration.getMaxHitPoints();
        this.maxShieldPoints = missileConfiguration.getMaxShields();
        this.currentHitpoints = maxHitPoints;
        this.currentShieldPoints = maxShieldPoints;
        this.deathSound = missileConfiguration.getDeathSound();
        this.allowedToDealDamage = missileConfiguration.isAllowedToDealDamage();
        this.objectType = missileConfiguration.getObjectType();
        this.damage = missileConfiguration.getDamage();
        this.missileEnum = missileConfiguration.getMissileType();
        this.boxCollision = missileConfiguration.isBoxCollision();
        this.piercesThroughObjects = missileConfiguration.isPiercesMissiles();
        this.maximumAmountOfPierces = missileConfiguration.getAmountOfPierces();
        this.amountOfPiercesLeft = maximumAmountOfPierces;
        this.isExplosive = missileConfiguration.isExplosive();
        this.collidedObjects = new ArrayList<>();
        this.appliesOnHitEffects = missileConfiguration.isAppliesOnHitEffects();
        this.isDestructable = missileConfiguration.isDestructable();

        if (missileConfiguration.getDestructionType() != null) {
            SpriteAnimationConfiguration destructionAnimation = new SpriteAnimationConfiguration(spriteConfiguration, 2, false);
            destructionAnimation.getSpriteConfiguration().setImageType(missileConfiguration.getDestructionType());
            this.destructionAnimation = new SpriteAnimation(destructionAnimation);
        }

    }


    public SpriteAnimation getAnimation () {
        if (this.animation != null) {
            return this.animation;
        }
        return null;
    }

    public void missileAction () {
        // Exists to be overriden by non-explosive missiles
    }

    public void detonateMissile () {
        //Exists to be overriden by explosive missiles
    }

    public boolean interactsWithMissiles () {
        return destroysMissiles || isDestructable;
    }

    public void setDestroysMissiles (boolean destroysMissiles) {
        this.destroysMissiles = destroysMissiles;
    }

    public MissileEnums getMissileEnum () {
        return missileEnum;
    }

    public void setMissileEnum (MissileEnums missileEnum) {
        this.missileEnum = missileEnum;
    }

    public boolean isPiercesThroughObjects () {
        return piercesThroughObjects;
    }

    public void setPiercesThroughObjects (boolean piercesThroughObjects) {
        this.piercesThroughObjects = piercesThroughObjects;
    }

    public int getAmountOfPiercesLeft () {
        return amountOfPiercesLeft;
    }

    public void setAmountOfPiercesLeft (int amountOfPiercesLeft) {
        this.amountOfPiercesLeft = amountOfPiercesLeft;
    }

    public void modifyPiercesLeft (int modifyAmount) {
        this.amountOfPiercesLeft += modifyAmount;
    }

    public void addCollidedObject (GameObject object) {
        if (!this.collidedObjects.contains(object)) {
            this.collidedObjects.add(object);
        }
    }

    public boolean collidedWithObject (GameObject object) {
        return collidedObjects.contains(object);
    }

    public void destroyMissile () {
        this.setVisible(false);
        if (this.getDestructionAnimation() != null) {
            centerDestructionAnimation();
            this.destructionAnimation.rotateAnimation(this.rotationAngle, false);
            AnimationManager.getInstance().addUpperAnimation(this.getDestructionAnimation());
        }
    }


    private boolean hasPiercedForStatsTracker = false;

    public void handleCollision (GameObject collidedObject) {
        // Handle different types of missiles
        if (isExplosive) {
            detonateMissile();
            destroyMissile();
            if (ownerOrCreator.equals(PlayerManager.getInstance().getSpaceship())) {
                GameStatsTracker.getInstance().addShotHit(1);
            }
        } else {
            pierceAndBounce(collidedObject);
            if (!hasPiercedForStatsTracker && ownerOrCreator.equals(PlayerManager.getInstance().getSpaceship())) {
                GameStatsTracker.getInstance().addShotHit(1);
            }
        }

    }

    private void pierceAndBounce (GameObject collidedObject) {
        //Removed the "hasColliddedWithObjects" check since we already do this chcek in MissileManager
        if (piercesThroughObjects && amountOfPiercesLeft > 0) {
            dealDamageToGameObject(collidedObject);
            addCollidedObject(collidedObject);
            amountOfPiercesLeft--;
            hasPiercedForStatsTracker = true;

            //Rework in a nicer way, just testing
            Item bouncingModuleAddon = PlayerInventory.getInstance().getItemFromInventoryIfExists(ItemEnums.BouncingModuleAddon);
            if (bouncingModuleAddon != null) {
                GameObject newTarget = EnemyManager.getInstance().findEnemyForMissileToBounceTo(collidedObject, this.collidedObjects);
                if (newTarget != null) {
                    bouncingModuleAddon.applyEffectToObject(this);
                    bounceToNewTarget(newTarget);
                }
            }

        } else if (!piercesThroughObjects || amountOfPiercesLeft <= 0) {
            dealDamageToGameObject(collidedObject);
            destroyMissile();

        }
    }

    public boolean hasCollidedBeforeWith(GameObject object){
        return collidedObjects.contains(object);
    }

    private void bounceToNewTarget (GameObject newTarget) {
        this.resetMovementPath();
        this.allowedVisualsToRotate = true;
        this.movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        this.movementConfiguration.setRotation(this.movementRotation);
        this.movementConfiguration.setCurrentLocation(this.currentLocation);
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());

        int centerX = newTarget.getCenterXCoordinate() - this.getWidth() / 2;
        int centerY = newTarget.getCenterYCoordinate() - this.getHeight() / 2;

        this.movementConfiguration.setDestination(new Point(centerX, centerY));
        this.rotateObjectTowardsDestination(true);
        this.allowedVisualsToRotate = false;
    }

    public boolean isExplosive () {
        return isExplosive;
    }

    public void setExplosive (boolean explosive) {
        isExplosive = explosive;
    }

    public boolean isDestructable () {
        return isDestructable;
    }

    public void setDestructable (boolean destructable) {
        isDestructable = destructable;
    }

    public boolean isDeletesMissiles () {
        return destroysMissiles;
    }

    public boolean isSpeedsUp () {
        return speedsUp;
    }

    public void setSpeedsUp (boolean speedsUp) {
        this.speedsUp = speedsUp;
    }

    public int getMaximumAmountOfPierces () {
        return maximumAmountOfPierces;
    }

    public void setMaximumAmountOfPierces (int maximumAmountOfPierces) {
        this.maximumAmountOfPierces = maximumAmountOfPierces;
    }
}