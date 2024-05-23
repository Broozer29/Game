package game.objects.missiles;

import game.managers.AnimationManager;
import game.movement.MovementConfiguration;
import game.movement.pathfinders.HomingPathFinder;
import game.movement.pathfinders.PathFinder;
import game.objects.GameObject;
import visualobjects.SpriteConfigurations.SpriteAnimationConfiguration;
import visualobjects.SpriteConfigurations.SpriteConfiguration;
import visualobjects.SpriteAnimation;

import java.util.ArrayList;
import java.util.List;

public class Missile extends GameObject {

    protected List<GameObject> collidedEnemies;
    protected MissileTypeEnums missileType;
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
        this.missileType = missileConfiguration.getMissileType();
        this.boxCollision = missileConfiguration.isBoxCollision();
        this.piercesThroughObjects = missileConfiguration.isPiercesMissiles();
        this.amountOfPiercesLeft = missileConfiguration.getAmountOfPierces();
        this.collidedEnemies = new ArrayList<>();

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
        // Exists to be overriden
    }

    public boolean isDestroysMissiles () {
        return destroysMissiles;
    }

    public void setDestroysMissiles (boolean destroysMissiles) {
        this.destroysMissiles = destroysMissiles;
    }

    public MissileTypeEnums getMissileType () {
        return missileType;
    }

    public void setMissileType (MissileTypeEnums missileType) {
        this.missileType = missileType;
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
        if (!this.collidedEnemies.contains(object)) {
            this.collidedEnemies.add(object);
        }
    }

    public boolean collidedWithObject(GameObject object){
        return collidedEnemies.contains(object);
    }

    public void destroyMissile () {
        this.setVisible(false);
        if (this.getDestructionAnimation() != null) {
            centerDestructionAnimation();
            AnimationManager.getInstance().addUpperAnimation(this.getDestructionAnimation());
        }
    }

    private void centerDestructionAnimation () {
        if (this.getAnimation() != null) {
            this.getDestructionAnimation().setOriginCoordinates(this.getAnimation().getCenterXCoordinate(), this.getAnimation().getCenterYCoordinate());
        } else {
            this.getDestructionAnimation().setOriginCoordinates(this.getCenterXCoordinate(), this.getCenterYCoordinate());
        }
    }
}