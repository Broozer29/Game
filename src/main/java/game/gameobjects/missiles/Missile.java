package game.gameobjects.missiles;

import game.gameobjects.player.PlayerManager;
import game.gamestate.GameStatsTracker;
import game.items.Item;
import game.items.PlayerInventory;
import game.items.enums.ItemEnums;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.StraightLinePathFinder;
import game.gameobjects.GameObject;
import game.gameobjects.enemies.EnemyManager;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class Missile extends GameObject {

    protected List<GameObject> collidedObjects;
    protected MissileEnums missileEnum;
    protected boolean isExplosive;
    protected boolean destroysMissiles;
    protected boolean piercesThroughObjects;
    protected int amountOfPiercesLeft;

    protected boolean isDestructable;
    protected boolean speedsUp;


    public Missile (SpriteConfiguration spriteConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteConfiguration, movementConfiguration);
        initMissile(missileConfiguration);
        if (movementConfiguration != null) {
            initMovementConfiguration(movementConfiguration);
        }
    }

    public Missile (SpriteAnimationConfiguration spriteAnimationConfiguration, MissileConfiguration missileConfiguration, MovementConfiguration movementConfiguration) {
        super(spriteAnimationConfiguration, movementConfiguration);
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
        this.amountOfPiercesLeft = missileConfiguration.getAmountOfPierces();
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
        //We only want to fire this once per enemy, thus the collidedobjects check
        if (piercesThroughObjects && amountOfPiercesLeft > 0 && !collidedObjects.contains(collidedObject)) {
            dealDamageToGameObject(collidedObject);
            addCollidedObject(collidedObject);
            amountOfPiercesLeft--;
            hasPiercedForStatsTracker = true;

            //Rework in a nicer way, just testing
            Item bouncingModuleAddon = PlayerInventory.getInstance().getItemByName(ItemEnums.BouncingModuleAddon);
            if (bouncingModuleAddon != null) {
                GameObject newTarget = EnemyManager.getInstance().getEnemyClosestToGameObject(collidedObject, this.collidedObjects);
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
}