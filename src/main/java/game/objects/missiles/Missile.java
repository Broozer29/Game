package game.objects.missiles;

import game.items.Item;
import game.items.PlayerInventory;
import game.items.enums.ItemEnums;
import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.Point;
import game.movement.pathfinders.RegularPathFinder;
import game.movement.pathfinders.StraightLinePathFinder;
import game.objects.GameObject;
import game.objects.enemies.EnemyManager;
import game.objects.missiles.missiletypes.Rocket1;
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

    public void detonateMissile (){
        //Exists to be overriden by explosive missiles
    }

    public boolean isDestroysMissiles () {
        return destroysMissiles;
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

    public boolean collidedWithObject(GameObject object){
        return collidedObjects.contains(object);
    }

    public void destroyMissile () {
        this.setVisible(false);
        if (this.getDestructionAnimation() != null) {
            centerDestructionAnimation();
            AnimationManager.getInstance().addUpperAnimation(this.getDestructionAnimation());
        }
    }

    public void handleCollision(GameObject collidedObject){
        applyDamageModification(collidedObject); // Adjust damage based on any modifiers.

        // Handle different types of missiles
        if (isExplosive) {
            detonateMissile();
            destroyMissile();
        } else {
            pierceAndBounce(collidedObject);
        }
    }

    private void pierceAndBounce(GameObject collidedObject){
        if (piercesThroughObjects && amountOfPiercesLeft > 0 && !collidedObjects.contains(collidedObject)) {
            dealDamageToGameObject(collidedObject);
            addCollidedObject(collidedObject);
            amountOfPiercesLeft--;

            //Rework in a nicer way, just testing
            Item bouncingModuleAddon = PlayerInventory.getInstance().getItemByName(ItemEnums.BouncingModuleAddon);
            if(bouncingModuleAddon != null) {
                GameObject newTarget = EnemyManager.getInstance().getEnemyClosestToGameObject(collidedObject, this.collidedObjects);
                if (newTarget != null) {
                    bouncingModuleAddon.applyEffectToObject(this);
                    bounceToNewTarget(newTarget);
                }
            }

        } else if (!piercesThroughObjects || amountOfPiercesLeft <= 0 && !collidedObjects.contains(collidedObject)) {
            dealDamageToGameObject(collidedObject);
            destroyMissile();
        }
    }

    private void bounceToNewTarget(GameObject newTarget){
        this.resetMovementPath();
        this.movementConfiguration.initDefaultSettingsForSpecializedPathFinders();
        this.movementConfiguration.setRotation(this.movementRotation);
        this.movementConfiguration.setCurrentLocation(this.currentLocation);
        this.movementConfiguration.setPathFinder(new StraightLinePathFinder());

        int centerX = newTarget.getCenterXCoordinate() - this.getWidth() / 2;
        int centerY = newTarget.getCenterYCoordinate() - this.getHeight() / 2;

        this.movementConfiguration.setDestination(new Point(centerX, centerY));
    }

    public boolean isExplosive () {
        return isExplosive;
    }

    public void setExplosive (boolean explosive) {
        isExplosive = explosive;
    }
}